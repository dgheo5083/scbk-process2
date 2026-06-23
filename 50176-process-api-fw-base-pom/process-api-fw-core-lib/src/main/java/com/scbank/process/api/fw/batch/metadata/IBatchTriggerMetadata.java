package com.scbank.process.api.fw.batch.metadata;

import java.util.Map;

import com.scbank.process.api.fw.core.utils.StringUtils;

/**
 * 프레임워크 배치 트리거 메타데이터 인터페이스
 * 
 * @author sungdon.choi
 */
public interface IBatchTriggerMetadata {

	/**
	 * 배치 트리거 타입
	 * 
	 * @return
	 */
	TriggerType getTriggerType();

	/**
	 * 배치 트리거 프러퍼티
	 * 
	 * @return
	 */
	Map<String, String> getProperties();

	/**
	 * 
	 * @param key
	 * @return
	 */
	default String getProperty(String key) {
		Map<String, String> properties = this.getProperties();
		if (properties == null || properties.isEmpty()) {
			return StringUtils.EMPTY;
		}

		return properties.getOrDefault(key, StringUtils.EMPTY);
	}

	/**
	 * 배치 트리거 타입 열거형 상수
	 */
	public static enum TriggerType {
		SIMPLE, CRON, NONE;

		public static TriggerType of(String type) {
			switch (type) {
				case "simple":
					return SIMPLE;
				case "cron":
					return CRON;
				default:
					return NONE;
			}
		}
	}
}
