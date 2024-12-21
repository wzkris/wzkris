//package com.wzkris.equipment.netty;
//
//import com.wzkris.equipment.netty.handler.HeartbeatHandler;
//import com.wzkris.equipment.netty.handler.PubMessageHandler;
//import com.wzkris.equipment.netty.handler.QueryParamExtractorHandler;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
//import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import io.netty.handler.timeout.IdleStateHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class NettyServer implements CommandLineRunner {
//
//    public static final String WEBSOCKET_URI = "/device_room";
//
//    @Override
//    public void run(String... args) throws Exception {
//        EventLoopGroup boosGroup = new NioEventLoopGroup();
//        EventLoopGroup workGroup = new NioEventLoopGroup();
//
//        ServerBootstrap bootstrap = new ServerBootstrap();
//        try {
//            bootstrap.group(boosGroup, workGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    //设置保持活动连接状态
//                    .childOption(ChannelOption.SO_KEEPALIVE, true)
//                    //使用匿名内部类的形式初始化通道对象
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline()
//                                    //给pipeline管道设置处理器
//                                    .addLast(new HttpServerCodec())
//                                    .addLast(new WebSocketServerCompressionHandler())// WebSocket消息压缩处理器
//                                    .addLast(new ChunkedWriteHandler())
//                                    .addLast(new HttpObjectAggregator(8192))
//                                    .addLast(new QueryParamExtractorHandler())// 参数校验
//                                    .addLast(new WebSocketServerProtocolHandler(WEBSOCKET_URI, null,
//                                            true, 65535, true, true))// 添加WebSocket握手处理器
//                                    .addLast(new IdleStateHandler(10, 0, 0))// 监控读超时事件
//                                    .addLast(new HeartbeatHandler())
//                                    .addLast(new PubMessageHandler());
//                        }
//                    });//给workerGroup的EventLoop对应的管道设置处理器
//            log.info("netty服务端启动...");
//            //绑定端口号，启动服务端
//            ChannelFuture channelFuture = bootstrap.bind(6543).sync();
//            //对关闭通道进行监听
//            channelFuture.channel().closeFuture().sync();
//        }
//        finally {
//            boosGroup.shutdownGracefully();
//            workGroup.shutdownGracefully();
//        }
//    }
//
//}
