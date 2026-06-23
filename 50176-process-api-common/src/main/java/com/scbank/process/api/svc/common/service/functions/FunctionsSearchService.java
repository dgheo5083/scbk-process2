package com.scbank.process.api.svc.common.service.functions;

import java.util.List;

import com.scbank.process.api.edmi.dto.mci.MciIbAddr0101Req;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0101Res;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0104Req;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0104Res;
import com.scbank.process.api.edmi.dto.mci.MciTm1517090001Req;
import com.scbank.process.api.edmi.dto.mci.MciTm1517090001Res;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciContTran;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.PrdctSggstrInfoDao;
import com.scbank.process.api.svc.common.dao.dto.BranchInfoResult;
import com.scbank.process.api.svc.common.dao.dto.EmployeeInfoParameter;
import com.scbank.process.api.svc.common.dao.dto.EmployeeInfoResult;
import com.scbank.process.api.svc.common.mapper.FunctionsSearchMapper;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFacialRecognitionEligibilityRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFacialRecognitionEligibilityResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressRefineRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressRefineResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindAddressResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindBranchRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindBranchResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindEmployeeRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindEmployeeResponse;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindOfficeRequest;
import com.scbank.process.api.svc.common.service.functions.dto.search.FncSchFindOfficeResponse;
import com.scbank.process.api.svc.shared.integration.HostClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(url = "/functions/search", name = "검색")
public class FunctionsSearchService {
    /**
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;

    private final HostClient hostClient;

    private final FunctionsSearchMapper mapper;

    private final PrdctSggstrInfoDao prdctSggstrInfoDao;

    @ServiceEndpoint(url = "/facialRecognitionEligibility", name = "안면인식 대상 여부 확인")
    public FncSchFacialRecognitionEligibilityResponse facialRecognitionEligibility(IServiceContext serviceContext,
            FncSchFacialRecognitionEligibilityRequest request) {

        String bizType = StringUtils.defaultIfEmpty(request.getBizType(), "");

        // S.안면인식 대상여부
        String faceTarget = "FACE_TARGET_" + bizType; // 프로퍼티에서 안면인식 대상여부 플래그 조회
        String faceTargetYN = "N"; // 안면인식 대상여부 플래그

        // if (sessionManager.isNative()) { // APP
        // faceTargetYN =
        // StringUtils.defaultIfEmpty(PropertiesUtils.getString(faceTarget), "N"); //
        // 안면인식 대상여부 플래그
        // } else { // WEB
        // faceTargetYN = "N";
        // }

        FncSchFacialRecognitionEligibilityResponse response = new FncSchFacialRecognitionEligibilityResponse();

        response.setFaceTargetYn(faceTargetYN);

        return response;
    }

    @ServiceEndpoint(url = "/findAddress", name = "주소검색")
    public FncSchFindAddressResponse findAddress(IServiceContext serviceContext,
            FncSchFindAddressRequest request) {

        MciRequestOptions options = hostClient.getMciRequestOptions("MCI_IB_ADDR01_04");

        MciContTran mciContTran = new MciContTran();
        mciContTran.setContDataCd("");
        mciContTran.setContData(request.getContData());
        mciContTran.setContDataLen(request.getContDataLen());

        // 필수 공통부 설정
        options.setMciContTran(mciContTran);
        options.setTranCd("IB_ADDR01_04");

        MciIbAddr0104Req req = mapper.toMciIbAddr0104Req(request);

        MciResponse<MciIbAddr0104Res> mciResponse = hostClient.sendMci(options, req,
                MciIbAddr0104Res.class);

        MciIbAddr0104Res res = mciResponse.getResponse();

        FncSchFindAddressResponse response = mapper.toFunctionsAddressSearchResponse(res);

        response.setContData(mciResponse.getHeader().getMciContTran().getContData());
        response.setContDataLen(mciResponse.getHeader().getMciContTran().getContDataLen());

        return response;
    }

    @ServiceEndpoint(url = "/findAddressRefine", name = "주소정제")
    public FncSchFindAddressRefineResponse findAddressRefine(IServiceContext serviceContext,
            FncSchFindAddressRefineRequest request) {

        MciRequestOptions options = hostClient.getMciRequestOptions("MCI_IB_ADDR01_01");

        // 필수 공통부 설정
        options.setTranCd("IB_ADDR01_01");

        MciIbAddr0101Req req = mapper.toMciIbAddr0101Req(request);

        MciResponse<MciIbAddr0101Res> mciResponse = hostClient.sendMci(options, req,
                MciIbAddr0101Res.class);

        MciIbAddr0101Res res = mciResponse.getResponse();

        FncSchFindAddressRefineResponse response = mapper.toFunctionsAddressRefineResponse(res);

        return response;
    }

    @ServiceEndpoint(url = "/findEmployee", name = "직원검색")
    public FncSchFindEmployeeResponse findEmployee(IServiceContext serviceContext,
            FncSchFindEmployeeRequest request) {

        EmployeeInfoParameter parameter = EmployeeInfoParameter.builder()
                .emplyNm(request.getEmplyNm())
                .clerkNo(request.getClerkNo())
                .loanTypeProduct(request.getLoanTypeProduct())
                .build();

        List<EmployeeInfoResult> result = prdctSggstrInfoDao.selectEmployeeList(parameter);

        FncSchFindEmployeeResponse response = new FncSchFindEmployeeResponse();

        response.setEmployeeInfoList(mapper.toEmployeeInfoList(result));

        return response;
    }

    @ServiceEndpoint(url = "/findBranch", name = "영업점찾기")
    public FncSchFindBranchResponse findBranch(IServiceContext serviceContext,
            FncSchFindBranchRequest request) {

        String searchKeyword = request.getSearchKeyword();

        List<BranchInfoResult> result = prdctSggstrInfoDao.selectBranchList(searchKeyword);

        FncSchFindBranchResponse response = new FncSchFindBranchResponse();

        response.setBranchInfoList(mapper.toBranchInfoList(result));

        return response;
    }

    @ServiceEndpoint(url = "/findOffice", name = "직장검색")
    public FncSchFindOfficeResponse findOffice(IServiceContext serviceContext,
            FncSchFindOfficeRequest request) {

        MciRequestOptions options = hostClient.getMciRequestOptions("MCI_TM1517090001");

        // 필수 공통부 설정
        options.setTranCd("TM1517090001");

        MciTm1517090001Req req = mapper.toMciTm1517090001Req(request);

        MciResponse<MciTm1517090001Res> mciResponse = hostClient.sendMci(options, req,
                MciTm1517090001Res.class);

        MciTm1517090001Res res = mciResponse.getResponse();

        return mapper.toFncSchFindOfficeResponse(res);
    }
}
