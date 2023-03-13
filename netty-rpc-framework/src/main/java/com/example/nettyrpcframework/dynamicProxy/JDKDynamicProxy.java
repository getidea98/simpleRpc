package com.example.nettyrpcframework.dynamicProxy;

import com.example.nettyrpcframework.NettyManager;
import com.example.nettyrpcframework.RPCRequest;

import java.lang.reflect.Proxy;

public class JDKDynamicProxy {

    public static JDKDynamicProxy object = new JDKDynamicProxy();

    private JDKDynamicProxy() {

    }

    /**
     * 1、动态代理
     * 2、JSON序列化和反序列化
     * 3、网络通讯（socket或者netty）
     * @param cls
     * @return
     */
    public Object create(final Class cls) {
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, (proxyObject, method, params) -> {
            RPCRequest rpcRequest = new RPCRequest();
            rpcRequest.setInterfaceStr(cls.getName());
            rpcRequest.setMethodStr(method.getName());
            rpcRequest.setParam(params);
            rpcRequest.setUnique(String.valueOf(System.currentTimeMillis()));
            Class[] paramTypes = new Class[params.length];
            rpcRequest.setParamType(paramTypes);
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }
            NettyManager.sendAndGetResult(rpcRequest);
            return rpcRequest.getResponse().getRespStr();
        });
    }
}
