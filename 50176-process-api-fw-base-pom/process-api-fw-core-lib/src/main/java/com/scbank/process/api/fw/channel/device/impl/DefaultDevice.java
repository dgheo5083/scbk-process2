package com.scbank.process.api.fw.channel.device.impl;

import com.scbank.process.api.fw.channel.device.IDevice;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 프레임워크 서비스 요청 기본 디바이스 정보
 *
 * @author sungdon.choi
 * @version 1.0
 * @since 2022. 1. 10.
 */
@Builder
@Data
@EqualsAndHashCode
public class DefaultDevice implements IDevice {

    private static final long serialVersionUID = 1L;

    /**
     * 디바이스 ID
     */
    private String id;

    /**
     * 디바이스명
     */
    private String name;

    /**
     * 디바이스 설명
     */
    private String description;

    /**
     * 디바이스 서비스 요청 User-Agent 헤더 정규식
     */
    private String regEx;

    /**
     * 디바이스 검색 순서
     */
    private int order;

    /**
     * 기본 디바이스 여부
     */
    private boolean defaultDevice;

    @Override
    public int compareTo(IDevice o) {
        return Integer.valueOf(this.order).compareTo(Integer.valueOf(o.getOrder()));
    }
}
