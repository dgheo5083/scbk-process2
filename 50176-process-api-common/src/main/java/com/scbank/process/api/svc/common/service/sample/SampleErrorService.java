package com.scbank.process.api.svc.common.service.sample;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.svc.common.service.sample.dto.ErrorTestRequest;
import com.scbank.process.api.svc.common.service.sample.dto.ErrorTestResponse;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/sample/error", name = "error TEST 서비스")
public class SampleErrorService {
    

    @ServiceEndpoint(url = "/errorTest", name = "서명 검증 테스트")
    public ErrorTestResponse errorTest(IServiceContext serviceContext,
            ErrorTestRequest request) {
    	ErrorTestResponse response = new ErrorTestResponse(); 
    	
    	log.debug("error Test : [{}]", request);
    	
    	if(StringUtils.isNotEmpty(request.getErrorCode())) {
    		PRCServiceException e = new PRCServiceException(request.getErrorCode());
//    		e.addErrorPageParameter("SECU_SERVICE_YN", "Y");
    		throw e;
    	}

        return response;
    }
    
}