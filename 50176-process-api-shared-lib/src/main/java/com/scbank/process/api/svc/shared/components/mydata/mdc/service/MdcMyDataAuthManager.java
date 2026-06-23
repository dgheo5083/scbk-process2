package com.scbank.process.api.svc.shared.components.mydata.mdc.service;

import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.mydata.IMyDataClient;
import com.scbank.process.api.svc.shared.components.mydata.mdc.model.MdcMyDataAuthVo;
import com.scbank.process.api.svc.shared.components.mydata.model.MyDataHttpJsonObject;
import com.scbank.process.api.svc.shared.dao.Ma3MydataApiTokenDao;
import com.scbank.process.api.svc.shared.dao.dto.Ma3MydataApiTokenParameter;
import com.scbank.process.api.svc.shared.dao.dto.Ma3MydataApiTokenResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AS-IS MdcMyDataApiClient <-> MdcMyDataService 간의 역참조로 인하여 리팩토링 실시함
 */
@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "마이데이터 인증키 관리자 공유 컴포넌트")
public class MdcMyDataAuthManager {

    /**
     * 마이데이터 인증 API 클라이언트
     */
    private final MdcMyDataAuthClient myDataAuthClient;

    /**
     * 마이데이터 인증키 DAO
     */
    private final Ma3MydataApiTokenDao mydataApiTokenDao;

    @ComponentOperation(name = "마이데이터 인증키 획득 및 신규 발급")
    public MdcMyDataAuthVo getToken() {
        String scope = "mobile";
        Ma3MydataApiTokenResult result = this.mydataApiTokenDao.selectToken(scope);
        if (result != null) {
            String token = StringUtils.defaultIfEmpty(result.getAccessToken(), "").trim();

            log.info("##### MdcMyDataServiceImpl::getToken token ##### {}", token);

            MyDataHttpJsonObject jsonToken = MyDataHttpJsonObject.fromObject(token);

            MdcMyDataAuthVo voToken = new MdcMyDataAuthVo().clone(jsonToken);

            if (voToken.isAccessToken()) {
                return voToken;
            }

            log.info("인증키를 만료로 인하여 인증키 새로 발급합니다.");
        }

        return this.changeToken();
    }

    @ComponentOperation(name = "마이데이터 인증키 DB 조회")
    public MdcMyDataAuthVo getSelectToken() {
        Ma3MydataApiTokenResult result = this.mydataApiTokenDao.selectToken("mobile");
        if (result == null) {
            throw new PRCServiceException(IMyDataClient.ERR_MA999, "토근 정보가 없습니다. 토큰 발급 후 진행 하세요.");
        }

        String token = StringUtils.defaultIfEmpty(result.getAccessToken(), "{}");

        MyDataHttpJsonObject jsonToken = MyDataHttpJsonObject.fromObject(token);

        MdcMyDataAuthVo voToken = new MdcMyDataAuthVo().clone(jsonToken);

        return voToken;
    }

    @ComponentOperation(name = "마이데이터 인증키 변경")
    @Transactional(value = "kfbdbTransactionManager", rollbackFor = { Throwable.class })
    public MdcMyDataAuthVo changeToken() {
        log.debug("##### MdcMyDataServiceImpl::changeToken #####");

        // KONG 에 토큰 발급 요청
        MyDataHttpJsonObject result = this.myDataAuthClient.getToken().throwException().getBody();

        // 발급일자
        String tokenDate = result.getAsString("expires_date", "").toString();

        JSONObject tokenMap = new JSONObject();

        tokenMap.put("access_token", result.getAsString("access_token", "").toString()); // 접속토큰
        tokenMap.put("expires_date", result.getAsString("expires_date", "").toString()); // 토큰 발급일자
        tokenMap.put("token_type", result.getAsString("token_type", "").toString());
        tokenMap.put("expires_in", Long.parseLong(result.getAsString("expiresIn", "").toString()));

        result.getAsJsonObject().put("DATA", tokenMap);

        disableToken();

        enableToken(tokenDate, result);

        return new MdcMyDataAuthVo().clone(result);
    }

    @ComponentOperation(name = "마이데이터 인증키 활성화")
    public void enableToken(String tokenDate, MyDataHttpJsonObject result) {
        Ma3MydataApiTokenParameter parameter = Ma3MydataApiTokenParameter.builder()
                .tokenDate(tokenDate)
                .connectLoc("MYDATA")
                .scope("mobile")
                .accessToken(result.getAsString("DATA", "").toString())
                .expiresIn(result.getAsString("expiresIn", "").toString())
                .tokenId(result.getAsString("token_id", "").toString())
                .build();

        this.mydataApiTokenDao.enabledToken(parameter);
    }

    @ComponentOperation(name = "마이데이터 인증키 폐기")
    public void disableToken() {
        this.mydataApiTokenDao.disabledToken("mobile");
    }

    @ComponentOperation(name = "changeInternetToken")
    public MdcMyDataAuthVo changeInternetToken() {
        // KONG 에 토큰 발급 요청
        MyDataHttpJsonObject result = this.myDataAuthClient.getInternetToken().throwException().getBody();

        // 발급일자
        String tokenDate = result.getAsString("expires_date", "").toString();

        JSONObject tokenMap = new JSONObject();

        tokenMap.put("access_token", result.getAsString("access_token", "").toString()); // 접속토큰
        tokenMap.put("expires_date", result.getAsString("expires_date", "").toString()); // 토큰 발급일자
        tokenMap.put("token_type", result.getAsString("token_type", "").toString());
        tokenMap.put("expires_in", Long.parseLong(result.getAsString("expiresIn", "").toString()));

        result.getAsJsonObject().put("DATA", tokenMap);

        disableInternetToken();

        enableInternetToken(tokenDate, result);

        return new MdcMyDataAuthVo().clone(result);
    }

    @ComponentOperation(name = "enableInternetToken")
    public void enableInternetToken(String tokenDate, MyDataHttpJsonObject result) {
        Ma3MydataApiTokenParameter parameter = Ma3MydataApiTokenParameter.builder()
                .tokenDate(tokenDate)
                .connectLoc("MYDATA")
                .scope("internet")
                .accessToken(result.getAsString("DATA", "").toString())
                .expiresIn(result.getAsString("expiresIn", "").toString())
                .tokenId(result.getAsString("token_id", "").toString())
                .build();

        log.debug("## enableInternetToken parameter ::::: {}", parameter);

        this.mydataApiTokenDao.enabledToken(parameter);
    }

    @ComponentOperation(name = "disableInternetToken")
    public void disableInternetToken() {
        log.debug("# disableInternetToken");
        this.mydataApiTokenDao.disabledToken("internet");
    }
}
