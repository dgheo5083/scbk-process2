package com.scbank.process.api.svc.shared.components.edoc;

import org.json.JSONObject;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class EdocAPComponent {

    private final EdocAPConnector connector;

    @ComponentOperation(name = "전자문서 전자서명 주입 및 XVARM 저장 요청")
    public JSONObject reqCreateSignPDF(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_CREATE_SIGN_PDF);

        return connector.request(edocPayloadInfo, "SIGNPDF_0001");
    }

    @ComponentOperation(name = "AMS HOST (여신계정계)와 통신하기위해 전자문서AP 서버를 호출")
    public JSONObject requestAMSHost(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_AMS_HOST);

        return connector.request(edocPayloadInfo, "AMSHOST_0001");
    }

    @ComponentOperation(name = "LPC 이미지 전송 (LPC XVARM ENGINE에 이미지 전송) 거래를 하기위해 전자문서AP 서버를 호출")
    public JSONObject requestLPCImage(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_LPC_IMAGE_TRANSFER);

        return connector.request(edocPayloadInfo, "LPC_IMG_0001");
    }

    @ComponentOperation(name = "LPC META (상품 META 및 문서 META정보 LPC에 전송) 거래를 하기위해 전자문서AP 서버를 호출")
    public JSONObject requestLPCMeta(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_LPC_IMAGE_META_TRANSFER);

        return connector.request(edocPayloadInfo, "LPCMETA_0002");
    }

    @ComponentOperation(name = "담보대출 후처리(TSA 인증 , LPC 문서전송 , LPC META 전송 , 공전소 발송등록)를 진행하기위해 전자문서AP 서버를 호출")
    public JSONObject requestWarrentPost(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_WARRENTPOST);

        return connector.request(edocPayloadInfo);
    }

    @ComponentOperation(name = "대출 추가문서를 전자문서 서버에 전송")
    public JSONObject requestAddTrans(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_LPC_ADD_TRANS);

        edocPayloadInfo.setIsAddScanYn("Y");

        return connector.request(edocPayloadInfo, "ADDTRAN_0001");
    }

    @ComponentOperation(name = "인터넷대출일때 전자문서 META를 LPC에 추가송부 전문으로 전송")
    public JSONObject requestInternetLoanImageMetaTrans(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_LPC_INTERNET_LOAN_IMAGE_META_TRANSFER);

        return connector.request(edocPayloadInfo, "INTM_0001");
    }

    @ComponentOperation(name = "전자문서서버에 공전소에 문서 전송 등록을 요청")
    public JSONObject requestOfficialTransRegist(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_OFFICIAL_TRANSFER_REGIST);

        return connector.request(edocPayloadInfo, "OFFICAL_0001");
    }

    @ComponentOperation(name = "LPC 복구처리를 진행")
    public JSONObject requestLPCRecovery(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_LPC_RECOVERY);

        return connector.request(edocPayloadInfo, "LPC_RCV_0001");
    }

    @ComponentOperation(name = "공전소 복구처리를 진행")
    public JSONObject requestOfficailRecovery(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_OFFICIAL_RECOVERY);

        return connector.request(edocPayloadInfo, "OFF_RCV_0001");
    }

    @ComponentOperation(name = "이메일을 발송(양식)")
    public JSONObject requestEmailSend(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_SEND_EMAIL);

        return connector.request(edocPayloadInfo, "EMAILSD_0001");
    }

    @ComponentOperation(name = "이메일을 발송한다.(전자문서)")
    public JSONObject requestEmailTermsSend(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_SEND_EMAIL_TERMS);

        return connector.request(edocPayloadInfo, "EMAILSD_0002");
    }

    @ComponentOperation(name = "LPC 상품 메타만 전송")
    public JSONObject requestLPCMetaTransfer(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_SEND_LPC_META_TRANSFER);

        return connector.request(edocPayloadInfo, "LPCMETA_0003");
    }

    @ComponentOperation(name = "LPC 이미지 전송 백업 (LPC XVARM ENGINE에 이미지 전송) 거래를 하기위해 전자문서AP 서버를 호출")
    public JSONObject requestLPCImageBackUp(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_LPC_IMAGE_TRANSFER_BACKUP);

        return connector.request(edocPayloadInfo);
    }

    @ComponentOperation(name = "ELS 전자문서 전자서명 주입 및 XVARM 저장 요청")
    public JSONObject reqESLCreateSignPDF(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_CREATE_ESL_SIGN_PDF);

        return connector.request(edocPayloadInfo, "SIGNPDF_0001");
    }

    @ComponentOperation(name = "추가서류 등록")
    public JSONObject requestAddDocs(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_LPC_ADD_DOCS);

        return connector.request(edocPayloadInfo, "");
    }

    /* TODO: 변경필요 */
    @ComponentOperation(name = "Edoc 서버 정상여부 체크")
    public JSONObject requestHealthCheck(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_HEALTH_CHECK);

        return connector.request(edocPayloadInfo);
    }

    @ComponentOperation(name = "Edoc 서버 EDMS XVARM 계약문서 다운로드 서비스")
    public JSONObject requestEdmsXvarmDocDownload(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_EDMS_DOC_DOWNLOAD);

        return connector.request(edocPayloadInfo);
    }

    @ComponentOperation(name = "Edoc 서버 EDMS XVARM 계약문서 바코드 주입 서비스(UI에서 생성된 PDF)")
    public JSONObject requestEdmsXvarmDocCheck(EdocPayloadInfo edocPayloadInfo) {

        edocPayloadInfo.setAction(EdocConstant.EDOC_AP_ACTION_EDMS_DOC_CHECK);

        return connector.request(edocPayloadInfo);
    }

}
