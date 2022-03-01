package com.n34.demo.response;

/**
 * 响应体中携带的响应信息
 */
public enum Status {
    SUCCESS,
    USER_NOT_FOUND,
    POST_NOT_FOUND,
    WRONG_PASSWORD,
    REPEATED_USERNAME,
    REPEATED_EMAIL,
    FAILED,
    INVALID_TOKEN
}
