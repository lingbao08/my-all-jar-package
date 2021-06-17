package com.lingbao.disruptor.handler.single;

import com.lingbao.disruptor.model.Order;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 22:10
 **/

@Slf4j
public class Handler1 implements EventHandler<Order>, WorkHandler<Order> {

    // EventHandler
    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        log.error("走过场handler1:set name");
        event.setOrderName("NNN");
//        Thread.sleep(100);
    }

    @Override
    public void onEvent(Order order) throws Exception {
        log.info("WorkHandler handler1：走过场1");
    }
}
