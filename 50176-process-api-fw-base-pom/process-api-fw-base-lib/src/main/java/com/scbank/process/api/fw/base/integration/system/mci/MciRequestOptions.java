package com.scbank.process.api.fw.base.integration.system.mci;

import com.scbank.process.api.fw.base.integration.system.mci.vo.MciContTran;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciSystemHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciTranCommHeader;
import com.scbank.process.api.fw.integration.cfg.AbstractIntegrationRequestOptions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author sungdon.choi
 */
public final class MciRequestOptions extends AbstractIntegrationRequestOptions {

	/**
	 * AS-IS BUSINESS_FUNCTION_ID
	 */
	@Getter
    @Setter
	private String tranCd;
	
	@Getter
    @Setter
	private String tmsgCreSysNm;
	
	@Getter
    @Setter
	private String blngBrNo;
	
	@Getter
    @Setter
	private String txnBrNo;
	
	@Getter
    @Setter
	private String empNo;
	
	@Getter
    @Setter
	private String chnlTypCd;
	
    @Getter
    @Setter
    private MciSystemHeader mciSystemHeader;

    @Getter
    @Setter
    private MciTranCommHeader mciTranCommHeader;

    @Getter
    @Setter
    private MciContTran mciContTran;
}
