package com.example.nettyrpcframework;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private final HashMap<String, Object> serverMapping = new HashMap<>();

    public NettyServerHandler() {

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String jsonStr = null;
        RPCRequest rpcRequest = JSONObject.parseObject(msg.toString(), RPCRequest.class);
        try {
            Object service = serverMapping.get(rpcRequest.getInterfaceStr());
            Method declaredMethod = service.getClass().getDeclaredMethod(rpcRequest.getMethodStr(), rpcRequest.getParamType());
            Object result = declaredMethod.invoke(service, rpcRequest.getParam());
            RPCResponse rpcResponse = new RPCResponse();
            rpcResponse.setRespStr(result.toString());
            rpcResponse.setUnique(rpcRequest.getUnique());
            jsonStr = JSONObject.toJSONString(rpcResponse);
        } catch (NoSuchMethodException e) {
            jsonStr = "RPC未实现方法:" + rpcRequest.getMethodStr();
        } catch (InvocationTargetException e) {
            jsonStr = "RPC被调用方法抛出异常:" + e.getMessage();
        } catch (IllegalAccessException e) {
            jsonStr = "RPC访问被调用方法权限不足:" + e.getMessage();
        }
        ctx.writeAndFlush(jsonStr);
        ctx.flush();
    }

    public void addNettyServerHandler(String serverName, Object object) {
        serverMapping.put(serverName, object);
    }
}
