package com.wzkris.payment.remote.api.balance;

import com.wzkris.common.core.model.Result;
import com.wzkris.payment.remote.api.balance.request.BalanceDecryRequest;
import com.wzkris.payment.remote.api.balance.request.BalanceIncryRequest;
import com.wzkris.payment.remote.api.balance.request.BalanceQueryRequest;
import com.wzkris.payment.remote.api.balance.response.BalanceQueryResponse;

/**
 * 余额/账户内部接口（服务间调用）：余额查询 + 加/减余额（守卫扣款）
 *
 * <p>按认证类型（租户/客户）分发到对应余额账户。订单/营销等业务服务通过它驱动资金。
 *
 * @author wzkris
 */
public interface BalanceRemoteApi {

    /**
     * 查询余额（账户不存在时懒创建）
     */
    Result<BalanceQueryResponse> query(BalanceQueryRequest request);

    /**
     * 增加余额（入账）
     */
    Result<Void> incry(BalanceIncryRequest request);

    /**
     * 扣减余额（守卫：余额不足拒绝）
     */
    Result<Void> decry(BalanceDecryRequest request);

}