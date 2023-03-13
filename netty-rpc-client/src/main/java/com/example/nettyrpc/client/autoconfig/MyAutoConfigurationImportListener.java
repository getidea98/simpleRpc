package com.example.nettyrpc.client.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfigurationImportEvent;
import org.springframework.boot.autoconfigure.AutoConfigurationImportListener;
import org.springframework.context.annotation.Import;

public class MyAutoConfigurationImportListener implements AutoConfigurationImportListener {

    @Override
    public void onAutoConfigurationImportEvent(AutoConfigurationImportEvent event) {
        System.out.println("MyAutoConfigurationImportListener.onAutoConfigurationImportEvent() " + event);
    }

}
