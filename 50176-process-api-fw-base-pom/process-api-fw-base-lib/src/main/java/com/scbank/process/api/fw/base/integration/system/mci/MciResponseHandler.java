package com.scbank.process.api.fw.base.integration.system.mci;

import com.scbank.process.api.fw.base.integration.constant.IntegrationConstant;
import com.scbank.process.api.fw.base.integration.system.mci.exception.MciSystemException;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciError;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.AdtnMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.MsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciMsgInfo.NewErrMsgInfo;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciReqHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciResHeader;
import com.scbank.process.api.fw.base.integration.system.mci.vo.MciSystemHeader;
import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.integration.context.IntegrationContext;
import com.scbank.process.api.fw.integration.response.IntegrationResponseHandler;

/**
 * @author sungdon.choi
 * @version 1.0
 * @since 25. 4. 21.
 */
public class MciResponseHandler implements IntegrationResponseHandler<MciReqHeader, MciResHeader, MciError> {

    @SuppressWarnings("unused")
    @Override
    public void checkErrorAndThrowable(IntegrationContext context, MciReqHeader requestHeader,
            MciResHeader responseHeader, MciError error) {
        if (!this.isError(responseHeader)) {
            return;
        }

        MciSystemHeader mciSystemHeader = responseHeader.getMciSystemHeader();
        MciMsgInfo mciMsgInfo = responseHeader.getMciMsgInfo();

        String procRsltDvcd = this.getProcRsltDvcd(mciSystemHeader);
        String errorCode = this.getResponseCode(responseHeader);
        String resRsltMsg = "";
        String resRsltEtcMsg = "";
    	String errorLocation = IntegrationConstant.SYSTEM_ID_MCI;
    	
    	if ("1".equals(procRsltDvcd)) {
    		MsgInfo msgInfo = this.getMsgInfo(responseHeader);
    		NewErrMsgInfo newErrMsgInfo = this.getNewErrMsgInfo(responseHeader);
    		String newErrMsgCd = newErrMsgInfo != null ? newErrMsgInfo.getNewErrMsgCd() : StringUtils.EMPTY;
    		String newErrMsgCtt = newErrMsgInfo != null ? newErrMsgInfo.getNewErrMsgCtt() : StringUtils.EMPTY;
    		
    		if (StringUtils.hasLength(newErrMsgCd)) {
    			resRsltMsg = msgInfo.getMsgCtt() + "[(" + newErrMsgCd + ")" + newErrMsgCtt + "]";
    		} else {
    			resRsltMsg = msgInfo.getMsgCtt();
    		}
    	} else if ("8".equals(procRsltDvcd) || "9".equals(procRsltDvcd)) {
    		AdtnMsgInfo adtnMsgInfo = this.getAdtnMsgInfo(responseHeader);
    		resRsltMsg = adtnMsgInfo.getAdtnMsg();
    	}

    	MciSystemException mciSystemException = new MciSystemException(responseHeader, errorCode, resRsltMsg);
        throw mciSystemException;
    }

    @Override
    public boolean isError(MciResHeader header) {
    	if (header == null) {
    		return true;
    	}
        return !"0".equals(this.getProcRsltDvcd(header.getMciSystemHeader()));
    }

    @Override
    public String getResponseCode(MciResHeader header) {
    	if (header == null) {
    		return StringUtils.EMPTY;
    	}
    	
    	MciSystemHeader systemHeader = header.getMciSystemHeader();
    	MciMsgInfo mciMsgInfo = header.getMciMsgInfo();
    	String procRsltDvcd = getProcRsltDvcd(systemHeader);
    	String errorCode = "";
    	if ("1".equals(procRsltDvcd)) {
    		MsgInfo msgInfo = mciMsgInfo.getMsgInfo();
    		errorCode = StringUtils.defaultIfEmpty(msgInfo.getMsgCd(), StringUtils.EMPTY);
    	} else if ("8".equals(procRsltDvcd) || "9".equals(procRsltDvcd)) {
    		AdtnMsgInfo adtnMsgInfo =  mciMsgInfo.getAdtnMsgInfo();
    		errorCode = StringUtils.defaultIfEmpty(adtnMsgInfo.getAdtnMsg(), StringUtils.EMPTY);
    	}
        return errorCode;
    }
    
    private MsgInfo getMsgInfo(MciResHeader header) {
    	MciMsgInfo mciMsgInfo = header.getMciMsgInfo();
    	return mciMsgInfo.getMsgInfo();
    }
    
    private AdtnMsgInfo getAdtnMsgInfo(MciResHeader header) {
    	MciMsgInfo mciMsgInfo = header.getMciMsgInfo();
    	return mciMsgInfo.getAdtnMsgInfo();
    }
    
    private NewErrMsgInfo getNewErrMsgInfo(MciResHeader header) {
    	MciMsgInfo mciMsgInfo = header.getMciMsgInfo();
    	return mciMsgInfo.getNewErrMsgInfo();
    }

    private String getProcRsltDvcd(MciSystemHeader mciSystemHeader) {
    	if (mciSystemHeader == null) {
    		return StringUtils.EMPTY;
    	}
        return StringUtils.defaultIfEmpty(mciSystemHeader.getProcRsltDvcd(), "").trim();
    }
}
