package com.ren.util;

/**
 * 字符处理工具类
 *
 * @auther CalmLake
 * @create 2017/11/3  16:24
 */
public class CalmLakeStringUtil {

    public static int stringToInt(String string) throws Exception {
        return Integer.valueOf(string);
    }

    public static boolean stringIsNull(String string) {
        if (string == null || string.equals("")) {
            return true;
        }
        return false;
    }

}
