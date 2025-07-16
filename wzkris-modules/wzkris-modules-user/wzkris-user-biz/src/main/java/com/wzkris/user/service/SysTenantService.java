package com.wzkris.user.service;

import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.vo.SelectVO;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
public interface SysTenantService {

    /**
     * 租户选择列表
     *
     * @param tenantName 租户名称
     */
    List<SelectVO> listSelect(@Nullable String tenantName);

    /**
     * 添加租户, 会创建租户管理员账号
     *
     * @param tenant   参数
     * @param username 登录账户
     * @param password 登录密码
     */
    boolean insertTenant(SysTenant tenant, String username, String password);

    /**
     * 删除租户及相关信息(hard delete)
     *
     * @param tenantId 租户ID
     */
    boolean deleteById(Long tenantId);

    /**
     * 校验租户账号数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkAccountLimit(Long tenantId);

    /**
     * 校验租户角色数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkRoleLimit(Long tenantId);

    /**
     * 校验租户部门数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkDeptLimit(Long tenantId);

    /**
     * 校验是否租户超管
     *
     * @param userIds 用户ID
     * @return true通过 false不通过
     */
    boolean checkAdministrator(List<Long> userIds);

    default boolean checkAdministrator(Long userId) {
        return this.checkAdministrator(Collections.singletonList(userId));
    }

    /**
     * 校验是否有租户的数据权限
     *
     * @param tenantId 租户ID
     */
    void checkDataScope(Long tenantId);

}
