package com.n34.space.utils;

public class FontUtils {
    public static String emphasize(String text, String word) {
        return text.replaceAll("(?i)" + word, "<b>" + word + "</b>");
    }
}
