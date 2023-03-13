package com.example.nettyrpc.service.ipml;

import com.example.nettyrpc.api.interfaces.HiService;
import com.example.nettyrpcframework.springboot.NettyRpcServer;
import org.springframework.stereotype.Component;


@Component
@NettyRpcServer(HiService.class)
public class HiServiceImpl implements HiService {
    public String hi(String msg) {
        return "hello, I'm server, I want say : " + msg;
    }
}