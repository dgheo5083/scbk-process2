package com.scbank.process.api.fw.base.shared;

import org.springframework.cache.annotation.Cacheable;

import com.scbank.process.api.fw.base.dao.errorcode.ScbkChnnlErrMgtDao;
import com.scbank.process.api.fw.base.dao.errorcode.dto.ScbkChnnlErrMsgInDto;
import com.scbank.process.api.fw.base.dao.errorcode.dto.ScbkChnnlErrMsgOutDto;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@SharedComponent(name = "제일은행 채널 에러코드 정보 조회 공통 컴포넌트", description = "제일은행 채널 에러코드 조회 공통 컴포넌트", author = "sungdon.choi")
public class ScbkChnnlErrMgtComponent {

    private final ScbkChnnlErrMgtDao scbkChnnlErrMgtDao;

    /**
     * 채널 에러메시지 정보를 획득한다.
     * 
     * @param chnnlMkCd 채널구분
     * @param errCd     에러코드
     * @param ntnlCd    국가코드
     * @param errMode   에러모듈
     * @return
     */
    @Cacheable(value = "csl-scbk-chnnl-err-msgs", key = "{#chnnlMkCd, #errCd, #ntnlCd, #errMode}")
    @ComponentOperation(name = "채널 에러메시지 정보 조회", author = "sungdon.choi")
    public ScbkChnnlErrMsgOutDto getScbkChnnlErrMsg(String chnnlMkCd, String errCd, String ntnlCd,
            String errMode) {
        log.debug("# chnnlMkCd={}, errCd={}, ntnlCd={}, errMode={}", chnnlMkCd, errCd, ntnlCd, errMode);

        ScbkChnnlErrMsgInDto input = new ScbkChnnlErrMsgInDto();
        input.setChnnlMkCd(chnnlMkCd);
        input.setErrCd(errCd);
        input.setNtnlCd(ntnlCd);
        input.setErrMode(errMode);

        ScbkChnnlErrMsgOutDto output = this.scbkChnnlErrMgtDao.selectChnnlErrMgt(input);
        return output;
    }

    /**
     * 채널 에러메시지 문자열을 획득한다.
     * 
     * @param chnnlMkCd 채널구분
     * @param errCd     에러코드
     * @param ntnlCd    국가코드
     * @param errMode   에러모듈
     * @return
     */
    @ComponentOperation(name = "채널 에러메시지 획득", author = "sungdon.choi")
    public String getErrorMessage(String chnnlMkCd, String errCd, String ntnlCd,
            String errMode) {
        ScbkChnnlErrMsgOutDto output = this.getScbkChnnlErrMsg(chnnlMkCd, errCd, ntnlCd, errMode);

        String resolvedErrMsg = "";
        String errInfoSlctFlg = "2";

        if (output != null) {
            errInfoSlctFlg = StringUtils.defaultIfEmpty(output.getErrInfoSlctFlg(), "");
            String dbErrMsg = StringUtils.defaultIfEmpty(output.getErrMsg(), "").trim();
            if ("1".equals(errInfoSlctFlg) || !StringUtils.hasLength(dbErrMsg)) {
                resolvedErrMsg = dbErrMsg;
            }
        }
        return resolvedErrMsg;
    }
}
