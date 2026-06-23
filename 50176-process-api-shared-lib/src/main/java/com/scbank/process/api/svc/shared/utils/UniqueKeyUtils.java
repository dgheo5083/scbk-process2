package com.scbank.process.api.svc.shared.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UniqueKeyUtils {

    private final static String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    /**
     * 시분초, 랜덤값, ip 주소 64진수 변환 후 유니크키 생성
     * 
     * @return
     */
    public static String UniqueKey() {

        Long random = generateRandomKey();
        Long now = DateTime();
        String ip = getServerIp();
        Long longIp = Long.valueOf(ip);

        // 시분초, 랜덤값, ip 주소 64진수 변환
        String now64 = toBase64(now);
        String random64 = toBase64(random);
        String ip64 = toBase64(longIp);

        String uniqueKey = ip64.concat(now64).concat(random64);
        log.debug("------------------------변 환 전------------------------------------");
        log.debug("IP 3자리 : " + longIp + " / 월일시분초 : " + now + " / 난수7자리 : " + random);
        log.debug("--------------------------------------------------------------------");
        log.debug("uniqueKey : " + uniqueKey);
        log.debug("--------------------------------------------------------------------");
        return uniqueKey;

    }

    /**
     * 시간 생성 long타입
     * 
     * @return
     */
    private static Long DateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMddHHmmssSSS");
        String dateTime = LocalDateTime.now().format(dateTimeFormatter);

        Long now = Long.valueOf(dateTime);
        return now;

    }

    /**
     * long 타입랜덤 난수 생성
     * 
     * @return
     */
    private static long generateRandomKey() {

        Random random = new Random();
        int num = random.nextInt(9999999) + 1;
        String formatNum = String.format("%07d", num);

        long randonNum = Long.valueOf(formatNum);
        return randonNum;

    }

    /**
     * long 타입데이터를 64진수로 변환
     * 
     * @param value
     * @return
     */
    public static String toBase64(long value) {
        if (value == 0)
            return "A";
        StringBuilder sb = new StringBuilder();
        // String valueformat = formatDouble(value,"###,##0");

        while (value > 0) {
            Integer aaa = (int) (value % 64);
            sb.append(BASE64_CHARS.charAt((int) (value % 64.0d)));
            value /= 64;
        }

        return sb.reverse().toString();
    }

    /**
     * IP주소 3자리 알아오기
     * 
     * @return
     */
    private static String getServerIp() {
        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (local == null) {
            return "";
        } else {
            String ip = local.getHostAddress();
            String[] parts = ip.split("\\.");

            return parts[3];
        }
    }

}
