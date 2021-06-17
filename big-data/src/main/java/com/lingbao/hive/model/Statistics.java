package com.lingbao.hive.model;

import lombok.Data;

/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 12:18
 **/

@Data
public class Statistics {

    //内部要有写成功的统计数，写失败的统计数，（写入到orc文件）
    //内部要有读成功的统计数，读失败的统计数，（从数据库读取数据）
}
