package com.wzkris.common.dubbo.filter;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.core.utils.TraceIdUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

@Activate(
        group = {
                org.apache.dubbo.common.constants.CommonConstants.PROVIDER,
                org.apache.dubbo.common.constants.CommonConstants.CONSUMER
        },
        order = Integer.MAX_VALUE)
public class DubboRequestFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (RpcContext.getServiceContext().isConsumerSide()) {
            String tracingId = TraceIdUtil.get();
            if (ObjectUtils.isNotEmpty(tracingId)) {
                RpcContext.getServiceContext().setAttachment(HeaderConstants.X_TRACING_ID, tracingId);
            }
        } else {
            String tracingId = RpcContext.getServiceContext().getAttachment(HeaderConstants.X_TRACING_ID);
            if (StringUtil.isNotBlank(tracingId)) {
                TraceIdUtil.set(tracingId);
            }
        }

        try {
            return invoker.invoke(invocation);
        } finally {
            if (RpcContext.getServiceContext().isProviderSide()) {
                TraceIdUtil.clear();
            }
        }
    }

}
