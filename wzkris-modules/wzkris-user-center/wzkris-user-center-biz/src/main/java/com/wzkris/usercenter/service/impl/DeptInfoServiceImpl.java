package com.wzkris.usercenter.service.impl;

import com.wzkris.usercenter.domain.DeptInfoDO;
import com.wzkris.usercenter.domain.vo.SelectTreeVO;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.mapper.RoleToDeptMapper;
import com.wzkris.usercenter.service.DeptInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理 服务实现
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class DeptInfoServiceImpl implements DeptInfoService {

    private final DeptInfoMapper deptInfoMapper;

    private final RoleToDeptMapper roleToDeptMapper;

    public List<SelectTreeVO> buildSelectTree(List<DeptInfoDO> depts) {
        List<DeptInfoDO> deptTrees = this.buildDeptTree(depts);
        return deptTrees.stream().map(SelectTreeVO::new).collect(Collectors.toList());
    }

    /**
     * 构建树结构
     *
     * @param depts 部门列表
     * @return 树结构列表
     */
    private List<DeptInfoDO> buildDeptTree(List<DeptInfoDO> depts) {
        List<DeptInfoDO> returnList = new ArrayList<>();
        List<Long> tempList = depts.stream().map(DeptInfoDO::getDeptId).toList();
        for (DeptInfoDO dept : depts) {
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
    public boolean saveDept(DeptInfoDO dept) {
        DeptInfoDO parent = deptInfoMapper.selectByIdForUpdate(dept.getParentId());
        if (parent != null) {
            Long[] newAncestors = Arrays.copyOf(parent.getAncestors(), parent.getAncestors().length + 1);
            newAncestors[newAncestors.length - 1] = parent.getDeptId();
            dept.setAncestors(newAncestors);
        }
        dept.setParentId(parent == null ? null : dept.getParentId());
        return deptInfoMapper.insert(dept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyDept(DeptInfoDO dept) {
        DeptInfoDO parent = deptInfoMapper.selectByIdForUpdate(dept.getParentId());
        if (ObjectUtils.isNotEmpty(parent)) {
            Long[] newAncestors = Arrays.copyOf(parent.getAncestors(), parent.getAncestors().length + 1);
            newAncestors[newAncestors.length - 1] = parent.getDeptId();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, dept.getAncestors());
        }
        return deptInfoMapper.updateById(dept) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long deptId) {
        boolean success = deptInfoMapper.deleteById(deptId) > 0;
        if (success) {
            roleToDeptMapper.deleteByDeptId(deptId);
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
        List<DeptInfoDO> children = deptInfoMapper.listSubsByParentId(deptId);
        List<DeptInfoDO> updateList = children.stream()
                .map(child -> {
                    // 替换旧的祖先路径为新的祖先路径
                    Long[] updatedAncestors = replaceAncestors(child.getAncestors(), oldAncestors, newAncestors);

                    // 创建新的部门对象，设置更新后的祖先路径
                    DeptInfoDO deptInfoDO = new DeptInfoDO(child.getDeptId());
                    deptInfoDO.setAncestors(updatedAncestors);
                    return deptInfoDO;
                })
                .toList();
        if (!CollectionUtils.isEmpty(updateList)) {
            // 批量更新
            deptInfoMapper.updateById(updateList);
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
    private void recursionFn(List<DeptInfoDO> list, DeptInfoDO t) {
        // 得到子节点列表
        List<DeptInfoDO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (DeptInfoDO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<DeptInfoDO> getChildList(List<DeptInfoDO> list, DeptInfoDO t) {
        List<DeptInfoDO> tlist = new ArrayList<>();
        for (DeptInfoDO n : list) {
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
    private boolean hasChild(List<DeptInfoDO> list, DeptInfoDO t) {
        return !CollectionUtils.isEmpty(getChildList(list, t));
    }

}
