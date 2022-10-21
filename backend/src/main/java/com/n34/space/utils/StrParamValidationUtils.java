package com.n34.space.utils;

import java.util.regex.Pattern;

public class StrParamValidationUtils {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    // 1到64个字母、数字或下划线
    private static final Pattern USERNAME_REGEX = Pattern.compile("^\\w{1,64}$");
    // 6到16个字母、数字或下划线
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^\\w{6,16}$");
    // 1到64个非空白字符
    private static final Pattern NICKNAME_REGEX = Pattern.compile("^\\S{1,64}$");

    public static boolean checkUsername(String username) {
        return RegexUtils.match(username, USERNAME_REGEX);
    }

    public static boolean checkPassword(String password) {
        return RegexUtils.match(password, PASSWORD_REGEX);
    }

    public static boolean checkNickname(String nickname) {
        return RegexUtils.match(nickname, NICKNAME_REGEX);
    }

    public static boolean checkEmail(String email) {
        return RegexUtils.match(email, EMAIL_REGEX);
    }
}
