package com.example.nettyrpcframework;


import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class NettyClientHandler extends ChannelDuplexHandler {

    private final Map<String, RPCRequest> futureMap = new ConcurrentHashMap<>();

    /**
     * 收到服务器的数据后，就会被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RPCResponse rpcResponse = JSONObject.parseObject(msg.toString(), RPCResponse.class);
        RPCRequest rpcRequest = futureMap.remove(rpcResponse.getUnique());
        rpcRequest.setResponse(rpcResponse);
        synchronized (rpcRequest) {
            rpcRequest.notify();
        }
    }


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RPCRequest) {
            RPCRequest request = (RPCRequest) msg;
            futureMap.putIfAbsent(request.getUnique(), request);
        }
        super.write(ctx, JSONObject.toJSONString(msg), promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }

}
