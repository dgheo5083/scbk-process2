package com.scbank.process.api.fw.base.integration.log;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.scbank.process.api.fw.core.component.ComponentOperation;

import lombok.RequiredArgsConstructor;

/**
 * 전문 로그 이벤트 발행 컴포넌트
 */
@RequiredArgsConstructor
@Component
public class IntegrationLogEventPublisher {

	/**
	 * 이벤트 발행 컴포넌트
	 */
	private final ApplicationEventPublisher publisher;
	
	@ComponentOperation(name = "전문로그 이벤트 발행 메소드")
	public void publish(IntegrationLogEvent event) {
		this.publisher.publishEvent(event);
	}
	
	@ComponentOperation(name = "전문로그  수집 이벤트 발행 메소드")
	public void publish(IntegrationLogCollectEvent event) {
		this.publisher.publishEvent(event);
	}
}
