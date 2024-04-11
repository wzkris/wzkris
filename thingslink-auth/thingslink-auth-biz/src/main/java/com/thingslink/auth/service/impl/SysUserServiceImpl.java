package com.thingslink.auth.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.thingslink.auth.domain.*;
import com.thingslink.auth.domain.dto.SysUserDTO;
import com.thingslink.auth.domain.vo.SysUserVO;
import com.thingslink.auth.mapper.*;
import com.thingslink.auth.service.SysUserService;
import com.thingslink.common.core.exception.BusinessException;
import com.thingslink.common.core.utils.MapstructUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.utils.PageUtil;
import com.thingslink.common.orm.utils.TenantUtil;
import com.thingslink.common.security.utils.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    private final SysRoleMapper sysRoleMapper;
    private final SysPostMapper sysPostMapper;
    private final SysDeptMapper sysDeptMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysUserPostMapper sysUserPostMapper;

    @Override
    public Page<SysUserVO> listPage(SysUser user) {
        PageUtil.startPage();
        // 查询
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        List<SysUser> userList = sysUserMapper.selectListInScope(lqw);
        // 先获取分页信息，否则总数会有问题
        Page page = PageUtil.getPage();

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

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param roleId 角色id
     * @return 用户信息集合信息
     */
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

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param roleId 角色id
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> listUnallocated(SysUser user, Long roleId) {
        List<Long> userIds = sysUserRoleMapper.listUserIdByRoleId(roleId);

        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(user);
        if (!CollectionUtils.isEmpty(userIds)) {
            lqw.notIn(SysUser::getUserId, userIds);
        }
        return sysUserMapper.selectListInScope(lqw);
    }

    /**
     * 新增保存用户信息
     *
     * @param dto 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUserDTO dto) {
        // 密码加密
        dto.setPassword(LoginUserUtil.encryptPassword(dto.getPassword()));
        try {
//            TenantUtil.setDynamic(LoginUserUtil.);
            int rows = sysUserMapper.insert(dto);
            // 新增用户与角色管理
            this.insertUserRole(dto.getUserId(), dto.getRoleIds());
            this.insertUserPost(dto.getUserId(), dto.getPostIds());
            // 新增用户信息
            return rows > 0;
        }
        finally {
            TenantUtil.clearDynamic();
        }
    }

    /**
     * 修改保存用户信息
     *
     * @param dto 用户信息
     * @return 结果
     */
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

    /**
     * 批量授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocateRoles(Long userId, Long[] roleIds) {
        sysUserRoleMapper.deleteByUserId(userId);
        this.insertUserRole(userId, roleIds);
    }

    /**
     * 新增用户与角色信息
     */
    private void insertUserRole(Long userId, Long[] roleIds) {
        if (ObjUtil.isNotEmpty(roleIds)) {
            List<SysUserRole> list = Arrays.stream(roleIds)
                    .map(roleId -> new SysUserRole(userId, roleId))
                    .toList();
            sysUserRoleMapper.insertBatch(list);
        }
    }

    /**
     * 新增用户部门信息
     */
    public void insertUserPost(Long userId, Long[] postIds) {
        if (ObjUtil.isNotEmpty(postIds)) {
            List<SysUserPost> list = Arrays.stream(postIds)
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
    public void checkDataScopes(Long... userIds) {
        userIds = Arrays.stream(userIds).filter(Objects::nonNull).toArray(Long[]::new);
        if (ObjUtil.isNotEmpty(userIds)) {
            if (!(sysUserMapper.checkDataScopes(userIds) == userIds.length)) {
                throw new AccessDeniedException("当前用户没有权限访问数据");
            }
        }
    }

    /**
     * 校验用户是否唯一
     *
     * @param user 筛选条件
     */
    public boolean checkUserUnique(SysUser user, Long userId) {
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<SysUser>()
                .eq(StringUtil.isNotNull(user.getUsername()), SysUser::getUsername, user.getUsername())
                .eq(StringUtil.isNotNull(user.getPhoneNumber()), SysUser::getPhoneNumber, user.getPhoneNumber())
                .eq(StringUtil.isNotNull(user.getEmail()), SysUser::getEmail, user.getEmail())
                .ne(ObjUtil.isNotNull(userId), SysUser::getUserId, userId);
        return sysUserMapper.exists(lqw);
    }

    /**
     * 校验相关参数的租户ID是否一致
     *
     * @param userDTO 用户参数
     */
    @Override
    public void checkTenantId(SysUserDTO userDTO) {
        Long tenantId;
        // userid为空则新增操作，判断是否传了租户ID
        if (userDTO.getUserId() == null) {
            if (userDTO.getTenantId() == null) {
                tenantId = LoginUserUtil.getTenantId();
            }
            else {
                tenantId = userDTO.getTenantId();
            }
        }
        else {
            // 不为空则是更新操作
            tenantId = new LambdaQueryChainWrapper<>(sysUserMapper)
                    .select(SysUser::getTenantId)
                    .eq(SysUser::getUserId, userDTO.getUserId())
                    .one().getTenantId();
        }
        if (userDTO.getDeptId() != null) {
            Long deptSize = new LambdaQueryChainWrapper<>(sysDeptMapper)
                    .eq(SysDept::getDeptId, userDTO.getDeptId())
                    .eq(SysDept::getTenantId, tenantId)
                    .count();
            if (deptSize != userDTO.getPostIds().length) {
                throw new BusinessException("操作失败，部门归属租户与用户租户不一致");
            }
        }
        if (ObjUtil.isNotEmpty(userDTO.getRoleIds())) {
            Long roleSize = new LambdaQueryChainWrapper<>(sysRoleMapper)
                    .eq(SysRole::getTenantId, tenantId)
                    .in(SysRole::getRoleId, Arrays.asList(userDTO.getRoleIds()))
                    .count();
            if (roleSize != userDTO.getRoleIds().length) {
                throw new BusinessException("操作失败，角色归属租户与用户租户不一致");
            }
        }
        if (ObjUtil.isNotEmpty(userDTO.getPostIds())) {
            Long postSize = new LambdaQueryChainWrapper<>(sysPostMapper)
                    .eq(SysPost::getTenantId, tenantId)
                    .in(SysPost::getPostId, Arrays.asList(userDTO.getPostIds()))
                    .count();
            if (postSize != userDTO.getPostIds().length) {
                throw new BusinessException("操作失败，岗位归属租户与用户租户不一致");
            }
        }
    }

}
