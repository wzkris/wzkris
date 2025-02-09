package com.wzkris.common.dubbo.filter;

import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.jboss.logging.MDC;

@Activate(group = {org.apache.dubbo.common.constants.CommonConstants.PROVIDER,
        org.apache.dubbo.common.constants.CommonConstants.CONSUMER}, order = Integer.MAX_VALUE)
public class DubboRequestFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (RpcContext.getServiceContext().isConsumerSide()) {
            Object tracingId = MDC.get(CommonConstants.X_TRACING_ID);
            if (StringUtil.isNotNull(tracingId)) {
                RpcContext.getServiceContext().setAttachment(CommonConstants.X_TRACING_ID, tracingId);
            }
        } else {
            String tracingId = RpcContext.getServiceContext().getAttachment(CommonConstants.X_TRACING_ID);
            if (StringUtil.isNotBlank(tracingId)) {
                MDC.put(CommonConstants.X_TRACING_ID, tracingId);
            }
        }

        Result result = invoker.invoke(invocation);

        if (RpcContext.getServiceContext().isProviderSide()) {
            MDC.clear();
        }

        return result;
    }

}
