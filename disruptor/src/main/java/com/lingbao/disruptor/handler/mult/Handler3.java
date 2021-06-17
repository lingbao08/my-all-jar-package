package com.lingbao.disruptor.handler.mult;

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
public class Handler3 implements WorkHandler<Order> {

    @Override
    public void onEvent(Order event) throws Exception {
        log.error("走过场handler3:set name");
        event.setOrderName("NNN");
//        Thread.sleep(100);
    }
}
