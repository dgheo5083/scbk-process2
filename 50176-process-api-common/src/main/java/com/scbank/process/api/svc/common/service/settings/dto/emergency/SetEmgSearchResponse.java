package com.scbank.process.api.svc.common.service.settings.dto.emergency;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetEmgSearchResponse", description = "PMS 긴급공지 응답 DTO", type = Type.RESPONSE)
public class SetEmgSearchResponse implements IMessageObject {

    @MessageField(id = "ntcShowYn", name = "공지사항 노출여부")
    private String ntcShowYn;

    @MessageField(id = "ntcImgUrl", name = "공지사항 이미지 URL")
    private String ntcImgUrl;

    @MessageField(id = "ntcPopupYn", name = "공지사항 팝업여부")
    private String ntcPopupYn;

    @MessageField(id = "ntcBtnFlg", name = "버튼플래그 (C:닫기, L:링크)")
    private String ntcBtnFlg;

    @MessageField(id = "ntcBtnUrl", name = "버튼링크")
    private String ntcBtnUrl;

    @MessageField(id = "ntcBtnName", name = "버튼명")
    private String ntcBtnName;

    @MessageField(id = "ntcTitle", name = "제목")
    private String ntcTitle;

    @MessageField(id = "ntcContents", name = "내용")
    private String ntcContents;

}
