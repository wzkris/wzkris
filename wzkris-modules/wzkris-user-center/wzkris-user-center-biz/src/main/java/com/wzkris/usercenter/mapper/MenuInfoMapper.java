package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.MenuInfoDO;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface MenuInfoMapper extends BaseMapperPlus<MenuInfoDO> {

    /**
     * 查询前端可见的菜单路由(按钮除外)
     *
     * @return 菜单列表
     */
    @Select("""
            <script>
                SELECT * FROM biz.menu_info
                WHERE menu_type IN ('D', 'M', 'I', 'O') AND status = '0'
                    AND scope = #{scope}
                <if test="menuIds != null and !menuIds.isEmpty()">
                    AND menu_id IN
                    <foreach collection="menuIds" item="menuId" separator="," open="(" close=")">
                        #{menuId}
                    </foreach>
                </if>
            </script>
            """)
    List<MenuInfoDO> listMenuRoutes(@Nullable List<Long> menuIds, String scope);

}
