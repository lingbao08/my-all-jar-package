package com.lingbao.springboot.start;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.phone")
public class PhoneProperties {

    private String name = "小米";

    private Integer storage = 1024;

    private String money = "1299rmb";

    //get set 方法
}