package com.scbank.process.api.fw.channel.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.ChannelProperties.MessageSourceConfig;
import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.core.validation.DefaultBeanValidator;
import com.scbank.process.api.fw.core.validation.IBeanValidator;
import com.scbank.process.api.fw.core.validation.attribute.DefaultValidationAttributeExtractorRegistry;
import com.scbank.process.api.fw.core.validation.attribute.IValidationAttributeExtractorRegistry;
import com.scbank.process.api.fw.core.validation.message.DefaultValidationMessageResolver;
import com.scbank.process.api.fw.core.validation.message.IValidationMessageResolver;
import com.scbank.process.api.fw.message.IMessageObject;

import jakarta.validation.Validator;

/**
 * <pre>
 * 프레임워크 채널 Dispatcher 유효성 검사 및 메시지 소스 설정 클래스입니다.
 *
 * 'framework.channel.enabled=true'일 때 활성화되며,
 * 입력 파라미터 검증, 메시지 리소스, Validation 관련 설정을 담당합니다.
 * </pre>
 *
 * 주요 구성:
 * <ul>
 * <li>입력 파라미터 Bean Validation 지원</li>
 * <li>MessageSource 기반 다국어/오류 메시지 지원</li>
 * <li>ValidationAttribute 해석기 및 ValidationMessage 리졸버 등록</li>
 * </ul>
 *
 * @author sungdon
 * @since 2025.04.26
 */
@Configuration
@ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_ENABLED, havingValue = "true")
public class ChannelValidationConfigurations {

    /**
     * 입력 DTO를 위한 Bean Validation Validator 등록
     */
    @Bean
    @ConditionalOnMissingBean(IBeanValidator.class)
    @ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_VALIDATION_ENABLED, havingValue = "true")
    IBeanValidator<IMessageObject> beanValidator(Validator validator) {
        return new DefaultBeanValidator<>(validator);
    }

    /**
     * 메시지 리소스를 관리하는 MessageSource 등록
     * (Validation 메시지, 에러 메시지 등)
     */
    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    @ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_MESSAGESOURCE_ENABLED, havingValue = "true")
    @DependsOn({ "channelProperties" })
    MessageSource messageSource(ChannelProperties properties) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        MessageSourceConfig config = properties.getMessageSource();
        List<String> locations = config.locations();
        if (!locations.isEmpty()) {
            messageSource.setBasenames(locations.toArray(String[]::new));
        }

        messageSource.setDefaultEncoding(config.encoding());
        messageSource.setFallbackToSystemLocale(config.fallbackToSystemLocale());
        messageSource.setCacheSeconds(config.cacheSeconds());
        return messageSource;
    }

    /**
     * ValidationAttribute 해석기 등록
     * (Constraint 속성값을 읽어오는 Registry)
     */
    @Bean
    @ConditionalOnMissingBean(IValidationAttributeExtractorRegistry.class)
    IValidationAttributeExtractorRegistry validationAttributeExtractorRegistry() {
        return new DefaultValidationAttributeExtractorRegistry();
    }

    /**
     * Validation 실패 시 메시지를 해결하는 Resolver 등록
     */
    @Bean
    @ConditionalOnMissingBean(IValidationMessageResolver.class)
    @DependsOn({ "messageSource", "validationAttributeExtractorRegistry" })
    IValidationMessageResolver validationMessageResolver(
            MessageSource messageSource,
            IValidationAttributeExtractorRegistry validationAttributeExtractorRegistry) {
        return new DefaultValidationMessageResolver(messageSource, validationAttributeExtractorRegistry);
    }
}
