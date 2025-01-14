package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.req.SysDeptQueryReq;
import com.wzkris.user.domain.vo.SelectTreeVO;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.mapper.SysRoleDeptMapper;
import com.wzkris.user.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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

    private final SysRoleDeptMapper roleDeptMapper;

    @Override
    public List<SelectTreeVO> listSelectTree(String deptName) {
        LambdaQueryWrapper<SysDept> lqw = Wrappers.lambdaQuery(SysDept.class)
                .select(SysDept::getDeptId, SysDept::getParentId, SysDept::getDeptName)
                .like(StringUtil.isNotEmpty(deptName), SysDept::getDeptName, deptName);

        List<SysDept> depts = deptMapper.selectListInScope(lqw);
        return this.buildDeptTreeSelect(depts);
    }

    /**
     * 构建下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    private List<SelectTreeVO> buildDeptTreeSelect(List<SysDept> depts) {
        List<SysDept> deptTrees = this.buildDeptTree(depts);
        return deptTrees.stream().map(SelectTreeVO::new).collect(Collectors.toList());
    }

    /**
     * 构建树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    private List<SysDept> buildDeptTree(List<SysDept> depts) {
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

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDept(SysDept dept) {
        SysDept parent = deptMapper.selectByIdForUpdate(dept.getParentId());
        dept.setAncestors(parent.getAncestors() + "," + dept.getParentId());
        return deptMapper.insert(dept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        SysDept parentDept = deptMapper.selectByIdForUpdate(dept.getParentId());
        if (StringUtil.isNotNull(parentDept)) {
            String newAncestors = parentDept.getAncestors() + "," + parentDept.getDeptId();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, dept.getAncestors());
        }
        return deptMapper.updateById(dept) > 0;
    }

    @Override
    public void deleteById(Long deptId) {
        deptMapper.deleteById(deptId);
        roleDeptMapper.deleteByDeptId(deptId);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = deptMapper.listChildren(new SysDeptQueryReq(deptId));
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

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result > 0;
    }

    @Override
    public void checkDataScopes(List<Long> deptIds) {
        if (ObjUtil.isNotEmpty(deptIds)) {
            if (deptMapper.checkDataScopes(deptIds) != deptIds.size()) {
                throw new AccessDeniedException("无此部门数据访问权限");
            }
        }
    }
}
