package com.scbank.process.api.fw.channel.device;

import java.io.Serializable;

import com.scbank.process.api.fw.core.utils.StringUtils;

/**
 * <pre>
 * 프레임워크 요청 디바이스 정보 인터페이스
 * </pre>
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022. 1. 4.
 */
public interface IDevice extends Serializable, Comparable<IDevice> {

    /**
     * 디바이스 ID를 가져온다.
     *
     * @return 디바이스 ID
     */
    default String getId() {
        return StringUtils.EMPTY;
    }

    /**
     * 디바이스 명으로 가져온다.
     *
     * @return 디바이스 명
     */
    default String getName() {
        return StringUtils.EMPTY;
    }

    /**
     * 디바이스 설명을 가져온다.
     *
     * @return 디바이스 설명
     */
    default String getDescription() {
        return StringUtils.EMPTY;
    }

    /**
     * 디바이스 User-Agent 정규 표현식 문자열을 가져온다.
     *
     * @return 디바이스 User-Agent 정규 표현식 문자열
     */
    default String getRegEx() {
        return StringUtils.EMPTY;
    }

    /**
     * 기본 디바이스 여부를 가져온다.
     *
     * @return 기본 설정 디바이스 여부를 가져온다.
     */
    default boolean isDefaultDevice() {
        return false;
    }

    /**
     * 디바이스 검색 순서를 가져온다.
     *
     * @return 디바이스 검색 순서
     */
    default int getOrder() {
        return 0;
    }
}
