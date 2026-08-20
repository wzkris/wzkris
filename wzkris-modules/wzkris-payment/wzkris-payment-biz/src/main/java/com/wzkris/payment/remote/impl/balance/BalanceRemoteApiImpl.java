package com.wzkris.payment.remote.impl.balance;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.payment.domain.CustomerBalanceInfoDO;
import com.wzkris.payment.domain.TenantBalanceInfoDO;
import com.wzkris.payment.remote.api.balance.BalanceRemoteApi;
import com.wzkris.payment.remote.api.balance.request.BalanceDecryRequest;
import com.wzkris.payment.remote.api.balance.request.BalanceIncryRequest;
import com.wzkris.payment.remote.api.balance.request.BalanceQueryRequest;
import com.wzkris.payment.remote.api.balance.response.BalanceQueryResponse;
import com.wzkris.payment.service.CustomerBalanceInfoService;
import com.wzkris.payment.service.TenantBalanceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 余额/账户内部接口实现：按认证类型分发到租户/客户余额账户
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class BalanceRemoteApiImpl extends AbstractApi implements BalanceRemoteApi {

    private final TenantBalanceInfoService tenantBalanceInfoService;

    private final CustomerBalanceInfoService customerBalanceInfoService;

    @Override
    public Result<BalanceQueryResponse> query(BalanceQueryRequest request) {
        BalanceQueryResponse resp = new BalanceQueryResponse();
        resp.setOwnerId(request.getOwnerId());
        if (request.getAuthType() == AuthTypeEnum.TENANT) {
            TenantBalanceInfoDO balance = tenantBalanceInfoService.getOrCreate(request.getOwnerId());
            resp.setBalance(balance.getBalance());
        } else {
            CustomerBalanceInfoDO balance = customerBalanceInfoService.getOrCreate(request.getOwnerId());
            resp.setBalance(balance.getBalance());
        }
        return ok(resp);
    }

    @Override
    public Result<Void> incry(BalanceIncryRequest request) {
        boolean suc = request.getAuthType() == AuthTypeEnum.TENANT
                ? tenantBalanceInfoService.incryBalance(request.getOwnerId(), request.getAmount(),
                request.getBizNo(), request.getBizType(), request.getRemark())
                : customerBalanceInfoService.incryBalance(request.getOwnerId(), request.getAmount(),
                request.getBizNo(), request.getBizType(), request.getRemark());
        return toRes(suc);
    }

    @Override
    public Result<Void> decry(BalanceDecryRequest request) {
        boolean suc = request.getAuthType() == AuthTypeEnum.TENANT
                ? tenantBalanceInfoService.decryBalance(request.getOwnerId(), request.getAmount(),
                request.getBizNo(), request.getBizType(), request.getRemark())
                : customerBalanceInfoService.decryBalance(request.getOwnerId(), request.getAmount(),
                request.getBizNo(), request.getBizType(), request.getRemark());
        return toRes(suc);
    }

}