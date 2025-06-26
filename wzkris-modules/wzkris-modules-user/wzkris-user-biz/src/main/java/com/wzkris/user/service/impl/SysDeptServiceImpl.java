package com.wzkris.user.service.impl;

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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

        List<SysDept> depts = deptMapper.selectLists(lqw);
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
    @Transactional(rollbackFor = Exception.class)
    public boolean insertDept(SysDept dept) {
        SysDept parent = deptMapper.selectByIdForUpdate(dept.getParentId());
        if (parent != null) {
            Long[] newAncestors = Arrays.copyOf(parent.getAncestors(), parent.getAncestors().length + 1);
            newAncestors[newAncestors.length - 1] = parent.getDeptId();
            dept.setAncestors(newAncestors);
        }
        dept.setParentId(parent == null ? null : dept.getParentId());
        return deptMapper.insert(dept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept) {
        SysDept parent = deptMapper.selectByIdForUpdate(dept.getParentId());
        if (ObjectUtils.isNotEmpty(parent)) {
            Long[] newAncestors = Arrays.copyOf(parent.getAncestors(), parent.getAncestors().length + 1);
            newAncestors[newAncestors.length - 1] = parent.getDeptId();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, dept.getAncestors());
        }
        return deptMapper.updateById(dept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long deptId) {
        boolean success = deptMapper.deleteById(deptId) > 0;
        if (success) {
            roleDeptMapper.deleteByDeptId(deptId);
        }
        return success;
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, Long[] newAncestors, Long[] oldAncestors) {
        // 查出子元素并更新祖先列表
        SysDeptQueryReq queryReq = new SysDeptQueryReq();
        queryReq.setParentId(deptId);
        List<SysDept> children = deptMapper.listChildren(queryReq);
        List<SysDept> updateList = children.stream()
                .map(child -> {
                    // 替换旧的祖先路径为新的祖先路径
                    Long[] updatedAncestors = replaceAncestors(child.getAncestors(), oldAncestors, newAncestors);

                    // 创建新的部门对象，设置更新后的祖先路径
                    SysDept sysDept = new SysDept(child.getDeptId());
                    sysDept.setAncestors(updatedAncestors);
                    return sysDept;
                })
                .toList();
        if (!CollectionUtils.isEmpty(updateList)) {
            // 批量更新
            deptMapper.updateById(updateList);
        }
    }

    private Long[] replaceAncestors(Long[] childAncestors, Long[] oldAncestors, Long[] newAncestors) {
        // 将数组转换为列表以便操作
        List<Long> childAncestorsList = new ArrayList<>(Arrays.asList(childAncestors));
        List<Long> oldAncestorsList = Arrays.asList(oldAncestors);
        List<Long> newAncestorsList = Arrays.asList(newAncestors);

        // 找到旧祖先路径的起始位置
        int startIndex = Collections.indexOfSubList(childAncestorsList, oldAncestorsList);

        // 如果找到旧祖先路径，则替换为新祖先路径
        if (startIndex != -1) {
            childAncestorsList
                    .subList(startIndex, startIndex + oldAncestorsList.size())
                    .clear();
            childAncestorsList.addAll(startIndex, newAncestorsList);
        }

        // 将列表转换回数组
        return childAncestorsList.toArray(new Long[0]);
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
            if (ObjectUtils.isNotEmpty(n.getParentId())
                    && n.getParentId().longValue() == t.getDeptId().longValue()) {
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
    public void checkDataScopes(Collection<Long> deptIds) {
        if (CollectionUtils.isNotEmpty(deptIds)) {
            if (!deptMapper.checkDataScopes(new HashSet<>(deptIds))) {
                throw new AccessDeniedException("无此部门数据访问权限");
            }
        }
    }

}
