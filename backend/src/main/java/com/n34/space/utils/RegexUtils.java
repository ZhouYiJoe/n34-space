package com.n34.space.utils;

import cn.hutool.core.util.ReUtil;

import java.util.*;
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

    public static boolean checkHashtag(Collection<String> hashtags) {
        for (String hashtag : hashtags) {
            if (hashtag.contains("@")) {
                return false;
            }
        }
        return true;
    }

    public static String parseHashtag(String post) {
        return ReUtil.replaceAll(post, "#([\\S\\s]+?)#", "<span class=\"hashtag-class\" style=\"color: orangered; cursor: pointer;\" link=\"/app/hashtag/\" param=\"$1\">#$1#</span>");
    }

    public static String correctSearchText(String searchText) {
        return ReUtil.replaceAll(searchText, "[#@]", "");
    }

    public static List<String> getAllAtUsername(String post) {
        return ReUtil.findAll("@(\\S+)", post, 1, new ArrayList<>());
    }

    public static String parseAtSymbol(String post) {
        return ReUtil.replaceAll(post, "@(\\S+)", "<span class=\"at-symbol-class\" style=\"color: orangered; cursor: pointer;\" link=\"/app/users/\" param=\"$1\">@$1</span>");
    }

    public static boolean checkAtUsername(Collection<String> usernames) {
        for (String username : usernames) {
            if (username.contains("#")) {
                return false;
            }
        }
        return true;
    }
}
