package com.scbank.process.api.svc.shared.components.edoc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.scbank.process.api.fw.base.utils.CodeUtils;
import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.svc.shared.components.edoc.dto.EdocPayloadInfo;
import com.scbank.process.api.svc.shared.components.sms.SmsComponent;
import com.scbank.process.api.svc.shared.components.sms.dto.SmsRequest;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
@MockitoSettings(strictness = Strictness.LENIENT)
public class EdocHelperTest {

	@Mock private SmsComponent sms;

	@InjectMocks private EdocHelper helper;

	@Test
	@DisplayName("전자문서 오류 시 담당자 수 만큼 SMS 발송")
	public void sendLpcErrorTest() {
		EdocPayloadInfo payload = mock(EdocPayloadInfo.class);
		when(payload.getTradNo()).thenReturn("TRAD001");
		when(payload.getBizType()).thenReturn("LOAN");

		ICodeItemInfo phone1 = mock(ICodeItemInfo.class);
		when(phone1.getValue()).thenReturn("010-1588-1599");
		ICodeItemInfo phone2 = mock(ICodeItemInfo.class);
		when(phone2.getValue()).thenReturn("02-1234-5678");

		try (MockedStatic<CodeUtils> codeUtils = mockStatic(CodeUtils.class);
			 MockedStatic<DateUtils> dateUtils = mockStatic(DateUtils.class)) {
			codeUtils.when(() -> CodeUtils.getCodes("ERR_LPC_PHONE_NO")).thenReturn(List.of(phone1, phone2));
			codeUtils.when(() -> CodeUtils.getCodeValue(anyString(), anyString())).thenReturn("오류[~~TRAD_NO~~][~~PRDCT_NM~~]");
			dateUtils.when(() -> DateUtils.getCurrentDate(anyString())).thenReturn("20260601");

			assertDoesNotThrow(() -> helper.sendLpcError(payload, "STEP1"));

			verify(sms, times(2)).sendMain(any(SmsRequest.class));
		}
	}
}
