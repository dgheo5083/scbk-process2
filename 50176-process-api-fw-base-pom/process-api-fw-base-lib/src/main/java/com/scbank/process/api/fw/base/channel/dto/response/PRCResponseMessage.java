package com.scbank.process.api.fw.base.channel.dto.response;

import com.scbank.process.api.fw.base.channel.dto.PRCResponseHeader;
import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.message.IMessageObject;

import lombok.Builder;
import lombok.Data;

/**
 * 프로세스 API 응답 메시지 클래스
 * 
 * @author sungdon.choi
 */
@Data
@Builder
public class PRCResponseMessage<T extends IMessageObject> implements IResponseMessage<PRCResponseHeader, T> {

    private static final long serialVersionUID = 1L;

    /**
     * 프로세스API 응답 헤더
     */
    private PRCResponseHeader header;

    /**
     * T 타입의 응답 Body 데이터
     */
    private T body;
}
