package com.lingbao.cache.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * 基于jedis的分布式锁
 *
 * 在复制使用时，需要注意对应的包
 *
 * @author lingbao08
 * @desc
 * @date 5/3/21 15:57
 **/
@Slf4j
public class JedisLock {

    @Autowired
    private Jedis jedis;

    public boolean lock(String k, long mills) {

        try {

            String result = jedis.set(k, k, SetParams.setParams().nx().px(mills));

            return StringUtils.equals("OK", result);
        } catch (Exception e) {
            log.error("jedis锁错误，", e);
            return false;
        }
    }
}
