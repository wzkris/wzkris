package com.thingslink.user.api.fallback;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.enums.BizCode;
import com.thingslink.user.api.RemoteSysUserApi;
import com.thingslink.user.api.domain.dto.LoginInfoDTO;
import com.thingslink.user.api.domain.dto.SysPermissionDTO;
import com.thingslink.user.api.domain.dto.SysUserDTO;
import com.thingslink.user.api.domain.vo.RouterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.thingslink.common.core.domain.Result.resp;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 系统用户API降级
 * @date : 2023/8/21 13:03
 */
@Slf4j
@Component
public class RemoteSysUserApiFallback implements FallbackFactory<RemoteSysUserApi> {

    @Override
    public RemoteSysUserApi create(Throwable cause) {
        log.error("-----------用户服务发生熔断-----------");
        return new RemoteSysUserApi() {
            @Override
            public Result<SysUserDTO> getByUsername(String username) {
                log.error("查询系统用户信息发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public Result<SysPermissionDTO> getPermission(Long userId, Long deptId) {
                log.error("查询系统用户权限发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

            @Override
            public void updateLoginInfo(LoginInfoDTO loginInfoDTO) {
                log.error("更新用户登录信息发生异常，errMsg：{}", cause.getMessage(), cause);
            }

            @Override
            public Result<List<RouterVO>> getRouter(Long userId) {
                log.error("查询系统用户前端路由发生异常，errMsg：{}", cause.getMessage(), cause);
                return resp(BizCode.RPC_INVOCATION, cause.getMessage());
            }

        };
    }
}
