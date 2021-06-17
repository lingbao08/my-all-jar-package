package com.lingbao.hive.model;

import lombok.Data;

/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 11:18
 **/
@Data
public class FailedRecord {

    private String BatchId;

    private Long  JobId;

    private String data;

    private String tableName;

    private Long dataId;


    private String remark;
}
