package com.sapo.mockprojectpossystem.common.util;

public class StringUtils {
    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
