package com.lingbao.design.template;

import com.lingbao.design.template.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lingbao08
 * @desc
 * @date 5/18/21 00:02
 **/

public interface CryInterface<T extends Supplier> {

    Logger logger = LoggerFactory.getLogger(CryInterface.class);


    default void driver(T t) {

        logger.info("======开  始==============");

        common();

        //各自内部不同的实现
        runMills(t);

    }

    default void common() {
        logger.info("======一些相同的公共处理==============");
    }

    void runMills(T driver);


}
