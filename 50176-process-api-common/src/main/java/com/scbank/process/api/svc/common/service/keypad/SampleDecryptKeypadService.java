package com.scbank.process.api.svc.common.service.keypad;

import java.util.Optional;

import com.scbank.process.api.fw.base.store.secure.SecureContextStore;
import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.svc.common.service.keypad.dto.SampleDecryptKeypadRequest;
import com.scbank.process.api.svc.common.service.keypad.dto.SampleDecryptKeypadResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "가상키패드 복호화", url = "/sample/decrypt")
public class SampleDecryptKeypadService {

	@ServiceEndpoint(url = "/decryptKeypad", name = "가상키패드 복호화")
	public SampleDecryptKeypadResponse decryptKeypad(IServiceContext serviceContext,
			SampleDecryptKeypadRequest request) {

		SampleDecryptKeypadResponse response = new SampleDecryptKeypadResponse();

		// 업무에 필요한 데이터들
		log.info("#### 업무 request : [{}]", request);
		
		Optional<SecureContext> secureContext = SecureContextStore.getContext();
		log.debug("### SercureContext 업무단 : {}", secureContext);
		
		HttpServletRequest httpRequest = serviceContext.request();
		
		String businessStr = request.getBusinessStr();
		String accntPw = request.getAccntPw();
		String ssn = request.getSsn();
		
		log.debug("### businessStr : [{}] ", businessStr);
		log.debug("### 계좌비밀번호 : [{}] ", accntPw);
		log.debug("### ssn : [{}] ", ssn);
		
		response.setDecStr1(businessStr);
		response.setDecStr2(accntPw);
		response.setDecStr3(ssn);

		return response;
		
	}
	
	
//		에러 처리 TEST
	
//		PRCFeignException ex = new PRCFeignException();
//		ex.setErrorCode("TEST_ERROR_CODE");
//		ex.setErrorLocation("TEST location");
//		ex.setErrorModule(IntegrationConstant.ERRMESG_EDMI_ETC);
//		ex.setErrorMessage("occured TEST error!");
//		ex.setNextPage("SAMPLE0041");
//		
//		Map<String, Object> errParamMap = new HashMap<>();
//		errParamMap.put("errorParam1", "errorParam1");
//		errParamMap.put("errorParam2", "errorParam2");
//		
//		ex.setNextPageParameters(errParamMap);
//		
//		throw ex;

}