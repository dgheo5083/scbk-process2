package com.scbank.process.api.svc.shared.components.edoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.scbank.process.api.fw.base.utils.PropertiesUtils;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.windfire.apis.asysConnectData;
import com.windfire.apis.asysadmin.asysAdmServer;
import com.windfire.apis.asysadmin.asysAdmServerColl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/*******************************************************************************
*  업   무  명 : XvarmEngine서버 정상 거래 가능한지 체크하는 클래스
*  설       명 : XvarmEngine서버가 정상적으로 업무수행이 가능한 상태인지 확인하는 클래스.
*  작   성  자 : 2038565
*  작   성  일 : 2026.04.09
*  관련 테이블 : 
*  관련 전문   : 
*  Copyright ⓒ SC제일은행. All Right Reserved
* ******************************************************************************
*  변경이력 (버전/변경일시/작성자)
*  <pre>
*  최초작성 
*  </pre>
******************************************************************************/
@Slf4j
@SharedComponent
public class EdocXvarmLiveCheckerComponent {
	
	private static asysConnectData con = null; //XVARM Engine 접근 객체
	
	private List<String> errorServerList  = new ArrayList<String>(); //에러중인 서버를 체크하자.
	private List<String> xvarmServerList  = new ArrayList<String>(); //property에서 읽어온 XVARM IP를 Array로 변환한다.
	
	private static String EDOC_XVARM_HOST;
	private static int 	  EDOC_XVARM_PORT;
	private static String EDOC_XVARM_ID;
	private static String EDOC_XVARM_SECRET;
	
	@PostConstruct
	private void setStaticValues() {
		EDOC_XVARM_HOST		= StringUtils.defaultIfEmpty(PropertiesUtils.getString("EDOC_XVARM_HOST") , "");
		EDOC_XVARM_PORT		= Integer.parseInt(StringUtils.defaultIfEmpty(PropertiesUtils.getString("EDOC_XVARM_PORT") , "0"));
		EDOC_XVARM_SECRET	= PropertiesUtils.getString("EDOC_XVARM_SECRET");
		EDOC_XVARM_ID 		= PropertiesUtils.getString("EDOC_XVARM_ID");
	}
	
	@ComponentOperation(name = "EdocXvarmLiveCheckerComponent 초기화")
	public void init() {
		log.debug("EdocXvarmLiveCheckerComponent EDOC_XVARM_HOST => {}",PropertiesUtils.getString("EDOC_XVARM_HOST"));
		log.debug("EdocXvarmLiveCheckerComponent EDOC_XVARM_PORT => {}",EDOC_XVARM_PORT);
		log.debug("EdocXvarmLiveCheckerComponent EDOC_XVARM_ID => {}",EDOC_XVARM_ID);
		log.debug("EdocXvarmLiveCheckerComponent EDOC_XVARM_SECRET => {}",EDOC_XVARM_SECRET);
		
		xvarmServerList = Arrays.asList(EDOC_XVARM_HOST.split("[|]"));
		for (int i = 0; i < xvarmServerList.size(); i++) {
			con = getEdocXvarmConnector(xvarmServerList.get(i) , EDOC_XVARM_PORT , "EDOC"); //asysConnectData 객체 생성
			String serverCheckResult = checkXvarmLiveConnector();
			edocXvarmLiveCheckerLog(" serverIP : [" + xvarmServerList.get(i) + "] || serverCheckResult : [" + serverCheckResult + "]");
			if(!serverCheckResult.equals("00")) {
				setErrorServerList(xvarmServerList.get(i));
			}else {
				setEdocXvarmConnector(con);
				break;
			}
		}
		edocXvarmLiveCheckerLog(" getErrorServerList Size : [" + getErrorServerList().size() + "]");
	}
	
	@ComponentOperation(name = "EdocXvarmLiveCheckerComponent 초기화")
	public void init(boolean batchCheck) {
		if(batchCheck) {
			xvarmServerList = Arrays.asList(EDOC_XVARM_HOST.split("[|]"));
			for (int i = 0; i < xvarmServerList.size(); i++) {
				con = getEdocXvarmConnector(xvarmServerList.get(i) , EDOC_XVARM_PORT , "EDOC"); //asysConnectData 객체 생성
				String serverCheckResult1 = checkXvarmLiveConnector();
				if(!serverCheckResult1.equals("00")) {
					setErrorServerList(xvarmServerList.get(i));
				}else {
					discon(con);
				}
			}
		}else {
			new EdocXvarmLiveCheckerComponent();
		}
	}
	
