package com.example.nettyrpc.api.interfaces;


import com.example.nettyrpcframework.NettyRpc;

@NettyRpc
public interface HiService {
    public String hi(String msg);
}
