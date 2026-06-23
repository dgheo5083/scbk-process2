package com.scbank.process.api.svc.common.service.support.dto.branchAtm;

import java.util.List;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage;
import com.scbank.process.api.fw.message.annotation.IntegrationMessage.Type;
import com.scbank.process.api.fw.message.annotation.MessageField;
import com.scbank.process.api.fw.message.annotation.RepeatedField;
import com.scbank.process.api.fw.message.enums.RepeatType;
import com.scbank.process.api.svc.common.dao.dto.SearchCityResult;
import com.scbank.process.api.svc.common.dao.dto.SearchLocalResult;
import com.scbank.process.api.svc.common.dao.dto.SearchNationResult;

import lombok.Data;

@Data
@IntegrationMessage(id = "SupLocFindForeignBranchCategoryResponse", type = Type.RESPONSE)
public class SupLocFindForeignBranchCategoryResponse implements IMessageObject {

    @MessageField(id = "searchNationList", name = "국가 목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    List<SearchNationResult> searchNationList;

    @MessageField(id = "searchCityList", name = "도시 목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    List<SearchCityResult> searchCityList;

    @MessageField(id = "searchLocalList", name = "지역 목록")
    @RepeatedField(repeatType = RepeatType.REFERENCE)
    List<SearchLocalResult> searchLocalList;

}
