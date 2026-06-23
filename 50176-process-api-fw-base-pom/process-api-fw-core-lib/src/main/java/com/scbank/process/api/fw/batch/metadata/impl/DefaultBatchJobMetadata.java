package com.scbank.process.api.fw.batch.metadata.impl;

import java.util.Map;

import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;

import lombok.Data;

/**
 * 프레임워크 배치 작업 메타데이터 구현 클래스
 */
@Data
public class DefaultBatchJobMetadata implements IBatchJobMetadata {

	/**
	 * 배치 컴포넌트 ID
	 */
	private String componentId;

	/**
	 * 배치 초기 파라미터
	 */
	private Map<String, String> initParameters;
}
