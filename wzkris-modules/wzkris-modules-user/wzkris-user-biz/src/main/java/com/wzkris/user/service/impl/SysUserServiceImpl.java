package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.orm.utils.PageUtil;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.SysUserPost;
import com.wzkris.user.domain.SysUserRole;
import com.wzkris.user.domain.dto.SysUserDTO;
import com.wzkris.user.domain.vo.SysUserVO;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.mapper.SysUserMapper;
import com.wzkris.user.mapper.SysUserPostMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 管理员 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserMapper sysUserMapper;
    private final SysDeptMapper sysDeptMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysUserPostMapper sysUserPostMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<SysUserVO> listPage(SysUser user) {
        PageUtil.startPage();
        // 查询
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        List<SysUser> userList = sysUserMapper.selectListInScope(lqw);
        // 先获取分页信息，否则总数会有问题
        Page<SysUserVO> page = PageUtil.getPage();

        if (CollectionUtils.isEmpty(userList)) {
            return page;
        }

        List<Long> deptIds = userList.stream().map(SysUser::getDeptId).collect(Collectors.toList());
        // 仅查出状态正常的部门
        Map<Long, SysDept> deptMap = sysDeptMapper.selectListInScope(new LambdaQueryWrapper<SysDept>().in(SysDept::getDeptId, deptIds))
                .stream()
                .collect(Collectors.toMap(SysDept::getDeptId, Function.identity()));
        // 转换成vo
        List<SysUserVO> userVOS = MapstructUtil.convert(userList, SysUserVO.class);
        for (SysUserVO userVO : userVOS) {
            SysDept dept = deptMap.get(userVO.getDeptId());
            if (dept != null) {
                userVO.setDeptName(dept.getDeptName());
                userVO.setDeptStatus(dept.getStatus());
            }
        }
        return page.setRows(userVOS);
    }

    @Override
    public List<SysUserVO> list(SysUser user) {
        // 查询
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        List<SysUser> userList = sysUserMapper.selectListInScope(lqw);
        List<Long> deptIds = userList.stream().map(SysUser::getDeptId).collect(Collectors.toList());
        // 仅查出状态正常的部门
        Map<Long, SysDept> deptMap = sysDeptMapper.selectListInScope(new LambdaQueryWrapper<SysDept>().in(SysDept::getDeptId, deptIds))
                .stream()
                .collect(Collectors.toMap(SysDept::getDeptId, Function.identity()));
        // 转换成vo
        List<SysUserVO> userVOS = MapstructUtil.convert(userList, SysUserVO.class);
        for (SysUserVO userVO : userVOS) {
            SysDept dept = deptMap.get(userVO.getDeptId());
            if (dept != null) {
                userVO.setDeptName(dept.getDeptName());
                userVO.setDeptStatus(dept.getStatus());
            }
        }
        return userVOS;
    }

    @Override
    public List<SysUser> listAllocated(SysUser user, Long roleId) {
        List<Long> userIds = sysUserRoleMapper.listUserIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        lqw.in(SysUser::getUserId, userIds);
        return sysUserMapper.selectListInScope(lqw);
    }

    @Override
    public List<SysUser> listUnallocated(SysUser user, Long roleId) {
        List<Long> userIds = sysUserRoleMapper.listUserIdByRoleId(roleId);

        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        if (!CollectionUtils.isEmpty(userIds)) {
            lqw.notIn(SysUser::getUserId, userIds);
        }
        return sysUserMapper.selectListInScope(lqw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(SysUserDTO userDTO) {
        // 密码加密
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        int rows = sysUserMapper.insert(userDTO);
        // 新增用户与角色管理
        this.insertUserRole(userDTO.getUserId(), userDTO.getRoleIds());
        this.insertUserPost(userDTO.getUserId(), userDTO.getPostIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUserDTO dto) {
        // 删除用户与角色关联
        sysUserRoleMapper.deleteByUserId(dto.getUserId());
        sysUserPostMapper.deleteByUserId(dto.getUserId());
        // 新增用户与角色管理
        this.insertUserRole(dto.getUserId(), dto.getRoleIds());
        this.insertUserPost(dto.getUserId(), dto.getPostIds());
        return sysUserMapper.updateById(dto) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoles(Long userId, List<Long> roleIds) {
        sysUserRoleMapper.deleteByUserId(userId);
        this.insertUserRole(userId, roleIds);
    }

    /**
     * 新增用户与角色信息
     */
    private void insertUserRole(Long userId, List<Long> roleIds) {
        if (ObjUtil.isNotEmpty(roleIds)) {
            List<SysUserRole> list = roleIds.stream()
                    .map(roleId -> new SysUserRole(userId, roleId))
                    .toList();
            sysUserRoleMapper.insertBatch(list);
        }
    }

    /**
     * 新增用户部门信息
     */
    public void insertUserPost(Long userId, List<Long> postIds) {
        if (ObjUtil.isNotEmpty(postIds)) {
            List<SysUserPost> list = postIds.stream()
                    .map(postId -> new SysUserPost(userId, postId))
                    .toList();
            sysUserPostMapper.insertBatch(list);
        }
    }

    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUser user) {
        return new LambdaQueryWrapper<SysUser>()
                .like(StringUtil.isNotNull(user.getUsername()), SysUser::getUsername, user.getUsername())
                .like(StringUtil.isNotNull(user.getNickname()), SysUser::getNickname, user.getNickname())
                .like(StringUtil.isNotNull(user.getPhoneNumber()), SysUser::getPhoneNumber, user.getPhoneNumber())
                .like(StringUtil.isNotNull(user.getEmail()), SysUser::getEmail, user.getEmail())
                .eq(StringUtil.isNotNull(user.getGender()), SysUser::getGender, user.getGender())
                .eq(StringUtil.isNotNull(user.getStatus()), SysUser::getStatus, user.getStatus())
                .eq(StringUtil.isNotNull(user.getStatus()), SysUser::getStatus, user.getStatus())
                .eq(StringUtil.isNotNull(user.getDeptId()), SysUser::getDeptId, user.getDeptId())
                .between(user.getParams().get("beginTime") != null && user.getParams().get("endTime") != null,
                        SysUser::getCreateAt, user.getParams().get("beginTime"), user.getParams().get("endTime"))
                .orderByDesc(SysUser::getUserId);
    }

    /**
     * 校验是否有数据权限
     *
     * @param userIds 被操作的对象id
     */
    public void checkDataScopes(List<Long> userIds) {
        userIds = userIds.stream().filter(Objects::nonNull).toList();
        if (ObjUtil.isNotEmpty(userIds)) {
            if (sysUserMapper.checkDataScopes(userIds) != userIds.size()) {
                throw new AccessDeniedException("当前用户没有权限访问数据");
            }
        }
    }

    /**
     * 校验用户是否唯一
     *
     * @param user 筛选条件
     */
    public boolean checkUserUnique(SysUser user) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>(SysUser.class)
                    .eq(StringUtil.isNotNull(user.getUsername()), SysUser::getUsername, user.getUsername())
                    .eq(StringUtil.isNotNull(user.getPhoneNumber()), SysUser::getPhoneNumber, user.getPhoneNumber())
                    .eq(StringUtil.isNotNull(user.getEmail()), SysUser::getEmail, user.getEmail())
                    .ne(ObjUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId());
            return sysUserMapper.exists(lqw);
        });
    }
}
