package com.scbank.process.api.svc.common.service.settings.dto.app;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IntegrationMessage(id = "HomeMenuInfo", description = "메뉴 모드별 정보", type = Type.RESPONSE)
public class HomeMenuInfo implements IMessageObject {

    @MessageField(id = "normal", name = "일반뱅킹 메뉴정보")
    @RepeatedField(repeatType = RepeatType.NONE)
    public List<MenuInfo> normal;

    @MessageField(id = "easy", name = "편한뱅킹 메뉴정보")
    @RepeatedField(repeatType = RepeatType.NONE)
    public List<MenuInfo> easy;

    @MessageField(id = "english", name = "영문뱅킹 메뉴정보")
    @RepeatedField(repeatType = RepeatType.NONE)
    public List<MenuInfo> english;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @IntegrationMessage(id = "MenuInfo", description = "탭정보", type = Type.RESPONSE)
    public static class MenuInfo implements IMessageObject {

        @MessageField(id = "order", name = "순번")
        private int order;

        @MessageField(id = "type", name = "구분")
        private String type;

        @MessageField(id = "url", name = "url")
        private String url;
    }

}
