package com.scbank.process.api.fw.channel.service.support;

import java.lang.reflect.Method;

public interface IServiceUrlGenerator {

    public String generate(Class<?> beanType, Method method);
}
