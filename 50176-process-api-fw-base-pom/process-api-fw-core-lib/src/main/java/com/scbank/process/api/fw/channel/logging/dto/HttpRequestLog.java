package com.scbank.process.api.fw.channel.logging.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HttpRequestLog extends HttpBaseLog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 요청 본문 메시지
     */
    private Object body;

    public HttpRequestLog() {
        super("request");
    }
}
