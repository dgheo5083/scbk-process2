package com.scbank.process.api.svc.shared.components.sign.dto;

import java.util.HashMap;
import java.util.Map;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignActionType;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignVerifyType;

import lombok.Data;

@Data
public class SignVerifyInfo {

    private SignVerifyType signVerifyType;

    private SignActionType signActionType;

    // MCI 거래코드
    private String tranCd = "";

    private String imsTranCd = "";

    // OLTP 거래코드(DB저장시 거래코드)
    private String inClassCd = "";

    private String svcCd = "";

    private String jobTp = "";

    private String connectType = "";

    private IMessageObject messageRequest;

    private Map<String, String> messageMap = new HashMap<>();

    public SignVerifyInfo clearMessage() {
        this.messageMap.clear();
        return this;
    }

    public SignVerifyInfo putMessage(String key, String value) {
        messageMap.put(key, value);
        return this;
    }

    public SignVerifyInfo removeMessage(String key) {
        messageMap.remove(key);
        return this;
    }

    public Map<String, String> getMessageMap() {
        return this.messageMap;
    }

}
