package com.example.nettyrpcframework.springboot;

import com.alibaba.nacos.api.exception.NacosException;
import com.example.nettyrpcframework.NettyManager;
import com.example.nettyrpcframework.NettyServerHandler;
import com.example.nettyrpcframework.PublishService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.net.UnknownHostException;
import java.util.Arrays;

public class ServerPublishApplicationListener implements ApplicationListener<ApplicationEvent>, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public ServerPublishApplicationListener() {

    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof org.springframework.boot.context.event.ApplicationStartedEvent) {
            System.out.println("MyApplicationListener.onApplicationEvent() " + event);
            String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(NettyRpcServer.class);
            NettyServerHandler nettyServerHandler = applicationContext.getBean(NettyServerHandler.class);

            if (beanNamesForAnnotation.length > 0) {
                // 集合不等于空，则必定有接口的实现类，则开启服务端的netty
                NettyManager.runServer(8888, nettyServerHandler);
            }

            for (String s : beanNamesForAnnotation) {
                Object bean = applicationContext.getBean(s);
                String[] nettyRpcServerClassName = getNettyRpcServerClassName(bean);
                Arrays.stream(nettyRpcServerClassName)
                        .forEach(serviceName -> {
                            nettyServerHandler.addNettyServerHandler(serviceName, bean);
                            try {
                                PublishService.registerService(serviceName, 8888);
                            } catch (NacosException e) {

                            } catch (UnknownHostException e) {

                            }
                        });
            }
        }
    }

    private String[] getNettyRpcServerClassName(Object bean) {
        NettyRpcServer annotation = bean.getClass().getAnnotation(NettyRpcServer.class);
        Class[] value = annotation.value();
        String[] strings = new String[value.length];
        for (int i = 0; i < value.length; i++) {
            strings[i] = value[i].getName();
        }
        return strings;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}