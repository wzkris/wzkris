package com.wzkris.common.dubbo.filter;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.jboss.logging.MDC;

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
            Object tracingId = MDC.get(HeaderConstants.X_TRACING_ID);
            if (ObjectUtils.isNotEmpty(tracingId)) {
                RpcContext.getServiceContext().setAttachment(HeaderConstants.X_TRACING_ID, tracingId);
            }
        } else {
            String tracingId = RpcContext.getServiceContext().getAttachment(HeaderConstants.X_TRACING_ID);
            if (StringUtil.isNotBlank(tracingId)) {
                MDC.put(HeaderConstants.X_TRACING_ID, tracingId);
            }
        }

        try {
            return invoker.invoke(invocation);
        } finally {
            if (RpcContext.getServiceContext().isProviderSide()) {
                MDC.clear();
            }
        }
    }

}
