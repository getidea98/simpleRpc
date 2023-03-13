package com.example.nettyrpc.client.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;

import java.util.Arrays;

public class MyConfigurationCondition implements AutoConfigurationImportFilter {

    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        System.out.println("MyConfigurationCondition.match() autoConfigurationClasses=" +
                Arrays.toString(autoConfigurationClasses) +
                ", autoConfigurationMetadata=" + autoConfigurationMetadata);
        return new boolean[0];
    }

}
