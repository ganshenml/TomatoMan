package com.example.ganshenml.tomatoman.util;

import android.text.TextUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符的工具类
 */
public final class StringTool {

    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0-9]))\\d{8}$");

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }


    public static boolean checkPassWord(String password) {
        boolean isPassWord = true;
        final String pattern2 = "^[0-9a-zA-Z]{6,16}$";
        Pattern pattern = Pattern.compile(pattern2);
        Matcher mat = pattern.matcher(password);
        if (!password.equals("") && !mat.find()) {
            isPassWord = false;
        }
        return isPassWord;

    }


    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        return false;
    }


    public static boolean isPwdToShort(String pwd) {
        if (pwd == null || "".equals(pwd))
            return true;

        if (pwd.length() < 6) {
            return true;
        }

        return false;
    }


    /**
     * 判断数据是否存在
     *
     * @param data
     * @return
     */
    public static boolean hasData(List<?> data) {
        if (data != null && !data.isEmpty()) {
            return true;
        }

        return false;
    }


    public static String replaceDoubleQuotationMarks(String ssid) {
        return ssid.replaceAll("\"", "");
    }

    public static String convertToQuotedString(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }

        final int lastPos = string.length() - 1;
        if (lastPos < 0 || (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
            return string;
        }

        return "\"" + string + "\"";
    }

    /**
     * @param serverVersion:服务器的版本信息
     * @param doorVersion：本地数据库存储的版本信息
     * @return
     */
    public static boolean downOtaByCompareVersion(String serverVersion, String doorVersion) {

        if (StringTool.isEmpty(doorVersion)) {
            return true;
        }

        if (StringTool.isEmpty(serverVersion)) {
            return true;
        }

        int compareId = serverVersion.compareToIgnoreCase(doorVersion);
        return compareId > 0;
    }


    /**
     * 比较版本
     *
     * @param serverVersion 服务器版本
     * @param doorVersion   door版本
     * @return
     */
    public static boolean compareEqualsVersion(String doorVersion, String serverVersion) {//这块弄反了，因此将serverVersion和doorVersion对调了下_Aaron_6.29
        LogTool.log(LogTool.Aaron, "固件升级  compareEqualsVersion 开始比较两个版本 ");

        if (StringTool.isEmpty(doorVersion)) {
            return false;
        }

        if (StringTool.isEmpty(serverVersion)) {
            return false;
        }

        int compareId = serverVersion.compareToIgnoreCase(doorVersion);
        LogTool.log(LogTool.Aaron, "serverVersion： " + serverVersion + "   doorVersion:  " + doorVersion + " 比较结果 = " + compareId);
        return compareId <= 0;//由==变为了<=，因为可能会有本地版本高于服务器版本

    }


    /**
     * 统计字符串中的字节数（中文按3个字节算）
     *
     * @param string
     * @return
     */
    public static int getStringByteCount(String string) {
        String E1 = "[\u4e00-\u9fa5]";// 中文
        String E2 = "[a-zA-Z]";// 英文
        String E3 = "[0-9]";// 数字

        int chineseCount = 0;
        int englishCount = 0;
        int numberCount = 0;

        String temp;
        for (int i = 0; i < string.length(); i++) {
            temp = String.valueOf(string.charAt(i));
            if (temp.matches(E1)) {
                chineseCount++;
            }
            if (temp.matches(E2)) {
                englishCount++;
            }
            if (temp.matches(E3)) {
                numberCount++;
            }
        }

        return chineseCount * 3 + englishCount + numberCount;
    }

    /**
     * 判断一个字符串是否是十六进制
     *
     * @param textStr
     * @return
     */
    public static boolean isHexadecimal(String textStr) {

        int length = textStr.length();
        for (int i = 0; i < length; i++) {
            char c = textStr.charAt(i);
            if (((c >= 'a') && (c <= 'f')) || ((c >= 'A') && (c <= 'F')) || ((c >= '0') && (c <= '9'))) {
                continue;
            } else {
                return false;
            }
        }
        return true;

    }


}
