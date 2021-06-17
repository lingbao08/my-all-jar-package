package com.lingbao.junit.service;

import com.lingbao.junit.BaseTest;
import com.lingbao.junit.model.Water;
import junitparams.FileParameters;
import junitparams.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 下面只列举了部分样例，更多示例，参考
 *
 * @link https://github.com/Pragmatists/JUnitParams/blob/master/src/test/java/junitparams/usage/SamplesOfUsageTest.java
 * <p>
 * 有更多的方法示例
 */
@Slf4j
public class WaterServiceTest extends BaseTest {

    @Autowired
    private WaterService waterService;

    @Test
    @Parameters({
            "nfsq,0,农夫山泉,true",
            "whh,100,娃哈哈,true",
            ",90,怡宝,true",
    })
    public void drink(String drinkName, Integer weight, String result, boolean expect) {
        //此处没有用到expect，仅作展示，表示可以传进来
        Water water = Water.builder().name(drinkName).weight(weight).build();

        String drink = waterService.drink(water);

        print(drink);

        Assert.assertEquals(drink, result);

    }


    /**
     * CSV文件进行test
     *
     * @param age
     * @param name
     */
    @Test
    @FileParameters("src/test/resources/test.csv")
    public void loadParamsFromCsv(int age, String name) {
        print(age + name);
    }


}