package com.scbank.process.api.fw.batch.context;

import java.io.Serializable;
import java.util.Map;

/**
 * Quartz 기반 배치 실행 시 전달되는 실행 컨텍스트 인터페이스.
 * 
 * <p>
 * 배치 실행 시 초기 파라미터, 실행 메타정보(Job/Trigger),
 * 에러 상태 등을 관리하기 위한 표준 인터페이스입니다.
 * </p>
 */
public interface IBatchContext extends Serializable {

    /**
     * Quartz JobDataMap을 기반으로 초기 파라미터 전체를 반환합니다.
     * 
     * @return 초기 파라미터 맵 (Key: 파라미터명, Value: 문자열 값)
     */
    Map<String, String> getInitParameters();

    /**
     * 지정된 초기 파라미터 값을 반환합니다.
     * 
     * @param name 파라미터 키
     * @return 해당 파라미터의 값 (없으면 null)
     */
    String getInitParameter(String name);

    /**
     * 문자열 파라미터를 가져옵니다. 값이 없으면 기본값을 반환합니다.
     * 
     * @param key          파라미터 키
     * @param defaultValue 값이 없을 경우 반환할 기본값
     * @return 파라미터 값
     */
    String getString(String key, String defaultValue);

    /**
     * 정수형 파라미터를 가져옵니다. 변환 불가능하거나 없으면 기본값 반환.
     * 
     * @param key          파라미터 키
     * @param defaultValue 기본값
     * @return 파라미터 정수값
     */
    int getInt(String key, int defaultValue);

    /**
     * boolean 파라미터를 가져옵니다. 값이 "true"(대소문자 무관)일 경우 true 반환.
     * 
     * @param key          파라미터 키
     * @param defaultValue 기본값
     * @return 파라미터 boolean 값
     */
    boolean getBoolean(String key, boolean defaultValue);

    /**
     * 현재 실행 중인 Job의 이름을 반환합니다.
     * 
     * @return Job 이름
     */
    String getJobName();

    /**
     * 현재 실행 중인 Trigger의 이름을 반환합니다.
     * 
     * @return Trigger 이름
     */
    String getTriggerName();

    /**
     * 실행 인스턴스를 고유하게 식별할 수 있는 ID를 반환합니다.
     * 예: UUID, timestamp 등
     * 
     * @return 실행 인스턴스 ID
     */
    String getInstanceId();

    /**
     * 실행 중 발생한 예외를 설정합니다.
     * 
     * @param t 발생한 예외
     */
    void setError(Throwable t);

    /**
     * 실행 중 발생한 예외를 반환합니다.
     * 
     * @return 발생한 예외 또는 null
     */
    Throwable getError();

    /**
     * 실행이 실패한 것으로 마킹합니다.
     */
    void markAsFailed();

    /**
     * 실행 실패 여부를 반환합니다.
     * 
     * @return 실패한 경우 true
     */
    boolean isFailed();
}
