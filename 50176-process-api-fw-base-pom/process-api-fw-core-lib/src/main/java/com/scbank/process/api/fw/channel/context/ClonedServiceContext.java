package com.scbank.process.api.fw.channel.context;

import java.util.Locale;

import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.session.ISessionContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 복제처리 서비스 컨텍스트 클래스
 * 비동기 서비스 처리 시 복사용으로 사용
 * 
 * @author sungdon.choi
 */
public record ClonedServiceContext(IServiceContext source) implements IServiceContext {

    public String channelId() {
        return source.channelId();
    }

    public String requestUId() {
        return source.requestUId();
    }

    public HttpServletRequest request() {
        return source.request();
    }

    public HttpServletResponse response() {
        return source.response();
    }

    @SuppressWarnings("deprecation")
	public IDevice device() {
        return source.device();
    }

    public Locale locale() {
        return source.locale();
    }

    public ServiceDefinitionMetadata serviceDefinition() {
        return source.serviceDefinition();
    }

    public Object attribute(String attrName) {
        return source.attribute(attrName);
    }

    public <T> T attribute(String attrName, Class<T> requiredType) {
        return source.attribute(attrName, requiredType);
    }

    @Override
    public void setAttribute(String attrName, Object value) {
        source.setAttribute(attrName, value);
    }

    public String parameter(String parameterName) {
        return source.parameter(parameterName);
    }

    @Override
    public ISessionContext session() {
        return source.session();
    }
}
