package com.scbank.process.api.svc.shared.components.edoc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.base.utils.PropertiesUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EdocServerFactory {

	private static List<String> serverUrls;

	static {
		serverUrls = new ArrayList<String>();
		serverUrls.add(PropertiesUtils.getString("EDOC_RELAY_SERVER_URL_1"));
		serverUrls.add(PropertiesUtils.getString("EDOC_RELAY_SERVER_URL_2"));
	}

	/**
	 * 
	 * 개요: 현재 가용한 서버의 서비스 아이피를 조회한다.
	 * 리스트의 순서는 현재 가용한 서버가 가장 맨 앞에 위치하도록 static 멤버로 관리된다.
	 * (리스트 순서의 동기화 적용)
	 * 
	 * 처리내용 :
	 * 
	 * @Method Name : getAvailableEdocServer
	 * @history
	 *          ---------------------------------------------------------------------------------
	 *          변경일 작성자 변경내용
	 *          ---------------------------------------------------------------------------------
	 *          2017. 12. 6. 966215 최초 작성
	 *          2020. 05. 29. 930385 MP서버 -> 전자문서서버(화상서버)로 이관
	 *          ---------------------------------------------------------------------------------
	 * @return
	 * @throws Exception
	 */
	public synchronized static String getAvailableEdocServer() {

		int index = 0;
		List<String> copyUrls = new ArrayList<String>();

		copyUrls.addAll(serverUrls);
		String serviceURLString = "";
		Socket socket = null;

		/* List ConcurrenModificationEexception 피하기위해 copy 본을 사용한다. */
		for (String serverUrl : copyUrls) {

			try {
				URL serviceURL = new URL(serverUrl);
				serviceURLString = serviceURL.getProtocol() + "://" + serviceURL.getHost() + ":" + serviceURL.getPort();
				/* 서버 응답이 없었을 경우 다음 대상 서버를 리스트의 맨 앞으로 세팅한다. */
				if (index > 0) {

					log.info("Current server key [" + serverUrl + "]");
					/* 가용한 서버 ordering */
					serverUrls.add(0, serverUrls.remove(index));

					log.info("Change server ordering [" + serverUrls + "]");
				}

				log.info("전자문서 중계서버 [" + serviceURLString + "] 상태 값 체크중......");

				SocketAddress soAddr = new InetSocketAddress(serviceURL.getHost(), serviceURL.getPort());
				socket = new Socket();
				socket.connect(soAddr, 3000);
				log.info("현재 전자문서 중계서버 [" + serviceURLString + "] 가 할당 되었습니다.");
				socket.close();

				index++;
				return serviceURLString;

			} catch (Exception e) {
				/* ignore this */
				log.info("현재 전자문서 중계서버 [" + serviceURLString + "] 가 응답하지 않습니다. Err [" + e.getMessage() + "]");
			} finally {
				if (socket != null)
					try {
						socket.close();
					} catch (IOException e) {
					}
			}
		}

		log.info("현재 모든 전자문서 서버가 응답하지 않습니다. [서비스 불가]");
		throw new PRCServiceException("현재 모든 전자문서 서버가 응답하지 않습니다. [서비스 불가]");
	}

}
