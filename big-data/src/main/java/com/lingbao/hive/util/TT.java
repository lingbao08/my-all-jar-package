package com.lingbao.hive.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lingbao08
 * @desc
 * @date 5/4/21 22:48
 **/
@Slf4j
public class TT {


    public static void main(String[] args) {
        System.out.println(powerSum(257));

//        Semaphore
        ReentrantLock reentrantLock = new ReentrantLock();

        reentrantLock.lock();
    }

    /**
     * 给定一个数字n，计算n 能否是哪些数字的平方的和。
     * eg:257==> 257 = 16^2 + 1^2，则257返回true。
     *
     * @param num
     * @throws Exception
     */
    private static boolean powerSum(int num) {

        if (num == 0 || num == 1) {
            return true;
        }

        //有可能不会让你用这个pow方法的，这里只是部分加快，即便没有这一段，也可以很好的执行
        Double d = Math.pow(num, 1 / 2d);
        if (d.intValue() == d) {
            return true;
        }


        // 计算开方后的整数向上取整
        long half = num / 2 + 1;
        long low = 0, high = half;

        if (half * half < 0) {
            log.error("该数字已经超出long的范畴");
            return false;
        }

        while (half * half > num) {
            high = half;
            low = half = half / 2 + 1;
        }

        int maxSqrt = 0;

        for (long i = low; i <= high; i++) {
            if (i * i > num) {
                maxSqrt = (int) i;
                break;
            }
        }

        log.info("low:{},high:{},maxSqrt:{}", low, high, maxSqrt);

        // 定住一端，夹击寻找
        for (int i = maxSqrt; i >= maxSqrt / 2; i--) {

            for (int j = 0; j <= maxSqrt / 2; j++) {
                int sum = i * i + j * j;
                if (sum > num) {
                    break;
                }

                if (sum == num) {
                    log.info("{} = {}^2 + {}^2 ", num, i, j);
                    return true;
                }
            }

        }


        return false;
    }
}
