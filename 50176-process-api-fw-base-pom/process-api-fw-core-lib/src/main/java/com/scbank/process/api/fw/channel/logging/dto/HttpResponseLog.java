package com.scbank.process.api.fw.channel.logging.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HttpResponseLog extends HttpBaseLog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 응답 HTTP 상태
     */
    private int status;

    /**
     * 처리시간(ms)
     */
    private long duration;

    /**
     * 응답 바디 데이터 문자열
     */
    private Object body;

    public HttpResponseLog() {
        super("response");
    }
}
