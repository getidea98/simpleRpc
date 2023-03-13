package com.example.nettyrpc.client.autoconfig;

import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/**
 * 验证指定的模板是否支持
 * @author Administrator 2021/4/1 13:22
 * @version 1.0
 */
public class MyTemplateAvailabilityProvider implements TemplateAvailabilityProvider {

    @Override
    public boolean isTemplateAvailable(String view, Environment environment,
                                       ClassLoader classLoader, ResourceLoader resourceLoader) {
        System.out.println("MyTemplateAvailabilityProvider.isTemplateAvailable() view=" +
                view + ", environment=" + environment + ", classLoader=" + classLoader +
                "resourceLoader=" + resourceLoader);
        return false;
    }

}
