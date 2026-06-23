package com.scbank.process.api.svc.common;

import org.springframework.boot.builder.SpringApplicationBuilder;

import com.scbank.process.api.fw.base.ProcessBaseApplication;

/**
 * process-api-common main 클래스
 */
public class CommonApplication extends ProcessBaseApplication {

    private static final String APPLICATION_NAME = "process-api-common";

    public static void main(String[] args) {
        configureApplication(new SpringApplicationBuilder()).run(args);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {

        builder.sources(CommonApplication.class);
        initBase(builder, APPLICATION_NAME);
        return builder;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return configureApplication(builder);
    }
}
