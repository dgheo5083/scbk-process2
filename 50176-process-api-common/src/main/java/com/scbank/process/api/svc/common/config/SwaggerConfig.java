package com.scbank.process.api.svc.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.scbank.process.api.fw.openapi.ServiceGroupOpenApiConfiguration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@Import({
        ServiceGroupOpenApiConfiguration.class
})
public class SwaggerConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info());
    }

    private Info info() {
        return new Info()
                .title("SC 제일은행 PROCESS API [공통 업무]")
                .description("PROCESS API 테스트")
                .version("1.0.0");
    }
}
