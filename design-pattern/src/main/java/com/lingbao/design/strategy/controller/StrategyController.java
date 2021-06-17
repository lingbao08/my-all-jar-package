package com.lingbao.design.strategy.controller;

import com.lingbao.design.strategy.strategy.AuthStrategy;
import com.lingbao.design.strategy.strategy.UserType;
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
@Slf4j
@RestController
public class StrategyController {

    @Autowired
    private List<AuthStrategy> authStrategyList;

    @RequestMapping("/")
    public String index() {

        UserType random = UserType.random();

        String exec = authStrategyList.parallelStream()
                .filter(authStrategy -> authStrategy.userType(random)).findFirst().get().exec();

        log.info("本次策略模式选中的内容:{}",exec);

        return exec;
    }


}
