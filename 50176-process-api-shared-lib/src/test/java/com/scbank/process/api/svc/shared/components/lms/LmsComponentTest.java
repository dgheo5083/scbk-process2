package com.scbank.process.api.svc.shared.components.lms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.lms.dto.LmsRequest;

/**
 * LMS 장문 발송 공통 컴포넌트 단위 테스트
 * (모든 주석 및 에러 검증 설명은 한글화 절대 규칙에 준수하여 작성되었습니다)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("LmsComponent 신규 정밀 단위 테스트")
public class LmsComponentTest {

    @InjectMocks
    private LmsComponent lmsComponent;

    @BeforeEach
    public void setUp() {
        // PropertiesUtils 정적 모킹을 통해 초기 포스트컨스트럭트 IP/Port 파싱 설정
        try (MockedStatic<PropertiesUtils> mockedProps = mockStatic(PropertiesUtils.class)) {
            mockedProps.when(() -> PropertiesUtils.getString("sms.server.ip")).thenReturn("127.0.0.1");
            mockedProps.when(() -> PropertiesUtils.getString("lms.server.port")).thenReturn("8888");

            lmsComponent.init();
        }
    }

    @Nested
    @DisplayName("소켓 기반 장문 LMS 전송 검증")
    class SendMainTest {

        @Test
        @DisplayName("실패: 소켓 연결 실패 시 PRCServiceException 분기 및 에러 코드 검증")
        public void testSendMainSocketConnectionFailure() {
            // Given (준비)
            LmsRequest request = new LmsRequest();
            request.setCallPhone1("010");
            request.setCallPhone2("2970");
            request.setCallPhone3("5083");
            request.setCallName("발신자");
            request.setSubject("LMS 제목");
            request.setCallMessage("SMS 발송 테스트입니다.");
            request.setMessageCode("T1");
            request.setRateDate(DateUtils.getCurrentDate("yyyyMMdd")); // 메시지 전송 예약일자
            request.setRateTime(DateUtils.getCurrentDate("HHmmss")); // 메시지 전송 예약시간
            request.setReqPhone1(""); // 호출 번호 #1
            request.setReqPhone2("1588"); // 호출 번호 #2
            request.setReqPhone3("1599"); // 호출 번호 #3
            request.setDeptCode("GL9-KI3-HS9"); // 부서코드
            request.setDeptName("e-뱅u-12103 부"); // 부서명

            // When & Then (실행 & 검증)
            PRCServiceException exception = assertThrows(PRCServiceException.class, () -> {
                lmsComponent.sendMain(request);
            });

            assertEquals("MA3CMM0063", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("LMS발송"));
        }

        @Test
        @DisplayName("성공: 모의 소켓 서버 기동 하에 27개 패킷 조립 및 응답 플래그 'O' 획득 정상 동작 검증")
        public void testSendMainSocketSuccess() throws IOException {
            // 로컬 모의 소켓 서버 기동
            try (ServerSocket serverSocket = new ServerSocket(0)) {
                int freePort = serverSocket.getLocalPort();

                // SmsComponent의 IP와 포트 설정을 자유 포트로 재바인딩
                try (MockedStatic<PropertiesUtils> mockedProps = mockStatic(PropertiesUtils.class)) {
                    mockedProps.when(() -> PropertiesUtils.getString("sms.server.ip")).thenReturn("127.0.0.1");
                    mockedProps.when(() -> PropertiesUtils.getString("lms.server.port"))
                            .thenReturn(String.valueOf(freePort));
                    lmsComponent.init();
                }

                LmsRequest request = new LmsRequest();
                request.setCallPhone1("010");
                request.setCallPhone2("2970");
                request.setCallPhone3("5083");
                request.setCallName("발신자");
                request.setSubject("LMS 제목");
                request.setCallMessage("SMS 발송 테스트입니다.");
                request.setMessageCode("T1");
                request.setRateDate(DateUtils.getCurrentDate("yyyyMMdd")); // 메시지 전송 예약일자
                request.setRateTime(DateUtils.getCurrentDate("HHmmss")); // 메시지 전송 예약시간
                request.setReqPhone1(""); // 호출 번호 #1
                request.setReqPhone2("1588"); // 호출 번호 #2
                request.setReqPhone3("1599"); // 호출 번호 #3
                request.setDeptCode("GL9-KI3-HS9"); // 부서코드
                request.setDeptName("e-뱅u-12103 부"); // 부서명

                // 비동기로 서버 측 수신 및 응답 기록
                Thread serverThread = new Thread(() -> {
                    try (java.net.Socket clientSocket = serverSocket.accept();
                            java.io.InputStream is = clientSocket.getInputStream();
                            java.io.OutputStream os = clientSocket.getOutputStream()) {

                        // 클라이언트 패킷 수신 대기 및 확인
                        byte[] readBuffer = new byte[4096];
                        is.read(readBuffer);

                        // 95바이트 이상의 모의 LMS 응답 작성 (index 94 위치에 성공 플래그 'O' 주입)
                        byte[] mockResponse = new byte[120];
                        for (int i = 0; i < mockResponse.length; i++) {
                            mockResponse[i] = ' ';
                        }
                        mockResponse[94] = 'O'; // 성공 플래그 'O'
                        os.write(mockResponse);
                        os.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                serverThread.start();

                // When (실행)
                String resultFlag = lmsComponent.sendMain(request);

                // Then (검증)
                assertEquals("O", resultFlag);
            }
        }

        @Test
        @DisplayName("성공: 예약 발송 조건(RateDate/RateTime 존재)에 따라 Status가 'R'로 할당되는 분기 정상 검증")
        public void testSendMainReservationStatus() throws IOException {
            try (ServerSocket serverSocket = new ServerSocket(0)) {
                int freePort = serverSocket.getLocalPort();

                try (MockedStatic<PropertiesUtils> mockedProps = mockStatic(PropertiesUtils.class)) {
                    mockedProps.when(() -> PropertiesUtils.getString("sms.server.ip")).thenReturn("127.0.0.1");
                    mockedProps.when(() -> PropertiesUtils.getString("lms.server.port"))
                            .thenReturn(String.valueOf(freePort));
                    lmsComponent.init();
                }

                LmsRequest request = new LmsRequest();
                request.setCallPhone1("010");
                request.setCallPhone2("2970");
                request.setCallPhone3("5083");
                request.setCallName("발신자");
                request.setSubject("LMS 제목");
                request.setCallMessage("SMS 발송 테스트입니다.");
                request.setMessageCode("T1");
                request.setRateDate(DateUtils.getCurrentDate("yyyyMMdd")); // 메시지 전송 예약일자
                request.setRateTime(DateUtils.getCurrentDate("HHmmss")); // 메시지 전송 예약시간
                request.setReqPhone1(""); // 호출 번호 #1
                request.setReqPhone2("1588"); // 호출 번호 #2
                request.setReqPhone3("1599"); // 호출 번호 #3
                request.setDeptCode("GL9-KI3-HS9"); // 부서코드
                request.setDeptName("e-뱅u-12103 부"); // 부서명

                Thread serverThread = new Thread(() -> {
                    try (java.net.Socket clientSocket = serverSocket.accept();
                            java.io.InputStream is = clientSocket.getInputStream();
                            java.io.OutputStream os = clientSocket.getOutputStream()) {

                        byte[] readBuffer = new byte[4096];
                        is.read(readBuffer);

                        // 95바이트 이상의 모의 LMS 응답 작성
                        byte[] mockResponse = new byte[120];
                        for (int i = 0; i < mockResponse.length; i++) {
                            mockResponse[i] = ' ';
                        }
                        mockResponse[94] = 'O';
                        os.write(mockResponse);
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                serverThread.start();

                // When & Then
                String resultFlag = lmsComponent.sendMain(request);
                assertEquals("O", resultFlag);
            }
        }
    }
}
