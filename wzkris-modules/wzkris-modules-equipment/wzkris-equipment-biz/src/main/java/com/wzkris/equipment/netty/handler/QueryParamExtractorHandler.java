package com.wzkris.equipment.netty.handler;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.equipment.constants.DeviceConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 参数处理
 */
@Slf4j
public class QueryParamExtractorHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest request) {
            String uri = request.uri();
            // 判断请求路径是否跟配置中的一致
            String[] uriArray = uri.split("/");
            String roomNo = uriArray[uriArray.length - 1];
            if (StringUtil.isBlank(roomNo) || !RedisUtil.hasKey(DeviceConstant.ROOM_PREFIX + roomNo)) {
                // 请求地址错误，关闭通道
                log.error("Request roomNo is invalid: {}", roomNo);
                ctx.close();
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception caught", cause);
        ctx.close();
    }
}
