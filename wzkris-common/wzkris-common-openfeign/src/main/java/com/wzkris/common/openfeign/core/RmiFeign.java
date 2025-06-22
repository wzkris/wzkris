package com.wzkris.common.openfeign.core;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.openfeign.utils.RpcMsgUtil;

/**
 * 空接口
 *
 * @author wzkris
 * @date 2025/06/12
 */
public interface RmiFeign {

    default void logPrintError(Throwable throwable) {
        String error = RpcMsgUtil.getDetailMsg(throwable);
        FeignLogAggregator.INSTANCE.count(this.getClass().getName() + StringUtil.HASH + error);
    }

}
