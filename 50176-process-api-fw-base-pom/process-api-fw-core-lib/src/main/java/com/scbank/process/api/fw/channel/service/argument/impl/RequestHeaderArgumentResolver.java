package com.scbank.process.api.fw.channel.service.argument.impl;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ValueConstants;

import com.google.common.base.Objects;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.argument.IServiceMethodArgumentResolver;
import com.scbank.process.api.fw.channel.service.metadata.ServiceMethodMetadata.ParameterMetadata;
import com.scbank.process.api.fw.core.utils.StringUtils;

public class RequestHeaderArgumentResolver implements IServiceMethodArgumentResolver {

    @Override
    public boolean supports(ParameterMetadata parameter) {
        return parameter.hasAnnotation(RequestHeader.class);
    }

    @Override
    public Object resolveArgument(ParameterMetadata parameter, IServiceContext context, Object input) throws Exception {

        RequestHeader annotation = parameter.getAnnotation(RequestHeader.class);

        String headerName = StringUtils.defaultIfEmpty(annotation.name(), "");
        if (!StringUtils.hasLength(headerName)) {
            headerName = annotation.value();
        }

        if (!StringUtils.hasLength(headerName)) {
            headerName = parameter.getName();
        }

        String headerValue = context.request().getHeader(headerName);
        if (!StringUtils.hasLength(headerValue)) {
            if (!Objects.equal(annotation.defaultValue(), ValueConstants.DEFAULT_NONE)) {
                headerValue = annotation.defaultValue();
            } else if (annotation.required()) {
                throw new IllegalArgumentException("Required Header [" + headerName + "] is Missing");
            } else {
                return null;
            }
        }

        return this.convert(headerValue, parameter.getType());
    }

    private Object convert(String raw, Class<?> targetType) {
        if (targetType == String.class)
            return raw;
        if (targetType == int.class || targetType == Integer.class)
            return Integer.parseInt(raw);
        if (targetType == long.class || targetType == Long.class)
            return Long.parseLong(raw);
        if (targetType == boolean.class || targetType == Boolean.class)
            return Boolean.parseBoolean(raw);
        if (targetType == double.class || targetType == Double.class)
            return Double.parseDouble(raw);
        return raw;
    }

}
