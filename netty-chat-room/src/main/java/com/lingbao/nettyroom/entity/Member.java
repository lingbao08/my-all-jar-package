package com.lingbao.nettyroom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingbao08
 * @desc
 * @date 10/21/21 23:33
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 用户姓名
     */
    private String userName;
}
