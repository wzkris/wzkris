package com.wzkris.principal.feign.staff;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.staff.fallback.StaffInfoFeignFallback;
import com.wzkris.principal.feign.staff.req.QueryStaffPermsReq;
import com.wzkris.principal.feign.staff.resp.StaffInfoResp;
import com.wzkris.principal.feign.staff.resp.StaffPermissionResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 员工管理
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.PRINCIPAL, contextId = "StaffInfoFeign",
        fallbackFactory = StaffInfoFeignFallback.class,
        path = "/feign-staff")
public interface StaffInfoFeign {

    /**
     * 根据用户名查询用户
     */
    @PostMapping("/query-by-username")
    StaffInfoResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostMapping("/query-by-phonenumber")
    StaffInfoResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostMapping("/query-permission")
    StaffPermissionResp getPermission(@RequestBody QueryStaffPermsReq staffPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
