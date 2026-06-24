package ru.psb.masking.common.util;

public final class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static String maskMiddle(String value, int visibleLeft, int visibleRight, char maskChar) {
        if (isBlank(value)) return value;
        int len = value.length();
        if (len <= visibleLeft + visibleRight) return value;
        return value.substring(0, visibleLeft)
                + String.valueOf(maskChar).repeat(len - visibleLeft - visibleRight)
                + value.substring(len - visibleRight);
    }
}
