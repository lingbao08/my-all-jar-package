package com.lingbao.design.template;

import com.lingbao.design.template.model.ChinaMobileSupplier;
import com.lingbao.design.template.model.ChinaUnionSupplier;
import com.lingbao.design.template.model.Supplier;

/**
 * @author lingbao08
 * @desc
 * @date 5/18/21 20:05
 **/

public interface ChinaUnionCryImpl<T extends ChinaUnionSupplier> extends CryInterface<ChinaUnionSupplier> {

    @Override
    default void runMills(ChinaUnionSupplier driver) {
        System.out.println("我是中国联通！");
    }
}
