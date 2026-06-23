package com.scbank.process.api.svc.shared.utils;

import java.util.Map;

import com.imas.lbs.TcpipClient;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class EmailUtils {

	/**
	 * 메일발송
	 * 
	 * @param AID
	 * @param RegNo
	 * @param UserName
	 * @param EmailAddr
	 * @return
	 */
	public boolean sendCompleteMail(String AID, String RegNo, String UserName, String EmailAddr) {

		try {
			log.debug("====Mail Send Start ====");

			String imasIP = PropertiesUtils.getString("IMAS_IP");
			String imasPort = PropertiesUtils.getString("IMAS_PORT");

			log.debug("====Mail Info [{}][{}][{}][{}][{}][{}]", AID, imasIP, imasPort, EmailAddr, RegNo, UserName);

			TcpipClient tc = new TcpipClient();
			tc.open(imasIP, Integer.parseInt(imasPort));

			log.debug("########## open OK?.....");

			tc.open(imasIP, Integer.parseInt(imasPort));

			// IMAS 이메일 연동 인코딩 이슈 개선
			tc.setArg("CHARSET", "UTF-8");

			log.debug("########## open OK!.....");

			// 핸들러명(MAS에 등록된 핸들러의 Alias)
			tc.setAID(AID);
			tc.setArg("CUSTNO", RegNo);
			tc.setArg("NAME", UserName);
			tc.setArg("EMAIL", EmailAddr);

			// 핸들러 실행-보내기
			tc.commit();
			tc.quit();

			log.debug("====Mail Send End ====");

		} catch (Throwable t) {
			log.error("====Mail Send Error ====");
			log.error(t.getMessage());
		}
		return true;
	}

	/**
	 * 메일발송
	 * 
	 * @param aid
	 * @param args
	 * @return
	 */
	public boolean sendCompleteMail(String aid, Map<String, String> args) {
		if (aid == null || aid.trim().isEmpty() || args == null || args.isEmpty()) {
			return false;
		}

		try {

			log.debug("====Mail Send Start ====");

			String imasIP = PropertiesUtils.getString("IMAS_IP");
			String imasPort = PropertiesUtils.getString("IMAS_PORT");

			log.debug("====Mail Info [{}][{}][{}][{}][{}][{}]", aid, imasIP, imasPort);

			TcpipClient tc = new TcpipClient();
			tc.open(imasIP, Integer.parseInt(imasPort));
			tc.setArg("CHARSET", "UTF-8");
			tc.setAID(aid);

			for (Map.Entry<String, String> entry : args.entrySet()) {
				log.debug("====Mail Info [{}][{}]", entry.getKey(), entry.getValue());
				tc.setArg(entry.getKey(), entry.getValue());
			}

			tc.commit();
			tc.quit();

			log.debug("====Mail Send End ====");

			return true;
		} catch (Throwable t) {
			log.error("====Mail Send Error ====");
			log.error(t.getMessage());
			return false;
		}
	}

	/**
	 * 메일발송
	 * 
	 * @param aid
	 * @param adminName
	 * @param adminEmail
	 * @param subject
	 * @param accounts
	 */
	public boolean sendCompleteMail(String aid, String recvName, String recvEmail, String subject, String content)
			throws Exception {
		if (aid == null || aid.trim().equals("") || recvName == null || recvName.trim().equals("") || recvEmail == null
				|| recvEmail.trim().equals("") || subject == null || subject.trim().equals("") || content == null
				|| content.trim().equals("")) {
			return false;
		}

		log.info("======>SEND TAX MAIL : {}", content);

		String imasIP = PropertiesUtils.getString("IMAS_IP");
		String imasPort = PropertiesUtils.getString("IMAS_PORT");

		try {
			/**
			 * MAS의 설치된 시스템의 IP Address와 RMI Port번호 테스트 10.61.24.162 9100 -> 10.61.17.236
			 * 9100 변경 (2018.08.30) 리얼 211.32.160.65 9100
			 **/
			TcpipClient tc = new TcpipClient();
			tc.open(imasIP, Integer.parseInt(imasPort));
			// tc.open("10.61.24.162", 9100);

			// 핸들러명(MAS에 등록된 핸들러의 Alias)
			tc.setAID(aid);
			tc.setArg("NAME", recvName);
			// 핸들러와 템플릿에서 사용할 관리자email을 인자로 넘김
			tc.setArg("ADMINEMAIL", recvEmail);
			// 핸들러와 템플릿에서 사용할 email 제목을 인자로 넘김
			tc.setArg("SUBJECT", subject);
			// 핸들러와 템플릿에서 사용할 email 내용을 인자로 넘김
			tc.setArg("CONTENT", content);

			// 핸들러 실행
			tc.commit();
			tc.quit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return true;
	}

}
