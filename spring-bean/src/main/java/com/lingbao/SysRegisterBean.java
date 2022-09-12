package com.lingbao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lingbao08
 * @date 2022-09-12 18:07:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysRegisterBean {

    /**
     * 要注册的class
     */
    private Class registerClass;

    /**
     * 别名
     */
    private String nickname;

    /**
     * 内部属性
     */
    private Map<String, Object> propMap;

    private String initMethod;

    private String destoryMethod;

    @Builder.Default
    private boolean primary = false;

}
