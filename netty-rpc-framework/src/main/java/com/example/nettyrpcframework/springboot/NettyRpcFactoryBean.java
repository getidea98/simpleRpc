package com.example.nettyrpcframework.springboot;

import com.example.nettyrpcframework.dynamicProxy.JDKDynamicProxy;
import org.springframework.beans.factory.FactoryBean;

public class NettyRpcFactoryBean implements FactoryBean {

    private Class mapperInterface;


    public NettyRpcFactoryBean(){

    }


    public NettyRpcFactoryBean(Class mapperInterface){
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object getObject() throws Exception {
        return JDKDynamicProxy.object.create(mapperInterface);
    }

    @Override
    public Class getObjectType() {
        return mapperInterface;
    }
}
