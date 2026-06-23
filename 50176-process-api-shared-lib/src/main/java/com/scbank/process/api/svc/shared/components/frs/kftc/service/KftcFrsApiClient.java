package com.scbank.process.api.svc.shared.components.frs.kftc.service;

import org.springframework.beans.factory.InitializingBean;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.frs.kftc.model.KftcFrsAuthVo;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpRequestEntity;
import com.scbank.process.api.svc.shared.components.frs.model.FrsHttpResponseEntity;
import com.scbank.process.api.svc.shared.components.frs.util.FrsUniqueUtils;
import com.scbank.process.api.svc.shared.components.frs.util.KftcFrsHelper;
import com.scbank.process.api.svc.shared.config.BaseHttpClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 금결원 안면인식 API 클라이언트 공유 컴포넌트
 * AS-IS를 참고하여 재작성하였음
 */
@Slf4j
@SharedComponent(name = "금결원 안면인식 요청 공유 컴포넌트")
public class KftcFrsApiClient extends KftcFrsApiClientBase implements InitializingBean {
	
	private static KftcFrsAuthVo frsAuthInfo;
	private final KftcFrsTokenManager kftcFrsTokenManager;
	
	public KftcFrsApiClient(BaseHttpClient httpClient, KftcFrsTokenManager kftcFrsTokenManager) {
		super(httpClient);
		this.kftcFrsTokenManager = kftcFrsTokenManager;
	}

	@Override
    public void afterPropertiesSet() throws Exception {
    	frsAuthInfo = new KftcFrsAuthVo();
    }
	
	@ComponentOperation(name = "안면인식 API 클라이언트 초기화 처리")
    public KftcFrsApiClient init() {
        validationOAuth();
        return this;
    }
	
	/**
     * 
     */
    @ComponentOperation(name = "안면인식 API 토큰 검증 요청")
    public void validationOAuth() {
        try {
            if (!frsAuthInfo.isAccessToken()) {
            	frsAuthInfo.clone(this.kftcFrsTokenManager.getToken());
            }
        } catch (PRCServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PRCServiceException("KFTC-MA004", "연동 정보가 유효하지 않습니다.", e);
        }
    }
    
    @ComponentOperation(name = "API토큰정보 획득 메소드")
    public KftcFrsAuthVo getAuth() {
		return frsAuthInfo;
	}
    
    /**
	 * 거래고유번호 생성
	 * 기관코드(3자리) + yyMMdd(6자리) + 기관부여번호*(11자리, 대문자와 숫자로 구성) 
	 */
    @ComponentOperation(name = "거래ID 획득 메소드")
	public String generateTranId() {
		
		return new StringBuilder(20)
				.append(KftcFrsHelper.ORG_CODE)
				.append(DateUtils.getCurrentDate("yyMMdd"))
				.append(FrsUniqueUtils.randomIdByPidToString11())
				.toString();
	}
    
    @ComponentOperation(name = "안면인식 API 송/수신 처리")
    public FrsHttpResponseEntity execute(FrsHttpRequestEntity entity) {
        return this.executeCall(entity);
    }
}
