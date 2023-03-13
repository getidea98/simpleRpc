package com.example.nettyrpcframework.springboot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(NettyRpcImportRegistrar.class)
public @interface EnableNettyRpc {

    /**
     * 接口所在包
     * @return
     */
    String[] value() default {};

    /**
     * 接口所在包
     * @return
     */
    String[] basePackages() default {};
}
