package com.scbank.process.api.fw.base.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("csl.filter.auth")
public class AuthFilterProperty {

    /**
     * 활성화 여부
     */
    private boolean enabled;

    private List<String> skipPathWithoutAuthHeader = new ArrayList<>();
}
