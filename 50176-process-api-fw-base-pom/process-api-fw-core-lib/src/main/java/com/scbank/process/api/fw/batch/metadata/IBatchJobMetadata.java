package com.scbank.process.api.fw.batch.metadata;

import java.util.Map;

/**
 * 프레임워크 배치작업 메타데이터 인터페이스
 */
public interface IBatchJobMetadata {

	/**
	 * 배치 컴포넌트 빈 ID
	 * 
	 * @return
	 */
	String getComponentId();

	/**
	 * 배치 작업 초기 파라미터
	 * 
	 * @return
	 */
	Map<String, String> getInitParameters();
}
