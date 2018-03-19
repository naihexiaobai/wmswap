package com.thief.wcs.communication;

/**
 * bcc
 *
 * @auther CalmLake
 * @create 2018/3/14  11:45
 */
public class BccGenerator {
    public static boolean IsBccRight(String contentData, String bccData) {
        return GetBcc(contentData).equals(bccData);
    }

    public static String GetBcc(String contentData) {
        if (contentData.isEmpty()) {
            return "";
        } else {
            int bcc = 0;
            for (int i = 0; i < contentData.length(); i++) {
                bcc = bcc ^ contentData.charAt(i);
            }
            String bccData = Integer.toHexString(bcc);
            if (bccData.length() == 1) {
                bccData = "0" + bccData;
            }
            return bccData.toUpperCase();
        }
    }

    public static  void main(String [] args){
//        [00021001101561SC01524]
//      [00021001101561SC04521]
        System.out.println(GetBcc("1SC045"));
    }
}
