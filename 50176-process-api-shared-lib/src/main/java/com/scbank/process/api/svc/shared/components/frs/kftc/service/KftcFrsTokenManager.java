package com.scbank.process.api.svc.shared.components.frs.kftc.service;

import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.frs.kftc.model.KftcFrsAuthVo;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpJsonObject;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;
import com.scbank.process.api.svc.shared.dao.FaceRecApiTokenDao;
import com.scbank.process.api.svc.shared.dao.dto.FaceRecApiTokenEnabledTokenParameter;
import com.scbank.process.api.svc.shared.dao.dto.FaceRecTokenInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "금결원 안면인식 API토큰 매니저 컴포넌트")
public class KftcFrsTokenManager {
	
	/**
     * 안면인식 인증 API 클라이언트
     */
    private final KftcFrsAuthClient kftcFrsAuthClient;
	
	/**
	 * 안면인식 API 토큰 DAO
	 */
	private final FaceRecApiTokenDao faceRecApiTokenDao;
	
	private final SmsComponent smsComponent;
	
	/**
     * 금결원 오픈뱅킹 API토큰 획득
     * 
     * @return
     */
    @ComponentOperation(name = "금결원 안면인식 API토큰 획득")
    public KftcFrsAuthVo getToken() {
    	FaceRecTokenInfo result = faceRecApiTokenDao.selectToken();
    	
    	log.debug("금결원 안면인식 사용가능 토큰 조회 => {}", result);
    	
        if (result != null) {
            String token = StringUtils.defaultIfEmpty(result.getData(), "{}");

            FrsHttpJsonObject jsonToken = FrsHttpJsonObject.fromObject(token);
            KftcFrsAuthVo voToken = new KftcFrsAuthVo().clone(jsonToken);
            if (voToken.isAccessToken()) {
                return voToken;
            }
            log.info("인증키를 만료로 인하여 인증키 새로 발급합니다.");
        }

		return changeToken();
	}
    
    /**
     * 금결원 오픈뱅킹 API토큰 재발급
     * 
     * @return
     */
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    @ComponentOperation(name = "금결원 안면인식 API토큰 재발급")
    public KftcFrsAuthVo changeToken() {
        FrsHttpJsonObject result = null;
        try {
			result = kftcFrsAuthClient.getToken()/* .throwException() */.getBody();
            
            log.debug("금결원 안면인식 API토큰 조회 result => {}", result.getAsJsonObject());

            long expiresIn = result.getAsLong("expires_in", 0L) * 1000L;
            String tokenDate = DateUtils.getDate(new Date(System.currentTimeMillis() + expiresIn), "YYYYMMDD");

            result.getAsJsonObject().put("expires_date", tokenDate);

            disableToken();
            enableToken(tokenDate, result.getAsJsonString());

            try {
                sendSMS("토큰 재발급 성공");
            } catch (Exception e) {
                log.error("안면인식 토큰 재발급 SMS 발송 오류");
            }
        } catch (Exception e) {
            log.error("안면인식 토큰 재발급 오류 => ",e.getMessage(),e);

            sendSMS("토큰 재발급 실패");
        }
        
        if (result == null) {
        	return null;
        }

        return new KftcFrsAuthVo().clone(result);
    }
    
    /**
     * 토큰 활성화 처리 수행
     * 
     * @param tokenDate
     * @param data
     */
    @ComponentOperation(name = "금결원 안면인식 토큰 활성화 처리 수행")
    public void enableToken(String tokenDate, String data) {
        try {
            String enData = StringUtils.nvl(data, "{}");

            FaceRecApiTokenEnabledTokenParameter parameter = FaceRecApiTokenEnabledTokenParameter.builder()
                    .data(enData)
                    .tokenDate(tokenDate)
                    .connectLoc("GLOBAL")
                    .build();
            faceRecApiTokenDao.enabledToken(parameter);
        } catch (Exception e) {
            throw new PRCServiceException(e);
        }
    }
    
    /**
     * 토큰 무효화 처리 수행
     */
    @ComponentOperation(name = "금결원 안면인식 토큰 무효화 처리 수행")
    public void disableToken() {
        faceRecApiTokenDao.disabledToken();
    }
    
    /**
     * 문자발송
     * 
     * @param msg
     */
    private void sendSMS(String msg) {
        // TODO
    	
    	String callPhone = PropertiesUtils.getString("kftc.face-id.magager.sms.callphone");
    	String callMessage = "[SC제일은행] MA3금결원 안면인식 배치 : " + msg;
    	String[] arrCallPhone = callPhone.split(";");
    	
    	log.debug("안면인식 토큰발급 sms callPhone => {}", callPhone);
    	log.debug("안면인식 토큰발급 sms callMessage => {}", callMessage);
    	
    	SmsRequest smsParam;
    	String callphone1 = "";
    	String callphone2 = "";
    	String callphone3 = "";
    	
    	if(arrCallPhone != null && arrCallPhone.length > 0) {
    		for(String phoneNumber : arrCallPhone) {
    			smsParam = new SmsRequest();
    			
    			if (phoneNumber.length() == 10) {
    				callphone1 = phoneNumber.substring(0, 2);
    				callphone2 = phoneNumber.substring(2, 5);
    				callphone3 = phoneNumber.substring(5);
    			} else if (phoneNumber.length() == 11) {
    				callphone1 = phoneNumber.substring(0, 3);
    				callphone2 = phoneNumber.substring(3, 7);
    				callphone3 = phoneNumber.substring(7);
    			} else {
    				continue;
    			}
    			
    			try {
    				smsParam.setMember("0");
        	        smsParam.setUserCode("ebanking");
        	        smsParam.setUserName("I5");
        	        smsParam.setCallPhone1(callphone1);
        	        smsParam.setCallPhone2(callphone2);
        	        smsParam.setCallPhone3(callphone3);
        	        smsParam.setCallMessage(callMessage);
        	        smsParam.setRateDate(DateUtils.getCurrentDate(DateUtils.YYYYMMDD));
        	        smsParam.setRateTime(DateUtils.getCurrentTime(DateUtils.HHMMSS));
        	        smsParam.setReqPhone1("");
        	        smsParam.setReqPhone2("1588");
        	        smsParam.setReqPhone3("1599");
        	        smsParam.setCallName("관리자");
        	        smsParam.setDeptCode("GL9-KI3-HS9");
        	        smsParam.setDeptName("e-뱅u-12103 부");
        	    	
        	    	String result = smsComponent.sendMain(smsParam);
        	    	
        	    	log.debug("안면인식 토큰발급 sms 발송결과 => {}", result);
    			} catch (PRCServiceException e) {
    				log.debug(e.getErrorMessage(), e);
    			}
    		}
    	}
    }
    
}
