package com.lingbao.base.retry;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 08:44
 **/

@Getter
@AllArgsConstructor
public enum RetryPolicyType {

    FIXED_RETRY_NO_CALLBACK("固定周期失败丢弃"),
    FIXED_RETRY_WITH_CALLBACK("固定周期失败重试"),
    DELAY_RETRY_NO_CALLBACK("延迟周期失败丢弃"),
    DELAY_RETRY_WITH_CALLBACK("延迟周期失败重试"),

    ;

    private String msg;
}
