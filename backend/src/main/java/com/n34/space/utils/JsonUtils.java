package com.n34.space.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class JsonUtils {
    private static final ObjectMapper OM = new ObjectMapper();

    /**
     * 设置拦截器的响应体
     */
    public static void setResponse(HttpServletResponse response, Object payload) throws IOException {
        byte[] json = new ObjectMapper().writeValueAsBytes(payload);
        OutputStream out = response.getOutputStream();
        out.write(json);
        out.flush();
        out.close();
    }

    public static String toJson(Object obj) {
        try {
            return OM.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return OM.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
