package com.wzkris.user.mapper.datascope;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.vo.SysUserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 用户表 数据权限层
 *
 * @author wzkris
 */
@DataScope(value = {@DataColumn(column = "dept_id")})
@Repository
public interface SysUserDscMapper extends BaseMapperPlus<SysUser> {

    /**
     * 带权限查询分页数据
     */
    @DataScope(value = {@DataColumn(alias = "d", column = "dept_id")})
    @Select("""
            SELECT u.*, d.dept_name, d.status AS deptStatus
                    		FROM biz.sys_user u LEFT JOIN biz.sys_dept d ON u.dept_id = d.dept_id
                    ${ew.customSqlSegment}
            """)
    List<SysUserVO> selectVOList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 带权限查询列表
     */
    default List<SysUser> selectLists(Wrapper<SysUser> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 检验权限
     *
     * @param userIds 待操作管理员id
     * @return 返回是否
     */
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT user_id) = ${userIds.size()} THEN true ELSE false END
                    FROM biz.sys_user WHERE user_id IN
                    <foreach collection="collection" item="userId" open="(" separator="," close=")">
                        <if test="userId != null and userId != ''">
                            #{userId}
                        </if>
                    </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> userIds);

}
