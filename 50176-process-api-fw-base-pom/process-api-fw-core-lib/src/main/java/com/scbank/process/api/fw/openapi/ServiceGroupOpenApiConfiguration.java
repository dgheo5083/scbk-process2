package com.scbank.process.api.fw.openapi;

import static org.springdoc.core.utils.Constants.SPRINGDOC_USE_MANAGEMENT_PORT;

import java.util.List;

import org.springdoc.core.customizers.SpringDocCustomizers;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.service.AbstractRequestService;
import org.springdoc.core.service.GenericResponseService;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.OperationService;
import org.springdoc.webmvc.api.MultipleOpenApiWebMvcResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.scbank.process.api.fw.channel.ChannelProperties;
import com.scbank.process.api.fw.channel.service.registry.IServiceRegistrar;

@Configuration
public class ServiceGroupOpenApiConfiguration {

	@Bean
	ServiceGroupedOpenApiBeanFactory serviceGroupedOpenApiBeanFactory(ObjectProvider<IServiceRegistrar> serviceRegistrar,
			OpenApiHeaderProperties openApiHeaderProperties) {
		return new ServiceGroupedOpenApiBeanFactory(serviceRegistrar, openApiHeaderProperties);
	}

	@Bean
	ServiceGroupedOpenApiRegistrar swaggerServiceGroupRegistrar(
			ConfigurableApplicationContext context,
			ChannelProperties channelProperties,
			ServiceGroupedOpenApiBeanFactory serviceGroupedOpenApiBeanFactory) {
		return new ServiceGroupedOpenApiRegistrar(context, channelProperties, serviceGroupedOpenApiBeanFactory);
	}

	/**
	 * Multiple open api resource multiple open api resource.
	 *
	 * @param groupedOpenApis           the grouped open apis
	 * @param defaultOpenAPIBuilder     the default open api builder
	 * @param requestBuilder            the request builder
	 * @param responseBuilder           the response builder
	 * @param operationParser           the operation parser
	 * @param springDocConfigProperties the spring doc config properties
	 * @param springDocProviders        the spring doc providers
	 * @param springDocCustomizers      the spring doc customizers
	 * @return the multiple open api resource
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = SPRINGDOC_USE_MANAGEMENT_PORT, havingValue = "false", matchIfMissing = true)
	@Lazy(false)
	MultipleOpenApiWebMvcResource multipleOpenApiResource(List<GroupedOpenApi> groupedOpenApis,
			ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder,
			GenericResponseService responseBuilder, OperationService operationParser,
			SpringDocConfigProperties springDocConfigProperties,
			SpringDocProviders springDocProviders, SpringDocCustomizers springDocCustomizers) {
		return new MultipleOpenApiWebMvcResource(groupedOpenApis,
				defaultOpenAPIBuilder, requestBuilder,
				responseBuilder, operationParser,
				springDocConfigProperties,
				springDocProviders, springDocCustomizers);
	}
}
