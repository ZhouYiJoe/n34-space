package com.n34.space.utils;

import cn.hutool.core.util.ReUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class RegexUtils {
    public static boolean match(String str, Pattern regex) {
        if (str == null) return false;
        if (regex == null) return false;
        return regex.matcher(str).matches();
    }

    public static Set<String> getAllHashtag(String post) {
        return ReUtil.findAll("#([\\S\\s]+?)#", post, 1, new HashSet<>());
    }

    public static String parseHashtag(String post) {
        return ReUtil.replaceAll(post, "#([\\S\\s]+?)#", "<div style=\"color: oranged;\" link=\"/app/hashtag/\" param=\"$1\">$1</div>");
    }

    public static String correctSearchText(String searchText) {
        return ReUtil.replaceAll(searchText, "[#@]", "");
    }
}
