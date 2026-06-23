package com.scbank.process.api.svc.shared.components.obs.kftc.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.obs.kftc.model.KftcObsAuthVo;
import com.scbank.process.api.svc.shared.components.obs.model.ObsHttpJsonObject;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.dao.OpenBankApiTokenDao;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankApiTokenEnabledTokenParameter;
import com.scbank.process.api.svc.shared.dao.dto.OpenBankApiTokenSelectTokenResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 금결원 오픈뱅킹 API토큰 매니저 컴포넌트
 */
@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "금결원 오픈뱅킹 API토큰 매니저 컴포넌트")
public class KftcObsTokenManager {

    /**
     * 금결원 오픈뱅킹 인증 API 클라이언트
     */
    private final KftcObsAuthClient kftcObsAuthClient;

    /**
     * 금결원 오픈뱅킹 API 토큰 DAO
     */
    private final OpenBankApiTokenDao openBankApiTokenDao;
    
    /**
     * SMS 발송 컴포넌트
     */
    private final SmsComponent smsComponent;

    /**
     * 금결원 오픈뱅킹 API토큰 획득
     * 
     * @return
     */
    @ComponentOperation(name = "금결원 오픈뱅킹 API토큰 획득")
    public KftcObsAuthVo getToken() {
        OpenBankApiTokenSelectTokenResult result = this.openBankApiTokenDao.selectToken();
        if (result != null) {
            String token = StringUtils.defaultIfEmpty(result.getData(), "{}");

            ObsHttpJsonObject jsonToken = ObsHttpJsonObject.fromObject(token);
            KftcObsAuthVo voToken = new KftcObsAuthVo().clone(jsonToken);
            if (voToken.isAccessToken()) {
                return voToken;
            }
            log.info("인증키를 만료로 인하여 인증키 새로 발급합니다.");
        }
        return this.changeToken();
    }

    /**
     * 금결원 오픈뱅킹 API토큰 재발급
     * 
     * @return
     */
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    @ComponentOperation(name = "금결원 오픈뱅킹 API토큰 재발급")
    public KftcObsAuthVo changeToken() {
        ObsHttpJsonObject result = null;
        try {
            result = this.kftcObsAuthClient.getToken().throwException().getBody();

            long expiresIn = result.getAsLong("expires_in", 0L) * 1000L;
            String tokenDate = DateUtils.getDate(new Date(System.currentTimeMillis() + expiresIn), "YYYYMMDD");

            result.getAsJsonObject().put("expires_date", tokenDate);

            disableToken();
            enableToken(tokenDate, result.getAsJsonString());

            try {
                sendSMS("토큰 재발급 성공");
            } catch (Exception e) {
                log.error("오픈뱅킹 토큰 재발급 SMS 발송 오류", e);
            }
        } catch (Exception e) {
            log.error("오픈뱅킹 토큰 재발급 오류", e);

            sendSMS("토큰 재발급 실패");
        }
        
        if (result == null) {
        	return null;
        }

        return new KftcObsAuthVo().clone(result);
    }

    /**
     * 토큰 활성화 처리 수행
     * 
     * @param tokenDate
     * @param data
     */
    @ComponentOperation(name = "토큰 활성화 처리 수행")
    public void enableToken(String tokenDate, String data) {
        try {
            String enData = StringUtils.nvl(data, "{}");

            OpenBankApiTokenEnabledTokenParameter parameter = OpenBankApiTokenEnabledTokenParameter.builder()
                    .data(enData)
                    .tokenDate(tokenDate)
                    .connectLoc("GLOBAL")
                    .build();
            this.openBankApiTokenDao.enabledToken(parameter);
        } catch (Exception e) {
            throw new PRCServiceException(e);
        }
    }

    /**
     * 토큰 무효화 처리 수행
     */
    @ComponentOperation(name = "금결원 오픈뱅킹 토큰 무효화 처리 수행")
    public void disableToken() {
        this.openBankApiTokenDao.disabledToken();
    }

    /**
     * 문자발송
     * 
     * @param msg
     */
    private void sendSMS(String msg) {
        // TODO
    	String callphone = PropertiesUtils.getString("kftc.open-platform.batch.sms.callphone");
		String callMessage = "[SC제일은행] MA3금결원 오픈뱅킹 배치 : " + msg;
		
		List<String> phoneList = StringUtils.hasLength(callphone) ? Arrays.asList(callphone.split(";")) : List.of();
		if (!CollectionUtils.isEmpty(phoneList)) {
			return;
		}
		
		for (String phone : phoneList) {
			String callphone1 = "";
			String callphone2 = "";
			String callphone3 = "";
			
			if (phone.length() == 10) {
				callphone1 = phone.substring(0, 2);
				callphone2 = phone.substring(2, 5);
				callphone3 = phone.substring(5);
			} else if (phone.length() == 11) {
				callphone1 = phone.substring(0, 3);
				callphone2 = phone.substring(3, 7);
				callphone3 = phone.substring(7);
			} else {
				continue;
			}
			
			try {
				SmsRequest request = new SmsRequest();
				request.setMember("0");
				request.setUserCode("ebanking");
				request.setUserName("I5");
				request.setCallPhone1(callphone1);
				request.setCallPhone2(callphone2);
				request.setCallPhone3(callphone3);
				request.setCallMessage(callMessage);
				request.setRateDate(DateUtils.getCurrentDate());
				request.setRateTime(DateUtils.getCurrentDate("HHmmss"));
				request.setReqPhone1("");
				request.setReqPhone2("1588");
				request.setReqPhone3("1599");
				request.setCallName("관리자");
				request.setDeptCode("GL9-KI3-HS9");
				request.setDeptName("e-뱅u-12103 부");
				
				String result = smsComponent.sendMain(request);
				
				log.debug("# SMS 발송결과: [{}]", result);
			} catch (PRCServiceException e) {
				log.error(e.getMessage(), e);
			}
		}
    }
}
