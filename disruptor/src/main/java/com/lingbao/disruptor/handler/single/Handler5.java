package com.lingbao.disruptor.handler.single;

import com.lingbao.disruptor.model.Order;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author lingbao08
 * @desc
 * @date 5/17/21 22:12
 **/

@Slf4j
public class Handler5 implements EventHandler<Order> {

    @Override
    public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
        event.setOrderId(NumberUtils.toInt(event.getOrderId() + "1"));
        log.info("handler5：走过场5");
//        Thread.sleep(100);
    }
}
