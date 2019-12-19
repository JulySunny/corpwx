package com.gabriel.corpwx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: yq
 * @date: 2019/12/19 22:38
 * @discription
 */
@Component
public class RedisService {

    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Object getZSet(final String key, long start, long end) {
        Set<?> result = null;
        ZSetOperations<Serializable, Object> operations = redisTemplate.opsForZSet();
        result = operations.range(key, 0, -1);
        return result;
    }

    public Object getZSetAll(final String key) {
        return getZSet(key, 0, -1);
    }

    public boolean setZSet(final String key, Object value, double score) {
        boolean result = false;
        try {
            ZSetOperations<Serializable, Object> operations = redisTemplate.opsForZSet();
            result = operations.add(key, value, score);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long removeZSet(final String key, Object... arr) {
        Long result = 0L;
        try {
            ZSetOperations<Serializable, Object> operations = redisTemplate.opsForZSet();
            result = operations.remove(key, arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * redis 计数
     *
     * @param key
     * @param delta
     * @return
     */
    public Long incr(final String key, long delta) {
        Long value = null;
        try {
            value = redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 基于 setnx 和 getset 加锁
     * @param key
     * @param value
     * @return
     */
    public boolean lock(String key, String value) {
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }

        String currValue = (String) redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(currValue) && Long.parseLong(currValue) < System.currentTimeMillis()) {
            String oldValue = (String) redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currValue)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 基于 setnx 和 getset 加锁
     * @param key
     * @param value
     * @return
     */
    public void unLock(String key, String value) {
        try {

            String currValue = (String) redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currValue) && currValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }

        } catch (Exception e) {
            logger.error("解锁异常 -> " + e.getMessage(), e);
        }
    }
}
