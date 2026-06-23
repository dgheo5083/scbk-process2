package com.scbank.process.api.svc.common.service.functions.dto.search;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;

import lombok.Data;

@Data
@IntegrationMessage(id = "FncSchFindBranchResponse", description = "영업점찾기 응답", type = Type.RESPONSE)
public class FncSchFindBranchResponse implements IMessageObject {

    @MessageField(id = "branchInfoList", name = "영업점목록")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<BranchInfo> branchInfoList;

    @Data
    public static class BranchInfo implements IMessageObject {

        @MessageField(id = "branchId", name = "영업점 ID")
        private String branchId;

        @MessageField(id = "branchType", name = "영업점 구분")
        private String branchType;

        @MessageField(id = "branchCd", name = "영업점 코드")
        private String branchCd;

        @MessageField(id = "branchName", name = "영업점 명")
        private String branchName;

        @MessageField(id = "branchTel1", name = "영업점 전화번호1")
        private String branchTel1;

        @MessageField(id = "branchTel2", name = "영업점 전화번호2")
        private String branchTel2;

        @MessageField(id = "branchTel3", name = "영업점 전화번호3")
        private String branchTel3;

        @MessageField(id = "branchAddress", name = "영업점 주소")
        private String branchAddress;
    }
}
