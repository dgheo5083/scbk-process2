package com.scbank.process.api.fw.session.adapter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "csl.session.adapter")
public class SessionAdapterProperties {

    private boolean enabled;

    private String rulePath;
}
