package com.scbank.process.api.svc.shared.components.obs.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
public class KftcObsHelper {
	public static final String PROP_RESTCLIENT_CONN_TIMEOUT = "rest.client.connect.timeout";
	public static final String PROP_RESTCLIENT_READ_TIMEOUT = "kftc.rest.read.timeout";
	public static final String PROP_RESTCLIENT_MAXT_CNT = "kftc.rest.connect.max.count";
	public static final String PROP_RESTCLIENT_PER_ROUTE_CNT = "kftc.rest.connect.per.route.count";

	private static final String PROP_BASEURL = "kftc.open-platform.api.base.url";
	private static final String PROP_CLIENT_ID = "kftc.open-platform.api.client_id";
	private static final String PROP_CLIENT_SECRET = "kftc.open-platform.api.client_secret";

	public final static String TOKEN_GRANTTYPE = "client_credentials";
	public final static String TOKEN_SCOPE = "sa";

	// TOKEN 인증시 데이타를 가져온다 : client_use_code[B000
	// public final static String bankTranCode = "B001234560";

	public final static String TOKEN = "TOKEN";
	public final static String REFRESH_TOKEN = "REFRESH_TOKEN";
	public final static String USER_ME = "USER_ME";
	public final static String ACCOUNT_LIST = "ACCOUNT_LIST";
	public final static String ACCOUNT_CANCEL = "ACCOUNT_CANCEL";
	public final static String USER_REGISTER = "USER_REGISTER";
	public final static String ACCOUNT_BALANCE_FINNUM = "ACCOUNT_BALANCE_FINNUM";
	public final static String ACCOUNT_BALANCE_ACNTNUM = "ACCOUNT_BALANCE_ACNTNUM";
	public final static String ACCOUNT_TRANSACTIONLIST_FINNUM = "ACCOUNT_TRANSACTIONLIST_FINNUM";
	public final static String ACCOUNT_TRANSACTIONLIST_ACNTNUM = "ACCOUNT_TRANSACTIONLIST_ACNTNUM";
	public final static String INQUIRY_REALNAME = "INQUIRY_REALNAME";
	public final static String INQUIRY_RECEIVE = "INQUIRY_RECEIVE";
	public final static String INQUIRY_REMITLIST = "INQUIRY_REMITLIST";
	public final static String TRANSFER_WITHDRAW_FINNUM = "TRANSFER_WITHDRAW_FINNUM";
	public final static String TRANSFER_WITHDRAW_ACNTNUM = "TRANSFER_WITHDRAW_ACNTNUM";
	public final static String TRANSFER_DEPOSIT_FINNUM = "TRANSFER_DEPOSIT_FINNUM";
	public final static String TRANSFER_DEPOSIT_ACNTNUM = "TRANSFER_DEPOSIT_ACNTNUM";
	public final static String TRANSFER_RESULT = "TRANSFER_RESULT";
	public final static String USER_REAGMT = "USER_REAGMT";
	public final static String CNTR_ACCOUNT_TYPE = "kftc.open-platform.api.CNTR_ACCOUNT_TYPE";
	public final static String CNTR_ACCOUNT_NUM = "kftc.open-platform.api.CNTR_ACCOUNT_NUM";
	public final static String TRANSFER_PURPOSE = "kftc.open-platform.api.TRANSFER_PURPOSE";
	public final static String DPS_PRINT_CONTENT = "kftc.open-platform.api.DPS_PRINT_CONTENT";
	public final static String WD_PRINT_CONTENT = "kftc.open-platform.api.WD_PRINT_CONTENT";
	public final static String WD_PASS_PHRASE = "kftc.open-platform.api.WD_PASS_PHRASE";
	public final static String RE_DPS_NAME_CHECK_OPTION = "kftc.open-platform.api.RE_DPS_NAME_CHECK_OPTION";

	/** 금결원 정상 코드 */
	public final static String SUCCESS_RSP_CODE = "A0000";
	/** 참가은행 정상 코드 */
	public final static String SUCCESS_BANK_RSP_CODE = "000";

	// FDS
	//public static final String MHUB_FDS_IP = "message-hub.log.server.ip";
	//public static final String MHUB_FDS_PORT = "message-hub.log.server.port";

	/**
	 * 
	 * @return
	 */
	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return sra.getRequest();
	}

	/**
	 * 
	 * @return
	 */
	public static String getClientId() {
		String clientId = StringUtils.defaultIfBlank(PropertiesUtils.getString(PROP_CLIENT_ID), "-");
		if ("-".equals(clientId)) {
			throw new PRCServiceException(
					String.format("KftcOpenPlatform.xx.properties::%s 파일 또는 속성값이 있는지 확인해 주세요.",
							PROP_CLIENT_ID));
		} /* end of if */

		try {
			return ObsCryptoUtils.decrypt(clientId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);

			String enClientId = "암호화 오류!";

			try {
				enClientId = ObsCryptoUtils.encrypt(clientId);
			} catch (Exception e1) {
				new PRCServiceException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), e1.getMessage(), e1);
			}

			log.error(String.format("암호화되지 않았습니다. 설정된 값을 [%s]변경하세요", enClientId));
		} /* end of if */

		return clientId;
	}

	/**
	 * 
	 * @return
	 */
	public static String getClientSecret() {
		String clientSecret = StringUtils.defaultIfBlank(PropertiesUtils.getString(PROP_CLIENT_SECRET), "-");

		if ("-".equals(clientSecret)) {
			throw new PRCServiceException(
					String.format("KftcOpenPlatform.xx.properties::%s 파일 또는 속성값이 있는지 확인해 주세요.",
							PROP_CLIENT_SECRET));
		} /* end of if */

		try {
			return ObsCryptoUtils.decrypt(clientSecret);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			
			String enClientSecret = null;

			try {
				enClientSecret = ObsCryptoUtils.encrypt(clientSecret);
			} catch (Exception e1) {
				new PRCServiceException("clientSecret 암호화 오류!", e1);
			}

			log.error(String.format("clientSecret 암호화되지 않았습니다. 설정된 값을 [%s]변경하세요", enClientSecret));
		} /* end of if */

		return clientSecret;
	}

	/**
	 * 
	 * @return
	 */
	public static String getBaseUrl() {
		return PropertiesUtils.getString(PROP_BASEURL);
	}

	public static boolean isHttps() {
		return (getBaseUrl().indexOf("https:") == 0);
	}
}