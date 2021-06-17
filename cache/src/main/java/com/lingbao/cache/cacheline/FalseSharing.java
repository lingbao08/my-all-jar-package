package com.lingbao.cache.cacheline;

/**
 * @author lingbao08
 * @DESCRIPTION 使用CPU的缓存行填充，采用java8的写法。
 * @create 3/27/21 10:51
 **/

public final class FalseSharing implements Runnable {
    public static final int NUM_THREADS = 4; // change
    public final static long ITERATIONS = 500_000_000L;
    private final int arrayIndex;
    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        Thread.sleep(1000);
        System.out.println("starting....");

        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));

    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        long i = ITERATIONS + 1;
        while (--i > 0) {
            longs[arrayIndex].value = i;
        }
    }
}
