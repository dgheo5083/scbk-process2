package com.scbank.process.api.svc.common.service.functions.dto.edoc;

import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.fw.message.annotation.MessageField;

import lombok.Data;

@Data
public class EdocData implements IMessageObject {

    @MessageField(id = "edocCode", name = "전자문서 코드", example = "UI_BBPL_R601")
    private String edocCode;

    // view / create
    @MessageField(id = "mappingData", name = "전자문서 매핑 데이터", example = "{\"data\":{\"CURRDATE\":\"2020.08.21\",\"CUSTNM\":\"정연수\",\"BRTHDT\":\"720907\",\"LOANSBJCT\":\"드림론 집단대출\",\"TRADEGU\":\"1\",\"LOANAMT\":\"45000000\",\"LOANBULLTNDAY\":\"Y\",\"RDMPTNDUE_GB\":\"2\",\"RDMPTNDUE_DTDD\":\"\",\"RDMPTNDUE_DTYY\":1,\"RDMPTNDUE_DTMM\":0,\"CDINT\":\"2.60\",\"CUPRYL_CHK\":\"Y\",\"LBLTYINTGRT_CHK\":\"N\",\"LBLTYINTGRT\":\"0.20\",\"SLRYTRNSFR_CHK\":\"Y\",\"SLRYTRNSFR\":\"0.20\",\"BCCARD_CHK\":\"Y\",\"BCCARD\":\"0.20\",\"PRFRNCLINTRATE1\":\"\",\"PRFRNCLINTRATE2\":\"\",\"PRFRNCLINTRATE3\":\"\",\"ADDINT1\":\"9\",\"ADDINT2\":\"10\",\"delayDAYUNIT\":\"Y\",\"EXCHANGEFEE1\":\"\",\"EXCHANGEFEE2\":\"Y\",\"LOANEXECMTHD\":\"1\",\"INTPYOFFOPTNT1\":\"2\",\"FNANCLINSTITUTNNM\":\"SC제일\",\"ACCTNO\":\"30020383045\",\"CUSTNM2\":\"정연수\",\"CNSLTNTROLE\":\"160431\",\"CNSLTNTNM\":\"\",\"LOANAMT_WON\":\"45,000,000원\",\"RDMPTNDUE_DTYY_ADD_YEAR\":\"1년\",\"PREPAY_KOR\":\"만기일시상환\",\"SOGRCODUPNA\":\"코튼클럽(주)\",\"JobPosition\":\"사업부서장\",\"SOCMSYSOAK\":\"69,280,000원\",\"SSRAPPRATE\":\"3.00%\",\"SSRBASERATE\":\"CD 91일물 유통수익률(0.69%)\",\"SLRYTRNSFR_ADD_PERCENT\":\"0.20%\",\"BCCARD_ADD_PERCENT\":\"0.20%\",\"LBLTYINTGRT_ADD_PERCENT\":\"미해당\",\"SUPERIORPARTY_ADD_PERCENT\":\"미해당\",\"PRNCPL_INTEQUAL_ADD_PERCENT\":\"미해당\",\"SLRYTRNSFR_ADD_YEAR_PERCENT\":\"연 0.20%\",\"BCCARD_ADD_YEAR_PERCENT\":\"연 0.20%\",\"LBLTYINTGRT_ADD_YEAR_PERCENT\":\"미해당\",\"SUPERIORPARTY_ADD_YEAR_PERCENT\":\"미해당\",\"PRNCPL_INTEQUAL_ADD_YEAR_PERCENT\":\"미해당\",\"RESULTRATE\":\"CD 91일물 유통수익률(0.69%) + 2.40%\",\"POPULACE\":\"미해당\",\"POPULACE_ADD_PERCENT\":\"미해당\",\"POPULACE_ADD_YEAR_PERCENT\":\"미해당\",\"ETCCUPRYL\":\"미해당\",\"ETCCUPRYL_ADD_PERCENT\":\"\",\"ETCCUPRYL_ADD_YEAR_PERCENT\":\"\",\"ETCCUPRYL_TIT\":\"\"}}") // JsonString
    private String mappingData;

    // @MessageField(id = "crfJsonData", name = "전자문서 데이터?") // ?? 대출에서??? 쓰는케이스 확인
    // final String crfJsonData;

    // append
    @MessageField(id = "fileData", name = "전자문서 이미지 파일 데이터")
    private String fileData;

    // ?
    @MessageField(id = "docCode", name = "")
    private String docCode;

    @MessageField(id = "elementId", name = "") // ELEMENT_ID
    private String elementId;

    // TODO: clip서버 요청시에만 필요
    @MessageField(id = "contractDocFlag", name = "") // 계약서류여부
    private String contractDocFlag;

    // TODO:
    @MessageField(id = "edoc_replace_yn", name = "") // 기존에 있는경우. Xvarm에 Replace 시도함.
    private String edoc_replace_yn;

    // TODO:
    @MessageField(id = "edocTsaYN", name = "") // TSA여부
    private String edocTsaYN;

    // TODO:
    @MessageField(id = "edocSignYN", name = "") // 전자서명여부
    private String edocSignYN;

}
