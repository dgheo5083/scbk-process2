package com.scbank.process.api.svc.shared.utils;

import java.security.SecureRandom;
import java.util.Random;

public class RandomKeyUtils {

    public static String getKey() {
        Random random = new Random();
        String randonKey = "000" + ((random.nextInt() % 5000) + 5000);
        randonKey = randonKey.substring(randonKey.length() - 4, randonKey.length());

        return randonKey;
    }

    public static String getKey(int size) {
        long time = System.currentTimeMillis();
        long s1 = new SecureRandom().nextLong();
        long s2 = new SecureRandom().nextLong();
        long s3 = new SecureRandom().nextLong();

        StringBuffer temp = new StringBuffer(Long.toHexString(((time & s1) | s2) ^ s3).toUpperCase());

        for (int i = temp.length(); i < size; i++)
            temp.append(new SecureRandom().nextInt(10));

        return temp.toString();

    }
}
