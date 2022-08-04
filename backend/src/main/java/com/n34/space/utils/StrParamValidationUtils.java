package com.n34.space.utils;

public class StrParamValidationUtils {
    private static final String EMAIL_REGEX = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
    private static final String USERNAME_REGEX = "^\\w{1,64}$";
    private static final String PASSWORD_REGEX = "^\\w{6,16}$";
    private static final String NICKNAME_REGEX = "^\\S{1,64}$";

    public static boolean checkUsername(String username) {
        return username != null && username.matches(USERNAME_REGEX);
    }

    public static boolean checkPassword(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }

    public static boolean checkNickname(String nickname) {
        return nickname != null && nickname.matches(NICKNAME_REGEX);
    }

    public static boolean checkEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
