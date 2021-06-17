package com.lingbao.design.template;

import com.lingbao.design.template.model.ChinaMobileSupplier;
import com.lingbao.design.template.model.ChinaUnion185Supplier;
import com.lingbao.design.template.model.ChinaUnionSupplier;
import com.lingbao.design.template.model.Supplier;
import org.springframework.stereotype.Component;

/**
 * @author lingbao08
 * @desc
 * @date 5/18/21 20:05
 **/

public class ChinaUnion185CryImpl implements ChinaUnionCryImpl<ChinaUnion185Supplier> {


    @Override
    public void runMills(ChinaUnionSupplier driver) {

        System.out.println("我是185号段");

        call();

    }


    private void call() {
        System.out.println("给185号段的人打电话！");
    }
}
