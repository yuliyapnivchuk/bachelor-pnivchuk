package com.itstep.util;

public class StringUtil {
    public static String deleteSpecChars(String str) {
        return str.replaceAll("\n", " ")
                .replaceAll("[^\\p{L} ]", "");
    }
}
