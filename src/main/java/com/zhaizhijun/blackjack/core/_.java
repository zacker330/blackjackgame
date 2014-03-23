package com.zhaizhijun.blackjack.core;

import java.util.Random;

/**
 * 工具类
 * User: zjzhai
 * Date: 2/24/14
 * Time: 10:27 PM
 */
public class _ {
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    /**
     * 产生一个随机的字符串
     *
     * @param length
     * @return
     */
    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

}
