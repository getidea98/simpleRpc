package com.example.nettyrpcframework.springboot;


import java.lang.annotation.*;


/**
 * 该注解标记在接口的实现类上，用于表用，当前实现类实现的接口
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface NettyRpcServer {

    Class[] value() default {};

    Class[] basePackages() default {};
}
