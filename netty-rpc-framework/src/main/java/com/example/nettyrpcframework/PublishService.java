package com.example.nettyrpcframework;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class PublishService {

    /**
     * 操作 Nacos
     */
    private static final NamingService namingService;

    static {
        try {
            namingService = getNacosNamingService();
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String DEFAULT_NACOS_ADDRESS = "172.22.17.7:8848";

    private static NamingService getNacosNamingService() throws NacosException {
        return NamingFactory.createNamingService(DEFAULT_NACOS_ADDRESS);
    }

    /**
     * 根据服务名称和地址注册服务
     * @param serviceName 服务名称
     * @param address 服务地址
     * @throws NacosException
     */
    public static void registerService(String serviceName, int port) throws NacosException, UnknownHostException {
        namingService.registerInstance(serviceName, InetAddress.getLocalHost().getHostAddress(), port);
    }

    /**
     * 根据服务名称 选择一个健康的节点
     * @param serviceName 服务名称
     * @throws NacosException
     */
    public static Instance selectOneInstance(String serviceName) throws NacosException {
        return namingService.selectOneHealthyInstance(serviceName);
    }
}
