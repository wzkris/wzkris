package com.thingslink.user.api;

import com.thingslink.common.core.domain.Result;
import com.thingslink.user.api.domain.dto.CustomerDTO;
import com.thingslink.user.api.domain.dto.LoginInfoDTO;
import com.thingslink.user.api.fallback.RemoteCustomerApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import static com.thingslink.common.core.constant.SecurityConstants.INNER_REQUEST_PATH;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 系统用户
 * @date : 2024/4/15 16:20
 */
@FeignClient(value = "thingslink-user", contextId = "RemoteCustomerApi",
        fallbackFactory = RemoteCustomerApiFallback.class)
public interface RemoteCustomerApi {

    /**
     * 根据手机号查询app用户
     */
    @GetMapping(INNER_REQUEST_PATH + "/query_app_user_by_phonenumber")
    Result<CustomerDTO> getByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber);

    /**
     * 更新用户登录信息
     */
    @PutMapping(INNER_REQUEST_PATH + "/update_app_user_logininfo")
    void updateLoginInfo(@RequestBody LoginInfoDTO loginInfoDTO);
}
