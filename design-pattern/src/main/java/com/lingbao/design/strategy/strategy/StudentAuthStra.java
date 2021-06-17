package com.lingbao.design.strategy.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-13 23:43
 **/
@Order(1)
@Component
public class StudentAuthStra implements AuthStrategy {

    @Override
    public boolean userType(UserType userType) {
        return userType == UserType.STUDENT;
    }

    @Override
    public String exec() {
        return "Stu";
    }
}
