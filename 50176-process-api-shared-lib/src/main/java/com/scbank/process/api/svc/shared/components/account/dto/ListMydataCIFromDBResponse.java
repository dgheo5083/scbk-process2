package com.scbank.process.api.svc.shared.components.account.dto;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.shared.dao.dto.MyDataCIInfoResult;

import lombok.Data;

/**
 * CSL 서비스 요청 정보 클래스
 * 마이데이터 CI 조회
 */
@Data
@IntegrationMessage(id = "ListMydataCIFromDBResponse", type = Type.RESPONSE)
public class ListMydataCIFromDBResponse implements IMessageObject {
    @MessageField(id = "pmsImgRootUrl", name = "")
    private String pmsImgRootUrl;

    @MessageField(id = "resultNullYN", name = "")
    private String resultNullYN;

    @MessageField(id = "mdtCIList", name = "반복부")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    private List<MyDataCIInfoResult> mdtCIList;

}
