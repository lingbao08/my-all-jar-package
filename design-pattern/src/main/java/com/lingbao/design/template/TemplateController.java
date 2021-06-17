package com.lingbao.design.template;

import com.lingbao.design.strategy.strategy.AuthStrategy;
import com.lingbao.design.strategy.strategy.UserType;
import com.lingbao.design.template.model.ChinaMobileSupplier;
import com.lingbao.design.template.model.ChinaUnion185Supplier;
import com.lingbao.design.template.model.ChinaUnionSupplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-13 23:46
 **/
public class TemplateController {


    public static void main(String[] args) {

        new ChinaUnionOthersCryImpl().driver(new ChinaUnionSupplier());

        System.out.println("---------------");

        new ChinaUnion185CryImpl().driver(new ChinaUnionSupplier());

        System.out.println("---------------");
        new ChinaMobileCryImpl().driver(new ChinaMobileSupplier());


    }

}
