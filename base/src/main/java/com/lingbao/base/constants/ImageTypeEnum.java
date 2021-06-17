package com.lingbao.base.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-27 10:56
 **/
@Getter
@AllArgsConstructor
public enum ImageTypeEnum {

    JPEG("JPEG"),
    PNG("png"),
    ;

    private String type;


}
