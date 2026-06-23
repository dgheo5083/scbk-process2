package com.scbank.process.api.fw.base.channel.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;

import lombok.Builder;
import lombok.Data;

/**
 * 프로세스API 응답 헤더 메시지 부
 * 
 * @author sungdon.choi
 */
@Data
@Builder
@IntegrationMessage(id = "prcHeader")
public class PRCResponseHeader implements IMessageObject {

    private static final long serialVersionUID = 1L;

    @MessageField(id = "requestUId", name = "요청 UUID")
    private String requestUId;

    @MessageField(id = "resCode", name = "응답코드(정상: 00, 오류인경우: 99)")
    private String resCode;

    @MessageField(id = "errorCode", name = "에러 코드")
    private String errorCode;

    @MessageField(id = "errorLocation", name = "에러 발생 위치")
    private String errorLocation;

    @MessageField(id = "errorModule", name = "에러 발생 호스트 모듈")
    private String errorModule;

    @MessageField(id = "errorMessage", name = "에러 메시지")
    private String errorMessage;

    @MessageField(id = "errorGuideMessages", name = "에러 가이드 메시지")
    @RepeatedField
    private List<String> errorGuideMessages;
    
    @MessageField(id = "nextPage", name = "에러 출력 이후 이동할 페이지")
    private String nextPage;
    
    @MessageField(id = "nextPageParameters", name = "에러 출력 이후 이동할 페이지 파라미터")
    @RepeatedField
    private List<NextPageParameter> nextPageParameters;
    
    @MessageField(id = "errorPageParameters", name = "오류 페이지 이동 여부 파라미터")
    @RepeatedField
    private List<ErrorPageParameter> errorPageParameters;
    
    /**
     * 다음 페이지 파라미터
     */
    @Data
    public static class NextPageParameter implements IMessageObject {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
    	
		@MessageField(id = "name", name = "파라미터 명")
		private String name;
		
		@MessageField(id = "value", name = "파라미터 값")
		private String value;
    }
    
    /**
     * 오류 페이지 이동 여부 파라미터
     */
    @Data
    public static class ErrorPageParameter implements IMessageObject {
    	/**
    	 * 
    	 */
    	private static final long serialVersionUID = 1L;
    	
    	@MessageField(id = "name", name = "파라미터 명")
    	private String name;
    	
    	@MessageField(id = "value", name = "파라미터 값")
    	private String value;
    }
}
