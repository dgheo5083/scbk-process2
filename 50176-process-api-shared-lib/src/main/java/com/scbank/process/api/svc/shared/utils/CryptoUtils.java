package com.scbank.process.api.svc.shared.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class CryptoUtils {

    private String CRYPTO_KEY = "danbEnVal";

    /**
     * 기본 암호화 메서드
     * - 암호화 방식: AES/CBC/PKCS5Padding
     * 
     * @param text
     * @return
     * @throws Exception
     */
    public String encrypt(String text) throws Exception {
        return encrypt(text, CRYPTO_KEY);
    }

    /**
     * 지정된 키를 사용하여 암호화 메서드
     * - 암호화 방식: AES/CBC/PKCS5Padding
     * 
     * @param text
     * @param key
     * @return
     * @throws Exception
     */
    public String encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        return new String(Base64.encodeBase64(results));
    }

    /**
     * 기본 복호화 메서드
     * - 암호화 방식: AES/CBC/PKCS5Padding
     * 
     * @param text
     * @return
     * @throws Exception
     */
    public String decrypt(String text) throws Exception {
        return decrypt(text, CRYPTO_KEY);
    }

    /**
     * 지정된 키를 사용하여 복호화 메서드
     * - 암호화 방식: AES/CBC/PKCS5Padding
     * 
     * @param text
     * @param key
     * @return
     * @throws Exception
     */
    public String decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // BASE64Decoder decoder = new BASE64Decoder();
        byte[] results = cipher.doFinal(Base64.decodeBase64(text.getBytes()));
        return new String(results, "UTF-8");
    }

}
