package com.scbank.process.api.svc.shared.components.sign.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.svc.shared.components.sign.constants.SignConstants;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;
import com.scbank.process.api.svc.shared.components.sign.enums.SignEnums.SignType;
import com.scbank.process.api.svc.shared.utils.VeraportUtils;
import com.wizvera.crypto.SEEDCBCCipher;
import com.wizvera.util.Base64;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class SignUtils {

    /**
     * 전자서명 암호화
     * 
     * @param encrypted
     * @return
     * @throws Exception
     */
    public byte[] encrypt(SignType signType, String signedData) {
        byte[] encrypted = null;

        try {
            byte[] key = HexFormat.of().parseHex(SignConstants.KEY);
            byte[] iv = HexFormat.of().parseHex(SignConstants.IV);
            byte[] plain;

            if (signType == SignType.FIN_CERT_PIN
                    || signType == SignType.FIN_CERT_BIO) {
                plain = VeraportUtils.base64SafeDecode(signedData);
            } else {
                plain = Base64.decode(signedData);
            }

            if (plain == null) {
                throw new Exception("encrypt base64 decode 실패");
            }

            encrypted = SEEDCBCCipher.encrypt(key, iv, plain);
            log.debug("SignUtils > encrypt - Success");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PRCServiceException("MA3CMM0003", "전자서명 데이터 처리중 에러가 발생하였습니다.", e);
        }

        return encrypted;
    }

    /**
     * 전자서명 복호화
     * 
     * @param encrypted
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] encrypted) {
        byte[] decrypted = null;

        try {
            // String encHex = Hex.byteArrayToHex(encrypted); // ByteArray를 hex값으로 변환
            // String encHex = Hex.byteArrayToHex(encrypted); // ByteArray를 hex값으로 변환
            // byte[] ByteHex = Hex.hexToByteArray(encHex); //hex를 ByteArray 값으로 변환
            // key = KEY.getBytes(StandardCharsets.UTF_8);
            // iv = IV.getBytes(StandardCharsets.UTF_8);
            byte[] key = HexFormat.of().parseHex(SignConstants.KEY);
            byte[] iv = HexFormat.of().parseHex(SignConstants.IV);

            decrypted = SEEDCBCCipher.decrypt(key, iv, encrypted);
            if (log.isDebugEnabled()) {
                log.debug("SignUtils > decrypt > Success - decrypted : [{}] ", decrypted);
            }

            // DERUTF8String decoder = new BERDecoder(decrypted);
            // ContentInfo ci = new ContentInfo();
            // ci.decode(decoder);
            // SignedData signedDataObject = (SignedData)ci.getContent();
            // log.debug("testDecryptSign decrypt Success - dumpText2 : {} ", dumpText2);

            // String decText = new String(decrypted2, StandardCharset.UTF_8);
            // log.debug("decrypt decrypt Success - decText : {} ", decText);
            // String base64Enc = new String(Base64.encode(decrypted2));
            // log.debug("decrypt decrypt Success - base64Enc : {} ", base64Enc);
            // String decText2 = new String(base64Enc.getBytes(), StandardCharset.UTF_8);
            // log.debug("decrypt decrypt Success - decText2 : {} ", decText2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PRCServiceException("MA3CMM0003", "전자서명 데이터 처리중 에러가 발생하였습니다.", e);
        }
        return decrypted;
    }

    // public byte[] signDataDecrypt(byte[] signedData) {
    // if (signedData == null || signedData.length == 0) {
    // // Error: 전자서명 데이터가 들어오지 않았습니다.
    // return null;
    // }

    // try {
    // byte[] decData = CipherClient.decrypt((byte) 8, signedData);
    // if (decData == null || decData.length == 0) {
    // // Error: 전자서명 데이터를 암호화 한 결과가 0 입니다.
    // throw new PRCServiceException("APPL_0010", "전자서명 데이터 처리중 에러가 발생하였습니다.");
    // }
    // return decData;

    // } catch (Exception e) {
    // // Error: 전자서명 데이터를 암호화 하는 도중 오류가 발생했습니다.
    // throw new PRCServiceException("APPL_0010", "전자서명 데이터 처리중 에러가 발생하였습니다.");
    // }
    // }

    // public String getSignData(byte[] signedData, boolean flag) throws Exception {
    // BERDecoder decoder = null;

    // InputStream bais = new ByteArrayInputStream(signedData);

    // decoder = new BERDecoder(bais);

    // ContentInfo ci = new ContentInfo();

    // ci.decode(decoder);

    // // PKCS7TrustManager tm = new PKCS7ManageCert();
    // // PKCS7KeyManager km = new PKCS7RSAKey();

    // SignedData signedDataObject = (SignedData) ci.getContent();

    // ContentInfo info = signedDataObject.getContentInfo();

    // if (flag) {
    // return new String(((Data) info.getContent()).getRawContent(), "utf-8");
    // } else {
    // return ((Data) info.getContent()).toString();
    // }
    // }

    /**
     * 서명 원문 문자열을 서명 비교가능 Map으로 변환
     * 
     * @param signOrgString
     * @return
     */
    public Map<String, List<String>> getCompareMapFromOrgString(String signOrgString) {
        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();

        String[] dataSplit = signOrgString.split("&");
        for (String row : dataSplit) {
            String key = row.split("=")[0];
            String value = "";
            List<String> rows = null;
            try {
                value = row.split("=")[1];
            } catch (Exception e) {
            }

            if (resultMap.containsKey(key)) {
                rows = resultMap.get(key);
                rows.add(value);
                resultMap.put(key, rows);
            } else {
                rows = new ArrayList<String>();
                rows.add(value);
                resultMap.put(key, rows);
            }
        }

        return resultMap;
    }

    /**
     * Object 객체를 서명값 비교 가능 Map 데이터로 변환
     * 
     * @param obj
     * @param signVerifyInfo
     * @return
     */
    public Map<String, List<String>> getCompareMapFromObject(Object obj, SignVerifyInfo signVerifyInfo) {
        Map<String, List<String>> returnMap = new LinkedHashMap<String, List<String>>();

        if (!(obj instanceof IMessageObject) && !(obj instanceof Map<?, ?>)) {
            throw new PRCServiceException("MA3CMM0008", "전자서명 데이터 검증에 실패하였습니다.");
        }

        if (signVerifyInfo == null) {
            throw new PRCServiceException("MA3CMM0008", "전자서명 데이터 검증에 실패하였습니다.");
        }

        try {
            if (log.isDebugEnabled()) {
                log.debug("SignUtils > getCompareMapFromObject - obj : [{}], signVerifyInfo : [{}]", obj.toString(),
                        signVerifyInfo.toString());
            }

            // Message Data(Object Reflection)
            SignUtils.collect(obj, returnMap);

            if (obj instanceof IMessageObject) {
                // Common Data
                returnMap.put("tranCd", List.of(signVerifyInfo.getTranCd())); // mci 거래코드
                returnMap.put("imsTranCd", List.of(signVerifyInfo.getImsTranCd()));
                returnMap.put("inClassCd", List.of(signVerifyInfo.getInClassCd()));
                returnMap.put("svcCd", List.of(signVerifyInfo.getSvcCd()));
                returnMap.put("jobTp", List.of(signVerifyInfo.getJobTp())); // 외환에서 필수
            }

            if (log.isDebugEnabled()) {
                log.debug("SignUtils > getCompareMapFromObject - returnMap : [{}]", returnMap.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new PRCServiceException("MA3CMM0008", "전자서명 데이터 검증에 실패하였습니다.");
        }

        return returnMap;
    }

    /**
     * 서명 데이터의 총 건수 가져오기
     * 
     * @param signDataMap
     * @return resultCount
     * @throws PRCServiceException
     */
    public Integer getTotalSignDataCount(Map<String, List<String>> signDataMap) {

        Integer resultCount = 0;
        try {
            // TO-BE 개선
            resultCount = signDataMap.entrySet().stream()
                    .filter(e -> SignConstants.excludeSignDataKeySet.stream()
                            .noneMatch(exclude -> e.getKey().toLowerCase().contains(exclude)))
                    .map(Map.Entry::getValue)
                    .filter(Objects::nonNull)
                    .mapToInt(List::size)
                    .max() // TODO: max로 TOBE 개선 확인필요
                    .orElse(0);
        } catch (Exception e) {
            throw new PRCServiceException("MA3CMM0007", "전자서명 데이터 처리중 에러가 발생하였습니다.", e);
        }

        return resultCount;
    }

    public void collect(Object obj, Map<String, List<String>> result) {
        if (obj == null) {
            return;
        }

        // Collection (List/Set)
        if (obj instanceof Collection<?> col) {
            for (Object o : col) {
                collect(o, result);
            }
            return;
        }

        // Array
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                collect(Array.get(obj, i), result);
            }
            return;
        }

        // Map
        if (obj instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object k = entry.getKey();
                Object o = entry.getValue();

                if (k == null || !(k instanceof String)) {
                    collect(o, result);
                } else {
                    if (isPrimitiveLike(o)) {
                        put(result, String.valueOf(k), String.valueOf(o));
                    } else {
                        collect(o, result);
                    }
                }
            }

            // for (Object o : map.values()) {
            // collect(o, result);
            // }
            return;
        }

        // IMessageObject
        if (obj instanceof IMessageObject) {
            inspectMessageObject((IMessageObject) obj, result);
        }
    }

    private void inspectMessageObject(IMessageObject mo, Map<String, List<String>> result) {
        Class<?> clazz = mo.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            Object obj;
            try {
                obj = field.get(mo);
            } catch (IllegalAccessException e) {
                continue;
            }
            if (obj == null) {
                continue;
            }

            // IMPORTANT: 변수명으로 통일
            // MessageField mf = field.getAnnotation(MessageField.class);
            String key;
            // if (mf != null && mf.id() != null && !mf.id().isEmpty()) {
            // key = mf.id();
            // } else {
            key = field.getName();
            // }

            // Primitive
            if (isPrimitiveLike(obj)) {
                put(result, key, String.valueOf(obj));
                continue;
            }

            // Collection
            if (obj instanceof Collection<?> col) {
                for (Object o : col) {
                    if (isPrimitiveLike(o)) {
                        put(result, key, String.valueOf(o));
                    } else {
                        // List 순회 조회
                        collect(o, result);
                    }
                }
                continue;
            }

            // Array
            if (obj.getClass().isArray()) {
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    Object o = Array.get(obj, i);
                    if (isPrimitiveLike(o)) {
                        put(result, key, String.valueOf(o));
                    } else {
                        collect(o, result);
                    }
                }
                continue;
            }

            // Map
            if (obj instanceof Map<?, ?> map) {
                for (Object o : map.values()) {
                    if (isPrimitiveLike(o)) {
                        put(result, key, String.valueOf(o));
                    } else {
                        collect(o, result);
                    }
                }
                continue;
            }

            // IMessageObject
            if (obj instanceof IMessageObject) {
                collect(obj, result);
            }
        }
    }

    private boolean isPrimitiveLike(Object o) {
        if (o == null)
            return false;
        Class<?> c = o.getClass();
        return c.isPrimitive() || c == String.class || Number.class.isAssignableFrom(c) || c == Boolean.class
                || c == Character.class;
    }

    private void put(Map<String, List<String>> result, String key, String value) {
        result.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    /**
     * IMessageObject 구현체로 부터 특정 필드 획득
     * 
     * @param IMessageObject :
     * @param String         : 필드명
     * @return Field : 필드값
     */
    public <T> T getFieldFromIMessageObject(IMessageObject mo, String fieldName, Class<T> requiredType) {

        try {
            Class<?> clazz = mo.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            Object object = field.get(mo);
            if (object == null) {
                return null;
            }

            return requiredType.isInstance(object) ? requiredType.cast(object) : null;
        } catch (Exception e) {
            return null;
        }
    }

}
