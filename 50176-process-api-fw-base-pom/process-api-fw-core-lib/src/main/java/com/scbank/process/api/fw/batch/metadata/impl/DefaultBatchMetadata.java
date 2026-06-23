package com.scbank.process.api.fw.batch.metadata.impl;

import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 프레임워크 배치 메타데이터 정보 구현 클래스
 */
@Data
@EqualsAndHashCode
public class DefaultBatchMetadata implements IBatchMetadata {

	/**
	 * 배치 ID
	 */
	private String id;

	/**
	 * 배치 설명
	 */
	private String description;

	/**
	 * 배치 실행 타켓 노드
	 */
	private String targetNode;

	/**
	 * 배치 작업 메타데이터
	 */
	private IBatchJobMetadata batchJobMetadata;

	/**
	 * 배치 트리거 메타데이터
	 */
	private IBatchTriggerMetadata batchTriggerMetadata;
}
