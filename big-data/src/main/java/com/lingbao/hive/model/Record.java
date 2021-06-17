package com.lingbao.hive.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 11:15
 **/

@Data
public class Record {

    private Map<String, Column> columns;


    public Column getColumn(String fieldName) {

        return columns.get(fieldName);
    }


    @Data
    @AllArgsConstructor
   public static class Column {

        private String name;

        private Object value;

    }
}
