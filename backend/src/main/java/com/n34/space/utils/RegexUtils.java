package com.n34.space.utils;

import java.util.regex.Pattern;

public class RegexUtils {
    public static boolean match(String str, Pattern regex) {
        if (str == null) return false;
        if (regex == null) return false;
        return regex.matcher(str).matches();
    }
}
