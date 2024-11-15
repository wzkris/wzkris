package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.exception.BusinessExceptionI18n;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.vo.SelectTree;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysDeptServiceImpl implements SysDeptService {
    private final SysDeptMapper deptMapper;

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    @Override
    public List<SelectTree> listDeptTree(SysDept dept) {
        LambdaQueryWrapper<SysDept> lqw = this.buildQueryWrapper(dept);
        List<SysDept> depts = deptMapper.selectListInScope(lqw);
        return this.buildDeptTreeSelect(depts);
    }

    private LambdaQueryWrapper<SysDept> buildQueryWrapper(SysDept sysDept) {
        return new LambdaQueryWrapper<SysDept>()
                .eq(ObjUtil.isNotNull(sysDept.getTenantId()), SysDept::getTenantId, sysDept.getTenantId())
                .like(StringUtil.isNotEmpty(sysDept.getDeptName()), SysDept::getDeptName, sysDept.getDeptName())
                .like(StringUtil.isNotEmpty(sysDept.getContact()), SysDept::getContact, sysDept.getContact())
                .like(StringUtil.isNotEmpty(sysDept.getEmail()), SysDept::getEmail, sysDept.getEmail())
                .eq(StringUtil.isNotEmpty(sysDept.getStatus()), SysDept::getStatus, sysDept.getStatus());
    }

    /**
     * 构建树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts) {
        List<SysDept> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(SysDept::getDeptId).toList();
        for (SysDept dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * 构建下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<SelectTree> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = this.buildDeptTree(depts);
        return deptTrees.stream().map(SelectTree::new).collect(Collectors.toList());
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    public int insertDept(SysDept dept) {
        if (StringUtil.isNotNull(dept.getParentId()) && dept.getParentId() != 0) {
            SysDept info = deptMapper.selectById(dept.getParentId());
            // 如果父节点为停用状态,则不允许新增子节点
            if (StringUtil.equals(CommonConstants.STATUS_DISABLE, info.getStatus())) {
                throw new BusinessExceptionI18n("business.disabled");
            }
            dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        }
        return deptMapper.insert(dept);
    }

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDept(SysDept dept) {
        SysDept parentDept = deptMapper.selectById(dept.getParentId());
        SysDept curDept = deptMapper.selectById(dept.getDeptId());
        if (StringUtil.isNotNull(parentDept) && StringUtil.isNotNull(curDept)) {
            String newAncestors = parentDept.getAncestors() + "," + parentDept.getDeptId();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, curDept.getAncestors());
        }
        else {
            // 否则不更新父节点
            dept.setParentId(null);
            dept.setAncestors(null);
        }
        return deptMapper.updateById(dept);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.listChildren(new SysDept(deptId));
        List<SysDept> updateList = children.stream().map(child -> {
            String ancestors = child.getAncestors().replaceFirst(oldAncestors, newAncestors);
            SysDept sysDept = new SysDept(child.getDeptId());
            sysDept.setAncestors(ancestors);
            return sysDept;
        }).toList();
        if (!CollectionUtils.isEmpty(updateList)) {
            // 批量更新
            deptMapper.updateById(updateList);
        }
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysDept> list, SysDept t) {
        // 得到子节点列表
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t) {
        List<SysDept> tlist = new ArrayList<>();
        for (SysDept n : list) {
            if (StringUtil.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDept> list, SysDept t) {
        return !CollectionUtils.isEmpty(getChildList(list, t));
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptIds 部门id
     */
    @Override
    public void checkDataScopes(List<Long> deptIds) {
        deptIds = deptIds.stream().filter(Objects::nonNull).toList();
        if (ObjUtil.isNotEmpty(deptIds)) {
            if (deptMapper.checkDataScopes(deptIds) != deptIds.size()) {
                throw new AccessDeniedException("没有访问数据权限");
            }
        }
    }
}
