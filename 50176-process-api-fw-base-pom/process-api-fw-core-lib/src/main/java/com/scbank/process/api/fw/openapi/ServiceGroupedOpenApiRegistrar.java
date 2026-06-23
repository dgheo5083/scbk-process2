package com.scbank.process.api.fw.openapi;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;

import com.scbank.process.api.fw.channel.ChannelProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * 서비스 메타데이터를 기반으로 {@link GroupedOpenApi} 인스턴스를 동적으로 생성하고
 * Spring 컨텍스트에 수동으로 빈으로 등록하는 컴포넌트.
 * <p>
 * 이 클래스는 애플리케이션 기동 시점에 자동 실행되며, SpringDoc에서 Swagger UI에
 * 서비스별 API 문서를 표시할 수 있도록 설정된 GroupedOpenApi 빈을 생성한다.
 *
 * @author sungdon.choi
 * @since 2025.05
 */
@RequiredArgsConstructor
public class ServiceGroupedOpenApiRegistrar {

	private final ConfigurableApplicationContext context;
	private final ChannelProperties channelProperties;
	private final ServiceGroupedOpenApiBeanFactory serviceGroupedOpenApiBeanFactory;

	@Value("${server.servlet.context-path}")
	private String contextPath;

	/**
	 * 애플리케이션 기동 후 실행되며,
	 * ChannelProperties에 정의된 서비스 정보를 기반으로 GroupedOpenApi를 생성하고,
	 * 이를 Spring 컨텍스트에 수동으로 등록한다.
	 */
	@PostConstruct
	public void registerApis() {
		if (channelProperties == null || channelProperties.getService() == null) {
			return;
		}

		List<GroupedOpenApi> apis = serviceGroupedOpenApiBeanFactory
				.createGroupedOpenApis(channelProperties.getService());

		for (GroupedOpenApi api : apis) {
			String beanName = api.getGroup() + "GroupedOpenApi";
			context.getBeanFactory().registerSingleton(beanName, api);
		}
	}
}
