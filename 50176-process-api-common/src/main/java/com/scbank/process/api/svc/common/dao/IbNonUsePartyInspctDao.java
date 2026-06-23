
package com.scbank.process.api.svc.common.dao;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.ComfyBankParameter;
import com.scbank.process.api.svc.common.dao.dto.ComfyBankResult;
import com.scbank.process.api.fw.core.component.ComponentOperation;

@DaoComponent(database = "kfbdb", author = "2037688", description = "ib 미사용 고객 대상 설문조사")
public interface IbNonUsePartyInspctDao {

    @ComponentOperation(name = "편한뱅킹 적재여부확인", description = "편한뱅킹 적재여부확인", author = "2037688")
    ComfyBankResult selectComfyBank(ComfyBankParameter input);

    @ComponentOperation(name = "편한뱅킹 적재", description = "편한뱅킹 적재", author = "2037688")
    int insertComfyBank(ComfyBankParameter input);
    
    @ComponentOperation(name = "편한뱅킹 업데이트", description = "편한뱅킹 업데이트", author = "2037688")
    int updateComfyBank(ComfyBankParameter input);
}
