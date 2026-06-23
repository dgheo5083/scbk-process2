package com.scbank.process.api.fw.core.runtime;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 런타임 컨텍스트 초기화 처리 클래스
 */
@Component("runtimeContextInitializer")
public class RuntimeContextInitializer implements ApplicationContextAware, EnvironmentAware {

    @Override
    public void setEnvironment(Environment environment) {
        RuntimeContext.setEnvironment(environment);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RuntimeContext.setApplicationContext(applicationContext);
    }
}
