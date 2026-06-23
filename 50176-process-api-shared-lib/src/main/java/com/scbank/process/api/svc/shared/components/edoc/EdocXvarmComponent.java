package com.scbank.process.api.svc.shared.components.edoc;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.utils.EdocUtils;
import com.scbank.process.api.svc.shared.utils.FileUtils;
import com.windfire.apis.asysConnectData;
import com.windfire.apis.asys.asysUsrElement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SharedComponent
@RequiredArgsConstructor
public class EdocXvarmComponent {
	
	private asysConnectData con = null; //XVARM engine Connect 객체 선언
	private final EdocXvarmLiveCheckerComponent edocXvarmLiveCheckerComponent;
	
	@ComponentOperation(name = "EdocXvarmComponent 초기화")
	public void init() {
		edocXvarmLiveCheckerComponent.init();
		con = edocXvarmLiveCheckerComponent.getEdocXvarmConnector();
	}
	
	/**
	 * 전자문서 및 이미지를 wormDisk에 저장한다.(암호화X)
	 * @param edocPath ( 문서가 존재하는 서버 물리경로 )
	 * @param classid ( EDOC_CC(전자문서) , EDOC_FAX_CC(FAX 전용)
	 * @return
	 */
	@ComponentOperation(name = "전자문서 및 이미지를 wormDisk에 저장한다.(암호화X)")
	public Map<String,String> create(File file , String classid){
		Map<String,String> output = create(file, classid , false);
		return output;
		
	}
	
	/**
	 * 전자문서 및 이미지를 wormDisk에 저장한다.(암호화X)
	 * @param edocPath ( 문서가 존재하는 서버 물리경로 )
	 * @param classid ( EDOC_CC(전자문서) , EDOC_FAX_CC(FAX 전용)
	 * @return
	 */
	@ComponentOperation(name = "전자문서 및 이미지를 wormDisk에 저장한다.(암호화X)")
	public Map<String,String> create(String path , String classid){
		Map<String,String> output = create(path, classid , false);
		return output;
		
	}
	
	/**
	 * 전자문서 및 이미지를 wormDisk에 저장한다.(암호화 O)
	 * @param edocPath ( 문서가 존재하는 서버 물리경로 )
	 * @param classid ( EDOC_CC(전자문서) , EDOC_FAX_CC(FAX 전용)
	 * @return
	 */
	@ComponentOperation(name = "전자문서 및 이미지를 wormDisk에 저장한다.(암호화 O)")
	public Map<String,String> create(String path , String classid , boolean encrypt){
		asysUsrElement uePage1 = new asysUsrElement(con);
		String filePath = "";
		if(encrypt) {
			File file = new File(path);
			Map<String,String> encData = edocEncryptFile(file);
			filePath = encData.get("encPath") + encData.get("encName");
		}else {
			filePath = path;
		}
		
		Map<String,String> output = new HashMap<String,String>();
		uePage1.m_localFile = filePath;
		uePage1.m_cClassId = classid;  // EDOC_CC, EDOC_FAX_CC
		uePage1.m_descr = "EDOC";
		uePage1.m_userSClass = "SUPER";
		uePage1.m_eClassId = "IMAGE";
		try {
			int ret = uePage1.create("XVARM_MAIN");
			if ( ret != 0 ) {
				log.debug("Error, create .. " + uePage1.getLastError());
				output.put("errorCode" , "0001");
				output.put("errorMsg"  , uePage1.getLastError());
				output.put("elementID" , "");
			}else {
				log.debug("Success, elementid .. " + uePage1.m_elementId);
				output.put("errorCode" , "0000");
				output.put("errorMsg"  , "");
				output.put("elementID" , uePage1.m_elementId);
			}
		}catch (Exception e) {
			output.put("errorCode" , "0002");
			output.put("errorMsg"  , e.getMessage());
			output.put("elementID" , "");
		} finally {
			discon();
		}
		return output;
		
	}
	
