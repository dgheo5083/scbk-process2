package com.scbank.process.api.fw.batch.metadata;

/**
 * 프레임워크 배치 메타데이터 인터페이스
 * 
 * @author sungdon.choi
 */
public interface IBatchMetadata {

	/**
	 * 배치 ID
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 배치 설명
	 * 
	 * @return
	 */
	default String getDescription() {
		return "";
	}

	default String getTargetNode() {
		return "all";
	}

	/**
	 * 배치 JOB 메타데이터 정보
	 * 
	 * @return
	 */
	IBatchJobMetadata getBatchJobMetadata();

	/**
	 * 배치 트리거 메타데이터 정보
	 * 
	 * @return
	 */
	IBatchTriggerMetadata getBatchTriggerMetadata();
}
