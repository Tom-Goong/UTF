package com.yaoting.utf.infrastructure.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static String removeSubStr(String origin, String str2Del, String regex) {
        List<String> strs = split2List(origin, regex);
        strs.remove(str2Del);
        return join2String(strs, regex);
    }

    public static String addSubStr(String origin, String str2Add, String regex) {
        List<String> strs = split2List(origin, regex);
        if (!strs.contains(str2Add)) {
            strs.add(str2Add);
        }
        return join2String(strs, regex);
    }

    public static List<String> split2List(String str, String regex) {
        if (!org.springframework.util.StringUtils.hasText(str)) {
            return new ArrayList<>();
        }
        List<String> strList = new ArrayList<>();
        for (String elem: str.split(regex)) {
            if (!hasText(elem)) {
                continue;
            }
            strList.add(elem);
        }
        return strList;
    }

    public static String join2String(List<String> strList, String str) {
        if (strList == null || strList.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String elem: strList) {
            if (!hasText(elem)) {
                continue;
            }
            sb.append(elem).append(str);
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : sb.toString();
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for(int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}
