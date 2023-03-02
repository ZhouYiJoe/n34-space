package com.n34.space.utils;

import java.util.List;

public class ConditionUtils {
    private static final List<String> ALL_CATEGORIES = AiUtils.getAllCategories();
    private static final String COND_FORMAT = "and not (category = '%s' and extreme = 1) ";

    public static String filterExtreme(String filterConfig) {
        StringBuilder cond = new StringBuilder();
        for (int i = 0; i < ALL_CATEGORIES.size(); i++) {
            if (filterConfig.charAt(i) == '1') {
                cond.append(String.format(COND_FORMAT, ALL_CATEGORIES.get(i)));
            }
        }
        return cond.toString();
    }
}