	/**
	 * 전자문서 및 이미지를 wormDisk에 저장한다.(암호화 O)
	 * @param edocPath ( 문서가 존재하는 서버 물리경로 )
	 * @param classid ( EDOC_CC(전자문서) , EDOC_FAX_CC(FAX 전용)
	 * @return
	 */
	@ComponentOperation(name = "전자문서 및 이미지를 wormDisk에 저장한다.(암호화 O)")
	public Map<String,String> create(File file , String classid , boolean encrypt){
		asysUsrElement uePage1 = new asysUsrElement(con);
		Map<String,String> encData = new HashMap<String,String>();
		String filePath = "";
		if(encrypt) {
			encData = edocEncryptFile(file);
			filePath = encData.get("encPath") + encData.get("encName");
		}else {
			filePath = file.getAbsolutePath();
		}
		
		Map<String,String> output = new HashMap<String,String>();
		uePage1.m_localFile = filePath;
		uePage1.m_cClassId = classid;  // EDOC_CC, EDOC_FAX_CC
		uePage1.m_descr = "EDOC";
		uePage1.m_userSClass = "SUPER";
		uePage1.m_eClassId = "IMAGE";
		try {
			int ret = uePage1.create("XVARM_MAIN");
			if ( ret != 0 ) {
				log.debug("Error, create .. " + uePage1.getLastError());
				output.put("errorCode" , "0001");
				output.put("errorMsg"  , uePage1.getLastError());
				output.put("elementID" , "");
			}else {
				log.debug("Success, elementid .. " + uePage1.m_elementId);
				output.put("errorCode" , "0000");
				output.put("errorMsg"  , "");
				output.put("elementID" , uePage1.m_elementId);
			}
		}catch (Exception e) {
			output.put("errorCode" , "0002");
			output.put("errorMsg"  , e.getMessage());
			output.put("elementID" , "");
		} finally {
			discon();
		}
		return output;
	}
	
	/**
	 * 채널 XVARM 서버에 파일을 저장한다.
	 * @param file		: 서버 물리 파일 객체
	 * @param classid	: XVARM 영역 구분값(EDOC_CC , EDOC_FAX_CC)
	 * @param descr		: 상세정보(IDV , EDOC)
	 * @param encrypt	: 암호화여부(true , false)
	 * @param fileExtenstion : 원본파일 확장자(암호화여부 true시 필요함)
	 * @return
	 */
	@ComponentOperation(name = "채널 XVARM 서버에 파일을 저장한다.")
	public Map<String,String> create(File file , String classid , String descr , boolean encrypt , String fileExtenstion){
		asysUsrElement uePage1 = new asysUsrElement(con);
		Map<String,String> encData = new HashMap<String,String>();
		String filePath = "";
		
		//encrypt 값이 true이면 파일 확장자도 던져야한다. (ex) fileExtenstion : "jpg" or "tif" or "pdf"
		
		if(encrypt) {
			encData = edocEncryptFile(file , fileExtenstion);
			filePath = encData.get("encPath") + encData.get("encName");
		}else {
			filePath = file.getAbsolutePath();
		}
		
		Map<String,String> output 	= new HashMap<String,String>();
		uePage1.m_localFile		= filePath;
		uePage1.m_cClassId		= classid;  // EDOC_CC, EDOC_FAX_CC
		uePage1.m_descr			= descr;	//IDV , EDOC etc...
		uePage1.m_userSClass 	= "SUPER";
		uePage1.m_eClassId		= "IMAGE";
		try {
			int ret = uePage1.create("XVARM_MAIN");
			if ( ret != 0 ) {
				log.debug("Error, create .. " + uePage1.getLastError());
				output.put("errorCode" , "0001");
				output.put("errorMsg"  , uePage1.getLastError());
				output.put("elementID" , "");
			}else {
				log.debug("Success, elementid .. " + uePage1.m_elementId);
				output.put("errorCode" , "0000");
				output.put("errorMsg"  , "");
				output.put("elementID" , uePage1.m_elementId);
			}
		}catch (Exception e) {
			output.put("errorCode" , "0002");
			output.put("errorMsg"  , e.getMessage());
			output.put("elementID" , "");
		} finally {
			discon();
		}
		return output;
	}
	
	/**
	 * 1건의 문서를 다운로드한다. ( Create에서 생성한 문서 그대로 리턴 )
	 * @param elementid ( 문서ID EX)XVARM_MAIN::2020032010181900::IMAGE )
	 * @param savePath ( 다운로드될 위치 지정 )
	 */
	@ComponentOperation(name = "1건의 문서를 다운로드한다. ( Create에서 생성한 문서 그대로 리턴 )")
	public Map<String,String> download(String elementid , String savePath , String saveFile){
		Map<String,String> output = download(elementid, savePath, saveFile , false);
		return output;
	}
	
