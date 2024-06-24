package com.thingslink.equipment.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳处理
 */
@Slf4j
public class HeartbeatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        // 如果接收到的消息是"ok"，则回复"ok"
        if ("ok".equals(frame.text())) {
            log.info("收到心跳，时间：{}", System.currentTimeMillis());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("ok"));
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 定义一个字符串，用于存储超时事件的类型
        if (evt instanceof IdleStateEvent event) {
            if (event.state().equals(IdleState.READER_IDLE)) {
                log.info("关闭连接，释放更多资源");
                ctx.channel().writeAndFlush(new TextWebSocketFrame("close"));
                ctx.channel().close();
            }
        }
    }

}
