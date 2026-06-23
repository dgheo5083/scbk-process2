package com.scbank.process.api.fw.base.dao.errorcode;

import java.util.List;

import com.scbank.process.api.fw.base.dao.errorcode.dto.ScbkChnnlErrMsgInDto;
import com.scbank.process.api.fw.base.dao.errorcode.dto.ScbkChnnlErrMsgOutDto;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;

@DaoComponent(id = "scbkChnnlErrMgtDao", database = "kfbdb", description = "채널 에러메시지 조회 Dao 컴포넌트", author = "sungdon.choi")
public interface ScbkChnnlErrMgtDao {

    /**
     * 채널 에러메시지 정보를 가져온다.
     * 
     * @param input ScbkChnnlErrMsgInDto
     * @return 채널 에러메시지 정보 목록
     */
    @ComponentOperation(name = "채널 에러메시지 정보 목록 조회", author = "sungdon.choi")
    List<ScbkChnnlErrMsgOutDto> selectChnnlErrMgtList(ScbkChnnlErrMsgInDto input);

    /**
     * 
     * @param input ScbkChnnlErrMsgInDto
     */
    @ComponentOperation(name = "채널 에러메시지 정보 단건", author = "sungdon.choi")
    ScbkChnnlErrMsgOutDto selectChnnlErrMgt(ScbkChnnlErrMsgInDto input);
}
