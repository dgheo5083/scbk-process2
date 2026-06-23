package com.scbank.process.api.svc.common.service.functions;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.session.ISessionContextManager;
import com.scbank.process.api.svc.common.components.ComfyBankComponent;
import com.scbank.process.api.svc.common.components.dto.GetComfyBankRequest;
import com.scbank.process.api.svc.common.components.dto.GetComfyBankResponse;
import com.scbank.process.api.svc.common.components.dto.RegisterComfyBankRequest;
import com.scbank.process.api.svc.common.components.dto.UpdateComfyBankRequest;
import com.scbank.process.api.svc.common.service.functions.dto.comfyBank.FncCfbRegisterComfyBankRequest;
import com.scbank.process.api.svc.common.service.functions.dto.comfyBank.FncCfbRegisterComfyBankResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@ServiceComponent(name = "공통기능 - ", url = "/functions/comfyBank")
public class FunctionsComfyBankService {

	private final ComfyBankComponent comfybankComponent;
	
	/*
     * 세션 컨텍스트 매니저
     */
    private final ISessionContextManager sessionManager;
	
	@ServiceEndpoint(url = "/registerComfyBank", name = "편한뱅킹 정보 적재 및 수정 (asis : MA3MSCMNG004_101S)")
    public void registerComfyBank(IServiceContext serviceContext, FncCfbRegisterComfyBankRequest request) {
		
		
		String userId = StringUtils.defaultIfEmpty(sessionManager.getLoginValue("UserID", String.class),"");
//		String originSessionSeniorMode = StringUtils.defaultIfEmpty(sessionManager.getGlobalValue("seniorMode", String.class), "");
		String dbChkYN = StringUtils.defaultIfEmpty(request.getDbChkYN(), "N");
		boolean isLoginMenu = "LGNADV000".equals(StringUtils.nvl(request.getMenuId(), ""));
		
		String seniorMode = StringUtils.nvl(request.getSeniorMode(), ""); // Y:고령자모드, N:일반모드
		sessionManager.setGlobalValue("seniorMode", seniorMode);
		
		// 로그인성 업무로 UserID가 필수 && 상태가 Y or N 인 사람만 대상 (아예이용안한 고객은 "" 이므로 DB적재하지않음)
		if(!"".equals(userId) && !"test01".equals(userId)  && seniorMode.matches("Y|N")) {
			try {
				GetComfyBankRequest getComfyBankRequest = new GetComfyBankRequest();
				getComfyBankRequest.setUserId(userId);
				
				GetComfyBankResponse getComfyBankResponse = comfybankComponent.getComfyBank(getComfyBankRequest);
				
				RegisterComfyBankRequest registerComfyBankRequest = new RegisterComfyBankRequest();
				UpdateComfyBankRequest updateComfyBankRequest = new UpdateComfyBankRequest(); 
				if(isLoginMenu) {
					//로그인시점 DB 적재여부 체크하여 없는 경우 insert
					if(getComfyBankResponse == null) {
						registerComfyBankRequest.setUserId(userId);
						registerComfyBankRequest.setOrgSeniorYn(seniorMode);
						log.debug("##### registerComfyBank insert : [{}]", registerComfyBankRequest);
						comfybankComponent.registerComfyBank(registerComfyBankRequest);
					}
				}else if("Y".equals(dbChkYN)) {
					//DB 변경로직
					if(getComfyBankResponse == null) {
						//로그인시 적재된 내역이 없는 경우 insert
						registerComfyBankRequest.setUserId(userId);
						registerComfyBankRequest.setChnSeniorYn(seniorMode);
						
						if("Y".equals(seniorMode)) {
							registerComfyBankRequest.setOthr1Chk("Y");
						}
						
						log.debug("##### registerComfyBank insert : [{}]", registerComfyBankRequest);
						comfybankComponent.registerComfyBank(registerComfyBankRequest);
					} else {
						//로그인시 적재된 내역이 있는 경우 업데이트
						String questRlpy1 = getComfyBankResponse.getQuestRply1() != null ? (String)getComfyBankResponse.getQuestRply1() : "";
						String othrInfo1 = getComfyBankResponse.getOthrInfo1() != null ? (String)getComfyBankResponse.getOthrInfo1() : "";
						
						updateComfyBankRequest.setUserId(userId);
						
						if("Y".equals(seniorMode) && !"Y".equals(questRlpy1) && "".equals(othrInfo1)) {
							//최초 DB 적재시 Y가 아닌사람이 처음 편한뱅킹 킨 날짜를 추가로 적재하자 
							updateComfyBankRequest.setOthr1Chk("Y");
						}
						updateComfyBankRequest.setChnSeniorYn(seniorMode);
						
						log.debug("##### updateComfyBankRequest update : [{}]", updateComfyBankRequest);
						comfybankComponent.updateComfyBank(updateComfyBankRequest);
					}
				}
				
			}catch (Exception e) {
				log.debug("#### registerComfyBank ERROR START");
				log.error("Exception : [{}]", e.getMessage());
				log.debug("#### registerComfyBank ERROR END");
			}
			
		}
	}	
}
