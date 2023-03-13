package com.example.nettyrpc.client.autoconfig;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

public class MyApplicationListener implements ApplicationListener<ApplicationEvent>, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public MyApplicationListener() {

    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof org.springframework.boot.context.event.ApplicationStartedEvent) {
            System.out.println("MyApplicationListener.onApplicationEvent() " + event);
            String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(RestController.class);
            System.out.println(Arrays.toString(beanNamesForAnnotation));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
