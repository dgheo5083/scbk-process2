package com.scbank.process.api.fw.channel.logging.dto;

import java.io.Serializable;
import java.util.Map;

import com.scbank.process.api.fw.core.utils.DateUtils;

import lombok.Data;

@Data
public abstract class HttpBaseLog implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 로그 타입
     */
    private final String logType;

    /**
     * 요청 tracking 아이디
     */
    private String trackingId;

    /**
     * 요청 메소드
     */
    private String method;

    /**
     * 요청 URI
     */
    private String uri;

    /**
     * 헤더정보
     */
    private Map<String, String> headers;

    /**
     * 타임스탬프
     */
    private String timestamp = DateUtils.getCurrentTime();

    protected HttpBaseLog(String logType) {
        this.logType = logType;
    }

}
