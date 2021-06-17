package com.lingbao.design.template;

import com.lingbao.design.template.model.ChinaMobileSupplier;
import com.lingbao.design.template.model.Supplier;
import org.springframework.stereotype.Component;

/**
 * @author lingbao08
 * @desc
 * @date 5/18/21 20:05
 **/

public class ChinaMobileCryImpl implements CryInterface<ChinaMobileSupplier> {


    @Override
    public void runMills(ChinaMobileSupplier driver) {
        System.out.println("中国移动牛逼！");

    }
}
