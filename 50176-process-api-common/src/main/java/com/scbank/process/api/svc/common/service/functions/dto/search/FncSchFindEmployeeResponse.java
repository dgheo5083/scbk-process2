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
@IntegrationMessage(id = "FncSchFindEmployeeResponse", description = "직원조회 응답", type = Type.RESPONSE)
public class FncSchFindEmployeeResponse implements IMessageObject {

    @MessageField(id = "employeeInfoList", name = "직원목록")
    @RepeatedField(repeatType = RepeatType.NONE)
    private List<EmployeeInfo> employeeInfoList;

    @Data
    public static class EmployeeInfo implements IMessageObject {

        @MessageField(id = "emplyNm", name = "직원명")
        private String emplyNm;

        @MessageField(id = "clerkNo", name = "직원번호")
        private String clerkNo;

        @MessageField(id = "brnchNo", name = "영업점명")
        private String brnchNo;

        @MessageField(id = "deptNm", name = "부서명")
        private String deptNm;
    }

}
