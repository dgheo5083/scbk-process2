package com.scbank.process.api.fw.base.integration.constant;

public class IntegrationConstant {

    public static final String SYSTEM_ID_HOST = "host";
    public static final String SYSTEM_ID_FEP = "fep";
    public static final String SYSTEM_ID_MCI = "mci";
    public static final String SYSTEM_ID_EDMI = "edmi";

    public static final String HOST_ENCODING = "cp933";
    public static final String FEP_ENCODING = "euc-kr";
    public static final String EDMI_ENCODING = "euc-kr";

    public static final String ERRCODE_HOST_TIMEOUT = "9003"; /* 9003 */
    public static final String ERRCODE_HOST_FAIL = "9002"; /* 9002 */
    public static final String ERRCODE_HOST_CAPABEND = "9001"; /* 9001 */
    public static final String ERRCODE_HOST_DFSMESSAGE = "H004"; /* H004 */
    public static final String ERRCODE_HOST_CAPABEND_RD = "9001"; /* 9001 */
    public static final String ERRCODE_HOST_ECAP = "9005"; /* 9005 */
    public static final String ERRCODE_HOST_CAPABEND_A = "9001";
    public static final String ERRCODE_EDMI_ETC = "ESVR";

    public static final String ERRMESG_HOST_TIMEOUT = "TIMEOUT";
    public static final String ERRMESG_HOST_TIMEOUT1 = "HOST 응답 timeout 이 발생 하였습니다.";
    public static final String ERRMESG_HOST_TIMEOUT2 = "이체거래인경우 이체결과를 확인하신후 다시 시도하여 주시기바랍니다.";

    public static final String ERRMESG_HOST_ECAP = "CAP_ERR";
    public static final String ERRMESG_HOST_ECAP1 = "시스템오류입니다.";
    public static final String ERRMESG_HOST_ECAP2 = "이체거래인경우 이체결과를 확인하신후 다시 시도하여 주시기바랍니다.";

    public static final String ERRMESG_HOST_FAIL = "IMSCONNE";
    public static final String ERRMESG_HOST_FAIL1 = "죄송합니다.";
    public static final String ERRMESG_HOST_FAIL2 = "시스템 준비중으로 정상적인 서비스를 제공해 드리지 못하고 있습니다.";
    public static final String ERRMESG_HOST_FAIL3 = "잠시후, 다시 시도해 주십시오.";

    public static final String ERRMESG_HOST_CAPABEND = "CAPABEND";
    public static final String ERRMESG_HOST_CAPABEND1 = "죄송합니다.";
    public static final String ERRMESG_HOST_CAPABEND2 = "시스템 준비중으로 정상적인 서비스를 제공해  드리지 못하고 있습니다.";
    public static final String ERRMESG_HOST_CAPABEND3 = "잠시후, 다시 시도해 주십시오.";

    public static final String ERRMESG_HOST_CAPABEND_RD = "CAPABEND";
    public static final String ERRMESG_HOST_CAPABEND_RD1 = "죄송합니다.";
    public static final String ERRMESG_HOST_CAPABEND_RD2 = "시스템 준비중으로 정상적인 서비스를 제공해  드리지 못하고 있습니다.";
    public static final String ERRMESG_HOST_CAPABEND_RD3 = "잠시후, 다시 시도해 주십시오.";

    public static final String ERRMESG_HOST_DFSMESSAGE = "HOST로부터 DFS MESSAGE를 수신했습니다.";

    public static final String ERRMESG_HOST_CAPABEND_A = "CAPABEND_A";
    public static final String ERRMESG_HOST_CAPABEND_A1 = "시스템오류입니다.";
    public static final String ERRMESG_HOST_CAPABEND_A2 = "이체거래인경우 이체결과를 확인하신후 다시 시도하여 주시기바랍니다.";

    public static final String ERRMESG_EDMI_ETC = "EDMIETC";
    public static final String ERRMESG_EDMI_ETC1 = "시스템오류입니다.";
    public static final String ERRMESG_EDMI_ETC2 = "이체거래인경우 이체결과를 확인하신후 다시 시도하여 주시기바랍니다.";

    public static final String PROPS_MCI_MAX_LOOP_CNT = "csl.integration.system.mci.rebound-strategy-policy.max-loop-cnt";
    public static final String PROPS_MCI_LIST_FIELD_NAME = "csl.integration.system.mci.rebound-strategy-policy.default-list-field-name";

    public static final int DEFAULT_MCI_MAX_LOOP_CNT = 10;
    public static final String DEFAULT_MCI_LIST_FIELD_NAME = "FO_PAY";

    public static final String PROPS_HOST_MAX_LOOP_CNT = "csl.integration.system.host.rebound-strategy-policy.max-loop-cnt";
    public static final String PROPS_HOST_LIST_FIELD_NAME = "csl.integration.system.host.rebound-strategy-policy.default-list-field-name";

    public static final int DEFAULT_HOST_MAX_LOOP_CNT = 10;
    public static final String DEFAULT_HOST_LIST_FIELD_NAME = "FO_PAY";

    /**
     * IB VAN_TYPE
     */
    public static String EN_IB_VAN_TYPE = "58";
    
    /**
     * KR IB VAN_TYPE
     */
    public static String KR_IB_VAN_TYPE = "58";
    
    /**
     * 스마트폰뱅킹 VAN_TYPE
     */
    //public static String KR_SMB_VAN_TYPE = "47";
    public static String KR_SMB_VAN_TYPE = "57";
    
    /**
     * 스마트폰뱅킹 VAN_TYPE
     */
    //public static String EN_SMB_VAN_TYPE = "47";
    public static String EN_SMB_VAN_TYPE = "57";
    
    /**
     * self banking VAN_TYPE
     */
    public static String SELF_VAN_TYPE = "56";

    /**
     * 스마트폰뱅킹 CIB VAN_TYPE
     */
    public static String KR_CIB_VAN_TYPE = "67";
    /**
     * 스마트폰뱅킹 CIB VAN_TYPE
     */
    public static String EN_CIB_VAN_TYPE = "67";

    /**
     * BCP IB VAN_TYPE
     */
    public static String KR_IB_BCP_VAN_TYPE = "28";
    /**
     * BCP IB VAN_TYPE
     */
    public static String EN_IB_BCP_VAN_TYPE = "29";

    /**
     * BCP 스마트폰뱅킹 VAN_TYPE
     */
    public static String KR_SMB_BCP_VAN_TYPE = "48";
    /**
     * BCP 스마트폰뱅킹 VAN_TYPE
     */
    public static String EN_SMB_BCP_VAN_TYPE = "48";

    /**
     * BCP 스마트폰뱅킹 VAN_TYPE
     */
    public static String KR_CIB_BCP_VAN_TYPE = "68";
    /**
     * BCP 스마트폰뱅킹 VAN_TYPE
     */
    public static String EN_CIB_BCP_VAN_TYPE = "68";

}
