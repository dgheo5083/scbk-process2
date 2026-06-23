package com.scbank.process.api.svc.shared.utils;

public class BizCommonUtils {

	/**
	 * SMS 마스킹 처리
	 * maskType 01 : 이름
	 * 		-2자 : 홍*
	 * 		-3자 : 홍*동
	 * 		-4자 : 을**덕

	 * maskType 02 : 계좌번호
	 * 		계좌번호 끝 3,4,5번째 자리 마스킹처리
	 * 		ex ) 123456789 -> 1234***89
	 * 		ex ) 45-123456 -> 45-1***56
	 *
	 * 필요 시 추가하여 사용
	 */
	public static String getMaskCustData(String targetStr, String maskType) {
		// 조건에 맞지 않는 경우 기존 문자열 return
		String returnMaskStr = targetStr;

		try {
			if ("01".equals(maskType) && targetStr != null) {
				if (targetStr.length() == 2) {
					returnMaskStr = targetStr.substring(0, 1) + "*";
				} else if (targetStr.length() == 3) {
					returnMaskStr = targetStr.substring(0, 1) + "*" + targetStr.substring(2, 3);
				} else if (targetStr.length() > 3) {
					String maskText = "";
					for (int idx = 0; idx < targetStr.length() - 2; idx++) {
						maskText = maskText + "*";
					}
					returnMaskStr = targetStr.substring(0, 1) + maskText + targetStr.substring(targetStr.length() - 1, targetStr.length());
				}
			} else if ("02".equals(maskType) && targetStr != null && targetStr.length() > 4) {
				returnMaskStr = targetStr.substring(0, targetStr.length() - 5) + "***" + targetStr.substring(targetStr.length() - 2, targetStr.length());

			} else {
				// 정의되지않은 타입의 문자열은 그대로 return
				returnMaskStr = targetStr;
			}
			return returnMaskStr;
		} catch (Exception e) {
			// 오류 발생 시 로그적재 및 기존문자 return
			return returnMaskStr;
		}
	}

}
