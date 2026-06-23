package com.scbank.process.api.svc.common.service.identity;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.common.service.identity.dto.liveness.IdtLivGetTargetRequest;
import com.scbank.process.api.svc.common.service.identity.dto.liveness.IdtLivGetTargetResponse;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/identity/liveness", name = "실명확인 - 안면인식")
public class IdentityLivenessService {

    @ServiceEndpoint(url = "/getTarget", name = "안면인식 대상 여부 확인")
    public IdtLivGetTargetResponse getTarget(IServiceContext serviceContext,
            IdtLivGetTargetRequest request) {

        String bizType = StringUtils.defaultIfEmpty(request.getBizType(), "");

        // S.안면인식 대상여부
        String livenessTarget = "FACE_TARGET_" + bizType; // 프로퍼티에서 안면인식 대상여부 플래그 조회
        String livenessTargetYn = "N"; // 안면인식 대상여부 플래그

        if (PRCSharedUtils.isSB()) { // APP
            // 안면인식 대상여부 플래그
            livenessTargetYn = StringUtils.defaultIfEmpty(PropertiesUtils.getString(livenessTarget), "N");
        } else { // WEB
            livenessTargetYn = "N";
        }

        IdtLivGetTargetResponse response = new IdtLivGetTargetResponse();

        response.setLivenessTargetYn(livenessTargetYn);

        return response;
    }

}
