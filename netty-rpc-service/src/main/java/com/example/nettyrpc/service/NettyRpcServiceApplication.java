package com.example.nettyrpc.service;

import com.example.nettyrpcframework.springboot.EnableNettyRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNettyRpc("com.example.nettyrpc.api.interfaces")
public class NettyRpcServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcServiceApplication.class, args);
    }


}
