package com.scbank.process.api.fw.base.integration.interceptors.logging;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.enums.MessageFormat;

import lombok.extern.slf4j.Slf4j;

/**
 * JSON 메시지 방식 연계시스템 요청/응답 로깅 인터셉터 구현 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@Component("integrationJacksonLoggingInterceptor")
public class IntegrationJacksonLoggingInterceptor extends AbstractIntegrationLoggingInterceptor {

    public IntegrationJacksonLoggingInterceptor(MessageContextFactory messageContextFactory) {
        super(messageContextFactory, Set.of(MessageFormat.JSON));
    }

    @Override
    public void before(IntegrationContext context, Object request) {
        //
    }

    @Override
    public void after(IntegrationContext context, Object request, Object response) {

    }
}
