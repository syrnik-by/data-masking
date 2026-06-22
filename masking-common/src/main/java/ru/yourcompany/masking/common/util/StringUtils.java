package ru.yourcompany.masking.common.util;

public final class StringUtils {

    private StringUtils() {}

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static String maskMiddle(String value, char maskChar, int visibleLeft, int visibleRight) {
        if (isBlank(value) || value.length() <= visibleLeft + visibleRight) return value;
        String left = value.substring(0, visibleLeft);
        String right = value.substring(value.length() - visibleRight);
        String middle = String.valueOf(maskChar).repeat(value.length() - visibleLeft - visibleRight);
        return left + middle + right;
    }
}