	@ComponentOperation(name = "에러서버 리스트 획득")
	public List<String> getErrorServerList() {
		return errorServerList;
	}
	
	/**
	 * 에러서버리스트를 저장한다.
	 * 해당 클래스에서만 사용하므로 접근제어자는 private이다.
	 * @param errorServerList
	 */
	@ComponentOperation(name = "에러서버리스트를 저장한다.")
	private void setErrorServerList(String errorServer) {
		if(!this.errorServerList.contains(errorServer)) {
			this.errorServerList.add(errorServer);
		}
	}

	
	/**
	 * asysConnectData 객체를 생성한다.
	 * @param con
	 * @param ip
	 * @param port
	 * @param desc
	 * @return
	 */
	private asysConnectData getEdocXvarmConnector(String ip, int port, String desc) {
		con = new asysConnectData( ip , port ,desc , EDOC_XVARM_ID , EDOC_XVARM_SECRET);
		return con;
	}
	
	/**
	 * 현재 저장된 Connector 객체를 가져온다.
	 * @return
	 */
	@ComponentOperation(name = "현재 저장된 Connector 객체 획득")
	protected asysConnectData getEdocXvarmConnector() {
		return con;
	}
	
	/**
	 * Xvarm Engine이 정상적인지 체크한다.
	 * 3초후에 응답이없으면 에러처리 진행.
	 * @return
	 */
	@ComponentOperation(name = "Xvarm Engine 체크")
	public String checkXvarmLiveConnector() {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		String result = "09";

		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				return checkXvarmObject(con);
			}
		};

		Future<String> future = executorService.submit(task);

		try {
			result = future.get(2, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			edocXvarmLiveCheckerLog(" checkXvarmLiveConnector Error발생 : [" + e.getMessage()+ "]");
		} finally {
			edocXvarmLiveCheckerLog(" 정상적으로 Thread ShutDown");
			executorService.shutdown();
		}
		return result;
	}
	
	/**
	 * 정상적인 XVARM Connector 객체를 저장한다.
	 * 
	 * @param con
	 */
	private static void setEdocXvarmConnector(asysConnectData con) {
		EdocXvarmLiveCheckerComponent.con = con;
	}
	
	/**
	 * Xvarm Connector 연결가능여부 판단
	 * @param con
	 * @return
	 * @throws InterruptedException
	 */
	private String checkXvarmObject(asysConnectData con) throws InterruptedException {
		String result = "09";
		asysAdmServerColl adsc = new asysAdmServerColl(con, false, null);
		int res = adsc.retrieveServers("XVARM_MAIN"); //MA3.0 , EDOC 서버에서 사용하는 XVARM Gateway는 XVARM_MAIN 이다.
		
		int cnt = adsc.getCollCount();
		
		for(int i = 0 ; i < cnt ; i++){
			asysAdmServer ads = (asysAdmServer)adsc.getCollObject(i);
			edocXvarmLiveCheckerLog(" SERVERID : " + ads.getField("SERVERID"));
			edocXvarmLiveCheckerLog(" DESCR : " + ads.getField("DESCR"));
			edocXvarmLiveCheckerLog(" ADDRESS : " + ads.getField("ADDRESS"));
			edocXvarmLiveCheckerLog(" PORT : " + ads.getField("PORT"));
			edocXvarmLiveCheckerLog(" ADMINSCLASS : " + ads.getField("ADMINSCLASS"));
			edocXvarmLiveCheckerLog(" DETAIL : " + ads.getField("DETAIL"));
		}
		
		if (res == 0) {
			// 연결 성공
			result = "00";
		} else {
			// 연결 실패 res 11
			result = "11";
		}
		edocXvarmLiveCheckerLog(" checkXvarmObject result : [" + result + "]");
		return result;
	}
	
	/**
	 * asysConnectData 객체를 메모리에서 제거하여준다.
	 * @param con
	 */
	private void discon(asysConnectData con){
		if(con != null){
			con.close();
			con = null;
		}
	}
	
	/**
	 * 로그 Control를 하기위해 아래 메서드를 통하여 로그를 출력하자.
	 * @param log
	 */
	private void edocXvarmLiveCheckerLog(String msg) {
		log.debug("EdocXvarmLiveChecker " + msg + "  #######");
	}

}
