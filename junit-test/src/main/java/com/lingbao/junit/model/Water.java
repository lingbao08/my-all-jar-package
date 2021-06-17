package com.lingbao.junit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingbao08
 * @desc
 * @date 5/18/21 22:34
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Water {

    private Integer weight;

    private String name;


}
