package com.lingbao.design.strategy.strategy;

import org.springframework.stereotype.Component;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-13 23:44
 **/
@Component
public class DefaultStra implements AuthStrategy {
    @Override
    public boolean userType(UserType userType) {
        return true;
    }

    @Override
    public String exec() {
        return "Def";
    }
}
