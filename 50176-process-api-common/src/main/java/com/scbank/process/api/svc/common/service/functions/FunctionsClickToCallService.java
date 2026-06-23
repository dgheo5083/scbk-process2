package com.scbank.process.api.svc.common.service.functions;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.common.dao.NfCustMgtDao;
import com.scbank.process.api.svc.common.dao.dto.NfCustInfoResult;
import com.scbank.process.api.svc.common.service.functions.dto.clickToCall.FncCtcSendRequest;
import com.scbank.process.api.svc.common.service.functions.dto.clickToCall.FncCtcSendResponse;
import com.scbank.process.api.svc.common.service.functions.dto.clickToCall.FncCtcValidateResponse;
import com.scbank.process.api.svc.shared.components.clickToCall.ClickToCallComponent;
import com.scbank.process.api.svc.shared.components.clickToCall.dto.ClickToCallRequest;
import com.scbank.process.api.svc.shared.components.clickToCall.dto.ClickToCallResponse;
import com.scbank.process.api.svc.shared.utils.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/functions/clickToCall", name = "전화상담")
public class FunctionsClickToCallService {

    private final ClickToCallComponent clickToCall;

    private final NfCustMgtDao nfCustMgtDao;

    /**
     * ASIS : MA3CSTCLT002_101S
     */
    @ServiceEndpoint(url = "/validate", name = "검증")
    public FncCtcValidateResponse validate(IServiceContext serviceContext) {

        FncCtcValidateResponse response = new FncCtcValidateResponse();

        String perBusNo = SessionUtils.getSessionValue("PerBusNo");

        // perBusNo = "6512121999123";

        if (StringUtils.isBlank(perBusNo)) {
            response.setName("");
        }

        if (StringUtils.isNotBlank(perBusNo)) {
            NfCustInfoResult nfCustInfoResult = nfCustMgtDao.selectNfCustInfo(perBusNo);

            if (nfCustInfoResult != null) {

                if (StringUtils.isNotBlank(nfCustInfoResult.getSsn())) {
                    if (SessionUtils.isLoginOrAuth("AUTH")) {
                        String cmpndCheckKey = nfCustInfoResult.getCmpndCheckKey();

                        if (!validAuth(cmpndCheckKey)) {
                            throw new PRCServiceException(
                                    "",
                                    "WAS 앗 접속이 끊겼어요! 서비스를 다시 이용해 주세요. 일정시간 거래가 없었거나 통신상태가 불안정한 경우 또는 다른 기기로 동시 접속 시 종료됩니다.");
                        }
                    }

                    String custName = SessionUtils.getSessionValue("CustName");
                    String hpNum = SessionUtils.getSessionValue("HpNum");

                    response.setName(custName);
                    response.setMobileNum(hpNum);
                }

            }
        }

        return response;
    }

    /**
     * ASIS : MA3CSTCLT002_102S
     */
    @ServiceEndpoint(url = "/send", name = "전화상담 요청")
    public FncCtcSendResponse send(IServiceContext serviceContext,
            FncCtcSendRequest request) {

        String perBusNo = SessionUtils.getSessionValue("PerBusNo");

        FncCtcSendResponse response = new FncCtcSendResponse();

        if ("Y".equals(request.getTimeChkYn())) {
            String nowCurrDate = DateUtils.getCurrentDate("HHmmss");

            // 컨택센터 연결 시간 0900 ~ 1800
            if (Integer.parseInt(nowCurrDate) > 180000 || Integer.parseInt(nowCurrDate) < 90000) {
                response.setTimeOver("Y");

                return response;
            }
        }

        // clicktocall 바로상담은 실제 전화요청이 접수되어 운영환경에서만 동작하도록 수정
        // > 실제 상담사연결해서 확인 필요 시 은행담당자통해 센터측에 사전공지 후 테스트진행할 것
        // TODO: 실테스트 한번 해봐야 한다!!!!
        if (RunMode.PRD.equals(RuntimeContext.getRunMode())) {
            ClickToCallRequest clickToCallRequest = ClickToCallRequest.builder()
                    .perBusNo(perBusNo)
                    .custName(request.getCustName())
                    .servicePath(request.getServicePath())
                    .callGroup(request.getCallGroup())
                    .custTelNo(request.getCustTelNo())
                    .url(request.getUrl())
                    .command(request.getCommand().replaceAll("&gt;", ">"))
                    .build();
            ClickToCallResponse clickToCallResponse = clickToCall.send(clickToCallRequest);
            response.setErrCode(clickToCallResponse.getErrorCode());
        } else {
            response.setErrCode("200");
        }

        return response;

    }

    private boolean validAuth(String checkKey) {
        try {
            String authTime = DateUtils.getCurrentDate("yyyyMMddHHmmssSS");
            String regNum = StringUtils.defaultString(SessionUtils.getSessionValue("PerBusNo"));
            String checkKeys[] = checkKey.split("_");

            log.debug("checkKeys ::" + checkKeys[0] + ":: ::" + checkKeys[1] + "::");

            if (checkKeys.length != 2) { // 키 '_' 로 잘랐을때 2개의 값이 나오지 않으면 false
                log.debug("checkKeys.length != 2");
                return false;
            }

            if (!regNum.equals(checkKeys[0])) { // 주민번호가 다르면 false 이걸 세션에서비교??
                log.debug("!regNum.equals(checkKeys[0])");
                return false;
            }

            if (!authTime.equals(checkKeys[1])) { // 인증 시간이 다르면
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS");
                Date thisAuthTime = formatter.parse(authTime);
                Date paramAuthTime = formatter.parse(checkKeys[1]);

                long diff = paramAuthTime.getTime() - thisAuthTime.getTime();

                if (diff > 0) {
                    log.debug("paramAuthTime.getTime() - thisAuthTime.getTime() > 0");
                    log.debug("thisAuthTime ::" + thisAuthTime.getTime() + "::");
                    log.debug("paramAuthTime ::" + paramAuthTime.getTime() + "::");
                    return false; // parameter로 넘어온 값이 더 작은경우
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
