package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.wzkris.common.orm.annotation.DeptScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.req.SysDeptQueryReq;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysDeptMapper extends BaseMapperPlus<SysDept> {

    /**
     * 带权限查询列表
     */
    @DeptScope
    default List<SysDept> selectLists(Wrapper<SysDept> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 根据条件查询所有子部门（包括自身）
     *
     * @param queryReq 查询条件
     * @return 部门列表
     */
    @DeptScope
    List<SysDept> listChildren(SysDeptQueryReq queryReq);

    /**
     * 根据部门ID查询名称
     */
    @Select("SELECT dept_name FROM biz_sys.sys_dept WHERE dept_id = #{deptId}")
    String selectDeptNameById(Long deptId);

    /**
     * 根据ID查询所有子部门id（包括自身）
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    @Select("SELECT dept_id FROM biz_sys.sys_dept WHERE #{deptId} = ANY(ancestors) OR dept_id = #{deptId}")
    List<Long> listChildrenIdById(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）(不包括自身)
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz_sys.sys_dept WHERE #{deptId} = ANY(ancestors) AND status = '0')")
    boolean checkExistNormalChildren(Long deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz_sys.sys_dept WHERE parent_id = #{deptId})")
    boolean checkExistChildren(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT dept_id FROM biz_sys.sys_user WHERE dept_id = #{deptId})")
    boolean checkExistUser(Long deptId);

    /**
     * 查看当前部门是否有待操作部门的操作权限
     *
     * @param deptIds 待操作的部门id
     * @return 是否
     */
    @DeptScope
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT dept_id) = ${deptIds.size()} THEN 1 ELSE 0 END
                    FROM biz_sys.sys_dept WHERE dept_id IN
                    <foreach collection="collection" item="deptId" open="(" separator="," close=")">
                        <if test="deptId != null and deptId != ''">
                            #{deptId}
                        </if>
                    </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> deptIds);
}
