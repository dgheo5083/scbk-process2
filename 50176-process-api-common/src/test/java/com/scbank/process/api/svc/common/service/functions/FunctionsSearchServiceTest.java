package com.scbank.process.api.svc.common.service.functions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.edmi.dto.mci.MciIbAddr0101Req;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0101Res;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0104Req;
import com.scbank.process.api.edmi.dto.mci.MciIbAddr0104Res;
import com.scbank.process.api.edmi.dto.mci.MciTm1517090001Req;
import com.scbank.process.api.edmi.dto.mci.MciTm1517090001Res;
import com.scbank.process.api.fw.base.integration.system.mci.MciRequestOptions;
import com.scbank.process.api.fw.base.integration.system.mci.MciResponse;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciContTran;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.dao.PrdctSggstrInfoDao;
import com.scbank.process.api.svc.common.dao.dto.BranchInfoResult;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("FunctionsSearchService")
class FunctionsSearchServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private IServiceContext ctx;

    @Mock
    private ISessionContextManager sessionManager;

    @Mock
    private HostClient hostClient;

    @Mock
    private FunctionsSearchMapper mapper;

    @Mock
    private PrdctSggstrInfoDao prdctSggstrInfoDao;

    @InjectMocks
    private FunctionsSearchService service;

    @Nested
    @DisplayName("facialRecognitionEligibility")
    class FacialRecognitionEligibility {

        @Test
        @DisplayName("안면인식 대상여부 N 반환")
        void facialRecognitionEligibilityTest() {
            FncSchFacialRecognitionEligibilityRequest request = new FncSchFacialRecognitionEligibilityRequest();
            request.setBizType("LOGIN");

            FncSchFacialRecognitionEligibilityResponse response = service.facialRecognitionEligibility(ctx, request);

            assertThat(response.getFaceTargetYn()).isEqualTo("N");
        }
    }

    @Nested
    @DisplayName("findAddress")
    class FindAddress {

        @Test
        @DisplayName("주소검색 성공")
        void findAddressTest() {
            FncSchFindAddressRequest request = new FncSchFindAddressRequest();
            request.setContData("data");
            request.setContDataLen("4");

            MciRequestOptions options = mock(MciRequestOptions.class);
            MciIbAddr0104Req mciReq = new MciIbAddr0104Req();
            MciIbAddr0104Res mciRes = new MciIbAddr0104Res();
            FncSchFindAddressResponse mapped = new FncSchFindAddressResponse();

            MciContTran contTran = new MciContTran();
            contTran.setContData("cont");
            contTran.setContDataLen("4");
            MciResHeader header = new MciResHeader();
            header.setMciContTran(contTran);

            MciResponse<MciIbAddr0104Res> mciResponse = new MciResponse<>();
            mciResponse.setHeader(header);
            mciResponse.setResponse(mciRes);

            when(hostClient.getMciRequestOptions("MCI_IB_ADDR01_04")).thenReturn(options);
            when(mapper.toMciIbAddr0104Req(request)).thenReturn(mciReq);
            when(hostClient.sendMci(eq(options), eq(mciReq), eq(MciIbAddr0104Res.class))).thenReturn(mciResponse);
            when(mapper.toFunctionsAddressSearchResponse(mciRes)).thenReturn(mapped);

            FncSchFindAddressResponse response = service.findAddress(ctx, request);

            assertThat(response.getContData()).isEqualTo("cont");
            assertThat(response.getContDataLen()).isEqualTo("4");
        }
    }

    @Nested
    @DisplayName("findAddressRefine")
    class FindAddressRefine {

        @Test
        @DisplayName("주소정제 성공")
        void findAddressRefineTest() {
            FncSchFindAddressRefineRequest request = new FncSchFindAddressRefineRequest();
            MciRequestOptions options = mock(MciRequestOptions.class);
            MciIbAddr0101Req mciReq = new MciIbAddr0101Req();
            MciIbAddr0101Res mciRes = new MciIbAddr0101Res();
            FncSchFindAddressRefineResponse mapped = new FncSchFindAddressRefineResponse();

            MciResponse<MciIbAddr0101Res> mciResponse = new MciResponse<>();
            mciResponse.setResponse(mciRes);

            when(hostClient.getMciRequestOptions("MCI_IB_ADDR01_01")).thenReturn(options);
            when(mapper.toMciIbAddr0101Req(request)).thenReturn(mciReq);
            when(hostClient.sendMci(eq(options), eq(mciReq), eq(MciIbAddr0101Res.class))).thenReturn(mciResponse);
            when(mapper.toFunctionsAddressRefineResponse(mciRes)).thenReturn(mapped);

            FncSchFindAddressRefineResponse response = service.findAddressRefine(ctx, request);

            assertThat(response).isSameAs(mapped);
        }
    }

    @Nested
    @DisplayName("findEmployee")
    class FindEmployee {

        @Test
        @DisplayName("직원검색 성공")
        void findEmployeeTest() {
            FncSchFindEmployeeRequest request = new FncSchFindEmployeeRequest();
            request.setEmplyNm("홍길동");
            List<EmployeeInfoResult> results = Collections.singletonList(new EmployeeInfoResult());

            when(prdctSggstrInfoDao.selectEmployeeList(any())).thenReturn(results);
            when(mapper.toEmployeeInfoList(results)).thenReturn(Collections.emptyList());

            FncSchFindEmployeeResponse response = service.findEmployee(ctx, request);

            assertThat(response.getEmployeeInfoList()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findBranch")
    class FindBranch {

        @Test
        @DisplayName("영업점찾기 성공")
        void findBranchTest() {
            FncSchFindBranchRequest request = new FncSchFindBranchRequest();
            request.setSearchKeyword("강남");
            List<BranchInfoResult> results = Collections.singletonList(new BranchInfoResult());

            when(prdctSggstrInfoDao.selectBranchList("강남")).thenReturn(results);
            when(mapper.toBranchInfoList(results)).thenReturn(Collections.emptyList());

            FncSchFindBranchResponse response = service.findBranch(ctx, request);

            assertThat(response.getBranchInfoList()).isEmpty();
        }
    }

    @Nested
    @DisplayName("findOffice")
    class FindOffice {

        @Test
        @DisplayName("직장검색 성공")
        void findOfficeTest() {
            FncSchFindOfficeRequest request = new FncSchFindOfficeRequest();
            MciRequestOptions options = mock(MciRequestOptions.class);
            MciTm1517090001Req mciReq = new MciTm1517090001Req();
            MciTm1517090001Res mciRes = new MciTm1517090001Res();
            FncSchFindOfficeResponse mapped = new FncSchFindOfficeResponse();

            MciResponse<MciTm1517090001Res> mciResponse = new MciResponse<>();
            mciResponse.setResponse(mciRes);

            when(hostClient.getMciRequestOptions("MCI_TM1517090001")).thenReturn(options);
            when(mapper.toMciTm1517090001Req(request)).thenReturn(mciReq);
            when(hostClient.sendMci(eq(options), eq(mciReq), eq(MciTm1517090001Res.class))).thenReturn(mciResponse);
            when(mapper.toFncSchFindOfficeResponse(mciRes)).thenReturn(mapped);

            FncSchFindOfficeResponse response = service.findOffice(ctx, request);

            assertThat(response).isSameAs(mapped);
        }
    }
}
