package com.scbank.process.api.svc.common.components;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.scbank.process.api.edmi.dto.mci.MciIbEdms001Req;
import com.scbank.process.api.edmi.dto.mci.MciIbEdms001Res;
import com.scbank.process.api.edmi.dto.mci.MciIbEdms001Res.DOCU;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.edoc.EdocAPComponent;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;
import com.scbank.process.api.svc.shared.integration.HostClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class SupportCustomerCenter {

    /**
     * EDMI 통합 클라이언트
     */
    private final HostClient hostClient;

    private final EdocAPComponent edocAPComponent;

    @ComponentOperation(name = "여신, 펀드 계약서류제공 신청내역 조회")
    public MciIbEdms001Res listFundRequest(String acctNum) {

        // 공통부 세팅
        MciRequestOptions mciOption = this.hostClient.getMciRequestOptions("MCI_IB_EDMS_001");
        mciOption.setTranCd("IB_EDMS_001"); // AS-IS BUSINESS_FUNCTION_ID
        mciOption.setBlngBrNo("0019"); // AS-IS AGENTBRANCH
        mciOption.setTxnBrNo("0019"); // AS-IS HOMEBRANCH

        List<DOCU> docuList2 = new ArrayList<>();
        // 개별부 세팅
        MciIbEdms001Req mciReq = MciIbEdms001Req.builder()
                .ACCT_NO(acctNum)
                .build();
        log.debug("*** mciReq :: {}", mciReq);

        MciIbEdms001Res mciRes = this.hostClient.sendMci(mciOption, mciReq, MciIbEdms001Res.class).getResponse();
        log.debug("*** mciRes :: {}", mciRes);

        // List<DOCU> docuList = mciRes.getDOCULIST();
        return mciRes;

    }

    @ComponentOperation(name = "EDMS 계약서류조회 PDF 파일이 존재하는지 체크")
    public JSONObject hasEdmsPdfFile(String edmsXvarmDocMeta) {

        EdocPayloadInfo payload = new EdocPayloadInfo();

        edmsXvarmDocMeta = StringUtils.defaultIfEmpty(edmsXvarmDocMeta, "[{}]");

        payload.setEdmsXvarmDocMeta(edmsXvarmDocMeta);
        JSONObject result = edocAPComponent.requestEdmsXvarmDocCheck(payload);

        return result;

    }

}
