package com.lingbao.hive.util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lingbao.hive.model.DefaultRecord;
import com.lingbao.hive.model.MappingModel;

import java.util.LinkedHashMap;

/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 12:07
 **/

public class DefaultRecordBuilder {
    public static DefaultRecord transform(ObjectNode objectNode, LinkedHashMap<String, MappingModel> mappingMap) {

        return new DefaultRecord();
    }
}
