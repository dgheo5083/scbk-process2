package com.scbank.process.api.svc.shared.components.edoc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.windfire.apis.asysConnectData;
import com.windfire.apis.asysadmin.asysAdmServer;
import com.windfire.apis.asysadmin.asysAdmServerColl;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class EdocXvarmLiveCheckerComponentTest {

	private EdocXvarmLiveCheckerComponent component;

	@BeforeEach
	void setUp() {
		component = new EdocXvarmLiveCheckerComponent();
		// @PostConstruct로 채워지는 static 값을 테스트용으로 직접 세팅
		ReflectionTestUtils.setField(EdocXvarmLiveCheckerComponent.class, "EDOC_XVARM_HOST", "127.0.0.1");
		ReflectionTestUtils.setField(EdocXvarmLiveCheckerComponent.class, "EDOC_XVARM_PORT", 0);
		ReflectionTestUtils.setField(EdocXvarmLiveCheckerComponent.class, "EDOC_XVARM_ID", "EDOC");
		ReflectionTestUtils.setField(EdocXvarmLiveCheckerComponent.class, "EDOC_XVARM_SECRET", "secret");
	}

	@Test
	@DisplayName("setStaticValues - 프로퍼티에서 XVARM 접속정보 로딩")
	public void setStaticValuesTest() {
		try (MockedStatic<PropertiesUtils> props = mockStatic(PropertiesUtils.class)) {
			props.when(() -> PropertiesUtils.getString("EDOC_XVARM_HOST")).thenReturn("127.0.0.1");
			props.when(() -> PropertiesUtils.getString("EDOC_XVARM_PORT")).thenReturn("9999");
			props.when(() -> PropertiesUtils.getString("EDOC_XVARM_ID")).thenReturn("EDOC");
			props.when(() -> PropertiesUtils.getString("EDOC_XVARM_SECRET")).thenReturn("secret");

			assertDoesNotThrow(() -> ReflectionTestUtils.invokeMethod(component, "setStaticValues"));
		}
	}

//	@Test
//	@DisplayName("init - 서버 점검 후 정상 종료 (응답없음→에러서버 등록)")
//	public void initTest() {
//		try (MockedConstruction<asysConnectData> con = Mockito.mockConstruction(asysConnectData.class)) {
//			assertDoesNotThrow(() -> component.init());
//			assertNotNull(component.getErrorServerList());
//		}
//	}

	@Test
	@DisplayName("init(batchCheck=true) - 배치 점검")
	public void initBatchTrueTest() {
		try (MockedConstruction<asysConnectData> con = Mockito.mockConstruction(asysConnectData.class)) {
			assertDoesNotThrow(() -> component.init(true));
		}
	}

	@Test
	@DisplayName("init(batchCheck=false) - 점검 미수행")
	public void initBatchFalseTest() {
		assertDoesNotThrow(() -> component.init(false));
	}

	@Test
	@DisplayName("checkXvarmObject - 서버 조회 성공(res=0) 시 00")
	public void checkXvarmObjectSuccessTest() {
		try (MockedConstruction<asysAdmServerColl> coll = Mockito.mockConstruction(asysAdmServerColl.class,
				(m, ctx) -> {
					when(m.retrieveServers(anyString())).thenReturn(0);
					when(m.getCollCount()).thenReturn(1);
					asysAdmServer ads = mock(asysAdmServer.class);
					when(ads.getField(anyString())).thenReturn("x");
					when(m.getCollObject(anyInt())).thenReturn(ads);
				})) {

			String result = ReflectionTestUtils.invokeMethod(component, "checkXvarmObject", (Object) null);
			assertEquals("00", result);
		}
	}

	@Test
	@DisplayName("checkXvarmObject - 서버 조회 실패(res!=0) 시 11")
	public void checkXvarmObjectFailTest() {
		try (MockedConstruction<asysAdmServerColl> coll = Mockito.mockConstruction(asysAdmServerColl.class,
				(m, ctx) -> {
					when(m.retrieveServers(anyString())).thenReturn(11);
					when(m.getCollCount()).thenReturn(0);
				})) {

			String result = ReflectionTestUtils.invokeMethod(component, "checkXvarmObject", (Object) null);
			assertEquals("11", result);
		}
	}

	@Test
	@DisplayName("getEdocXvarmConnector / getErrorServerList 접근자")
	public void accessorsTest() {
		assertNotNull(component.getErrorServerList());
		// con은 static, init 미수행 시 null일 수 있음 - 호출만으로 커버
		ReflectionTestUtils.invokeMethod(component, "getEdocXvarmConnector");
	}
}