	/**
	 * 1건의 문서를 다운로드하고 , 문서 복호화를 진행한다.(SEED 복호화)
	 * @param elementid ( 문서ID EX)XVARM_MAIN::2020032010181900::IMAGE )
	 * @param savePath ( 다운로드될 위치 지정 )
	 */
	@ComponentOperation(name = "1건의 문서를 다운로드하고 , 문서 복호화를 진행한다.(SEED 복호화)")
	public Map<String,String> download(String elementid , String savePath , String saveFile ,  boolean decryptYN){
		//savePath 경로의 폴더를 체크하고 , 없으면 생성해야한다.
		FileUtils.makeDir(saveFile);
		log.debug("EdocXvarmUtil download elementid: " + elementid + "  ||  savePath : " + savePath + "  ||  saveFile : " + saveFile);
		Map<String,String> output = new HashMap<String,String>();
		asysUsrElement uePage1 = new asysUsrElement(con);
		uePage1.m_elementId = elementid;
		uePage1.m_cClassId = "EDOC_CC";
		
		int ret = uePage1.getContent(savePath + saveFile, "", "");
		log.debug("EdocXvarmUtil download ret :  " + ret);
		try {
			if ( ret != 0 ) {
				output.put("errorCode" , "0003");
				output.put("errorMsg"  , uePage1.getLastError());
			}else {
				output.put("errorCode" , "0000");
				output.put("errorMsg"  , "");
			}
		} catch(Exception e) {
			output.put("errorCode" , "0004");
			output.put("errorMsg"  , e.getMessage());
		} finally {
			discon();
		}
		
		if(decryptYN) { //SEED 복호화를 수행한다.
			boolean decrypteYN = edocDecryptFile(savePath , saveFile);
			log.debug("EdocXvarmUtil decrypteYN :  " + decrypteYN);
		}
	
		return output;
	}
	
	/**
	 * 1건의 문서를 삭제 처리 한다.
	 * @param elementid
	 */
	@ComponentOperation(name = "1건의 문서를 삭제 처리 한다.")
	public Map<String,String> delete(String elementid){
		Map<String,String> output = new HashMap<String,String>();
		asysUsrElement uePage1 = new asysUsrElement(con);
		uePage1.m_elementId = elementid;
		try {
			if ( (uePage1.delete()) != 0 ) {
				output.put("errorCode" , "0005");
				output.put("errorMsg"  , uePage1.getLastError());
			}else {
				output.put("errorCode" , "0000");
				output.put("errorMsg"  , "");
			}
		} catch (Exception e) {
			output.put("errorCode" , "0006");
			output.put("errorMsg"  , e.getMessage());
		} finally {
			discon();
		}
		return output;
	}
	
	/**
	 * 1건의 문서를 교체 한다. (암호화 X)
	 * @param elementid
	 */
	@ComponentOperation(name = "1건의 문서를 교체 한다. (암호화 X)")
	public Map<String,String> replace(File file , String elementid) { 
		Map<String,String> output = replace( file , elementid , false);
		return output;
	}
	
	/**
	 * 1건의 문서를 교체 한다. (암호화 O)
	 * @param elementid
	 */
	@ComponentOperation(name = "1건의 문서를 교체 한다. (암호화 O)")
	public Map<String,String> replace(File file , String elementid, boolean encryptYN) { 
		Map<String,String> output = new HashMap<String,String>();
		Map<String,String> encData = new HashMap<String,String>();
		String filePath = "";
		if(encryptYN) {
			encData = edocEncryptFile(file);
			log.debug("EdocXvarmUtil replace encData : {}" + encData);
			filePath = encData.get("encPath") + encData.get("encName");
		}else {
			filePath = file.getAbsolutePath();
		}
		asysUsrElement uePage1 = new asysUsrElement(con);
		uePage1.m_elementId = elementid;
		log.debug("EdocXvarmUtil replace elementid : [" + elementid + "]  ||  replacePath : [" + filePath + "]");
		int ret = uePage1.replaceContent(filePath, "", "");
		log.debug("EdocXvarmUtil replace Ret : " + ret);
		
		try {
			if ( ret != 0 ) {
				output.put("errorCode" , "0007");
				output.put("errorMsg"  , uePage1.getLastError());
			}else {
				output.put("errorCode" , "0000");
				output.put("errorMsg"  , "");
			}
		} catch(Exception e) {
			output.put("errorCode" , "0008");
			output.put("errorMsg"  , e.getMessage());
		} finally {
			discon();
		}
		
		return output;
	}
	
