package com.example.nettyrpcframework;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyManager {

    public static Map<String, Channel> clientChannelMap = new ConcurrentHashMap<>();

    public static Channel createClientChannel(String ip, int port) throws InterruptedException {
        NioEventLoopGroup group = null;
        // 创建线程组
        group = new NioEventLoopGroup();
        //  创建启动助手
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_TIMEOUT, 3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();
                        // String 编解码器
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        // 客户端处理类
                        pipeline.addLast(new NettyClientHandler());
                    }
                });
        Channel clientChannel = bootstrap.connect(ip, port).sync().channel();
        Channel oldChannel = clientChannelMap.put(ip + port, clientChannel);
        if (oldChannel != null) {
            oldChannel.close();
        }
        System.out.println("===========客户端启动成功==========");
        return clientChannel;
    }


    public static void runServer(int port, NettyServerHandler nettyServerHandler) {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(nettyServerHandler); //业务处理类

                        }

                    }); //自定义一个初始化类

            bootstrap.bind(port).sync();
            System.out.println("服务提供方启动，开始监听了......");

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }
    }

    /**
     * {@link NettyClientHandler @NettyClientHandler}
     */
    public static void sendAndGetResult(RPCRequest rpcRequest) throws InterruptedException, NacosException {
        synchronized (rpcRequest) {
            Instance instance = PublishService.selectOneInstance(rpcRequest.getInterfaceStr());
            Channel channel = clientChannelMap.get(instance.getIp() + instance.getPort());
            if (channel == null) {
                channel = createClientChannel(instance.getIp(), instance.getPort());
            }
            // 将任务交给netty(新线程)处理
            channel.writeAndFlush(rpcRequest);
            // 等待消息返回，并唤醒当前线程 @com.example.nettyrpcframework.NettyClientHandler.channelRead

            rpcRequest.wait();
        }
    }
}
