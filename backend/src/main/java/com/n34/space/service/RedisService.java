package com.n34.space.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    void setex(String k, String v, long timeout, TimeUnit timeUnit);

    void hset(String key, Object hashKey, Object value);

    Object hget(String key, Object hashKey);

    Boolean expire(String key, long timeout, TimeUnit unit);

    void hmset(String key, Map<Object, Object> m);

    Boolean del(String key);
}
