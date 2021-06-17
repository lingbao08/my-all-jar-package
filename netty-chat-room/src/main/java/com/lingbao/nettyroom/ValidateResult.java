package com.lingbao.nettyroom;

import lombok.Data;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2019-02-22 22:05
 **/

@Data
public class ValidateResult {
    private boolean success;

    private String cause;

    private ValidateResult() {
    }

    private ValidateResult(boolean success, String cause) {
        this.success = success;
        this.cause = cause;
    }

    public static ValidateResult ofSuccess() {
        return new ValidateResult(true, "");
    }

    public static ValidateResult ofFailed(String cause) {
        return new ValidateResult(false, cause);
    }


    public static ValidateResult of(boolean success, String cause) {
        return new ValidateResult(success, cause);
    }
}
