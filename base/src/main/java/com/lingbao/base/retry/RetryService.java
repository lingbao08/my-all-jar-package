package com.lingbao.base.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sun.jvm.hotspot.utilities.IntegerEnum;

import java.util.concurrent.Callable;
import java.util.function.IntToLongFunction;
import java.util.function.Predicate;

import static com.lingbao.base.retry.RetryPolicyType.FIXED_RETRY_NO_CALLBACK;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 08:45
 **/

@Slf4j
//@Component
@Deprecated
public class RetryService {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;


    private static FixedBackOffPolicy newFixedBackOffPolicy() {
        return new FixedBackOffPolicy();
    }

    private static FixedBackOffPolicy newFixedBackOffPolicy(long period) {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(period);
        return fixedBackOffPolicy;
    }

    private static ExponentialBackOffPolicy newExponentialBackOffPolicy() {
        return new ExponentialBackOffPolicy();
    }

    private static ExponentialBackOffPolicy newExponentialBackOffPolicy(
            long interval, double multiplier, long maxInterval) {
        ExponentialBackOffPolicy policy = newExponentialBackOffPolicy();

        new IntToLongFunction() {
            @Override
            public long applyAsLong(int value) {
                return 0;
            }
        };
        if (interval > 0) {
            policy.setInitialInterval(interval);
        }
        if (multiplier > 0) {
            policy.setMultiplier(multiplier);
        }
        if (maxInterval > 0) {
            policy.setMaxInterval(maxInterval);
        }
        return policy;
    }

    public <T> void exec(Callable<T> callable, Predicate<T> predicate) {
        exec(callable, predicate, null, FIXED_RETRY_NO_CALLBACK, 0, 0, 0, 0);
    }

    public <T> void exec(Callable<T> callable, Predicate<T> predicate, RetryPolicyType retryPolicyType) {
        exec(callable, predicate, null, retryPolicyType, 0, 0, 0, 0);
    }

    public <T> void exec(Callable<T> callable, Predicate<T> predicate, Callable<T> recoveryCallback, RetryPolicyType retryPolicyType) {
        exec(callable, predicate, recoveryCallback, retryPolicyType, 0, 0, 0, 0);
    }


    public <T> void exec(Callable<T> retryCallback, Predicate<T> predicate, Callable<T> recoveryCallback,
                         RetryPolicyType retryPolicyType,
                         int execTimes, long interval, double multiplier, long maxInterval) {

        Assert.notNull(retryCallback, "retryCallback is not empty！");

        Assert.notNull(predicate, "predicate is not empty！");

        RetryPolicy retryPolicy = execTimes > 0 ? new SimpleRetryPolicy(execTimes) : new SimpleRetryPolicy();
        BackOffPolicy backoffPolicy;
        switch (retryPolicyType) {
            case DELAY_RETRY_WITH_CALLBACK:
                Assert.notNull(recoveryCallback, "recoveryCallback is not empty！");
            case DELAY_RETRY_NO_CALLBACK:
                backoffPolicy = newExponentialBackOffPolicy(interval, multiplier, maxInterval);
                break;
            case FIXED_RETRY_WITH_CALLBACK:
                Assert.notNull(recoveryCallback, "recoveryCallback is not empty！");
            case FIXED_RETRY_NO_CALLBACK:
            default:
                backoffPolicy = newFixedBackOffPolicy(interval);
                break;
        }
        exec(retryCallback, predicate, retryPolicy, backoffPolicy);
    }

    /**
     * 默认执行固定频次
     */
    public <T> void exec(Callable<T> callable, Predicate<T> predicate, RetryPolicy retryPolicy, BackOffPolicy policy) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(policy);
        RetryCallback<Object, Throwable> retry = context -> {
            log.info("============ retry =======================");
            if (predicate.test(callable.call())) {
                throw new RuntimeException(":::::::::::::: RETRY :::::::::::::::");
            }
            return true;
        };
        RecoveryCallback<Object> recovery = context -> {
            log.info("============ recovery =======================");
            return null;
        };


        taskExecutor.execute(() -> {
            try {
                retryTemplate.execute(retry, recovery);
            } catch (Throwable throwable) {
                log.error("重试出错，{}", throwable);
            }
        });

    }
}
