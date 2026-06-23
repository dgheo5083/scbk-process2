package com.scbank.process.api.fw.channel;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.scbank.process.api.fw.channel.config.ChannelCommonConfigurations;
import com.scbank.process.api.fw.channel.config.ChannelHttpMessageConfigurations;
import com.scbank.process.api.fw.channel.config.ChannelServiceConfigurations;
import com.scbank.process.api.fw.channel.config.ChannelValidationConfigurations;
import com.scbank.process.api.fw.channel.constants.ChannelConstants;
import com.scbank.process.api.fw.channel.converter.HttpMessageConverterComposite;
import com.scbank.process.api.fw.channel.device.IDevice;
import com.scbank.process.api.fw.channel.device.IDeviceManager;
import com.scbank.process.api.fw.channel.device.IDeviceResolver;
import com.scbank.process.api.fw.core.validation.IBeanValidator;

/**
 * <pre>
 * 채널 Dispatcher 프레임워크의 공통 인프라 컴포넌트를 설정하는 자동 구성 클래스입니다.
 *
 * 'framework.channel.enabled=true'일 경우 활성화되며,
 * 프레임워크의 Device 인식, 메시지 변환, Validator, 메시지 소스, UUID 생성기 등
 * 기본 채널 환경 구성을 위한 필수 Bean 들을 등록합니다.
 * </pre>
 *
 * <p>
 * 주요 구성:
 * <ul>
 * <li>디바이스 정보 등록: {@link IDevice}, {@link IDeviceResolver},
 * {@link IDeviceManager}</li>
 * <li>입력 메시지 변환기: {@link HttpMessageConverterComposite} 및 각 메시지 타입별
 * Converter</li>
 * <li>유효성 검사기: {@link IBeanValidator}, 메시지소스 + 속성 해석기</li>
 * <li>Dispatcher UUID 생성기, Locale 설정기, Session 해석기 등 기본 인프라 제공</li>
 * </ul>
 *
 * <p>
 * 이 클래스는 서비스 별 Dispatcher 흐름에서 사용할 수 있도록 공통 Bean 을 구성하며, 필요 시 @Override 가능합니다.
 * </p>
 *
 * <p>
 * ※ 실제 세부 구성은 @Import된 4개의 설정 클래스를 통해 분리되어 관리됩니다.
 * </p>
 *
 * @author sungdon.choi
 * @since 2025.04.16
 */
@Import({
		ChannelCommonConfigurations.class,
		ChannelHttpMessageConfigurations.class,
		ChannelServiceConfigurations.class,
		ChannelValidationConfigurations.class
})
@Configuration
@ConditionalOnProperty(name = ChannelConstants.FRAMEWORK_CHANNEL_ENABLED, havingValue = "true")
public class ChannelConfiguration {

}
