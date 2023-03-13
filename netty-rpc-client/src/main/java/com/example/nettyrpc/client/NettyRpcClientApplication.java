package com.example.nettyrpc.client;

import com.example.nettyrpcframework.springboot.EnableNettyRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableNettyRpc("com.example.nettyrpc.api.interfaces")
public class NettyRpcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyRpcClientApplication.class, args);
    }

}
