package com.www.util;

import java.util.Locale;

/**
 * 进制转换
 *
 * @auther CalmLake
 * @create 2017/11/14  10:32
 */
public class HexUtil {

    public static byte intToByte(int i) {
        return Integer.valueOf(i).byteValue();
    }

    public static void main(String[] arg) {
        //  01 xor A0 xor 7C xor FF xor 02 = 20
        int q = 0xff31^0xff32^0xff33^0xff34;
        int w = 10;
        int e = 0xff7C;
        int r = 0xffFF;
        int t = 0xff02;
        System.out.println("异或结果--" + Integer.toHexString(45));

    }

    public static void main1(String[] arg) {

        byte[] bytes = new byte[512];
        //MCKey-4
        bytes[0] = intToByte(0xff49);
        bytes[1] = intToByte(0xff48);
        bytes[2] = intToByte(0xff49);
        bytes[3] = intToByte(0xff57);
        //机器号-4，SC01-54 43 30 31
        bytes[4] = intToByte(0xff54);
        bytes[5] = intToByte(0xff43);
        bytes[6] = intToByte(0xff30);
        bytes[7] = intToByte(0xff31);
        //cycle-2,13 载货下车
        bytes[8] = intToByte(Integer.valueOf(13).byteValue());
        //作业区分-2,01 入库
        bytes[9] = intToByte(0);
        bytes[10] = intToByte(1);
        //货型高度-1
        bytes[11] = intToByte(0);
        //货型宽度-1
        bytes[12] = intToByte(0);
        //排-2
        bytes[13] = intToByte(0);
        bytes[14] = intToByte(1);
        //列-2
        bytes[15] = intToByte(0);
        bytes[16] = intToByte(6);
        //层-2
        bytes[17] = intToByte(0);
        bytes[18] = intToByte(1);
        //站台-4,ML02-4D 4C 30 32
        bytes[19] = intToByte(0xff4D);
        bytes[20] = intToByte(0xff4C);
        bytes[21] = intToByte(0xff30);
        bytes[22] = intToByte(0xff32);
        //码头-4
        bytes[23] = intToByte(0xff4D);
        bytes[24] = intToByte(0xff4C);
        bytes[25] = intToByte(0xff30);
        bytes[26] = intToByte(0xff32);
        String sss = getBCC(bytes);
        String s = strToHexStr(sss);
        int i = 0;
    }

    public static String getBCC(byte[] data) {
        String ret = "";
        byte BCC[] = new byte[1];
        for (int i = 0; i < data.length; i++) {
            BCC[0] = (byte) (BCC[0] ^ data[i]);
        }
        String hex = Integer.toHexString(BCC[0] & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        ret += hex.toUpperCase();
        return ret;
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String strToHexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     *
     * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStrToStr(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @param b byte数组
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            // sb.append(" ");
        }
        return sb.toString().toUpperCase(Locale.ENGLISH).trim();
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @param src Byte字符串，每个Byte之间没有分隔符
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
        }
        return ret;
    }

    /**
     * String的字符串转换成unicode的String
     *
     * @param strText 全角字符串
     * @return String 每个unicode之间无分隔符
     * @throws Exception
     */
    public static String strToUnicode(String strText) throws Exception {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u" + strHex);
            else
                str.append("\\u00" + strHex);  // 低位在前面补00
        }
        return str.toString();
    }

    /**
     * unicode的String转换成String的字符串
     *
     * @param hex 16进制值字符串 （一个unicode为2byte）
     * @return String 全角字符串
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            String s1 = s.substring(2, 4) + "00";          // 高位需要补上00再转
            String s2 = s.substring(4);                    // 低位直接转
            int n = Integer.valueOf(s1, 16) + Integer.valueOf(s2, 16);  // 将16进制的string转为int
            char[] chars = Character.toChars(n);           // 将int转换为字符
            str.append(new String(chars));
        }
        return str.toString();
    }

    /**
     * @功能: BCD码转为10进制串(阿拉伯数据)
     * @参数: BCD码
     * @结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
                .toString().substring(1) : temp.toString();
    }

    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }
}
