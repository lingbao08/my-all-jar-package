package com.lingbao.disruptor.strategy;

import com.lingbao.base.anno.NotTest;
import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.WaitStrategy;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 23:38
 **/

@NotTest
public class MyStrategy implements WaitStrategy {

    private static final int SPIN_TRIES = 100;

    @Override
    public long waitFor(
            final long sequence, Sequence cursor, final Sequence dependentSequence,
            final SequenceBarrier barrier) throws AlertException, InterruptedException {
        long availableSequence;
        int counter = SPIN_TRIES;

        while ((availableSequence = dependentSequence.get()) < sequence) {
            counter = applyWaitMethod(barrier, counter);
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {
    }

    private int applyWaitMethod(final SequenceBarrier barrier, int counter)
            throws AlertException {
        barrier.checkAlert();

        if (0 == counter) {
            LockSupport.parkNanos(100);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Thread.yield();
        } else {
            --counter;
        }

        return counter;
    }
}
