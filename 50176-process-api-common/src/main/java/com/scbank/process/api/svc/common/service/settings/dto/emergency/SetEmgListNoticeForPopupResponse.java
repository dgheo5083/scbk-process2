package com.scbank.process.api.svc.common.service.settings.dto.emergency;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "SetEmgListNoticeForPopupResponse", description = "팝업용 긴급공지 응답 DTO", type = Type.RESPONSE)
public class SetEmgListNoticeForPopupResponse implements IMessageObject {

    @MessageField(id = "noticeForPopupList", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<NoticeForPopup> noticeForPopupList;

    @Data
    public static class NoticeForPopup implements IMessageObject {
        @MessageField(id = "id", name = "id")
        private String id;

        @MessageField(id = "title", name = "제목")
        private String title;

        @MessageField(id = "subTitle", name = "부 제목")
        private String subTitle;

        @MessageField(id = "targetVersion", name = "MA30/MA40")
        private String targetVersion;

        @MessageField(id = "targetUrl", name = "타겟 URL (MA30 용)")
        private String targetUrl;

        @MessageField(id = "targetMenuId", name = "타겟 MenuId (MA40 용)")
        private String targetMenuId;

        @MessageField(id = "targetParams", name = "타겟 Params (MA40 용)")
        private String targetParams;

        @MessageField(id = "productGb", name = "구분")
        private String productGb;
    }

}
