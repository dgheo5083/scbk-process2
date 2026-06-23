package com.scbank.process.api.fw.batch.metadata.impl;

import java.util.Map;

import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;

import lombok.Data;

/**
 * 프레임워크 배치 트리거 메타데이터 구현 클래스
 */
@Data
public class DefaultBatchTriggerMetadata implements IBatchTriggerMetadata {

	/**
	 * 배치 트리거 타입
	 */
	private TriggerType triggerType;

	/**
	 * 배치 트리거 프로퍼티
	 */
	private Map<String, String> properties;
}
