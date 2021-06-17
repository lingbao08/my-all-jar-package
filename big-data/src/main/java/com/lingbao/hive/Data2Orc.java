package com.lingbao.hive;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lingbao.base.utils.DateUtil;
import com.lingbao.base.utils.StringUtil;
import com.lingbao.hive.model.*;
import com.lingbao.hive.util.DefaultRecordBuilder;
import com.lingbao.hive.util.RecordTransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.orc.OrcConf;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.TypeDescription.Category;
import org.apache.orc.Writer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 11:05
 **/
@Slf4j
public class Data2Orc {


    public static final int _1M = 1024 * 1024;

    public static TypeDescription getHiveSchema(LinkedHashMap<String, MappingModel> mappings) {
        TypeDescription schema = TypeDescription.createStruct();

        mappings.values().forEach(
                x -> schema.addField(x.getTargetColumnName(), getTypeDescriptionByJavaType(x.getTargetColumnType()))
        );

        return schema;
    }

    private static TypeDescription getTypeDescriptionByJavaType(String type) {
        type = StringUtils.deleteWhitespace(type);
        if (StringUtils.startsWithIgnoreCase(type, "decimal(")) {
            final TypeDescription decimal = TypeDescription.createDecimal();
            final String substringBetween = StringUtils.substringBetween(type, "(", ")");
            final String[] split = StringUtils.split(substringBetween, ',');
            return decimal.withScale(Integer.parseInt(split[1])).withPrecision(Integer.parseInt(split[0]));
        }
        if (Category.DECIMAL.getName().equalsIgnoreCase(type)) {
            return TypeDescription.createDecimal().withScale(0).withPrecision(10);
        }
        for (Category category : Category.values()) {
            if (category.getName().equalsIgnoreCase(type)) {
                return new TypeDescription(category);
            }
        }
        return TypeDescription.createString();
    }

    public static String[] concatValue(ObjectNode objectNode, Map<String, TransformationChain> transformationChainMap,
                                       LinkedHashMap<String, MappingModel> mappingMap) {

        String[] result = new String[mappingMap.size()];
        if (objectNode == null) {
            return result;
        }

        DefaultRecord defaultRecord = DefaultRecordBuilder.transform(objectNode, mappingMap);

        Record record = RecordTransformUtil.transform(transformationChainMap, defaultRecord);

        int i = 0;
        Collection<MappingModel> values = mappingMap.values();

        for (MappingModel value : values) {
            Object value1 = record.getColumn(value.getTargetColumnName()).getValue();
            if (value1 != null) {
                result[i] = value1.toString();
            }
            i++;
        }

        return result;
    }


    public static String createFileName() {
        String dateStr = DateUtil.toYMDString(new Date());
        String executorCode = "localhost:10020";

        executorCode = StringUtil.removeChars(executorCode, ':', '.');

        String fileName = executorCode + "_" +
                System.currentTimeMillis() + "_" + Thread.currentThread().getId() + "_" +
                ThreadLocalRandom.current().nextInt(100);

        return "/etl_date=" + dateStr + "/" + fileName;
    }

    public static Writer createWriter(File file, TypeDescription schema) {
        try {
            final Configuration configuration = new Configuration();
            configuration.setInt(OrcConf.ROWS_BETWEEN_CHECKS.getAttribute(), 100);
            final OrcFile.WriterOptions writerOptions = OrcFile.writerOptions(configuration);
            writerOptions.setSchema(schema);
            writerOptions.overwrite(true);
            // 默认 64M 条带大小更少内存
            // writerOptions.stripeSize(10 * _1M);
            // 禁用索引，减少内存
            writerOptions.rowIndexStride(0);
            return OrcFile.createWriter(new Path(file.toURI()), writerOptions);
        } catch (IOException e) {
            log.error("创建Writer错误：{}", e);
        }
        return null;
    }


    public static FailedRecord buildFailedRecord(ObjectNode failData, String message) {
        FailedRecord failedRecord = new FailedRecord();
        failedRecord.setBatchId("1");
        failedRecord.setJobId(1L);
        failedRecord.setData(failData.toString());
        failedRecord.setRemark("值错误：" + message);
        failedRecord.setTableName("xxx");
        failedRecord.setDataId(1L);
        return failedRecord;
    }

}
