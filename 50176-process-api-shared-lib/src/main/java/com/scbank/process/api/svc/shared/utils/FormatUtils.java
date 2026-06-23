package com.scbank.process.api.svc.shared.utils;

import java.text.DecimalFormat;

import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class FormatUtils {

    private final DecimalFormat df = new DecimalFormat("#,###,###,###,###,##0"); // 천 단위
    private final DecimalFormat df2 = new DecimalFormat("##,##0"); // 만 단위

    private int type;

    private String temp;

    /**
     * 금액(원) 표시 (천단위 구분)
     * 
     * @param pStrMoney
     * @return
     */
    public String getFrmMoney(String pStrMoney) {
        if (pStrMoney == null)
            return "0";
        pStrMoney = pStrMoney.trim();
        if ("".equals(pStrMoney)) {
            return "0";
        }

        double doubleVal = 0;
        try {
            doubleVal = Double.parseDouble(pStrMoney);
        } catch (Exception e) {
            return pStrMoney;
        } // end try-catch
        return df.format(doubleVal);
    }

    /**
     * 금액(원) 표시 (만단위 구분)
     * 
     * @param pStrMoney
     * @return
     */
    public String getFrmWon(String pStrMoney) {
        if (pStrMoney == null)
            return "0";
        pStrMoney = pStrMoney.substring(0, pStrMoney.length() - 4);
        if ("".equals(pStrMoney)) {
            return "0";
        }

        double doubleVal = 0;
        try {
            doubleVal = Double.parseDouble(pStrMoney);
        } catch (Exception e) {
            return pStrMoney;
        } // end try-catch
        return df2.format(doubleVal);
    }

    /**
     * 계좌번호 형식으로 표시
     * 
     * @param pStrAcctNo
     * @return
     */
    public String getAcct(String pStrBnkCd, String pStrAcctNo, String pMaskYn) {
        if (pStrAcctNo == null) {
            return "";
        }
        // --------------------------------------------------------------------------------
        pStrAcctNo = pStrAcctNo.trim();
        pStrBnkCd = pStrBnkCd.trim();

        // --------------------------------------------------------------------------------
        if (pStrBnkCd.length() == 3) {
            pStrBnkCd = pStrBnkCd.substring(pStrBnkCd.length() - 2);
        }
        // --------------------------------------------------------------------------------
        if (!"23".equals(pStrBnkCd) && !"SC제일은행".equals(pStrBnkCd)) {
            int viewCount = pStrAcctNo.length() - 6;
            if (viewCount > 0) {
                pStrAcctNo = StringUtils.rpad(pStrAcctNo.substring(0, viewCount), pStrAcctNo.length(), '*');
            }
            return pStrAcctNo;
        }
        // --------------------------------------------------------------------------------
        int strLen = pStrAcctNo.length();

        if (strLen == 11 || strLen == 13) {
            // pStrAcctNo = pStrAcctNo.replaceAll("\u0020", "");
            char ch = pStrAcctNo.charAt(3);
            if (Character.isDigit(ch)) {
                // 국내계좌일 경우 xxx-xx-xxxxxx
                if (pMaskYn == "Y") {
                    // return setDelim(pStrAcctNo, "999-99-9999999");
                    return setDelim(pStrAcctNo, "999 9* ***99*");
                } else {
                    return setDelim(pStrAcctNo, "999 99 999999");
                }
            } else {
                // 외환계좌인 경우 xxx-xxx-xxxxxx (514-JSD-000468)
                if (pMaskYn == "Y") {
                    return setDelim(pStrAcctNo, "999 99 999999");
                } else {
                    return setDelim(pStrAcctNo, "999 99 999999");
                }
            }

        } else if (strLen == 13 || strLen == 16) {
            pStrAcctNo = pStrAcctNo.replaceAll(" ", "");
            if (pMaskYn == "Y") {
                return setDelim(pStrAcctNo, "999****99999*");
                // return setDelim(pStrAcctNo, "999-9999-9999-99");
            } else {
                return setDelim(pStrAcctNo, "999 9999 9999 99");
            }
        } else {
            return pStrAcctNo;
        }
    }

    /**
     * 계좌번호 형식으로 표시
     * 
     * @param pStrAcctNo
     * @return
     */
    public String getFrmAcct(String pStrAcctNo) {
        // return getFrmAcct("39", pStrAcctNo);
        return getFrmAcct("23", pStrAcctNo);
    }

    /**
     * 계좌번호 형식으로 표시
     * 
     * @param pStrBnkCd
     * @param pStrAcctNo
     * @return
     */
    public String getFrmAcct(String pStrBnkCd, String pStrAcctNo) {
        return getFrmAcct(pStrBnkCd, pStrAcctNo, "N");
    }

    /**
     * 계좌번호 형식으로 표시
     * 
     * @param pStrBnkCd
     * @param pStrAcctNo
     * @return
     */
    public String getFrmAcct(String pStrBnkCd, String pStrAcctNo, String pMaskYn) {
        if (pStrAcctNo == null) {
            return "";
        }
        // --------------------------------------------------------------------------------
        pStrAcctNo = pStrAcctNo.trim();
        pStrBnkCd = pStrBnkCd.trim();

        // --------------------------------------------------------------------------------
        if (pStrBnkCd.length() == 3) {
            pStrBnkCd = pStrBnkCd.substring(pStrBnkCd.length() - 2);
        }
        // --------------------------------------------------------------------------------
        if (!"23".equals(pStrBnkCd) && !"SC제일은행".equals(pStrBnkCd)) {
            int viewCount = pStrAcctNo.length() - 6;
            if (viewCount > 0) {
                pStrAcctNo = StringUtils.rpad(pStrAcctNo.substring(0, viewCount), pStrAcctNo.length(), '*');
            }
            return pStrAcctNo;
        }
        // --------------------------------------------------------------------------------
        int strLen = pStrAcctNo.length();

        if (strLen == 11 || strLen == 13) {
            pStrAcctNo = pStrAcctNo.replaceAll("-", "");
            char ch = pStrAcctNo.charAt(3);
            if (Character.isDigit(ch)) {
                // 국내계좌일 경우 xxx-xx-xxxxxx
                if (pMaskYn == "Y") {
                    // return setDelim(pStrAcctNo, "999-99-9999999");
                    return setDelim(pStrAcctNo, "999-9*-***99*");
                } else {
                    return setDelim(pStrAcctNo, "999-99-999999");
                }
            } else {
                // 외환계좌인 경우 xxx-xxx-xxxxxx (514-JSD-000468)
                if (pMaskYn == "Y") {
                    return setDelim(pStrAcctNo, "999-999-99999");
                } else {
                    return setDelim(pStrAcctNo, "999-999-99999");
                }
            }

        } else if (strLen == 13 || strLen == 16) {
            pStrAcctNo = pStrAcctNo.replaceAll("-", "");
            if (pMaskYn == "Y") {
                return setDelim(pStrAcctNo, "999-****-9999-9*");
                // return setDelim(pStrAcctNo, "999-9999-9999-99");
            } else {
                return setDelim(pStrAcctNo, "999-9999-9999-99");
            }
        } else {
            return pStrAcctNo;
        }
    }

    /**
     * 계좌번호 포맷팅(마스킹 및 마스킹 타입 적용..금소법으로 인한 추가)
     * 
     * @param pStrBnkCd
     * @param pStrAcctNo
     * @param pMaskYn
     * @param pMaskType
     * @return
     */
    public String getFrmAcct(String pStrBnkCd, String pStrAcctNo, String pMaskYn, String pMaskType) {
        if (pStrAcctNo == null) {
            return "";
        }
        // --------------------------------------------------------------------------------
        pStrAcctNo = pStrAcctNo.trim();
        pStrBnkCd = pStrBnkCd.trim();

        // --------------------------------------------------------------------------------
        if (pStrBnkCd.length() == 3) {
            pStrBnkCd = pStrBnkCd.substring(pStrBnkCd.length() - 2);
        }
        // --------------------------------------------------------------------------------
        if (!"23".equals(pStrBnkCd) && !"SC제일은행".equals(pStrBnkCd)) {
            int viewCount = pStrAcctNo.length() - 6;
            if (viewCount > 0) {
                pStrAcctNo = StringUtils.rpad(pStrAcctNo.substring(0, viewCount), pStrAcctNo.length(), '*');
            }
            return pStrAcctNo;
        }
        // --------------------------------------------------------------------------------
        int strLen = pStrAcctNo.length();

        if (strLen == 11 || strLen == 13) {
            pStrAcctNo = pStrAcctNo.replaceAll("-", "");
            char ch = pStrAcctNo.charAt(3);
            if (Character.isDigit(ch)) {
                // 국내계좌일 경우 xxx-xx-xxxxxx
                if (pMaskYn.equals("Y") && pMaskType.equals("")) {
                    // return setDelim(pStrAcctNo, "999-99-9999999");
                    return setDelim(pStrAcctNo, "999-9*-***99*");
                } else if (pMaskYn.equals("Y") && pMaskType.equals("1")) {
                    return setDelim(pStrAcctNo, "999-99-***999"); // 금소법프로젝트에서 사용하는 계좌 마스킹타입 추가함.
                } else {
                    return setDelim(pStrAcctNo, "999-99-999999");
                }
            } else {
                // 외환계좌인 경우 xxx-xxx-xxxxxx (514-JSD-000468)
                if (pMaskYn == "Y") {
                    return setDelim(pStrAcctNo, "999-999-99999");
                } else {
                    return setDelim(pStrAcctNo, "999-999-99999");
                }
            }

        } else if (strLen == 13 || strLen == 16) {
            pStrAcctNo = pStrAcctNo.replaceAll("-", "");
            if (pMaskYn == "Y") {
                return setDelim(pStrAcctNo, "999-****-9999-9*");
                // return setDelim(pStrAcctNo, "999-9999-9999-99");
            } else {
                return setDelim(pStrAcctNo, "999-9999-9999-99");
            }
        } else {
            return pStrAcctNo;
        }
    }

    /**
     * 계좌번호를 마스킹처리한다.
     * 
     * @param pStrGjbh 계좌번호
     * @return
     */
    public String getFrmAcct_hidden(String pStrGjbh) {
        return getFrmAcct_hidden("23", pStrGjbh);
    }

    /**
     * 계좌번호를 마스킹처리한다.
     * 
     * @param pStrGjbh  계좌번호
     * @param pStrBnkCd 은행코드
     * @return
     */
    public String getFrmAcct_hidden(String pStrBnkCd, String pStrGjbh) {
        return getFrmAcct(pStrBnkCd, pStrGjbh, "Y");
    }

    /**
     * 계좌번호를 마스킹한다.(금소법으로 인하여 마스킹 타입이 추가됨)
     * 
     * @param acctNum
     * @param maskYN
     * @param maskType
     * @return
     */
    public String getFrmAcct_hidden(String acctNum, String maskYN, String maskType) {
        return getFrmAcct("23", acctNum, maskYN, maskType);
    }

    /**
     * 문자열을 시간 형식으로 표시 (HH:MM, HH:MM:SS)
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmTime(String pStrSrc) {
        if (pStrSrc == null)
            return "";
        pStrSrc = pStrSrc.trim();
        if (pStrSrc.length() != 6 && pStrSrc.length() != 4)
            return pStrSrc;

        if (pStrSrc.length() == 6)
            return setDelim(pStrSrc, "99:99:99");
        else if (pStrSrc.length() == 4)
            return setDelim(pStrSrc, "99:99");

        return pStrSrc;
    }

    /**
     * 문자열을 날짜형식 + 시간형식 으로 표시 (YYYY-MM-DD HH:MM:SS)
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmDateTime(String pStrSrc) {
        if (pStrSrc == null)
            return "";
        pStrSrc = pStrSrc.trim();
        if (pStrSrc.length() != 14)
            return pStrSrc;
        return setDelim(pStrSrc, "9999-99-99 99:99:99");
    }

    /**
     * 문자열을 날짜형식 + 시간형식 으로 표시 (YYYY-MM)
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmDateMonth(String pStrSrc) {
        if (pStrSrc == null)
            return "";
        pStrSrc = pStrSrc.trim();
        if (pStrSrc.length() != 6)
            return pStrSrc;
        return setDelim(pStrSrc, "9999-99");
    }

    /**
     * 문자열을 세금계산서일련번호 형식으로 표시 (###-####-####)
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmTax(String pStrSrc) {
        if (pStrSrc == null)
            return "";
        pStrSrc = pStrSrc.trim();
        if (pStrSrc.length() != 11)
            return pStrSrc;
        return setDelim(pStrSrc, "999-9999-9999");
    }

    /**
     * 문자열을 대출계좌번호 형식으로 표시
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmDcgj(String pStrSrc) {
        if (pStrSrc == null)
            return "";
        pStrSrc = pStrSrc.trim();

        if (pStrSrc.length() == 15)
            return setDelim(pStrSrc, "999-99-999999-9999");
        else if (pStrSrc.length() == 13)
            return setDelim(pStrSrc, "999-9999-9999-99");
        else if (pStrSrc.length() == 12)
            return setDelim(pStrSrc, "999-99-999999-9");
        else
            return pStrSrc;
    }

    /**
     * 문자열을 외환계좌번호 형식으로 표시
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmFexAcct(String pStrSrc) {
        if (pStrSrc == null)
            return "";
        pStrSrc = pStrSrc.trim();

        if (pStrSrc.length() == 15)
            return setDelim(pStrSrc, "999-99-9999999999");
        else if (pStrSrc.length() == 13)
            return setDelim(pStrSrc, "999-9999-9999-99");
        else
            return pStrSrc;
    }

    /**
     * YYYY-MM-DD 형식.
     * str : yyyymmdd
     * separator : default .
     * 
     * @return
     */
    public String getDate(String pStrSrc, String separator) {
        if (typeof(separator) == "undefined" || separator == "") {
            separator = ".";
        }

        if (typeof(pStrSrc) == "undefined" || pStrSrc == "") {
            pStrSrc = getFrmDatedate(pStrSrc);
        }

        return setDelim(pStrSrc, "9999" + separator + "99" + separator + "99");

    }

    private String typeof(String pStrSrc) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 문자열을 날짜 형식으로 표시 (YYYYMMDD)
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmDatedate(String pStrSrc) {
        return getFrmDate(pStrSrc, "");
    }

    /**
     * 문자열을 날짜 형식으로 표시 (YYYY.MM.DD)
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmDate(String pStrSrc) {
        return getFrmDate(pStrSrc, ".");
    }

    /**
     * 문자열을 날짜 형식으로 표시 (YYYY-MM-DD)
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmmDate(String pStrSrc) {
        return getFrmDate(pStrSrc, "-");
    }

    /**
     * 문자열을 날짜 형식으로 표시 (YYYY-MM-DD)
     *
     * @param pStrSrc   기준문자열
     * @param pStrDelim 기준문자열
     * @return String
     */
    public String getFrmDate(String pStrSrc, String pStrDelim) {
        if (pStrSrc == null)
            return "";

        pStrSrc = pStrSrc.trim();

        if (pStrSrc.length() != 8)
            return pStrSrc;
        return setDelim(pStrSrc, "9999" + pStrDelim + "99" + pStrDelim + "99");
    }

//    /**
//     * 문자열을 외화 형식으로 표시
//     *
//     * @param pStrThcd  외화코드
//     * @param pStrMoney 금액문자열
//     * @param pIntPos   소수점자리위치(2이면 #.##)
//     * @return String
//     */
//    public String getDisplayNumber(String pStrThcd, String pStrMoney, String pIntPos) {
//        String strThcd = pStrThcd == null ? "" : pStrThcd.trim();
//        String strMoney = ""; // 정수
//        String intPos = ""; // 소수점
//
//        if (strThcd != null && strThcd != "") {
//            int dotIndex = strThcd.indexOf(".");
//            
//            if (dotIndex != -1) {
//            	strMoney = strThcd.substring(0, dotIndex);
//            	intPos = strThcd.substring(dotIndex);
//            	
//            	if (intPos.length() == 2) {
//            		intPos += "0";
//            	}
//            }
//        } else {
//        	strMoney = strThcd;
//            intPos = "";
//        }
//        
//        return strMoney + intPos;
//    }

    /**
     * 문자열을 외화 형식으로 표시 (123,000,000.12)
     *
     * @param pStrThcd  외화코드
     * @param pStrMoney 금액문자열
     * @param pIntPos   소수점자리위치(2이면 ####.##)
     * @return String
     */
    public String getFrmFore(String pStrMoney, int pIntPos) {
        return getFrmFore("", pStrMoney, pIntPos);
    }

    /**
     * 문자열을 외화 형식으로 표시 (123,000,000.12)
     *
     * @param pStrThcd  외화코드
     * @param pStrMoney 금액문자열
     * @param pIntPos   소수점자리위치(2이면 ####.##)
     * @return String
     */
    public String getFrmFore(String pStrThcd, String pStrMoney, int pIntPos) {
        if (pStrMoney == null)
            return "0.00";

        pStrMoney = pStrMoney.trim();

        if (pStrThcd == null)
            pStrThcd = "";
        if (!pStrThcd.equals("")) {
            if (pStrThcd.equals("JPY") || pStrThcd.equals("IDR"))
                return pStrMoney;
        }

        if (pStrMoney.equals(""))
            return "0.00";

        String pmyb = "";

        if (!"".equals(pStrMoney) && "-".equals(pStrMoney.substring(0, 1))) {
            pmyb = "-";
        }

        if (pStrMoney.length() < 6) {
            for (int i = 0; i < 6 - pStrMoney.length(); i++) {
                pStrMoney = "0" + pStrMoney;
            }
        }

        String result = "";
        String result1 = "";
        String result2 = "";

        result1 = pStrMoney.substring(0, pStrMoney.length() - pIntPos);
        result2 = pStrMoney.substring(pStrMoney.length() - pIntPos);

        for (int i = result2.length() - 1; i > 1; i--) {
            if ("0".equals(result2.substring(i))) {
                result2 = result2.substring(0, i);
            } else {
                break;
            }
        }

        if ("".equals(result2) || Integer.parseInt(result2) == 0) {
            result = getFrmMoney(result1) + ".00";
        } else {
            // frmMoney에서 0보다 큰 값의 경우는 -처리를 해주므로 0일경우에만 -를 붙여줌
            if (Double.parseDouble(StringUtils.parseString(result1).equals("") ? "0" : result1) == 0) {
                if (result1.indexOf("-") > -1)
                    result = getFrmMoney(result1) + "." + result2;
                else
                    result = pmyb + getFrmMoney(result1) + "." + result2;
            } else
                result = getFrmMoney(result1) + "." + result2;
        }

        return result;
    }

    /**
     * 문자열을 외화 형식으로 표시 : 차세대 대응 개발 (123,000,000.000)
     *
     * @param pStrThcd  외화코드
     * @param pStrMoney 금액문자열
     * @param pIntPos   소수점자리위치(3이면 ###.###)
     * @return String
     */
    public String getFrmFore2(String pStrThcd, String pStrMoney, int pIntPos) {

        String defaultMoney = "0000000000";

        if (pStrMoney == null || "".equals(pStrMoney)) {
            if (pIntPos == 0) {
                return "0";
            }
            return "0." + defaultMoney.substring(0, pIntPos);
        }

        pStrMoney = pStrMoney.trim();
        if (!pStrThcd.equals("")) {
            if ("JPY".equals(pStrThcd) || "IDR".equals(pStrThcd)) {
                return pStrMoney;
            }
        }

        if (pStrMoney.length() < 4) {
            for (int i = 0; i < 4 - pStrMoney.length(); i++) {
                pStrMoney = "0" + pStrMoney;
            }
        }
        String result = "";
        // money = Convert.ToInt32(money).ToString();
        result = pStrMoney.substring(0, pStrMoney.length() - pIntPos);
        result = getFrmMoney(result) + "." + pStrMoney.substring(pStrMoney.length() - pIntPos);

        return result;
    }

    /**
     * 문자열을 외화 형식으로 표시 (123,000,000.000)
     *
     * @param pStrMoney 금액문자열
     * @param pIntPos   소수점자리위치(3이면 ###.###)
     * @return String
     */
    public String getFrmFore2(String pStrMoney, int pIntPos) {
        return getFrmFore2("", pStrMoney, pIntPos);
    }

    /**
     * 문자열을 외화 형식으로 표시 : 기본 3자리 처리 (123,000,000.000)
     *
     * @param pStrThcd  외화코드
     * @param pStrMoney 금액문자열
     * @return String
     */
    public String getFrmFore2(String pStrThcd, String pStrMoney) {
        return getFrmFore2(pStrThcd, pStrMoney, 3);
    }

    /**
     * 문자열을 외화 형식으로 표시 : 기본 3자리 처리 (123,000,000.000)
     *
     * @param pStrMoney 금액문자열
     * @return String
     */
    public String getFrmFore2(String pStrMoney) {
        return getFrmFore2("", pStrMoney, 3);
    }

    /**
     * 문자열을 외화 형식으로 표시 : 차세대 대응 개발 (123,000,000.000)
     *
     * @param pStrThcd     외화코드
     * @param pStrMoney    금액문자열
     * @param pIntPos      소수점자리위치(3이면 ###.###)
     * @param pIntDecPoint 최종적으로 표현할 소수점 자리 ( ex 1,001.26000 이면 1,001.26 으로 표현된다.)
     * @return String
     */
    public String getFrmFore3(String pStrThcd, String pStrMoney, int pIntPos, int pIntDecPoint) {

        String defaultMoney = "0000000000";

        if (pStrMoney == null || "".equals(pStrMoney)) {
            if (pIntPos == 0) {
                return "0";
            }
            return "0." + defaultMoney.substring(0, pIntDecPoint);
        }

        pStrMoney = pStrMoney.trim();
        if (!pStrThcd.equals("")) {
            if (pStrThcd.equals("JPY") || pStrThcd.equals("IDR"))
                return pStrMoney;
        }
        if (pStrMoney.equals(""))
            return "0.000";
        if (pStrMoney.length() < 4) {
            for (int i = 0; i < 4 - pStrMoney.length(); i++) {
                pStrMoney = "0" + pStrMoney;
            }
        }

        String result = "";
        String tmp = new String();
        tmp = pStrMoney;
        if (pStrMoney.length() < pIntPos) {
            for (int i = pStrMoney.length(); i < pIntPos; i++) {
                tmp = "0" + tmp;
            }
        }
        // result = pStrMoney.substring(0, pStrMoney.length() - pIntPos);
        // result = result + "." + pStrMoney.substring(pStrMoney.length() - pIntPos);
        result = tmp.substring(0, tmp.length() - pIntPos);
        result = result + "." + tmp.substring(tmp.length() - pIntPos);

        String suffix = String.format("%0" + pIntDecPoint + "d", 0);
        DecimalFormat df = new DecimalFormat("#,##0." + suffix);
        return df.format(Double.parseDouble(result));
    }

    /**
     * 문자열을 외화 형식으로 표시 (123,000,000.000)
     *
     * @param pStrMoney 금액문자열
     * @param pIntPos   소수점자리위치(3이면 ###.###)
     * @return String
     */
    public String getFrmFore3(String pStrMoney, int pIntPos) {
        return getFrmFore3("", pStrMoney, pIntPos, pIntPos);
    }

    /**
     * 문자열을 외화 형식으로 표시 (123,000,000.000)
     *
     * @param pStrMoney 금액문자열
     * @param pIntPos   소수점자리위치(3이면 ###.###)
     * @return String
     */
    public String getFrmFore3(String pStrMoney, int pIntPos, int decimalPoint) {
        return getFrmFore3("", pStrMoney, pIntPos, decimalPoint);
    }

    /**
     * 문자열을 외화 형식으로 표시 : 기본 3자리 처리 (123,000,000.000)
     *
     * @param pStrThcd  외화코드
     * @param pStrMoney 금액문자열
     * @return String
     */
    public String getFrmFore3(String pStrThcd, String pStrMoney) {
        return getFrmFore3(pStrThcd, pStrMoney, 3, 3);
    }

    /**
     * 문자열을 외화 형식으로 표시 : 기본 3자리 처리 (123,000,000.000)
     *
     * @param pStrMoney 금액문자열
     * @return String
     */
    public String getFrmFore3(String pStrMoney) {
        return getFrmFore2("", pStrMoney, 3);
    }

    /**
     * 문자열을 마이너스 붙여서 금액 형식으로 표시 (-123,000,000) (관세EBPP에만 쓸듯)
     *
     * @param pStrMoney 금액문자열
     * @return String
     */
    public String getFrmMoney2(String pStrMoney) {
        String[] mmon;
        double doubleVal;

        if (pStrMoney == null || pStrMoney.trim().equals(""))
            return "0";

        pStrMoney = pStrMoney.trim();

        try {
            mmon = pStrMoney.split("[-]");

            if (mmon.length > 0) {
                doubleVal = Double.parseDouble(mmon[1]);
            } else {
                doubleVal = Double.parseDouble(pStrMoney);
            }
        } catch (Exception e) {
            return getFrmMoney(pStrMoney);
        }

        return df.format(doubleVal);
    }

    /**
     * 문자열을 금액 형식으로 표시 (123,000,000) (차세대에서 원화금액 소수점 3자리 처리하는 금액필드 마지막 3자리 삭제)
     *
     * @param pStrMoney 금액문자열
     * @return String
     */
    public String getFrmMoney3(String pStrMoney) {
        if (pStrMoney == null)
            return "0";
        pStrMoney = pStrMoney.trim();
        if (pStrMoney.equals("") || pStrMoney.equals("0"))
            return "0";
        pStrMoney = pStrMoney.substring(0, pStrMoney.length() - 3);
        double doubleVal;
        try {
            doubleVal = Double.parseDouble(pStrMoney);
        } catch (Exception e) {
            return pStrMoney;
        }
        return df.format(doubleVal);
    }

    /**
     * 문자열을 카드 형식으로 표시 (0000-0000-0000-0000)
     *
     * @param pStrSrc    기준문자열
     * @param pBlnIsMask 마스킹여부
     * @return String
     */
    public String getFrmCard(String pStrSrc, boolean pBlnIsMask) {
        if (pBlnIsMask) {
            return setDelim(pStrSrc, "9999-9999-****-9999");
        }
        return setDelim(pStrSrc, "9999-9999-9999-9999");
    }

    /**
     * 문자열을 펀드 계좌번호 형식으로 표시
     *
     * @param pStrSrc 기준문자열
     * @return String
     */
    public String getFrmFund(String pStrSrc) {
        if (pStrSrc == null)
            return "";
        pStrSrc = pStrSrc.trim();
        if (pStrSrc.length() != 10)
            return pStrSrc;
        return setDelim(pStrSrc, "999-9999-999");
    }

    /**
     * 문자열을 전화번호 형식으로 표시
     *
     * @param pStrSrc   기준문자열
     * @param pStrDelim 구분자
     * @return String
     */
    public String getFrmPhone(String pStrSrc, String pStrDelim) {
        return getFrmPhone(pStrSrc, pStrDelim, "N");
    }

    /**
     * 문자열을 전화번호 형식으로 표시
     *
     * @param pStrSrc   기준문자열
     * @param pStrDelim 구분자
     * @param pMaskYn   마스킹
     * @return String
     */
    public String getFrmPhone(String pStrSrc, String pStrDelim, String pMaskYn) {
        String regEx = "(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})";
        if ("Y".equals(pMaskYn)) {
            return pStrSrc.replaceAll(regEx, "$1" + pStrDelim + "****" + pStrDelim + "$3");
        } else {
            return pStrSrc.replaceAll(regEx, "$1" + pStrDelim + "$2" + pStrDelim + "$3");
        }
    }

    /**
     * 숫자로 구성된 문자열을 특정 형식으로 포멧한다. EX> setDelim("20001215","9999-99-99"); RESULT>
     * 2000-12-15
     *
     * @param pStrSrc       기준문자열
     * @param pStrDelimPtrn 패턴
     * @return String
     */
    public String setDelim(String pStrSrc, String pStrDelimPtrn) {
        if ((pStrSrc == null) || (pStrSrc.equals("")) || (pStrSrc.equals("0")))
            return "";
        int k = 0;
        for (int j = 0; j < pStrDelimPtrn.length(); j++) {
            if (pStrDelimPtrn.charAt(j) == '9' || pStrDelimPtrn.charAt(j) == '*' || pStrDelimPtrn.charAt(j) == 's') {
                k++;
            }
        }
        if (pStrSrc.length() != k) {
            return pStrSrc;
        }

        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0, j = 0; i < pStrDelimPtrn.length(); i++) {
                if (pStrDelimPtrn.charAt(i) == '9' || pStrDelimPtrn.charAt(i) == 's') {
                    sb.append(pStrSrc.charAt(j));
                    j++;
                } else if (pStrDelimPtrn.charAt(i) == '*') {
                    sb.append(pStrDelimPtrn.charAt(i));
                    j++;
                } else {
                    sb.append(pStrDelimPtrn.charAt(i));
                }
            }
        } catch (Exception e) {
            return "";
        }
        return sb.toString();
    }

    /**
     * 외국인등록번호 에 표시형식을 포멧한다. EX>외국인등록번호(123456-******-1)
     *
     * @param pStrSabh 기준문자열-String
     * @return iStrFmtSabh 형식이 적용된 문자열-String
     */
    public String getFrmForeignNo(String pStrSabh) {
        return getFrmSabh(pStrSabh, "N", "Y");
    }

    /**
     * 외국인등록번호 에 표시형식을 포멧한다. EX>외국인등록번호(123456-******-1)
     * 
     * @param pStrSabh
     * @param pMaskYn
     * @return String
     */
    public String getFrmForeignNo(String pStrSabh, String pMaskYn) {
        return getFrmSabh(pStrSabh, pMaskYn, "Y");
    }

    /**
     * 사업자등록번호 및 주민등록번호 에 표시형식을 포멧한다. EX>사업자등록번호(123-**-****5),
     * 주민등록번호(123456-1******)
     *
     * @param pStrSabh 기준문자열-String
     * @return iStrFmtSabh 형식이 적용된 문자열-String
     */
    public String getFrmSabh(String pStrSabh) {
        return getFrmSabh(pStrSabh, "N");
    }

    /**
     * 사업자등록번호 및 주민등록번호 에 표시형식을 포멧한다. EX>사업자등록번호(123-**-****5),
     * 주민등록번호(123456-1******)
     * 
     * @param pStrSabh
     * @param pMaskYn
     * @return String
     */
    public String getFrmSabh(String pStrSabh, String pMaskYn) {
        return getFrmSabh(pStrSabh, pMaskYn, "N");
    }

    /**
     * 사업자등록번호 및 주민등록번호 에 표시형식을 포멧한다. EX>사업자등록번호(123-**-****5),
     * 주민등록번호(123456-1******)
     * 
     * @param pStrSabh
     * @param pMaskYn
     * @param pForeignYn
     * @return String
     */
    public String getFrmSabh(String pStrSabh, String pMaskYn, String pForeignYn) {
        String iStrFmtSabh = "";
        if (pStrSabh != null && !"".equals(pStrSabh)) {
            pStrSabh = pStrSabh.trim();
            if (pStrSabh.length() == 10) {// 사업자번호
                if (pMaskYn != null && pMaskYn.equals("Y")) {
                    iStrFmtSabh = setDelim(pStrSabh, "999-**-****9");
                } else {
                    iStrFmtSabh = setDelim(pStrSabh, "999-99-99999");
                }
            } else if (pStrSabh.length() == 13) {// 주민등록번호
                if (pMaskYn != null && pMaskYn.equals("Y")) {
                    if ("Y".equals(pForeignYn)) {// 외국인번호
                        iStrFmtSabh = setDelim(pStrSabh, "999999-******-9");
                    } else {
                        iStrFmtSabh = setDelim(pStrSabh, "999999-9******");
                    }
                } else {
                    if ("Y".equals(pForeignYn)) {// 외국인번호
                        iStrFmtSabh = setDelim(pStrSabh, "999999-999999-9");
                    } else {
                        iStrFmtSabh = setDelim(pStrSabh, "999999-9999999");
                    }
                }

            } else {
                iStrFmtSabh = pStrSabh;
            }
        }

        return iStrFmtSabh;
    }

    /**
     * 이름을 마스킹처리한다.(한글은 2번째, 영문은 앞 6개 마스킹)
     *
     * @param pStrName 기준문자열-String
     * @return str 형식이 적용된 문자열-String
     */
    public String getFrmName(String pStrName) {
        return getFrmName(pStrName, "Y");
    }

    /**
     * 이름을 마스킹처리한다.(한글은 2번째, 영문은 앞 6개 마스킹)
     * 
     * @param pStrName
     * @param pMaskYn
     * @return String
     */
    public String getFrmName(String pStrName, String pMaskYn) {
        if (!"Y".equals(pMaskYn)) {
            return pStrName;
        }
        boolean isHan = false;
        for (int i = 0; i < pStrName.length(); i++) {
            if (StringUtils.isKorean(pStrName.charAt(i))) { // 한글이면 true
                isHan = true;
            }
        }
        if (isHan) {
            // 한글일경우
            if (pStrName.length() == 2) {
                return setDelim(pStrName, "s*");
            } else {
                return setDelim(pStrName, "s*" + StringUtils.rpad("", pStrName.length() - 2, 's'));
            }
        } else {
            // 한글이 아닐경우
            if (pStrName.length() <= 6) {
                return setDelim(pStrName, StringUtils.rpad("", pStrName.length(), '*'));
            } else {
                return setDelim(pStrName, "******" + StringUtils.rpad("", pStrName.length() - 6, 's'));
            }
        }
    }

    /**
     * 여권번호에 마스킹처리한다.(발행기관영문 약자1, 발급일련번호 뒷3자리 마스킹)
     *
     * @param pStrPassPort 기준문자열-String
     * @return str 형식이 적용된 문자열-String
     */
    public String getFrmPassPort(String pStrPassPort) {
        return getFrmPassPort(pStrPassPort, "Y");
    }

    /**
     * 여권번호에 마스킹처리한다.(발행기관영문 약자1, 발급일련번호 뒷3자리 마스킹)
     * 
     * @param pStrPassPort
     * @param pMaskYn
     * @return String
     */
    public String getFrmPassPort(String pStrPassPort, String pMaskYn) {
        if ("Y".equals(pMaskYn)) {
            return setDelim(pStrPassPort, "*" + StringUtils.rpad("", pStrPassPort.length() - 4, '9') + "***");
        } else {
            return setDelim(pStrPassPort, "9" + StringUtils.rpad("", pStrPassPort.length() - 4, '9') + "999");
        }
    }

    /**
     * 운전면허번호에 마스킹처리한다.(숫자 첫 2번째, 일련번호 뒷자리2, 체크값 첫번째 마스킹)
     *
     * @param pStrDriverLicenseNo 기준문자열-String
     * @return str 형식이 적용된 문자열-String
     */
    public String getFrmDriverLicenseNo(String pStrDriverLicenseNo) {
        return getFrmDriverLicenseNo(pStrDriverLicenseNo, "Y");
    }

    /**
     * 운전면허번호에 마스킹처리한다.(숫자 첫 2번째, 일련번호 뒷자리2, 체크값 첫번째 마스킹)
     * 
     * @param pStrDriverLicenseNo
     * @param pMaskYn
     * @return String
     */
    public String getFrmDriverLicenseNo(String pStrDriverLicenseNo, String pMaskYn) {
        if ("Y".equals(pMaskYn)) {
            return setDelim(pStrDriverLicenseNo, "ss-9*-9999**-*9");
        } else {
            return setDelim(pStrDriverLicenseNo, "ss-99-999999-99");
        }
    }

    /**
     * 건강보험번호에 마스킹처리한다.(가입자유형코드 1자리, 일련번호8자리중 2~5번째 자리 마스킹)
     *
     * @param pStrDriverLicenseNo 기준문자열-String
     * @return str 형식이 적용된 문자열-String
     */
    public String getFrmHealthInsuranceNo(String pStrHealthInsuranceNo) {
        return getFrmHealthInsuranceNo(pStrHealthInsuranceNo, "Y");
    }

    /**
     * 건강보험번호에 마스킹처리한다.(가입자유형코드 1자리, 일련번호8자리중 2~5번째 자리 마스킹)
     * 
     * @param pStrHealthInsuranceNo
     * @param pMaskYn
     * @return String
     */
    public String getFrmHealthInsuranceNo(String pStrHealthInsuranceNo, String pMaskYn) {
        if ("Y".equals(pMaskYn)) {
            return setDelim(pStrHealthInsuranceNo, "*-9****999");
        } else {
            return setDelim(pStrHealthInsuranceNo, "9-99999999");
        }

    }

    /**
     * 전자금융사기방지 맥 주소 뒷자리 *로 표시.
     * changePatten(바꿀문자), searchPatten(바뀔문자)
     * Return : result
     */
    public String getMacAddress(String pStrMac) {
        String result = "";
        //String changePatten = "";
        //String searchPatten = "";
        if (pStrMac.length() == 17) {
            String changePatten = "**-**";
            String searchPatten = pStrMac.substring(12, pStrMac.length());
            result = pStrMac.replace(searchPatten, changePatten);
        } else {
        	StringBuffer searchPattern = new StringBuffer();
        	StringBuffer changePattern = new StringBuffer();
        	
            for (int i = 12; i < pStrMac.length(); i++) {
            	searchPattern.append(pStrMac.charAt(i));
            	changePattern.append("*");
            }
            result = pStrMac.replace(searchPattern, changePattern);
        }
        return result;
    }

    /**
     * 이메일번호에 마스킹처리한다.(@ 앞에 1,2,5,6번째 마스킹처리)
     *
     * @param pStrDriverLicenseNo 기준문자열-String
     * @return str 형식이 적용된 문자열-String
     */
    public String getFrmEmail(String pStrEmail) {
        return getFrmEmail(pStrEmail, "Y");
    }

    /**
     * 이메일번호에 마스킹처리한다.(@ 앞에 1,2,5,6번째 마스킹처리)
     * 
     * @param pStrEmail
     * @param pMaskYn
     * @return String
     */
    public String getFrmEmail(String pStrEmail, String pMaskYn) {
        if (!"Y".equals(pMaskYn)) {
            return pStrEmail;
        }
        String[] lStrSplit = pStrEmail.split("@");
        if (lStrSplit[0].length() > 6) {
            return "**" + lStrSplit[0].substring(2, 4) + "**" + lStrSplit[0].substring(6) + "@" + lStrSplit[1];
        } else if (lStrSplit[0].length() == 6) {
            return "**" + lStrSplit[0].substring(2, 4) + "**" + "@" + lStrSplit[1];
        } else if (lStrSplit[0].length() == 5) {
            return "**" + lStrSplit[0].substring(2, 4) + "*" + "@" + lStrSplit[1];
        } else if (lStrSplit[0].length() == 4) {
            return "**" + lStrSplit[0].substring(2, 4) + "@" + lStrSplit[1];
        } else if (lStrSplit[0].length() == 3) {
            return "**" + lStrSplit[0].substring(2, 3) + "@" + lStrSplit[1];
        } else if (lStrSplit[0].length() == 2) {
            return "**" + "@" + lStrSplit[1];
        } else if (lStrSplit[0].length() == 1) {
            return "*" + "@" + lStrSplit[1];
        }
        return pStrEmail;
    }

    /**
     * 주소에 마스킹처리한다 (주소의 숫자만 마스킹처리)
     *
     * @param pStrAddr 기준문자열-String
     * @return str 형식이 적용된 문자열-String
     */
    public String getFrmAddr(String pStrAddr) {
        return getFrmAddr(pStrAddr, "Y");
    }

    /**
     * 주소에 마스킹처리한다 (주소의 숫자만 마스킹처리)
     * 
     * @param pStrAddr
     * @param pMaskYn
     * @return String
     */
    public String getFrmAddr(String pStrAddr, String pMaskYn) {
        if (!"Y".equals(pMaskYn)) {
            return pStrAddr;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pStrAddr.length(); i++) {
            if (pStrAddr.charAt(i) >= '0' && pStrAddr.charAt(i) <= '9') {
                sb.append("*");
            } else {
                sb.append(pStrAddr.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 문자열을 format형태로 찍어 준다.
     * getNumberFormat('100000', 1, '#,#00') => 100,000 출력.
     * getNumberFormat('100000', 1, '#,#00.00') => 1,000.00 출력.
     * getNumberFormat('100000', 10, '#,#00.00') => 100.00 출력.
     */
    public String getNumberFormat(String strNumber, String strNumberType, String strFormat) {
        if (strNumber == null || strNumber == "") {
            strNumber = "";
            return strNumber;
        }
        String sign = "";
        String num1 = "";
        String num2 = "";
        int fixedLen = 0;
        int floorLen = 0;

        if (Integer.parseInt(strNumberType) > 1)
            floorLen = new String(strNumberType).length() - 1;

        if (strNumber.indexOf("-") != -1) {
            sign = "-";
        }

        if (Integer.parseInt(strNumber) != 0) {
            strNumber = strNumber.substring(0, strNumber.length() - floorLen);

            if (strFormat != "" && strFormat.indexOf(".") > -1) {
                temp = strFormat.substring(strFormat.indexOf(".") + 1);
                fixedLen = temp.length();
            }

            num1 = formatCommas(strNumber.substring(0, strNumber.length() - fixedLen));
            num2 = strNumber.substring(strNumber.length() - fixedLen);

        } else {
            num1 = "0";
            if (strFormat != "" && strFormat.indexOf(".") > -1) {
                temp = strFormat.substring(strFormat.indexOf(".") + 1);
                fixedLen = temp.length();
            }
            for (int i = 0; i < fixedLen; i++) {
                num2 += "0";
            }
        }

        if (fixedLen != 0) {
            return sign + num1 + "." + num2;
        } else {
            return sign + num1;
        }
    }

    private String formatCommas(String sub) {
        // s = trim(s);
        if (sub == null || sub == "")
            return "0";
        if (sub == "0")
            return sub;
        String str = sub.replace("", sub);
        int len = str.length();
        String tmp = "";
        String tm2 = "";
        int i = 0;
        while (str.charAt(i) == '0')
            i++;
        str = str.substring(i, len);
        len = str.length();
        if (len < 3) {
            return str;
        } else {
            int sit = len % 3;
            if (sit > 0) {
                tmp = tmp + str.substring(0) + ',';
                len = len - sit;
            }
            while (len > 3) {
                tmp = tmp + str.substring(sit, sit + 3) + ',';
                len = len - 3;
                sit = sit + 3;
            }
            tmp = tmp + str.substring(sit, sit + 3) + tm2;
            str = tmp;
        }
        return str;
    }

    /**
     * IP에 마스킹처리한다(ipv4 : 17~24비트영역, ipv6 :113~128비트영역 마스킹처리)
     *
     * @param pStrAddr 기준문자열-String
     * @return str 형식이 적용된 문자열-String
     */
    public String getFrmIp(String pStrIp) {
        return getFrmIp(pStrIp, "Y");
    }

    /**
     * IP에 마스킹처리한다(ipv4 : 17~24비트영역, ipv6 :113~128비트영역 마스킹처리)
     * 
     * @param pStrIp
     * @param pMaskYn
     * @return String
     */
    public String getFrmIp(String pStrIp, String pMaskYn) {
        if (!"Y".equals(pMaskYn)) {
            return pStrIp;
        }
        if (pStrIp.indexOf(".") > -1) {
            // ipv6
            return pStrIp.replaceAll("(^.+[.]{1}.+[.]{1})(.+)([.]{1}.+)$", "$1***$3");
        }
        // ipv4
        return pStrIp.replaceAll("(^.*)(.{4})$", "$1****");
    }

    /**
     * 현재 년도를 리턴한다.
     * 
     * @return int
     */
    public int getCurrentYear() {
        java.util.GregorianCalendar cal = new java.util.GregorianCalendar();
        return cal.get(java.util.Calendar.YEAR);
    }

    /**
     * 주민등록번호("-"제외)와 계약일자로 보험연령 구하기
     * 
     * @param regno
     * @return
     */
    public int getInsuAge(String regno, String polidt) {
        try {
            if ("1111111111111".equals(regno) || "0000000000000".equals(regno))
                return 0;

            int birthYear = Integer.parseInt(regno.substring(0, 2));

            String gender = regno.substring(6, 7);
            if ("1/2/5/6".indexOf(gender) > -1)
                birthYear += 1900;
            else if ("7/8".indexOf(gender) > -1) {
                // 여권으로 조립한 외국인인 경우 무조건 7,8로 조립됨. 생년(끝2자리)이 올해(끝2자리)보다 크면
                // 1900년생으로 간주
                int currYear = getCurrentYear();
                if (birthYear > (currYear % 2000))
                    birthYear += 1900;
                else
                    birthYear += 2000;
            } else
                birthYear += 2000;

            String birthdt = birthYear + regno.substring(2, 6);
            polidt = formatNumber(polidt);

            int diffYear = Integer.parseInt(polidt.substring(0, 4)) - Integer.parseInt(birthdt.substring(0, 4));
            int diffMon = Integer.parseInt(polidt.substring(4, 6)) - Integer.parseInt(birthdt.substring(4, 6));
            int diffDay = Integer.parseInt(polidt.substring(6, 8)) - Integer.parseInt(birthdt.substring(6, 8));

            return (int) Math.round((diffYear * 12 + diffMon + (diffDay >= 0 ? 0 : -1)) / 12.0);
        } catch (Exception ex) {
            // ex.printStackTrace();
            return 0;
        }
    }

    /**
     * 입력문자열에서 숫자가 아닌 것을 제외시킨다. 숫자가 전부 없으면 "0"을 반환한다.
     * 
     * @param numStr
     * @return
     */
    public String formatNumber(String numStr) {
        if (numStr == null)
            return "";

        StringBuffer sb = new StringBuffer();
        char[] chs = numStr.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char ch = chs[i];
            if (Character.isDigit(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 주민등록번호("-"제외)로 성별 구하기
     * 
     * @param regno
     * @return
     */
    public String getGender(String regno) {
        if (regno == null || regno.length() < 7)
            return "1";

        String gender = regno.substring(6, 7);
        if ("1/3/5/7".indexOf(gender) < 0)
            return "2";
        else
            return "1";
    }

    /**
     * 이율 자르기
     */
    @SuppressWarnings("unused")
    public String getRateFormat(String strRate) {
        String retRate = "";
        String arguments = "";
        if (!strRate.equals("")) {
            if (strRate.length() == 7) {
                retRate = strRate.substring(0, 2) + "." + strRate.substring(2, 4) + "0%";
            } else if (strRate.length() == 6) {
                retRate = strRate.substring(0, 1) + "." + strRate.substring(1, 3) + "0%";
            }

            else if (strRate.length() == 5) {
                retRate = strRate.substring(0, 2) + "." + strRate.substring(2, 3) + "0%";
            } else if (strRate.length() == 4) {
                if (type == 0) {
                    retRate = strRate.substring(0, 1) + "." + strRate.substring(1, 2) + "00%";
                } else if (type == 1)
                    retRate = strRate.substring(0, 1) + "." + strRate.substring(1, 3) + "0%";
            } else if (strRate.length() == 3) {
                retRate = "0." + strRate + "%";
            } else if (strRate.length() == 2) {
                retRate = "0.0" + strRate + "%";
            } else if (strRate.length() == 1) {
                retRate = "0.00" + strRate + "%";

            } else {
                retRate = "0%";
            }
        }

        return retRate;
    }

    /**
     * 펀드 이율 Format
     */
    public String getFundRate(String strRate) {
        String retRate = "";

        if (!strRate.equals("")) {
            if (strRate.length() == 7) {
                retRate = strRate.substring(0, 5) + "." + strRate.substring(5, 7) + "%";
            } else if (strRate.length() == 6) {
                retRate = strRate.substring(0, 4) + "." + strRate.substring(4, 6) + "%";
            } else if (strRate.length() == 5) {
                retRate = strRate.substring(0, 3) + "." + strRate.substring(3, 5) + "%";
            } else if (strRate.length() == 4) {
                retRate = strRate.substring(0, 2) + "." + strRate.substring(2, 4) + "%";
            } else if (strRate.length() == 3) {
                retRate = strRate.substring(0, 1) + "." + strRate.substring(1, 3) + "%";
            } else if (strRate.length() == 2) {
                retRate = "0." + strRate + "%";
            } else if (strRate.length() == 1) {
                retRate = "0.0" + strRate + "%";
            } else {
                retRate = "0%";
            }
        }

        return retRate;
    }

    /**
     * 이율 displayFormatte
     */
    @SuppressWarnings("unused")
    public String getForRate(String strRate) {
        String retRate = "";
        String arguments = "";
        if (!strRate.equals("")) {
            if (strRate.length() == 7) {
                retRate = strRate.substring(0, 2) + "." + strRate.substring(2, 4) + "0%";
            } else if (strRate.length() == 6) {
                retRate = strRate.substring(0, 1) + "." + strRate.substring(1, 3) + "0%";
            }

            else if (strRate.length() == 5) {
                retRate = strRate.substring(0, 2) + "." + strRate.substring(2, 3) + "0%";
            } else if (strRate.length() == 4) {
                if (type == 0) {
                    retRate = strRate.substring(0, 1) + "." + strRate.substring(1, 2) + "00%";
                } else if (type == 1)
                    retRate = strRate.substring(0, 1) + "." + strRate.substring(1, 3) + "0%";
            } else if (strRate.length() == 3) {
                retRate = "0." + strRate + "%";
            } else if (strRate.length() == 2) {
                retRate = "0.0" + strRate + "%";
            } else if (strRate.length() == 1) {
                retRate = "0.00" + strRate + "%";

            } else {
                retRate = "0%";
            }
        }

        return retRate;
    }

    /**
     * 외환이율 자르기
     */
    public String getForeignFormat(String strRate) {
        String retRate = "";
        if (retRate == "0") {
            retRate = "0.00";
        } else if (strRate.length() == 7) {
            retRate = strRate.substring(0, 3) + "." + strRate.substring(5, 7) + "%";
        } else if (strRate.length() == 6) {
            retRate = strRate.substring(0, 2) + "." + strRate.substring(4, 6) + "%";
        } else if (strRate.length() == 5) {
            retRate = strRate.substring(0, 1) + "." + strRate.substring(3, 5) + "%";
        } else if (strRate.length() == 4) {
            retRate = "0." + strRate.substring(0, 2) + "%";
        } else if (strRate.length() == 3) {
            retRate = "0.0" + strRate.substring(0, 1) + "%";
        } else if (strRate.length() == 2) {
            retRate = "0.00" + strRate.substring(0, 1) + "%";
        } else if (strRate.length() == 1) {
            retRate = "0.000" + strRate + "%";

        } else {
            retRate = "0%";
        }

        return retRate;
    }

}
