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
public class Handler2 implements WorkHandler<Order> {


    @Override
    public void onEvent(Order order) throws Exception {
        log.error("走过场handler2:set name");
        order.setOrderName("NNN");
//        Thread.sleep(100);
    }
}
