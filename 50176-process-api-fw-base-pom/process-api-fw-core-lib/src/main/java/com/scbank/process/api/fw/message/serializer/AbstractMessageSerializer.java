package com.scbank.process.api.fw.message.serializer;

import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.context.MessageContext;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.fw.message.metadata.IIntegrationMessageMetadata;
import com.scbank.process.api.fw.message.metadata.IMessageFieldMetadata;
import com.scbank.process.api.fw.message.metadata.registry.IIntegrationMessageMetadataRegistrar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 전문 메시지 직렬화 추상 클래스
 * 
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 14.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessageSerializer {

    protected IIntegrationMessageMetadataRegistrar integrationMessageMetadataRegistrar;

    protected IIntegrationMessageMetadataRegistrar getIntegrationMessageMetadataRegistrar() {
        if (integrationMessageMetadataRegistrar == null) {
            integrationMessageMetadataRegistrar = RuntimeContext.getBean(IIntegrationMessageMetadataRegistrar.class);
        }
        return integrationMessageMetadataRegistrar;
    }

    protected IIntegrationMessageMetadata findIntegrationMessageMetadata(Class<?> targetClass) {
        IIntegrationMessageMetadataRegistrar registrar = this.getIntegrationMessageMetadataRegistrar();
        IIntegrationMessageMetadata metadata = registrar.getMetadata(targetClass);
        return metadata;
    }

    /**
     * 전문필드 직열화/역직렬화 처리 인코딩 문자열을 가져온다.
     *
     * @param fieldMetadata   전문필드 메타데이터
     * @param defaultEncoding 기본 인코딩 문자열
     * @return
     */
    protected String getEncoding(IMessageFieldMetadata fieldMetadata, String defaultEncoding) {
        String fieldEncoding = fieldMetadata.getEncoding();
        if (StringUtils.isEmpty(fieldEncoding)) {
            fieldEncoding = defaultEncoding;
        }
        return fieldEncoding;
    }

    /**
     * 필드 길이를 가져온다.
     *
     * @param metadata {@link IMessageFieldMetadata}
     * @return 필드 길이
     */
    protected int[] getFieldLength(IMessageFieldMetadata metadata) {
        if (metadata == null) {
            return new int[] { 0, 0 };
        }

        int length = metadata.getLength();
        int scale = metadata.getScale();
        return new int[] { length, scale };
    }

    protected int getRepeatCount(IMessageFieldMetadata metadata, IMessageObject object, MessageContext ctx) {
        int repeatCount = -1;
        RepeatType repeatType = metadata.getRepeatType();
        switch (repeatType) {
            case FIXED -> {
                String value = null;
                try {
                    value = metadata.getRepeatCount();
                    repeatCount = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    log.warn("array metadata {} fixed repeat count value is not number, value is {}",
                            metadata.getId(), value);
                    repeatCount = 0;
                }

                if (repeatCount == -1 && StringUtils.isBlank(value)) {
                    log.warn("field {} record fixed repeat count value is null, set zero value",
                            metadata.getId());
                    repeatCount = 0;
                }
            }
            case REFERENCE -> {
                String value = null;
                try {
                    value = metadata.getRepeatCount();
                    repeatCount = (Integer) ctx.getPathValue(value);
                } catch (NumberFormatException e) {
                    log.warn("array metadata {} reference repeat count value is not number, value is {}",
                            metadata.getId(), value);
                    repeatCount = 0;
                }

                if (repeatCount == -1 && StringUtils.isBlank(value)) {
                    log.warn("field {} record reference repeat count value is null, set zero value",
                            metadata.getId());
                    repeatCount = 0;
                }
            }
            default -> {
                repeatCount = -1;
            }
        }
        return repeatCount;
    }
}
