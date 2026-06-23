package com.scbank.process.api.fw.channel.service.support;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import org.springframework.core.annotation.AnnotationUtils;

import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;

public class CamelCaseServiceUrlGenerator implements IServiceUrlGenerator {

    private static final List<String> SUFFIXES = List.of("Service", "Svc", "_SVC");

    @Override
    public String generate(Class<?> beanType, Method method) {
        ServiceComponent serviceComponent = AnnotationUtils.findAnnotation(beanType, ServiceComponent.class);

        StringBuffer url = new StringBuffer();
        String serviceUrl = serviceComponent.url();
        if (StringUtils.isEmpty(serviceUrl)) {
            serviceUrl = this.generateUrlFromClassName(beanType);
        }

        String methodUrl = "";
        ServiceEndpoint serviceEndpoint = AnnotationUtils.findAnnotation(method, ServiceEndpoint.class);
        methodUrl = serviceEndpoint.url();
        if (StringUtils.isEmpty(methodUrl)) {
            methodUrl = method.getName();
        }

        if (!methodUrl.startsWith("/")) {
            methodUrl = "/" + methodUrl;
        }

        url.append(serviceUrl).append(methodUrl);

        String normalized = URI.create(url.toString()).normalize().getPath();
        return normalized;
    }

    /**
     * 클래스명으로 url 문자열을 만든다.
     * ex) CamelCase -> /camel/case
     * 
     * @param beanType
     * @return
     */
    private String generateUrlFromClassName(Class<?> beanType) {
        String className = beanType.getSimpleName();

        for (String suffix : SUFFIXES) {
            if (className.endsWith(suffix)) {
                className = className.substring(0, className.length() - suffix.length());
                break;
            }
        }

        //
        StringBuffer sb = new StringBuffer("/");
        for (int i = 0; i < className.length(); i++) {
            char ch = className.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                sb.append("/");
            }
            sb.append(Character.toLowerCase(ch));
        }
        return sb.toString();
    }
}
