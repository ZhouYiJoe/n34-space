package com.n34.space.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AiUtils {
    private static final String BASE_URL = "http://127.0.0.1:5000/";
    private static final String SENTIMENT_URL = BASE_URL + "get_sentiment";
    private static final String CATEGORY_URL = BASE_URL + "get_category";
    private static final String ALL_CATEGORIES_URL = BASE_URL + "get_all_categories";

    public static boolean getSentiment(String text) {
        Map<String, Object> param = new HashMap<>();
        param.put("text", text);
        String response = HttpRequest.post(SENTIMENT_URL)
                .body(JSONUtil.toJsonStr(param)).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return (boolean) jsonObject.get("extreme");
    }

    public static String getCategory(String text) {
        Map<String, Object> param = new HashMap<>();
        param.put("text", text);
        String response = HttpRequest.post(CATEGORY_URL)
                .body(JSONUtil.toJsonStr(param)).execute().body();
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return (String) jsonObject.get("category");
    }

    public static List<String> getAllCategories() {
        String response = HttpRequest.get(ALL_CATEGORIES_URL).execute().body();
        return JSONUtil.toList(response, String.class);
    }
}
