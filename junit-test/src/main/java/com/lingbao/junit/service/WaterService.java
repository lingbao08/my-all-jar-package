package com.lingbao.junit.service;

import com.lingbao.junit.model.Water;
import com.lingbao.springboot.test.PhoneProperties;
import com.lingbao.springboot.test.service.PhoneService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

/**
 * @author lingbao08
 * @desc
 * @date 5/18/21 22:33
 **/
@Service
public class WaterService {

    public String drink(Water water) {


        if (StringUtils.equals(water.getName(), "nfsq")) {
            return "农夫山泉";
        }
        if (StringUtils.startsWith(water.getName(), "wh")) {
            throw new IllegalArgumentException("警告：不允许用娃哈哈矿泉水！");
        }
        if (water.getWeight() != null && water.getWeight() > 100) {
            return "桶装水";
        }

        return "其他水";

    }


}
