package com.wzkris.principal.feign.member;

import com.wzkris.common.openfeign.constants.ServiceIdConstant;
import com.wzkris.principal.feign.admin.req.LoginInfoReq;
import com.wzkris.principal.feign.member.fallback.MemberInfoFeignFallback;
import com.wzkris.principal.feign.member.req.QueryMemberPermsReq;
import com.wzkris.principal.feign.member.resp.MemberInfoResp;
import com.wzkris.principal.feign.member.resp.MemberPermissionResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 租户成员管理
 * @date : 2024/4/15 16:20
 */
@FeignClient(name = ServiceIdConstant.PRINCIPAL, contextId = "MemberInfoFeign",
        fallbackFactory = MemberInfoFeignFallback.class,
        path = "/feign-member")
public interface MemberInfoFeign {

    /**
     * 根据用户名查询用户
     */
    @PostMapping("/query-by-username")
    MemberInfoResp getByUsername(@RequestBody String username);

    /**
     * 根据手机号查询用户
     */
    @PostMapping("/query-by-phonenumber")
    MemberInfoResp getByPhoneNumber(@RequestBody String phoneNumber);

    /**
     * 查询管理员权限
     */
    @PostMapping("/query-permission")
    MemberPermissionResp getPermission(@RequestBody QueryMemberPermsReq memberPermsReq);

    /**
     * 更新用户登录信息
     */
    @PostMapping("/update-logininfo")
    void updateLoginInfo(@RequestBody LoginInfoReq loginInfoReq);

}
