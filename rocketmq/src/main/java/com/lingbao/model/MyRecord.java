package com.lingbao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingbao08
 * @desc
 * @date 10/31/21 18:47
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyRecord {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 消息
     */
    private String msg;

    /**
     * 时间戳
     */
    private long timestamp;
}
