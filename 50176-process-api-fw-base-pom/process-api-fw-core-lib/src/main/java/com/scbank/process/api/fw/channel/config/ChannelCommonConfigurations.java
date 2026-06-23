package com.scbank.process.api.fw.channel.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceManager;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.channel.device.impl.DefaultDevice;
import com.scbank.process.api.fw.channel.device.impl.DefaultDeviceManager;
import com.scbank.process.api.fw.channel.device.impl.DefaultDeviceResolver;
import com.scbank.process.api.fw.core.utils.LocaleUtils;
import com.scbank.process.api.fw.core.uuid.IIdentifyGenerator;
import com.scbank.process.api.fw.session.ISessionContextResolver;
import com.scbank.process.api.fw.session.impl.DefaultSessionContextResolver;

/**
 * <pre>
 * 프레임워크 채널 공통 설정 클래스입니다.
 *
 * - Device, UUID, Locale, Session 관련 공통 Bean 들을 등록합니다.
 * - Dispatcher 흐름에 필요한 기본 인프라를 제공합니다.
 * - framework.channel.enabled=true 조건에서만 활성화됩니다.
 * </pre>
 *
 * 주요 역할:
 * <ul>
 * <li>UUID Generator 제공</li>
 * <li>Locale Resolver 기본 설정</li>
 * <li>Device 목록 및 관리 기능 등록</li>
 * <li>SessionContextResolver 등록</li>
 * </ul>
 * 
 * 이 클래스는 @Import로 묶여서 ChannelConfigurations에서 함께 관리됩니다.
 * 
 * @author sungdon
 * @since 2025.04.26
 */
@Configuration
@ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_ENABLED, havingValue = "true")
public class ChannelCommonConfigurations {

    @Value("${csl.runtime.default-locale:ko_KR}")
    private String defaultLocale;

    /**
     * HTTP 요청별 UUID를 생성하는 Generator
     */
    @Bean("requestUUIdGenerator")
    @ConditionalOnMissingBean(name = "requestUUIdGenerator")
    IIdentifyGenerator requestUUIdGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    /**
     * HTTP 요청의 Locale을 결정하는 Resolver
     */
    @Bean
    @ConditionalOnMissingBean(LocaleResolver.class)
    LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(LocaleUtils.toLocale(defaultLocale));
        return resolver;
    }

    /**
     * 기본 제공하는 Device 목록 등록
     * (PC, MOBILE)
     */
    @Bean
    List<IDevice> deviceList() {
        List<IDevice> devices = new ArrayList<>();
        devices.add(DefaultDevice.builder()
                .id("PC")
                .name("PC")
                .description("PC")
                .defaultDevice(true)
                .order(2)
                .build());
        devices.add(DefaultDevice.builder()
                .id("MOBILE")
                .name("MOBILE")
                .description("MOBILE")
                .order(1)
                .regEx(".*iPhone.+|.*Android.+")
                .build());
        return devices;
    }

    /**
     * Device 정보를 관리하는 Manager
     */
    @Bean
    @ConditionalOnMissingBean(IDeviceManager.class)
    @DependsOn({ "deviceList" })
    IDeviceManager deviceManager(List<IDevice> deviceList) {
        IDeviceManager manager = new DefaultDeviceManager();
        manager.add(deviceList);
        return manager;
    }

    /**
     * HTTP 요청별 Device를 식별하는 Resolver
     */
    @Bean
    @ConditionalOnMissingBean(IDeviceResolver.class)
    @DependsOn({ "deviceManager" })
    IDeviceResolver deviceResolver(IDeviceManager deviceManager) {
        return new DefaultDeviceResolver(deviceManager);
    }

    /**
     * HTTP 요청별 SessionContext를 관리하는 Resolver
     */
    @Bean
    @ConditionalOnMissingBean(ISessionContextResolver.class)
    ISessionContextResolver sessionResolver() {
        return new DefaultSessionContextResolver();
    }
}
