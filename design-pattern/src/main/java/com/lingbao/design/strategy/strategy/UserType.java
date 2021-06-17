package com.lingbao.design.strategy.strategy;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-13 23:40
 **/
@ToString
@Getter
@AllArgsConstructor
public enum UserType {

    STUDENT(1),
    TEACHER(2),
    LEADER(10),

    UNKNOWN(-1),
    ;

    private int code;


    public static UserType random() {

        return Arrays.stream(values()).collect(Collectors.toList())
                .get(new Random().nextInt(values().length));

    }

    public static void main(String[] args) {
        System.out.println(random());
    }
}
