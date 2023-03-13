package com.example.nettyrpc.client.autoconfig;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

public class MyFailureAnalyzer implements FailureAnalyzer {

    @Override
    public FailureAnalysis analyze(Throwable failure) {
        System.out.println("MyFailureAnalyzer.analyze() failure=" + failure);
        return new FailureAnalysis("MyFailureAnalyzer execute", "test spring.factories", failure);
    }

}