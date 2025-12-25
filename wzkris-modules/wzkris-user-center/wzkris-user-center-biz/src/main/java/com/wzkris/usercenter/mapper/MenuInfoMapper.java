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
    List<MenuInfoDO> listMenuRoutes(@Nullable List<Long> menuIds, String scope);

    /**
     * 根据ID集合查询权限
     *
     * @param menuIds 角色ID集合
     * @return 权限列表
     */
    @Select("""
            <script>
                SELECT perms FROM biz.menu_info WHERE status = '0' AND menu_id IN
                    <foreach collection="menuIds" item="menuId" separator="," open="(" close=")">
                        #{menuId}
                    </foreach>
            </script>
            """)
    List<String> listPermsByMenuIds(List<Long> menuIds);

}
