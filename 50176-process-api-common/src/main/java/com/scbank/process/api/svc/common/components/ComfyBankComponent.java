package com.scbank.process.api.svc.common.components;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.common.components.dto.GetComfyBankRequest;
import com.scbank.process.api.svc.common.components.dto.GetComfyBankResponse;
import com.scbank.process.api.svc.common.components.dto.RegisterComfyBankRequest;
import com.scbank.process.api.svc.common.components.dto.UpdateComfyBankRequest;
import com.scbank.process.api.svc.common.dao.IbNonUsePartyInspctDao;
import com.scbank.process.api.svc.common.dao.dto.ComfyBankParameter;
import com.scbank.process.api.svc.common.dao.dto.ComfyBankResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "편한뱅킹 컴포넌트")
public class ComfyBankComponent {

    private final IbNonUsePartyInspctDao ibNonUsePartyInspctDao;

    /**
     * 편한뱅킹 적재여부확인
     *
     * @param selectComfyBank
     * @return
     * @throws Exception
     * @description MA3MSCMNG004_101S
     */
    @ComponentOperation(name = "편한뱅킹 적재여부확인 [ASIS:MA3MSCMNG004_101S]", author = "권태민")
    public GetComfyBankResponse getComfyBank(GetComfyBankRequest request) {
    	GetComfyBankResponse response = new GetComfyBankResponse();
    	ComfyBankParameter parameter = new ComfyBankParameter();
        parameter.setUserId(request.getUserId());

        ComfyBankResult result = ibNonUsePartyInspctDao.selectComfyBank(parameter);
        response.setSsnOrTaxid(result.getSsnOrTaxid());
        response.setUsrNo(result.getUsrNo());
        response.setPartyNm(result.getPartyNm());
        response.setRegistDt(result.getRegistDt());
        response.setRegistTm(result.getRegistTm());
        response.setQuestRply1(result.getQuestRply1());
        response.setQuestRply2(result.getQuestRply2());
        response.setQuestRply3(result.getQuestRply3());
        response.setQuestRply4(result.getQuestRply4());
        response.setQuestRply5(result.getQuestRply5());
        response.setOthrInfo1(result.getOthrInfo1());
        response.setOthrInfo2(result.getOthrInfo2());
        response.setEmailAddr(result.getEmailAddr());
        response.setCntctPlce(result.getCntctPlce());
        
        return response;
    }
    
    /**
     * 편한뱅킹 적재
     *
     * @param registerComfyBank
     * @return
     * @throws Exception
     * @description MA3MSCMNG004_101S
     */
    @ComponentOperation(name = "편한뱅킹 적재 [ASIS:MA3MSCMNG004_101S]", author = "권태민")
    public int registerComfyBank(RegisterComfyBankRequest request) {
    	ComfyBankParameter parameter = new ComfyBankParameter();
    	parameter.setUserId(request.getUserId());
    	parameter.setOrgSeniorYn(request.getOrgSeniorYn());
    	parameter.setChnSeniorYn(request.getChnSeniorYn());
    	parameter.setOthr1Chk(request.getOthr1Chk());
    	
    	int result = ibNonUsePartyInspctDao.insertComfyBank(parameter);
    	
    	return result;
    }
    
    
    /**
     * 편한뱅킹 업데이트
     *
     * @param updateComfyBank
     * @return
     * @throws Exception
     * @description MA3MSCMNG004_101S
     */
    @ComponentOperation(name = "편한뱅킹 업데이트 [ASIS:MA3MSCMNG004_101S]", author = "권태민")
    public int updateComfyBank(UpdateComfyBankRequest request) {
    	ComfyBankParameter parameter = new ComfyBankParameter();
    	parameter.setUserId(request.getUserId());
    	parameter.setOrgSeniorYn(request.getOrgSeniorYn());
    	parameter.setChnSeniorYn(request.getChnSeniorYn());
    	parameter.setOthr1Chk(request.getOthr1Chk());
    	
    	int result = ibNonUsePartyInspctDao.updateComfyBank(parameter);
    	
    	return result;
    }
    

}
