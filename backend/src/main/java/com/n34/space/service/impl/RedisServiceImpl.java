package com.n34.space.service.impl;

import com.n34.space.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void setex(String k, String v, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(k, v, timeout, timeUnit);
    }

    @Override
    public void hset(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Object hget(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void hmset(String key, Map<Object, Object> m) {
        redisTemplate.opsForHash().putAll(key, m);
    }

    @Override
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }
}
