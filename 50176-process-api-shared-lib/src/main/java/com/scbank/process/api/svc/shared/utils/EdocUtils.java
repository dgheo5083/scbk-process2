package com.scbank.process.api.svc.shared.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.wizvera.util.Base64;

import kr.co.pace.crypto.SeedUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class EdocUtils {

    /* Base64형태로 변환하기위한 Padding 변수처리 */
    private final String[] Base64Padding = new String[] { "", "===", "==", "=" };

    /**
     * Base64 URL Safe Decode 수행
     * STEP01 : [-] -> [+] , [_] -> [/] String Replace
     * STEP02 : Base64 Padding 추가
     * STEP03 : Base64 Decode 진행.
     * 
     * @param str
     * @return
     */
    public byte[] base64SafeDecode(String str) {
        byte[] result = null;

        if (StringUtils.defaultIfEmpty(str, "").equals("")) { // 빈값이면 NULL 리턴한다.
            return result;
        }
        String b64 = str.replace('-', '+').replace('_', '/').replaceAll("\\r|\\n", ""); // URL Safe 변환 코드 Base64Encoding
                                                                                        // 형태로 변환
        b64 = b64 + Base64Padding[b64.length() % 4];
        result = Base64.decode(b64);
        return result;
    }

    /**
     * Base64 URL Safe Decode 수행
     * STEP01 : [-] -> [+] , [_] -> [/] String Replace
     * 
     * @param str
     * @return
     */
    public String base64SafeReplace(String str) {
        String result = "";

        if (StringUtils.isBlank(str)) { // 빈값이면 NULL 리턴한다.
            return result;
        }
        /* URL Safe 변환 코드 Base64Encoding 형태로 변환 */
        result = str.replace('-', '+').replace('_', '/').replaceAll("\\r|\\n", "");
        return result;
    }

    /**
     * Base64 URL Safe Encode 수행
     * STEP01 : [+] -> [-] , [/] -> [_] String Replace
     * 
     * @param str
     * @return
     */
    public String base64EncodeSafeReplace(String str) {
        String result = "";
        if (StringUtils.isBlank(str)) { // 빈값이면 NULL 리턴한다.
            return result;
        }
        result = str.replace('+', '-').replace('/', '_').replaceAll("\\n|\\r", "");
        return result;
    }

    /**
     * JSON 형식을 쿼리 스트링 형식으로 변환한다.
     * 
     * @param jsonObject
     * @return
     */
    public String json2QueryString(JSONObject jsonObject) {

        Iterator<String> iters = jsonObject.keySet().iterator();

        String queryString = "&";
        while (iters.hasNext()) {
            String key = iters.next();
            queryString += key + "=" + jsonObject.get(key) + "&";
        }

        return queryString;
    }

    /**
     * JSON 형식을 encode 쿼리 스트링 형식으로 변환한다.
     * 
     * @param jsonObject
     * @return
     */
    public String json2QueryStringToURLEncode(JSONObject jsonObject) {

        Iterator<String> iters = jsonObject.keySet().iterator();
        String queryString = "&";
        while (iters.hasNext()) {
            String key = iters.next();
            try {
                Object objectData = jsonObject.get(key);
                queryString += key + "=" + URLEncoder.encode(objectData.toString(), "UTF-8") + "&";
            } catch (UnsupportedEncodingException e) {
                queryString += key + "=" + jsonObject.get(key) + "&";
            } catch (Exception e) {
                e.printStackTrace();
                queryString += key + "=" + jsonObject.get(key) + "&";
            }
        }

        return queryString;
    }

    public boolean deCryptionFile(String dirPath, String decDirPath, List<String> fileNames) {
        log.debug("[CryptionUtil::fileDecode] start ");
        List<String> success = new ArrayList<String>();
        List<String> failed = new ArrayList<String>();
        boolean isDecryption = true;
        try {
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);
                boolean result = deCryptionFile(dirPath, decDirPath, fileName);

                if (result) {
                    success.add(fileName);
                    log.debug("[CryptionUtil::fileDecode] success : " + success);
                } else {
                    failed.add(fileName);
                    log.debug("[CryptionUtil::fileDecode] failed : " + failed);
                    isDecryption = false;
                }
            }
        } catch (Exception e) {
            log.debug("[CryptionUtil::fileDecode] Exception : " + failed);
            e.printStackTrace();
            isDecryption = false;
        }

        log.debug("[CryptionUtil::fileDecode] end ");
        return isDecryption;

    }

    public boolean deCryptionFile(String dirPath, String decDirPath, String fileName) {
        return deCryptionFile(dirPath, decDirPath, fileName, "");
    }

    public boolean deCryptionFile(String dirPath, String decDirPath, String fileName, String decFileName) {
        SeedUtil seedUtil = SeedUtil.getInstance();
        boolean result = false;
        try {

            if ("".equals(decFileName)) {
                decFileName = fileName;
            }

            String filePath = SecurityFilterUtils.getPMFilter(dirPath + fileName);
            String decFilePath = SecurityFilterUtils.getPMFilter(decDirPath + decFileName);
            FileUtils.makeDir(decDirPath);
            log.debug("[CryptionUtil::fileDecode] filePath : " + filePath);

            File file = new File(filePath);
            if (!file.exists()) {
                log.debug("[CryptionUtil::fileDecode] file not found " + filePath);
                // TODO : 파일리스트중 파일 없으면 진행 안되게 해야함. (앞부분에서 체크할것이므로 여기로 올수는 없음)
            } else if (file.length() <= 0) {
                log.debug("[CryptionUtil::fileDecode] Encrypted file is 0 byte (file size 0) " + filePath);
                // TODO : 파일리스트중 파일 변환시 0byte 인경우 (앞부분에서 체크할것이므로 여기로 올수는 없음)
            } else {
                File decFile = seedUtil.getDecryptFile(file, decFilePath); // 암호화 파일 복호화
                if (decFile.exists()) {
                    log.debug("[CryptionUtil::deCryptionFile] file decode Success - orgFileName" + file);
                    log.debug("[CryptionUtil::deCryptionFile] file decode Success - tmpFileName" + decFilePath);
                    result = true;
                } else {
                    log.debug("[CryptionUtil::deCryptionFile] file decode Failed - orgFileName" + file);
                    log.debug("[CryptionUtil::deCryptionFile] file decode Failed - tmpFileName" + decFilePath);
                }
            }
        } catch (Exception e) {
            log.debug("deCryptionFile fileNames : " + decFileName);
            log.debug("deCryptionFile : " + e);
        }
        return result;
    }

    /**
     * 파일의 SEED 암호를 수행한다.
     * 
     * @param _orgFilePath 원본 파일 디렉토리
     * @param _encFilePath 암호화 파일 디렉토리
     * @param _orgFileName 원본 파일 이름
     * @param _encFileName 암호화 파일 이름
     * @return
     */
    public HashMap<String, String> enCryptionFile(String _orgFilePath, String _encFilePath, String _orgFileName,
            String _encFileName) {
        SeedUtil seedUtil = SeedUtil.getInstance();
        HashMap<String, String> output = new HashMap<String, String>();
        try {
            String filePath = SecurityFilterUtils.getPMFilter(_orgFilePath + _orgFileName);
            String encFilePath = SecurityFilterUtils.getPMFilter(_encFilePath + _encFileName);
            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {
                File encFile = seedUtil.getEncryptFile(file, encFilePath);
                log.debug("file encode		 : (filePath) : " + filePath);
                log.debug("                    : (encFile) : " + encFile);
                if (encFile.exists()) {
                    String encPath = encFile.getParent() + File.separator;
                    String encName = encFile.getName();
                    output.put("encPath", encPath);
                    output.put("encName", encName);
                    log.debug("[CryptionUtil::enCryptionFile] output : " + output);
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Success - file" + file);
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Success - encFile" + encFile);
                } else {
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Failed - file" + file);
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Failed - encFile" + encFile);
                }

            } else {
                log.debug("fileEncode  file not found : " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public HashMap<String, String> enCryptionFile(String filePath, String encFilePath) {
        HashMap<String, String> output = new HashMap<String, String>();
        SeedUtil seedUtil = SeedUtil.getInstance();
        try {
            File file = new File(filePath);
            if (file.exists() && file.length() > 0) {
                File encFile = seedUtil.getEncryptFile(file, encFilePath);
                log.debug("enCryptionFile file encode		 : (filePath) : " + filePath);
                log.debug("                    : (encFile) : " + encFile);
                if (encFile.exists()) {
                    String encPath = encFile.getParent() + File.separator;
                    String encName = encFile.getName();
                    output.put("encPath", encPath);
                    output.put("encName", encName);
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Success - file" + file);
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Success - encFile" + encFile);
                } else {
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Failed - file" + file);
                    log.debug("[CryptionUtil::enCryptionFile] file enCryptionFile Failed - encFile" + encFile);
                }
            } else {
                log.debug("fileEncode  file not found : " + filePath);
            }
        } catch (Exception e) {
            log.debug("enCryptionFile : {}", e);
            e.printStackTrace();
        }

        return output;
    }

}
