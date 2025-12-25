package com.wzkris.usercenter.service;

import com.wzkris.usercenter.domain.TenantInfoDO;
import com.wzkris.usercenter.domain.vo.SelectVO;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
public interface TenantInfoService {

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
    boolean saveTenant(TenantInfoDO tenant, String username, String password);

    /**
     * 删除租户及相关信息(hard delete)
     *
     * @param tenantId 租户ID
     */
    boolean removeById(Long tenantId);

    /**
     * 校验租户账号数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkAccountLimit(Long tenantId);

    /**
     * 校验租户职位数量
     *
     * @param tenantId 租户ID
     * @return true通过 false不通过
     */
    boolean checkPostLimit(Long tenantId);

    /**
     * 校验是否租户超管
     *
     * @param memberIds 用户ID
     */
    void checkAdministrator(List<Long> memberIds);

    default void checkAdministrator(Long memberId) {
        this.checkAdministrator(Collections.singletonList(memberId));
    }

}
