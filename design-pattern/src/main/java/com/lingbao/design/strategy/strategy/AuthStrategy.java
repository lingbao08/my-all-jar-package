package com.lingbao.design.strategy.strategy;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-13 23:40
 **/

public interface AuthStrategy {

    boolean userType(UserType userType);

    String exec();

}