	/**
	 * 다건의 문서를 다운로드한다. ( Create에서 생성한 문서 그대로 리턴 )
	 * @param elementid ( 문서ID EX)XVARM_MAIN::2020032010181900::IMAGE )
	 * @param savePath ( 다운로드될 위치 지정 )
	 */
	@ComponentOperation(name = "다건의 문서를 다운로드한다. ( Create에서 생성한 문서 그대로 리턴 )")
	public Map<String,String> multDownload(String savePath , List<Map<String,Object>> downFileList){
		Map<String,String> output = multDownload(savePath, downFileList, false);
		return output;
	}
	
	/**
	 * 다건의 문서를 다운로드하고 , 문서 복호화를 진행한다.(SEED 복호화)
	 * @param elementid ( 문서ID EX)XVARM_MAIN::2020032010181900::IMAGE )
	 * @param savePath ( 다운로드될 위치 지정 )
	 */
	@ComponentOperation(name = "다건의 문서를 다운로드하고 , 문서 복호화를 진행한다.(SEED 복호화)")
	public Map<String,String> multDownload(String savePath , List<Map<String,Object>> downFileList,  boolean decryptYN){
		//savePath 경로의 폴더를 체크하고 , 없으면 생성해야한다.
		FileUtils.makeDir(savePath);
		log.debug("EdocXvarmUtil multDownload savePath : " + savePath);
		Map<String,String> output = new HashMap<String,String>();
		
		asysUsrElement uePage1 = null;
		int docDownCount = downFileList.size();
		String errMsg = "";
		if(downFileList.size() > 0) {
			try {
				for(int i = 0; i < downFileList.size(); i++) {
					Map<String, Object> temp = downFileList.get(i);
					log.debug("EdocXvarmUtil multDownload elementid: " + temp.get("elementid") +  "  ||  saveFile : " + temp.get("saveFile"));
					uePage1 = new asysUsrElement(con);
					uePage1.m_elementId = (String) temp.get("elementid");
					uePage1.m_cClassId = "EDOC_CC";
					
					int ret = uePage1.getContent(savePath + temp.get("saveFile").toString(), "", "");
					log.debug("EdocXvarmUtil multDownload ret :  " + ret);
					
					if ( ret == 0 ) {
						docDownCount--;
						boolean decrypteYN = edocDecryptFile(savePath , temp.get("saveFile").toString());
						log.debug("EdocXvarmUtil multDownload decrypteYN :  " + decrypteYN);
					} else {
						errMsg = uePage1.getLastError();
					}
				}
				
				if(docDownCount == 0) {
					output.put("errorCode" , "0000");
					output.put("errorMsg"  , "");
				} else {
					output.put("errorCode" , "0003");
					output.put("errorMsg"  , errMsg);
				}
			} catch (Exception e) {
				output.put("errorCode" , "0004");
				output.put("errorMsg"  , e.getMessage());
			} finally {
				discon();
			}
		} else {
			output.put("errorCode" , "0004");
			output.put("errorMsg"  , "XVarm Mult Down Error");
		}
	
		return output;
	}
	
	/**
	 * 전달받은 전자문서 암호화 처리(로직정리 나중에하자)
	 * @param file
	 * @return
	 */
	@ComponentOperation(name = "전달받은 전자문서 암호화 처리")
	private Map<String,String> edocEncryptFile(File file) {
		Map<String,String> output = new HashMap<String,String>();
		if(file.exists()) {
			String filePath = file.getParent() + File.separator;
			String fileName = file.getName();
			int pdfIndex = fileName.indexOf(".pdf");
			int tifIndex = fileName.indexOf(".tif");
			
			if(pdfIndex > 0) {
				String encFileName = fileName.substring(0 , pdfIndex) + "_enc.pdf";
				log.debug("EdocXvarmUtil edocEncryptFile filePath : " + filePath);
				log.debug("EdocXvarmUtil edocEncryptFile encFileName : " + encFileName);
				output = EdocUtils.enCryptionFile(filePath, filePath, fileName , encFileName);
			}else if(tifIndex > 0) {
				String encFileName = fileName.substring(0 , pdfIndex) + "_enc.tif";
				output = EdocUtils.enCryptionFile(filePath, filePath, fileName , encFileName);
			}else {
				log.debug("##EdocXvarmUtil edocEncryptFile 파일은 있는데 , 파일명이 제대로 되지 않았다.");
			}
		}
		return output;
		
	}
	
