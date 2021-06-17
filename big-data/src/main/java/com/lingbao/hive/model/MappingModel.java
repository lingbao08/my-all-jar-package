package com.lingbao.hive.model;

import lombok.Data;

/**
 * @author lingbao08
 * @desc 映射关系
 * @date 3/27/21 11:11
 **/

@Data
public class MappingModel {

    private String targetColumnName;

    private String targetColumnType;
}
