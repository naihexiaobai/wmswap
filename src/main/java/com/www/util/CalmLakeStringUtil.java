package com.www.util;

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

    public static Byte StringToByte(String integer){
        return  Byte.valueOf(integer);
    }

    /**
     * Ascii转换为字符串
     *
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * 字符串转换为Ascii
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    public static String getSubString(String time, int start, int end) {
        String string = time.substring(start, end);
        return string;
    }

}
