package com.scbank.process.api.fw.base.integration.interceptors.logging;

import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.interceptor.IntegrationInterceptor;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.context.MessageContextFactory;
import com.scbank.process.api.fw.message.enums.MessageFormat;
import com.scbank.process.api.fw.message.option.MessageFormatOptions.MessageFormatOption;
import com.scbank.process.api.fw.message.option.SerializationOptions;

import lombok.RequiredArgsConstructor;

/**
 * 연계 시스템별 전문 요청/응답 로깅처리 인터셉터 추상 클래스
 * 
 * @author sungdon.choi
 */
@RequiredArgsConstructor
public abstract class AbstractIntegrationLoggingInterceptor implements IntegrationInterceptor {

    protected final MessageContextFactory messageContextFactory;

    protected final Set<MessageFormat> supportedMessageFormatSet;

    /**
     * 필드 마스킹 처리가 활성화 된 옵션을 가지고 있는 MessageContext를 생성하여 리턴한다.
     * IntegrationContext에서 가지고 있는 MessageContext를 기준으로 복사 후 필드 마스킹 옵션 추가
     * 
     * @param context {@link IntegrationContext}
     * @return MessageContext
     */
    protected MessageContext getMessageContext(IntegrationContext context) {
        Map<String, Object> extendedOptions = Map.of(MessageFormatOption.FIELD_MASK.name(), true);
        SerializationOptions mergedOptions = SerializationOptions.merge(context.getSerializationOptions(),
                extendedOptions);
        MessageContext messageContext = (MessageContext) context.getAttribute("_MESSAGE_CTX_",
                MessageContext.class);
        MessageContext extentedCtx = messageContextFactory.merge(messageContext, mergedOptions, null);
        return extentedCtx;
    }

    protected boolean isSupported(IntegrationContext ctx) {
        if (CollectionUtils.isEmpty(supportedMessageFormatSet)) {
            return false;
        }

        MessageContext messageContext = (MessageContext) ctx.getAttribute("_MESSAGE_CTX_",
                MessageContext.class);
        return supportedMessageFormatSet.contains(messageContext.getFormat());
    }

}
