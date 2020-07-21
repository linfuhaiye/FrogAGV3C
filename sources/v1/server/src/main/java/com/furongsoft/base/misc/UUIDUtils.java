package com.furongsoft.base.misc;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author linyehai
 */
public class UUIDUtils {
    public static String[] chars = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "0", "A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    };

    /**
     * 获取UUID
     *
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取指定数目的UUID
     *
     * @param number UUID数量
     * @return UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] retArray = new String[number];
        for (int i = 0; i < number; i++) {
            retArray[i] = getUUID();
        }
        return retArray;
    }

    /**
     * 获取8位UUID
     *
     * @return
     */
    public static String getShortUuid() {
        StringBuffer stringBuffer = new StringBuffer();
        String uuid = getUUID();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            stringBuffer.append(chars[x % 0x3E]);
        }
        return stringBuffer.toString();
    }
}
