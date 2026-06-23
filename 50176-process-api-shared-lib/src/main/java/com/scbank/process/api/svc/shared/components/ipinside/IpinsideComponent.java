package com.scbank.process.api.svc.shared.components.ipinside;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.interezen.common.util.coder.Coder;
import com.interezen.flag.FlagBean;
import com.interezen.loader.QLoader;
import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.enums.RunMode;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.shared.components.ipinside.dto.DFinderApiInfo;
import com.scbank.process.api.svc.shared.components.ipinside.dto.IpinsideHddInfo;
import com.scbank.process.api.svc.shared.components.ipinside.dto.PcFixInfo;
import com.scbank.process.api.svc.shared.components.ipinside.dto.QLoaderFlagInfo;
import com.scbank.process.api.svc.shared.constants.PRCSharedConstants;
import com.scbank.process.api.svc.shared.dao.DeviceAuthUserDao;
import com.scbank.process.api.svc.shared.dao.IpIntrcptListInfoDao;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserParameter;
import com.scbank.process.api.svc.shared.dao.dto.DeviceAuthUserResult;
import com.scbank.process.api.svc.shared.dao.dto.IPInsideAboardParameter;
import com.scbank.process.api.svc.shared.utils.PRCSharedUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class IpinsideComponent {

	// 세션
	private final ISessionContextManager sessionManager;

	private final IpIntrcptListInfoDao ipIntrcptListInfoDao;

	private final DeviceAuthUserDao deviceAuthUserDao;

	@ComponentOperation(name = "getIpinsidInfo", description = "IPInside Mac주소, HDD시리얼 정보조회")
	public IpinsideHddInfo getIpinsidInfo() {

		String ipinsideHdd = PRCSharedUtils.getIpinsideHdd();

		Coder coder = new Coder();

		String realMacHdd = "";
		StringBuffer hddSerials = new StringBuffer(); // HDD Serial 값
		String activeMac = "";

		IpinsideHddInfo ipinsideHddInfo = new IpinsideHddInfo();

		if (StringUtils.isNotBlank(ipinsideHdd)) {
			try {

				realMacHdd = coder.Decode(ipinsideHdd);

				log.debug("@@@ ipinside getIpinsidInfo ActiveMAC Decode : " + realMacHdd); // 복호화한 값을 찍어본다.

				// ActiveMAC을 찾아낸다.
				String[] dataArray = null;
				dataArray = realMacHdd.split(";");
				int dataCount = dataArray.length;
				int colonPos1, colonPos2 = 0;
				int nUse = 0; // 실제통신여부
				for (int i = 0; i < dataCount; i++) {
					if (dataArray[i] == null || "".equals(dataArray[i].trim()))
						continue;
					colonPos1 = dataArray[i].indexOf(":");
					if (colonPos1 <= 0)
						continue;
					colonPos2 = dataArray[i].indexOf(":", colonPos1 + 1);
					if (colonPos2 <= 0)
						continue;

					activeMac = dataArray[i].substring(1, colonPos1);
					nUse = Integer.parseInt(dataArray[i].substring(colonPos1 + 1, colonPos2));

					if (nUse > 0)
						break;
				}
				log.debug("@@@ ipinside parseIpinsidInfo ActiveMAC : " + activeMac);

				String realMacHdd2 = coder.Decode(ipinsideHdd);
				// HDD Serial을 찾아낸다. (첫번째꺼 하나만...)
				String sHDD2 = realMacHdd2.substring(realMacHdd2.indexOf(";$") + 2); // HDD Serial부분만 잘라낸다.
				String[] dataArray2 = null;
				dataArray2 = sHDD2.split(";");// ;를 구분자로 자른다.
				int dataCount2 = dataArray2.length;
				int colonPos1_2 = 0;
				String hdd = "";
				for (int n = 0; n < dataCount2; n++) {
					if (dataArray2[n] == null || "".equals(dataArray2[n].trim()))
						continue;
					colonPos1_2 = dataArray2[n].indexOf(":");
					if (colonPos1_2 <= 0)
						continue;
					hdd = dataArray2[n].substring(1, colonPos1_2);// HDD Serial을 찾았다.
					// hddSerials = hddSerials + hdd + ":";
					hddSerials.append(hdd).append(":");
				}
			} catch (Exception e) {
				log.error("@@@ ipinside parseIpinsidInfo error ipinsideCOMM[" + ipinsideHdd + "]", e);
			}

		}

		ipinsideHddInfo.setHddSerials(hddSerials.toString());
		ipinsideHddInfo.setMacAddress(activeMac);

		return ipinsideHddInfo;

	}

	@ComponentOperation(name = "sendNPInsideInfo", description = "해외 IP차단 서비스(수집서버 전송)")
	public QLoader sendNPInsideInfo(String userId, boolean isOpen, boolean isNull) {
		String ipinsideAx = PRCSharedUtils.getIpinsideAx();

		IServiceContext sCtx = ServiceContextHolder.getContext();
		HttpServletRequest request = sCtx.request();

		QLoader loader = new QLoader();

		int etcAccessCode = 1;
		try { // 로그인 시, (2번째 인자:: true-로그인성공, false-로그인실패)
				// VFDS 단말키 IPINSIDE USER_NAME 자리에 값 세팅하기위해 단말키값 불러온다
			String VFDSDeviceKey = StringUtils
					.defaultString(sessionManager.getGlobalValue("VFDSDeviceKey", String.class));
			log.debug("hdg ipinside sendIpInsideInfo VFDSDeviceKey : {}", VFDSDeviceKey);
			log.debug("hdg ipinside sendIpInsideInfo userId : {}", userId);

			// 대고객 Open인 경우
			if ((("".equals(ipinsideAx) == false) && (ipinsideAx != null)) || isNull) {
				log.debug(
						"hdg ipinside sendIpInsideInfo IP Tracer : null of clientEmbededData OK!! activexEmbededData|_IPINSIDE_AX["
								+ ipinsideAx + "]");
				if (isOpen) {
					// dongyul.lee(20151104) MAC차단 오류 수정
					loader.sendPNInsideInfo(request, "1016020400", userId, VFDSDeviceKey, "", "", "", "", etcAccessCode,
							ipinsideAx, "", "");
					log.debug("hdg ipinside sendIpInsideInfo Tracer : Open to external user...");
				}
				// 내부만 Open인 경우
				else {
					log.debug("hdg ipinside sendIpInsideInfo IP Tracer : Open to internal user only");
					if ((request.getRemoteAddr().equals("23.20.115.43") == true)
							|| (request.getRemoteAddr().equals("23.20.201.22") == true)
							|| (request.getRemoteAddr().equals("23.20.201.23") == true)
							|| (request.getRemoteAddr().equals("23.20.201.24") == true)
							|| (request.getRemoteAddr().equals("118.219.55.2") == true)
							|| (request.getRemoteAddr().equals("172.20.24.200") == true)
							|| (request.getRemoteAddr().equals("172.20.24.207") == true)
							|| (request.getRemoteAddr().equals("23.20.201.42") == true)) {
						log.debug("hdg ipinside SendPNInsideInfo IP Tracer : legal access from internal proxy");
						loader.sendPNInsideInfo("1016020400", userId, VFDSDeviceKey, "", "", etcAccessCode, ipinsideAx,
								"", "", request);
					}
				}
			}
		} catch (Exception e) {
			log.error("hdg ipinside sendIpInsideInfo 수집서버 전송 error", e);
		}

		return loader;
	}

	/**
	 * FlagBean 값 FlagValue : 문서에 있는 내용임 - -1:데이터 전송되지않음. 수집서버 이상, - 0:국내정상, -
	 * 1:?(국내정상이라는 뜻인가?), - 2:해외아이피 또는 사설사용자, - 4:국내 차단사용자, - 6:해외차단사용자, -
	 * 8:정책에의한차단사용자, - 9:공인아피지확인되지 않은사용자
	 * 
	 * @param loader
	 */
	@ComponentOperation(name = "getQLoaderFlagMap", description = "QLoader 데이터추출")
	public QLoaderFlagInfo getQLoaderFlagMap(QLoader loader) {

		QLoaderFlagInfo qloaderFlagInfo = new QLoaderFlagInfo();

		FlagBean flag = null;

		try {
			flag = loader.getFlag();

			if (flag.getFlagValue() < 0) {
				qloaderFlagInfo.setAbroadYn("N");
				qloaderFlagInfo.setBlackListYn("N");
				qloaderFlagInfo.setBciCountryCode(PRCSharedConstants.COUNTRYCODE_KOREA);
			} else {
				qloaderFlagInfo.setAbroadYn(flag.isAbroadIP() ? "Y" : "N");
				qloaderFlagInfo.setBlackListYn(flag.isBlackList() ? "Y" : "N");
				qloaderFlagInfo.setBciCountryCode(Integer.toString(flag.bciCountryCode()));
			}

			log.debug("hdg ipinside getQLoaderFlagMap flag isAbroadIP {}", flag.isAbroadIP());
			log.debug("hdg ipinside getQLoaderFlagMap flag isBlackList {}", flag.isBlackList());
			log.debug("hdg ipinside getQLoaderFlagMap flag bciCountryCode {}", Integer.toString(flag.bciCountryCode()));

			log.debug("hdg ipinside getQLoaderFlagMap qloaderFlagInfo getAbroadYn {}", qloaderFlagInfo.getAbroadYn());
			log.debug("hdg ipinside getQLoaderFlagMap qloaderFlagInfo getBlackListYn {}",
					qloaderFlagInfo.getBlackListYn());
			log.debug("hdg ipinside getQLoaderFlagMap qloaderFlagInfo getBciCountryCode {}",
					qloaderFlagInfo.getBciCountryCode());

			qloaderFlagInfo.setFlagValue(flag.getFlagValue());

		} catch (Exception e) {
			log.error("hdg ipinside getQLoaderFlagMap FlagBean  error: " + e.getMessage(), e);
			throw new PRCServiceException("PRCCMM0009", "interezen Error", e);
		}
		log.debug("hdg ipinside getQLoaderFlagMap resultMap[" + qloaderFlagInfo.toString() + "]");

		return qloaderFlagInfo;
	}

	@ComponentOperation(name = "checkProtectedIp", description = "해외아이피 차단여부 확인")
	public String checkProtectedIp(String protectedIpYn, String workId, String issueId, String userIp) {
		String result = "N";
		// 해외 IP차단 여부 체크
		if ("Y".equals(protectedIpYn) || "1".equals(protectedIpYn)) {

			log.debug("@@@ ipinside checkProtectedIp.protectedIpYn[" + protectedIpYn + "]");
			StringBuffer buffer = new StringBuffer();
			StringTokenizer token = new StringTokenizer(userIp, ".");
			int total = 0;
			while (token.hasMoreTokens()) {
				String newToken = token.nextToken();
				log.debug("@@@ ipinside parsing Token : " + newToken);
				buffer.append(StringUtils.lpad(newToken, 3, '0'));
				if (total < token.countTokens()) {
					buffer.append(".");
				}
			}
			log.debug("@@@ ipinside RESULT IP : *******************************" + buffer.toString());

			String convertIp = buffer.toString();

			IPInsideAboardParameter params = IPInsideAboardParameter.builder().ipStartAddr(convertIp)
					.ipEndAddr(convertIp).build();

			log.debug("@@@ ipinside ipIntrcptListInfoDao.selectAboardYn params[" + params.toString() + "]");

			try {
				int cnt = ipIntrcptListInfoDao.selectAboardYn(params);
				// 해외 IP검색 결과가 0이면 대상자(국내 IP검사 대상자 이므로)
				if (cnt == 0) {
					result = "1";
				}
			} catch (Exception e) {
				return "DB_ERROR";
			}
		}

		log.debug("@@@ ipinside checkProtectedIp result[" + result + "]");

		return result;
	}

	/**
	 * 인터리젠 IP추적기 수집서버로 IP정보를 전달한다. 인스뱅 통합
	 * 
	 * @param serviceType type[LOGIN(1016020100, 1016040100);TRANSFER(1016020200,
	 *                    1016040200);기타(1016020300, 1016040300)]
	 * @param userId      접속자 ID(또는 접속시도 ID)
	 */
	@ComponentOperation(name = "sendPNInsideInfo", description = "인터리젠 IP추적기 수집서버로 IP정보 전달")
	public void sendPNInsideInfo(String serviceType, String userId) {

		IServiceContext sCtx = ServiceContextHolder.getContext();
		HttpServletRequest request = sCtx.request();

		// 서비스코드 IB와 상이함
		String serviceNo = this.getServiceNo(serviceType);
		String nip = PRCSharedUtils.getIpinsideIp();
		String data = PRCSharedUtils.getIpinsideAx();

		QLoader loader = new QLoader();

		try {
			// VFDS 단말키 IPINSIDE USER_NAME 자리에 값 세팅하기위해 단말키값 불러온다
			String VFDSDeviceKey = StringUtils
					.defaultString(sessionManager.getGlobalValue("VFDSDeviceKey", String.class));
			log.debug("IpinsideServiceImpl VFDSDeviceKey : " + VFDSDeviceKey);

			// 스마트폰뱅킹은 아래와같이 변경되어야 한다.
			loader.sendPNInsideInfo(request, serviceNo, userId, VFDSDeviceKey, "", "", "", userId, 1, data, nip, "");
			log.debug("@@@ ipinisde sendSmartNPInsideInfo IP Tracer : Open to external user...");
		} catch (Exception e) {
			log.error("@@@ ipinisde sendSmartNPInsideInfo IP Tracer exception...", e);
		}

	}

	/**
	 * 인터리젠 서비스코드 조회 - LOGIN(1016020100, 1016040100) -
	 * TRANSFER(1016020200,1016040200) - 기타(1016020300, 1016040300)
	 * 
	 * @param serviceType
	 * @return
	 */
	@ComponentOperation(name = "getServiceNo", description = "인터리젠 서비스코드 조회")
	public String getServiceNo(String serviceType) {
		String serviceNo = ""; // 서비스 번호

		if ("LOGIN".equals(serviceType)) {
			serviceNo = PRCSharedUtils.isSB() ? "1016040100" : "1016020100";
		} else if ("TRANSFER".equals(serviceType)) {
			serviceNo = PRCSharedUtils.isSB() ? "1016040200" : "1016020200";
		} else if ("CERT".equals(serviceType) || "ETC".equals(serviceType)) {
			serviceNo = PRCSharedUtils.isSB() ? "1016040300" : "1016020300";
		} else {
			serviceNo = PRCSharedUtils.isSB() ? "1016040300" : "1016020300";
		}

		return serviceNo;
	}

	/**
	 * 인터리젠 IP추적기 수집서버로 IP정보를 전달한다. scfbSendPNInsideInfo 의 스마트폰 뱅킹 전용 버전
	 * 
	 * @param serviceType  type[LOGIN(1016040100);TRANSFER(1016040200);기타(1016040300)]
	 * @param userId       접속자 ID(또는 접속시도 ID)
	 * @param dynamicValue 거래 성공(1)/실패 여부(0)
	 * @param smartNip     smart NAT IP
	 * @param smartData    smart Embeded Data
	 * @param isOpen       대고객 Open 여부
	 * @param isNull       clientEmbededData가 null인 경우도 허용(Send)할지 여부
	 */
	@Deprecated
	@ComponentOperation(name = "sendSmartNPInsideInfo", description = "인터리젠 IP추적기 수집서버로 IP정보 전달")
	public void sendSmartNPInsideInfo(String serviceType, String userId, String dynamicValue, String smartNip,
			String smartData, boolean isOpen, boolean isNull) {

		IServiceContext sCtx = ServiceContextHolder.getContext();
		HttpServletRequest request = sCtx.request();

		log.debug("sendSmartNPInsideInfo check log");

		// 서비스코드 IB와 상이함
		String serviceNo = "1016040100"; // 서비스 번호

		if ("LOGIN".equals(serviceType)) {
			serviceNo = "1016040100";
		} else if ("TRANSFER".equals(serviceType)) {
			serviceNo = "1016040200";
		} else if ("CERT".equals(serviceType) || "ETC".equals(serviceType)) {
			serviceNo = "1016040300";
		} else {
			serviceNo = "1016040300";
		}

		log.debug("@@@ ipinisde sendSmartNPInsideInfo IP Tracer Login Transaction in..." + "Open[" + isOpen
				+ "]sServiceNo[" + serviceNo + "] userID[" + userId + "] smartNip[" + smartNip + "] smartData["
				+ smartData + "]");

		QLoader loader = new QLoader();

		try { // 로그인 시, (2번째 인자:: true-로그인성공, false-로그인실패)
				// VFDS 단말키 IPINSIDE USER_NAME 자리에 값 세팅하기위해 단말키값 불러온다
			String VFDSDeviceKey = StringUtils
					.defaultString(sessionManager.getGlobalValue("VFDSDeviceKey", String.class));
			log.debug("IpinsideServiceImpl VFDSDeviceKey : " + VFDSDeviceKey);

			// 대고객 Open인 경우
			if (StringUtils.isNotBlank(smartData) || isNull) {
				log.debug("@@@ ipinisde sendSmartNPInsideInfo IP Tracer : null of clientEmbededData OK!!");
				if (isOpen) {
					// 스마트폰뱅킹은 아래와같이 변경되어야 한다.
					loader.sendPNInsideInfo(request, serviceNo, userId, VFDSDeviceKey, "", "", "", userId, 1, smartData,
							smartNip, "");
					log.debug("@@@ ipinisde sendSmartNPInsideInfo IP Tracer : Open to external user...");
				}
				// 내부만 Open인 경우
				else {
					log.debug("@@@ ipinisde sendSmartNPInsideInfo IP Tracer : Open to internal user only");
					if ((request.getRemoteAddr().equals("23.20.115.43") == true)
							|| (request.getRemoteAddr().equals("23.20.201.22") == true)
							|| (request.getRemoteAddr().equals("23.20.201.23") == true)
							|| (request.getRemoteAddr().equals("23.20.201.24") == true)
							|| (request.getRemoteAddr().equals("118.219.55.2") == true)
							|| (request.getRemoteAddr().equals("172.20.24.200") == true)
							|| (request.getRemoteAddr().equals("172.20.24.207") == true)
							|| (request.getRemoteAddr().equals("23.20.201.42") == true)) {
						log.debug("@@@ ipinisde sendSmartNPInsideInfo IP Tracer : legal access from internal proxy");
						loader.sendPNInsideInfo(request, serviceNo, userId, VFDSDeviceKey, "", "", "", userId, 1,
								smartData, smartNip, "");
					}
				}
			}
		} catch (Exception e) {
			log.error("@@@ ipinisde sendSmartNPInsideInfo IP Tracer exception...", e);
		}
	}

	@ComponentOperation(name = "getPcFixInfoNOtherPCYes", description = "PC지정서비스&다른PC접속여부")
	public PcFixInfo getPcFixInfoNOtherPCYes(String pcFixValue) throws PRCServiceException {
		return getPcFixInfoNOtherPCYes(pcFixValue, sessionManager.getLoginValue("UserID", String.class));
	}

	@ComponentOperation(name = "getPcFixInfoNOtherPCYes", description = "PC지정서비스&다른PC접속여부")
	public PcFixInfo getPcFixInfoNOtherPCYes(String pcFixValue, String userId) throws PRCServiceException {
		PcFixInfo pcFixInfo = new PcFixInfo();
		Integer deviceCount = getCountDeviceAuth(userId);
		pcFixValue = StringUtils.defaultString(pcFixValue);

		if (pcFixValue.equals("0")) {
			if (deviceCount != 0) {
				pcFixValue = "1";
			} else {
				pcFixValue = "0";
			}
		} else if (pcFixValue.equals("-1")) { // 접속장애......
			pcFixValue = "0";
		}
		String OtherPC = "N";
		if (pcFixValue.equals("1") || pcFixValue.equals("2")) {
			OtherPC = getOtherPCYes(userId);
		}

		pcFixInfo.setPcFixValue(pcFixValue);
		pcFixInfo.setOtherPcYes(OtherPC);

		log.debug("@@@ ipinside getPcFixInfoNOtherPCYes pcFixInfo[" + pcFixInfo.toString() + "]");
		return pcFixInfo;
	}

	/**
	 * PC 지정 서비스에 등록된 단말수를 가져온다.
	 *
	 * @param userId
	 * @throws SCBKSystemException
	 */
	@ComponentOperation(name = "getCountDeviceAuth", description = "PC 지정 서비스에 등록된 단말수 조회")
	public Integer getCountDeviceAuth(String userId) throws PRCServiceException {

		log.debug("@@@ ipinside getCountDeviceAuth PC 지정 서비스에 등록된 단말수 시작...... mode {}", RuntimeContext.getRunMode());

		Integer deviceCount = 0;

		DeviceAuthUserParameter params = DeviceAuthUserParameter.builder().serviceNo("1016020500").userId(userId)
				.build();

		if (!RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
			deviceCount = deviceAuthUserDao.selectDeviceAuthCount(params);
		}

		log.debug("@@@ ipinside getCountDeviceAuth PC 지정 서비스에 등록된 단말수 종료......deviceCount[" + deviceCount + "]");

		return deviceCount;
	}

	/**
	 * PC 지정 서비스 가입 고객에 한해서 미지정 단말 거래 가능여부를 가져온다
	 *
	 * @param userId
	 */
	@ComponentOperation(name = "getOtherPCYes", description = "PC 지정 서비스 가입 고객에 한해서 미지정 단말 거래 가능여부")
	public String getOtherPCYes(String userId) throws PRCServiceException {
		log.debug("@@@ ipinside getOtherPCYes ######### PC 지정 서비스 가입 고객에 한해서 미지정 단말 거래 가능여부 시작 userId[" + userId + "]");

		String otherPcYes = "N";

		DeviceAuthUserParameter params = DeviceAuthUserParameter.builder().userId(userId).build();

		if (!RunMode.LOCAL.equals(RuntimeContext.getRunMode())) {
			DeviceAuthUserResult result = deviceAuthUserDao.selectDeviceAuthUserInfo(params);

			if (result != null) {
				otherPcYes = StringUtils.defaultIfBlank(result.getOtherpcYes(), "N").toUpperCase();
			}
		}

		log.debug("@@@ ipinside getOtherPCYes #########  PC 지정 서비스 가입 고객에 한해서 미지정 단말 거래 가능여부 종료 OTHERPC_YES["
				+ otherPcYes + "]");
		return otherPcYes;
	}

	@ComponentOperation(name = "simpleDataDecode2", description = "")
	public String[] simpleDataDecode2() throws PRCServiceException {

		String deviceId = PRCSharedUtils.getIpinsideHdd();

		log.debug("@@@ ipinside simpleDataDecode2 deviceId[{}]", deviceId);
		String resultArr[] = { "", "" };

		if (StringUtils.isNotBlank(deviceId)) {

			String realMacDeviceID = "";
			String[] dataArray = null;
			int colonPos1, colonPos2 = 0;
			Coder code = new Coder();
			realMacDeviceID = code.Decode(deviceId);
			dataArray = realMacDeviceID.split(";");

			if (dataArray.length > 0) {
				colonPos1 = dataArray[0].indexOf(":");
				resultArr[0] = dataArray[0].substring(1, colonPos1);
			}
			if (dataArray.length > 1) {
				colonPos2 = dataArray[1].indexOf(":");
				resultArr[1] = dataArray[1].substring(2, colonPos2);
			}
		}
		log.debug("@@@ ipinside simpleDataDecode2 resultArr0[" + resultArr[0] + "], resultArr1[" + resultArr[1] + "]");
		return resultArr;
	}

	@ComponentOperation(name = "simpleDataDecode", description = "")
	public String simpleDataDecode() throws PRCServiceException {

		String pinDeviceId = "";
		String deviceId = PRCSharedUtils.getIpinsideHdd();

		if (StringUtils.isNotBlank(deviceId)) {
			log.debug("@@@ ipinside simpleDataDecode deviceId[" + deviceId + "]");

			String realMacDeviceID = "";
			String[] dataArray = null;
			int colonPos1, colonPos2 = 0;
			Coder code = new Coder();
			realMacDeviceID = code.Decode(deviceId);
			log.debug("@@@ ipinside simpleDataDecode realMacDeviceID->" + realMacDeviceID);
			dataArray = realMacDeviceID.split(";");
			for (int i = 0; i < dataArray.length; i++) {
				log.debug("@@@ ipinside simpleDataDecode dataArray->" + dataArray[i]);
			}

			colonPos1 = dataArray[0].indexOf(":");
			log.debug("@@@ ipinside simpleDataDecode colonPos1->" + colonPos1);
			pinDeviceId = dataArray[0].substring(1, colonPos1);
			log.debug("@@@ ipinside simpleDataDecode pinDeviceId->" + pinDeviceId);
			colonPos2 = dataArray[1].indexOf(":");
			log.debug("@@@ ipinside simpleDataDecode IMEI Value->" + colonPos2);
			pinDeviceId = pinDeviceId + "$$$" + dataArray[1].substring(2, colonPos2);
			log.debug("@@@ ipinside simpleDataDecode pinDeviceId->" + pinDeviceId);
		}

		return pinDeviceId;
	}

	@ComponentOperation(name = "simpleDataDecodeIMEI", description = "")
	public String simpleDataDecodeIMEI() throws PRCServiceException {

		String pinImeiValue = "";
		String deviceId = PRCSharedUtils.getIpinsideHdd();

		log.debug("@@@ ipinside simpleDataDecodeIMEI deviceId[" + deviceId + "]");
		String realMacDeviceID = "";

		String[] dataArray = null;
		int colonPos = 0;
		Coder code = new Coder();
		realMacDeviceID = code.Decode(deviceId);
		dataArray = realMacDeviceID.split(";");
		colonPos = dataArray[1].indexOf(":");
		log.debug("@@@ ipinside simpleDataDecodeIMEI 1 ->" + colonPos);
		pinImeiValue = dataArray[1].substring(2, colonPos);
		log.debug("@@@ ipinside simpleDataDecodeIMEI ->" + pinImeiValue);

		return pinImeiValue;
	}

	/**
	 * 비로그인에서 사용하는 getPcFixValue 처리
	 */
	@ComponentOperation(name = "getPcFixValueThrow", description = "")
	public void getPcFixValueThrow(String userId) throws PRCServiceException {

		String ipinsideAx = PRCSharedUtils.getIpinsideAx();
		String ipinsideHdd = PRCSharedUtils.getIpinsideHdd();

		log.debug("@@@ ipinside getPcFixValueThrow ipinsideAx[" + ipinsideAx + "]userId[" + userId + "]");
		QLoader loader = this.sendNPInsideInfo(userId, true, false);
		int qResult = 0;
		try {
			qResult = loader.getPCDeginateInfo(null, "1016020500", userId, ipinsideHdd);
		} catch (Exception e) {
			log.error("@@@ ipinside getPcFixValueThrow ipinsideAx[" + ipinsideAx + "]userId[" + userId + "] msg["
					+ e.getMessage() + "]", e);
		}

		PcFixInfo pcFixInfo = getPcFixInfoNOtherPCYes(qResult + "", userId);
		if (pcFixInfo != null) {
			// PC미지정 단말 사용 가능여부. Y/N
			String OtherPCYes = StringUtils.defaultString(pcFixInfo.getOtherPcYes());
			// PC 지정 서비스 신청 상태 ( 0: 미신청 or 미지정PC , 2:
			String PcFixValue = StringUtils.defaultString(pcFixInfo.getPcFixValue());
			sessionManager.setGlobalValue("OtherPCYes", OtherPCYes);
			sessionManager.setGlobalValue("PcFixValue", PcFixValue);
			log.debug("@@@ ipinside getPcFixValueThrow PcFixValue[" + PcFixValue + "], OtherPCYes[" + OtherPCYes + "]");
			if ("1".equals(PcFixValue) && !"Y".equals(OtherPCYes)) {
				log.error("@@@ ipinside getPcFixValueThrow error PcFixValue[" + PcFixValue + "], OtherPCYes["
						+ OtherPCYes + "]");

				String errorCode = "PRCCMM0045";
				String errorMsg = "단말기 지정 서비스에 가입되어 있어요.<br/>새로운 단말기에서 이용하려면 단말기 지정/해제를 해야 해요.<br/><br/>단말기 지정메뉴로 이동할까요?";
				if (PRCSharedUtils.isIB()) {
					errorCode = "PRCCMM0046";
					errorMsg = "단말기 지정 서비스에 가입되어 있어요. 새로운 단말기에서 이용하려면 단말기 지정/해제가 필요해요.<br/>모바일뱅킹 또는 영업점에서 단말기 지정/해제 후 이용해 주세요.";
				} else {
				}

				PRCServiceException e = new PRCServiceException(errorCode, errorMsg);

				if ("PRCCMM0045".equals(errorCode)) {
					e.setNextPage("MA3CRTDDD002");

				}
				throw e;
			}
		}
	}

	/*
	 * Dfinder restApi를 이용한 VFDS 탐지결과 조회 및 수집
	 */
	@ComponentOperation(name = "dFinderApi", description = "")
	public DFinderApiInfo dFinderApi(String userId, String serViceType) throws PRCServiceException {

		log.debug("dFinderApi 탐지결과 수집 및 조회 시작 =================");

		String svrName = "";
		DFinderApiInfo dfinderApiInfo = new DFinderApiInfo();
		JSONObject jsonParam = new JSONObject();
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		BufferedReader br = null;
		HttpsURLConnection conn = null;
		InputStream in = null;
		StringBuffer sb = new StringBuffer();
		int status = 0;

		if (("P").equals(serViceType)) {
			svrName = PropertiesUtils.getString("DFINDER_SERVER_PRODUCE");
		} else if (("C").equals(serViceType)) {
			svrName = PropertiesUtils.getString("DFINDER_SERVER_CONCURRENT");
		} else if (("R").equals(serViceType)) {
			svrName = PropertiesUtils.getString("DFINDER_SERVER_RETRIEVE");
		} else {
			svrName = "";
		}

		String dFinderurl = PropertiesUtils.getString("DFINDER_SERVER_URL") + svrName + "?timeout=500";
		Integer VFDS_TIME_OUT = Integer.parseInt(PropertiesUtils.getString("VFDS_TIME_OUT"));

		log.debug("VFDS dFinderurl : " + dFinderurl);

		try {

			URL url = URI.create(dFinderurl).toURL();

			TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkServerTrusted(X509Certificate[] crts, String authType) {
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}
			} };

			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, certs, new java.security.SecureRandom());
			conn.setDefaultSSLSocketFactory(context.getSocketFactory());
			conn.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			conn.setDoInput(true);
			conn.setConnectTimeout(VFDS_TIME_OUT);
			conn.setReadTimeout(VFDS_TIME_OUT);

			jsonParam.put("usid", userId);

			log.debug("VFDS dFinder param : " + jsonParam.toString());

			conn.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			bw.write(jsonParam.toString());
			bw.flush();
			bw.close();

			if (conn != null) {
				status = conn.getResponseCode();
				log.debug("response status :::: " + status);

				String readLine = null;
				in = conn.getInputStream();

				br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

				while ((readLine = br.readLine()) != null) {
					sb.append(readLine);
				}
			}
			br.close();
		} catch (Exception e) {

			if (conn != null) {
				conn.disconnect();
				conn = null;
			}

			if (in != null) {
				in = null;
			}

			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e1) {
			}
			log.error("dFinderApi error : " + e.getMessage());
		}

		try {

			JSONObject resultJSONObject = (JSONObject) parser.parse(sb.toString());
			log.debug("VFDS dFinderApi : resultData \n" + resultJSONObject);

			// result_code 가 "F"인 경우 에러
			if ("F".equals(resultJSONObject.get("result_code").toString())) {
				log.error(
						"DFINDER 에러가 발생하였습니다. [result_code : " + resultJSONObject.get("result_code").toString() + "]");
			}

			dfinderApiInfo.setResultCode(resultJSONObject.get("result_code").toString()); // 결과코드

			String rspActnMethCd = "00";
			String rspActnStatCd = "00";

			// AS-IS FDS 상품코드와 동기화 작업 수행
			if ("1.0".equals(resultJSONObject.get("result_code").toString())
					|| "2.0".equals(resultJSONObject.get("result_code").toString())
					|| "3.0".equals(resultJSONObject.get("result_code").toString())) {
				rspActnMethCd = "04";
				rspActnStatCd = "01";
			} else if ("4.0".equals(resultJSONObject.get("result_code").toString())) {
				rspActnMethCd = "06";
				rspActnStatCd = "01";
			} else if ("5.0".equals(resultJSONObject.get("result_code").toString())) {
				rspActnMethCd = "03";
				rspActnStatCd = "01";
			} else {
				rspActnMethCd = "00";
				rspActnStatCd = "02";
			}

			dfinderApiInfo.setRspActnMethCd(rspActnMethCd); // 조치방법코드
			dfinderApiInfo.setRspActnStatCd(rspActnStatCd); // 조치상태코드

		} catch (Exception e) {
			log.error("dFinderApi error : " + e.getMessage());
		}

		log.debug("dfinderApiInfo :::: " + dfinderApiInfo.toString());
		log.debug("VFDS dFinderApi  탐지결과 수집 및 조회 종료 =================");

		return dfinderApiInfo;
	}

	/**
	 * malWareChk를 이용한 악성앱 여부 전송
	 * 
	 * @param deViceType 디바이스 Type ("ANY", "PC", "MOBILE")
	 * @param wData      단말정보 ipInside w_data 암호화된 데이터
	 * @param nData      단말정보 ipInside n_data 암호화된 데이터
	 * @param afData     단말정보 ipinside f_data 암호화된 데이터
	 * @param spyWare    악성앱정보
	 * @param usId       사용자id
	 * @param uuidKey    단말정보 ipinside uuidKey 데이터(평문 사용자식별Key 문자열)
	 * 
	 * @return Map
	 */
	@ComponentOperation(name = "checkMalWare", description = "")
	public String checkMalWare(String deViceType, String wData, String nData, String afData, String spyWare,
			String usId, String uuidKey) throws PRCServiceException {

		log.info("malWareChk 악성앱 여부 전송 시작 =================");

		String resultCode = "";
		JSONObject jsonParam = new JSONObject();
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		BufferedReader br = null;
		HttpsURLConnection conn = null;
		OutputStream os = null;
		StringBuffer resultJSONString = new StringBuffer();

		String webIpinsideUrl = PropertiesUtils.getString("DFINDER_SERVER_URL")
				+ PropertiesUtils.getString("DFINDER_SERVER_PRODUCE") + "?timeout=500";
		Integer VFDS_TIME_OUT = Integer.parseInt(PropertiesUtils.getString("VFDS_TIME_OUT"));
		log.debug("sendFdsLog : " + webIpinsideUrl);

		/*
		 * 악성앱 호출 방식 변경 As-is : restApi -> VFDS 서버 전송 To-be : 전문전송 -> Message Hub에서 파일로
		 * 저장(SPY_xxxxxxxx.log) ->VFDS 서버에서 해당파일 가지고 가서 처리 악성앱 호출 방법 파라미터 추가
		 * (MALWARE_MESSAGE_YN) "Y" : Message Hub 로 데이터 전송 "N" : RestApi 호출
		 */
		try {

			jsonParam.put("@device_type", deViceType); // 디바이스 Type ("ANY", "PC", "MOBILE")
			jsonParam.put("@wdata", wData); // 단말정보 ipInside w_data 암호화된 데이터
			jsonParam.put("@ndata", nData); // 단말정보 ipInside n_data 암호화된 데이터
			jsonParam.put("@af_data", afData); // 단말정보 ipinside f_data 암호화된 데이터
			jsonParam.put("Spyware", spyWare); // 악성앱정보
			jsonParam.put("usid", usId); // 사용자id
			jsonParam.put("@uuidKey", uuidKey); // 단말정보 ipinside uuidKey 데이터(평문 사용자식별Key 문자열)

			log.info("webIpinside param : " + jsonParam.toString());

			TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkServerTrusted(X509Certificate[] crts, String authType) {
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}
			} };

			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, certs, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			URL url = URI.create(webIpinsideUrl).toURL();
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(VFDS_TIME_OUT);
			conn.setReadTimeout(VFDS_TIME_OUT);
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			os = conn.getOutputStream();
			os.write(jsonParam.toJSONString().getBytes("UTF-8"));
			os.flush();
			os.close();

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String buf;

			while ((buf = br.readLine()) != null) {
				resultJSONString.append(buf);
			}

			br.close();
		} catch (Exception e) {

			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e1) {
			}

			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e1) {
			}
			log.error("malWareChk error : " + e.getMessage());
		}

		try {

			JSONObject resultJSONObject = (JSONObject) parser.parse(resultJSONString.toString());
			log.debug("malWareChk : resultData \n{}", resultJSONObject.toString());

			resultCode = resultJSONObject.getAsString("result_code").toString();

		} catch (Exception e) {
			log.error("malWareChk error : " + e.getMessage());
		}

		log.info("malWareChk  악성앱 여부 전송 종료 =================");

		return resultCode;
	}

	/**
	 * sendWebNPInsideInfo를 이용한 모바일 웹 VFDS 전송 처리
	 * 
	 * @param afData        단말정보 ipinside f_data 암호화된 데이터
	 * @param uuidkey       단말정보 ipinside uuidKey 데이터(평문 사용자식별Key 문자열)
	 * @param VFDSDeviceKey 단말키값
	 * 
	 * @return Map
	 */
	@ComponentOperation(name = "sendWebNPInsideInfo", description = "")
	public String sendWebNPInsideInfo(String afData, String uuidkey, String VFDSDeviceKey) throws PRCServiceException {

		log.debug("sendWebNPInsideInfo 웹정보 전송 시작 =================");

		String resultCode = "";
		JSONObject jsonParam = new JSONObject();
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		BufferedReader br = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		StringBuffer resultJSONString = new StringBuffer();

		String webIpinsideUrl = PropertiesUtils.getString("WEB_IPINSIDE_URL")
				+ PropertiesUtils.getString("DFINDER_SERVER_PRODUCE") + "?timeout=500";
		Integer VFDS_TIME_OUT = Integer.parseInt(PropertiesUtils.getString("VFDS_TIME_OUT"));
		log.debug("webIpinsideUrl : " + webIpinsideUrl);

		try {

			jsonParam.put("@af_data", afData); // 단말정보 ipinside af_data 암호화된 데이터
			jsonParam.put("@uuidKey", uuidkey); // 단말정보 ipinside uuidKey 데이터(평문 사용자식별Key 문자열)
			jsonParam.put("VFDSDeviceKey", VFDSDeviceKey); // 단말키 값

			log.info("webIpinside param : " + jsonParam.toString());

			URL url = URI.create(webIpinsideUrl).toURL();

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(VFDS_TIME_OUT);
			conn.setReadTimeout(VFDS_TIME_OUT);
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			os = conn.getOutputStream();
			os.write(jsonParam.toJSONString().getBytes("UTF-8"));
			os.flush();
			os.close();

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String buf;

			while ((buf = br.readLine()) != null) {
				resultJSONString.append(buf);
			}

			br.close();
		} catch (Exception e) {

			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e1) {
			}

			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e1) {
			}
			log.error("sendWebNPInsideInfo error : " + e.getMessage());
		}

		try {

			JSONObject resultJSONObject = (JSONObject) parser.parse(resultJSONString.toString());
			log.debug("webIpinside : resultData \n{}", resultJSONObject.toString());

			resultCode = resultJSONObject.get("result_code").toString();

		} catch (Exception e) {
		}

		log.debug("sendWebNPInsideInfo 웹 전송 종료 =================");

		return resultCode;
	}

	/**
	 * 
	 * @param hostData
	 * @return
	 * @throws PRCServiceException
	 */
	@ComponentOperation(name = "sendFdsLog", description = "FDS 거래로그 전송")
	public String sendFdsLog(String vFdsDeviceKey, String hostData) throws PRCServiceException {

		log.debug("# sendFdsLog 전송 시작 =================");

		String resultCode = "";
		JSONObject jsonParam = new JSONObject();
		JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
		BufferedReader br = null;
		HttpsURLConnection conn = null;
		OutputStream os = null;
		StringBuffer resultJSONString = new StringBuffer();

		String webIpinsideUrl = PropertiesUtils.getString("DFINDER_SERVER_URL")
				+ PropertiesUtils.getString("DFINDER_SERVER_PRODUCE") + "?timeout=500";
		Integer VFDS_TIME_OUT = Integer.parseInt(PropertiesUtils.getString("VFDS_TIME_OUT"));
		log.debug("sendFdsLog : " + webIpinsideUrl);

		try {

			jsonParam.put("usid", vFdsDeviceKey); // 단말키
			jsonParam.put("host_data", hostData); // HOST로 보낼 데이터부

			log.info("webIpinside param : " + jsonParam.toString());

			TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkServerTrusted(X509Certificate[] crts, String authType) {
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// TODO Auto-generated method stub

				}
			} };

			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, certs, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			URL url = URI.create(webIpinsideUrl).toURL();
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setConnectTimeout(VFDS_TIME_OUT);
			conn.setReadTimeout(VFDS_TIME_OUT);
			conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			os = conn.getOutputStream();
			os.write(jsonParam.toJSONString().getBytes("UTF-8"));
			os.flush();
			os.close();

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String buf;

			while ((buf = br.readLine()) != null) {
				resultJSONString.append(buf);
			}

			br.close();
		} catch (Exception e) {

			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e1) {
			}

			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e1) {
			}
			log.error("sendFdsLog error : " + e.getMessage());
		}

		try {

			JSONObject resultJSONObject = (JSONObject) parser.parse(resultJSONString.toString());
			log.debug("# sendFdsLog : resultData \n{}", resultJSONObject.toString());

			resultCode = resultJSONObject.getAsString("result_code").toString();

		} catch (Exception e) {
		}

		log.debug("# sendFdsLog 전송 종료 =================");

		return resultCode;
	}

	@ComponentOperation(name = "getAbroadYn", description = "해외접속여부 체크")
	public String getAbroadYn() {

		String abroadYn = "N";
		if (sessionManager.isLogin()) {
			abroadYn = StringUtils.defaultIfBlank(sessionManager.getLoginValue("FlagIsAbroadYN", String.class), "N");
		} else {
			// IPINSIDE_AX
			if (StringUtils.isNotBlank(PRCSharedUtils.getIpinsideAx())) {

				QLoader loader = this.sendNPInsideInfo("CheckCountMA", true, false);
				FlagBean flag = null;

				try {
					flag = loader.getFlag();

					if (flag.getFlagValue() < 0) { // 인터리젠 서버값 비정상일 때 IB는 정상일 수 있도록 처리
						abroadYn = "N";
					} else {
						abroadYn = flag.isAbroadIP() ? "Y" : "N";
					}
				} catch (Exception e) {
					log.error("SCBKAddAuthMdm - interezen FlagBean", e);
				}

			}
		}

		return abroadYn;

	}
}