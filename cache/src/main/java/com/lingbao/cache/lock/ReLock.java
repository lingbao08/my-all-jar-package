package com.lingbao.cache.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 基于redission的redis分布式锁
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 07:58
 **/
@Component
public class ReLock {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * redis 分布式锁
     * @param callable 要执行的方法
     * @param key      分布式锁key
     * @param waitTime 分布式锁获取的等待时间
     * @param lockTime 分布式锁锁定的最大时间
     * @param <T>      返回类型
     * @return
     */
    public <T> T lock(final Callable<T> callable, final String key, long waitTime, long lockTime) throws Exception {
        final RLock getLock = redissonClient.getLock(key);
        try {
            if (getLock.tryLock(waitTime, lockTime, TimeUnit.SECONDS)) {
                return callable.call();
            }
            throw new Exception("获取锁失败");
        } finally {
            getLock.unlock();
        }
    }
}
