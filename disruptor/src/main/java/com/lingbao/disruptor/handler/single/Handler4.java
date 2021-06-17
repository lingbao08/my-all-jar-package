package com.lingbao.disruptor.handler.single;

import com.lingbao.disruptor.model.Order;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 22:10
 **/

@Slf4j
public class Handler4 implements EventHandler<Order> {

    // EventHandler
    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        log.error("走过场handler4:set name");
        event.setOrderName("NNN");
//        Thread.sleep(100);
    }
}
