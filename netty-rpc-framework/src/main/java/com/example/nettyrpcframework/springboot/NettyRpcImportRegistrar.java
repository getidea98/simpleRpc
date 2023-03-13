package com.example.nettyrpcframework.springboot;

import com.example.nettyrpcframework.NettyRpc;
import com.example.nettyrpcframework.NettyServerHandler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 扫描EnableNettyRpc注解参数包路径下面的被NettyRpc标记的接口信息。
 * 并将接口信息封装到beanDefinition，注册到spring容器中。
 * 随后生成该接口的代理对象
 */
public class NettyRpcImportRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, BeanPostProcessor {

    public NettyRpcImportRegistrar(){

    }

    /**
     * 资源加载器
     */
    private ResourceLoader resourceLoader;

    /**
     * 在程序里面NettyRpcServer注解的value属性内的class对象集合，将不会在产生动态代理对象
     */
    List<String> excludeInterfaceList = new ArrayList<>();


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        registerServerBeanDefinitions(registry);
        registerClientBeanDefinitions(metadata, registry);
    }

    public void registerClientBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // 创建scanner
        NettyRpcScanner scanner = new NettyRpcScanner(registry);
        scanner.setResourceLoader(resourceLoader);

        // 设置扫描器scanner扫描的过滤条件
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(NettyRpc.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        ClassExcludeFilter classExcludeFilter = new ClassExcludeFilter();
        scanner.addExcludeFilter(classExcludeFilter);

        // 获取指定要扫描的basePackages
        String[] basePackages = getBasePackages(metadata);
        scanner.doScan(basePackages);
    }

    /**
     * 扫描当前程序下面，所有被NettyRpcServer标记的类。
     * 1、将所有的接口添加到集合中，因为已有实现类，则要避免后面产生接口的代理对象
     * 2、将NettyServerHandler扫描进spring容器
     * 2、将当前bean放入NettyServerHandler的map中
     *
     * @param registry
     */
    public void registerServerBeanDefinitions(BeanDefinitionRegistry registry) {
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            NettyRpcServer nettyRpcServer = getNettyRpcServer(beanDefinition);
            if (nettyRpcServer == null) {
                continue;
            }
            for (Class aClass : nettyRpcServer.value()) {
                excludeInterfaceList.add(aClass.getName());
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(NettyServerHandler.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition("nettyServerHandler", beanDefinition);

        beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ServerPublishApplicationListener.class);
        beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition("serverPublishApplicationListener", beanDefinition);
    }

    /**
     * 从beanDefinition中获取NettyRpcServer的信息。返回null则是没有被NettyRpcServer标记
     *
     * @param beanDefinition
     * @return
     */
    private NettyRpcServer getNettyRpcServer(BeanDefinition beanDefinition) {
        if (!(beanDefinition instanceof ScannedGenericBeanDefinition)) {
            return null;
        }
        String beanClassName = beanDefinition.getBeanClassName();
        try {
            Class<?> aClass = Class.forName(beanClassName);
            return aClass.getAnnotation(NettyRpcServer.class);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取base packages
     */
    protected static String[] getBasePackages(AnnotationMetadata importingClassMetadata) {
        // 获取到@EnableSimpleRpcClients注解所有属性
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableNettyRpc.class.getCanonicalName());
        Set<String> basePackages = new HashSet<>();
        assert attributes != null;
        // value 属性是否有配置值，如果有则添加
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        // basePackages 属性是否有配置值，如果有则添加
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        // 如果上面两步都没有获取到basePackages，那么这里就默认使用当前项目启动类所在的包为basePackages
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        String[] strings = new String[basePackages.size()];
        return basePackages.toArray(strings);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private class ClassExcludeFilter extends AbstractTypeHierarchyTraversingFilter {

        ClassExcludeFilter() {
            super(false, false);
        }

        @Override
        protected boolean matchClassName(String className) {
            return excludeInterfaceList.contains(className);
        }

    }

}
