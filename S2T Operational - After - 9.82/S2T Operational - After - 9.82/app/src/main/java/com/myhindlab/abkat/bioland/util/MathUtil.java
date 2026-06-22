package com.myhindlab.abkat.bioland.util;

/**
 * @author Administrator
 * @Description:
 * @date 2022/11/11 15:36
 */
public class MathUtil {

    public static String byteToHexString(byte data){
        String hex = Integer.toHexString(data & 0xFF);
        if (hex.length() == 1){
            hex = "0" + hex;
        }
        return  hex;
    }

    /**
     * 16进制转2进制
     * @param a
     * @return
     */
    public static String HToB(String a) {
        String b = Integer.toBinaryString(Integer.valueOf(toD(a, 16)));
        if (b.length() == 7){
            b = "0" + b;
        }else  if (b.length() == 6){
            b = "00" + b;
        }else  if (b.length() == 5){
            b = "000" + b;
        }else  if (b.length() == 4){
            b = "0000" + b;
        }else  if (b.length() == 3){
            b = "00000" + b;
        }else  if (b.length() == 2){
            b = "000000" + b;
        }else  if (b.length() == 1){
            b = "0000000" + b;
        }else  if (b.length() == 0){
            b = "00000000";
        }
        return b;
    }

    public static String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))
                    * Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }

    // 将十六进制中的字母转为对应的数字
    public static int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {
                i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {
            i = 11;
        }
        if (a.equals("c")) {
            i = 12;
        }
        if (a.equals("d")) {
            i = 13;
        }
        if (a.equals("e")) {
            i = 14;
        }
        if (a.equals("f")) {
            i = 15;
        }
        return i;
    }

}
