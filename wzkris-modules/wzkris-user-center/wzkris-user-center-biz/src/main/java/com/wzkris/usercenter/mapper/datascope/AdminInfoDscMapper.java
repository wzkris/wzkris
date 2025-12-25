package com.wzkris.usercenter.mapper.datascope;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.annotation.DataColumn;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.AdminInfoDO;
import com.wzkris.usercenter.domain.vo.admin.AdminMngVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * 管理员表 数据权限层
 *
 * @author wzkris
 */
@DataScope(value = {@DataColumn(column = "dept_id")})
@Mapper
@Repository
public interface AdminInfoDscMapper extends BaseMapperPlus<AdminInfoDO> {

    /**
     * 带权限查询分页数据
     */
    @DataScope(value = {@DataColumn(alias = "d", column = "dept_id")})
    @Select("""
            SELECT u.*, d.dept_name, d.status AS deptStatus
                    		FROM biz.admin_info u LEFT JOIN biz.dept_info d ON u.dept_id = d.dept_id
                    ${ew.customSqlSegment}
            """)
    List<AdminMngVO> selectVOList(@Param(Constants.WRAPPER) Wrapper<AdminInfoDO> queryWrapper);

    /**
     * 带权限查询列表
     */
    default List<AdminInfoDO> selectLists(Wrapper<AdminInfoDO> queryWrapper) {
        return this.selectList(queryWrapper);
    }

    /**
     * 检验权限
     *
     * @param adminIds 待操作管理员id
     * @return 返回是否
     */
    @Select("""
            <script>
                SELECT CASE WHEN COUNT(DISTINCT admin_id) = ${adminIds.size()} THEN true ELSE false END
                    FROM biz.admin_info WHERE admin_id IN
                    <foreach collection="collection" item="adminId" open="(" separator="," close=")">
                        <if test="adminId != null and adminId != ''">
                            #{adminId}
                        </if>
                    </foreach>
            </script>
            """)
    boolean checkDataScopes(Collection<Long> adminIds);

}