	/**
	 * 전달받은 전자문서 암호화 처리(로직정리 나중에하자)
	 * @param file
	 * @return
	 */
	@ComponentOperation(name = "전달받은 전자문서 암호화 처리")
	private Map<String,String> edocEncryptFile(File file , String fileExtention) {
		
		Map<String,String> output = new HashMap<String,String>();
		
		if(file.exists()) {
			String filePath = file.getParent() + File.separator;
			String fileName = file.getName();
			
			int fileExtentionIndex = fileName.indexOf("." + fileExtention);
			
			log.debug("EdocXvarmUtil edocEncryptFile fileExtentionIndex : " + fileExtentionIndex + "  ||  fileExtention : [" + fileExtention + "]");
			
			if(fileExtentionIndex > 0) {
				String encFileName = fileName.substring(0 , fileExtentionIndex) + "_enc." + fileExtention;
				log.debug("EdocXvarmUtil edocEncryptFile filePath : " + filePath);
				log.debug("EdocXvarmUtil edocEncryptFile fileName : " + fileName);
				log.debug("EdocXvarmUtil edocEncryptFile encFileName : " + encFileName);
				output = EdocUtils.enCryptionFile(filePath, filePath, fileName , encFileName);
			}else {
				log.debug("EdocXvarmUtil edocEncryptFile 파일은 있는데 , 파일명이 제대로 되지 않았다.");
			}
		}
		return output;
		
	}
	
	/**
	 * Xvarm에서 다운로드한 문서 복호화 진행( 해당 클래스 내부에서사용)
	 * @param file
	 * @return
	 */
	@ComponentOperation(name = "Xvarm에서 다운로드한 문서 복호화 진행")
	private boolean edocDecryptFile(String filePath , String filename) {
		boolean isDecryptYn = false;
		File file = new File(filePath + filename);
		if(file.exists()) {
			int pdfIndex = filename.indexOf("_enc.pdf");
			int tifIndex   = filename.indexOf("_enc.tif");
			if(pdfIndex > 0) {
				String decFileName = filename.substring(0 , pdfIndex) + ".pdf";
				log.debug("EdocXvarmUtil edocDecryptFile filePath :[" + filePath +"] || filename :[" + filename +"] || decFileName :[" + decFileName + "]");
				isDecryptYn = EdocUtils.deCryptionFile(filePath, filePath, filename , decFileName);
			}else if(tifIndex > 0) {
				String decFileName = filename.substring(0 , tifIndex) + ".tif";
				isDecryptYn = EdocUtils.deCryptionFile(filePath, filePath, filename , decFileName);
			} else {
				log.debug("EdocXvarmUtil edocDecryptFile 파일은 있는데 , 파일명이 제대로 되지 않았다.");
			}
		}else {
			log.debug("EdocXvarmUtil edocDecryptFile filePath 파일이 존재하지 않음.");
		}
		return isDecryptYn;
	}
	
	/**
	 * 현재 생성된 Connection 가져오기.
	 * @return
	 */
	@ComponentOperation(name = "현재 생성된 Connection 가져오기")
	public asysConnectData getXvarmEngineConnector() {
		return con;
	}
	
	/**
	 * Create , Replace , Select , Delete 수행후 반드시 Connect을 끊어줘야함.
	 */
	@ComponentOperation(name = "Connection 끊기")
	public void discon(){
		log.debug("###################EdocXvarmUtil discon Start ############################");
		if(con != null){
			con.close();
			con = null;
			log.debug("###################EdocXvarmUtil discon Success ############################");
		}
		log.debug("###################EdocXvarmUtil discon End ############################");
	}
}
