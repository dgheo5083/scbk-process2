package com.scbank.process.api.svc.shared.components.edoc;

import java.util.List;

import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class EdocHelper {

    private final SmsComponent sms;

    /**
     * 전자문서 통신 오류 및 실패시 IT담당자에게 SMS발송
     * 
     * @throws SCBKAppException
     */
    @ComponentOperation(name = "전자문서 통신 오류 및 실패시 IT담당자에게 SMS발송")
    public void sendLpcError(EdocPayloadInfo edocPayloadInfo, String errorModule) {

        List<ICodeItemInfo> codeList = CodeUtils.getCodes("ERR_LPC_PHONE_NO");
        String smsSendMessage = this.getEdocErrorMsg(edocPayloadInfo, errorModule);

        SmsRequest request = new SmsRequest();

        request.setMember("0");
        request.setUserCode("ebanking");
        request.setUserName("15");
        request.setCallMessage(smsSendMessage);
        request.setRateDate(DateUtils.getCurrentDate("yyyyMMdd"));
        request.setRateTime(DateUtils.getCurrentDate("HH24MI"));
        request.setReqPhone1("");
        request.setReqPhone2("1588");
        request.setReqPhone3("1599");
        request.setCallName("[sc제일은행]");
        request.setDeptCode("OG5-QY5-MQ5");
        request.setDeptName("e-뱅u-12103 부");

        for (int i = 0; i < codeList.size(); i++) {
            String sendPhone = codeList.get(i).getValue();
            String[] sendPhoneSplit = sendPhone.split("-");

            request.setCallPhone1(sendPhoneSplit[0]);
            request.setCallPhone2(sendPhoneSplit[1]);
            request.setCallPhone3(sendPhoneSplit[2]);

            sms.sendMain(request);
        }
    }

    /**
     * 전자문서 연계 통신 오류 및 실패시 운영 담당자에 보낼 메세지 조립
     * 
     * @param input
     * @return
     */
    private String getEdocErrorMsg(EdocPayloadInfo edocPayloadInfo, String errorModule) {
        String resultMessage = "";
        String TRAD_NO = StringUtils.defaultIfEmpty(edocPayloadInfo.getTradNo(), "");
        String bizType = StringUtils.defaultIfEmpty(edocPayloadInfo.getBizType(), "");
        String ErrorModule = errorModule; // 에러STEP 정의함.
        String smsErrorMsg = CodeUtils.getCodeValue("SMS_ERROR_MSG", ErrorModule);
        String PRDCT_NM = CodeUtils.getCodeValue("PRODUCT_NAME", bizType);
        log.debug("#### WarrentErrorSMSService PRDCT_NM : " + PRDCT_NM);
        log.debug("#### WarrentErrorSMSService SMS_ERROR_MSG Default : " + smsErrorMsg);
        smsErrorMsg = StringUtils.replace(smsErrorMsg, "~~TRAD_NO~~", TRAD_NO);
        smsErrorMsg = StringUtils.replace(smsErrorMsg, "~~PRDCT_NM~~", PRDCT_NM);
        resultMessage = smsErrorMsg;
        return resultMessage;
    }

}
