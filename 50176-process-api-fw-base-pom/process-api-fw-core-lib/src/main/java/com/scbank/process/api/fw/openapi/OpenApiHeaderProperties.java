package com.scbank.process.api.fw.openapi;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "csl.openapi")
public class OpenApiHeaderProperties {

    private List<Header> headers;

    public static record Header(String name, String description, boolean required, String type) {
    }
}
