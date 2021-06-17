package com.lingbao.hive;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lingbao.hive.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hive.common.type.HiveDecimal;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.Decimal64ColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DecimalColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.DoubleColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.TimestampColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.TypeDescription;
import org.apache.orc.TypeDescription.Category;
import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.orc.Writer;


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;


/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 11:02
 **/

@Slf4j
public class WriterHelper {

    private static final int ROW_BATCH_SIZE = 100;

    private String hiveTable;
    private String hdfsAddress;
    private String dbPath;
    private String filePathPrefix;
    private double maxSize = 0;
    private LinkedHashMap<String, MappingModel> mappingMap;

    private Map<String, TransformationChain> transformationChainMap;

    private Writer writer;

    private String fileName;

    private JDBC2HiveTask task;

    private TypeDescription schema;
    // 统计信息
    private Statistics statistics;

    private final List<FailedRecord> failedRecordList = new LinkedList<>();
    private List<String> fieldNames;
    private TypeDescription.Category[] schemaCategoryArray;
    private File tempFile;
    private long writeSucceedRecords = 0L;
    private long writeFailedRecords = 0L;
    private long readSucceedRecords = 0L;
    private final Map<Long, VectorizedRowBatch> rowBatchMap = new HashMap<>();

    public WriterHelper(JDBC2HiveTask task, Statistics statistics) {
        this.task = task;
        this.statistics = statistics;
    }

    private VectorizedRowBatch getRowBatch() {
        final Long key = Thread.currentThread().getId();
        VectorizedRowBatch rowBatch = rowBatchMap.get(key);
        if (rowBatch == null) {
            rowBatch = schema.createRowBatch(ROW_BATCH_SIZE);
            rowBatchMap.put(key, rowBatch);
        }
        return rowBatch;
    }

    public void writer(List<ObjectNode> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        final VectorizedRowBatch batch = getRowBatch();
        final List<FailedRecord> failedRecordList = new LinkedList<>();
        while (!list.isEmpty()) {
            batch.reset();
            failedRecordList.clear();
            fillRowBatch(batch, list, failedRecordList);
            writer(batch, failedRecordList);
        }
    }

    private synchronized void writer(VectorizedRowBatch batch, List<FailedRecord> failedRecordList) throws Exception {
        if (writer == null) {
            fileName = Data2Orc.createFileName();
            tempFile = new File(filePathPrefix, fileName).getCanonicalFile();
            writer = Data2Orc.createWriter(tempFile, schema);
        }
        final long succeedRecords = batch.size;
        this.failedRecordList.addAll(failedRecordList);
        writer.addRowBatch(batch);
        writeSucceedRecords += succeedRecords;
        writeFailedRecords += failedRecordList.size();
        readSucceedRecords += succeedRecords + failedRecordList.size();
        if (tempFile.length() >= maxSize) {
            closeWriter();
        }
    }

    private void fillRowBatch(VectorizedRowBatch batch, List<ObjectNode> list, final List<FailedRecord> failedRecordList) {
        final Iterator<ObjectNode> iterator = list.iterator();
        int row = 0;
        while (iterator.hasNext() && row < ROW_BATCH_SIZE) {
            final ObjectNode next = iterator.next();
            iterator.remove();
            try {
                String[] fieldArr = Data2Orc.concatValue(next, transformationChainMap, mappingMap);
                for (int col = 0; col < batch.numCols; col++) {
                    final ColumnVector cv = batch.cols[col];
                    final String val = fieldArr[col];
                    if (cv instanceof TimestampColumnVector) {
                        final TimestampColumnVector timestampColumnVector = (TimestampColumnVector) cv;
                        final long timestamp = NumberUtils.toLong(val, 0L);
                        if (timestamp > 0L) {
                            timestampColumnVector.set(row, new Timestamp(timestamp));
                        } else {
                            timestampColumnVector.set(row, null);
                        }
                    } else if (cv instanceof DecimalColumnVector) {
                        DecimalColumnVector decimalColumnVector = (DecimalColumnVector) cv;
                        if (StringUtils.isNotEmpty(val)) {
                            HiveDecimal hiveDecimal = HiveDecimal.create(val);
                            if (hiveDecimal == null) {
                                throw new IllegalArgumentException(appendExMsg(Category.DECIMAL, fieldNames.get(col), val));
                            }
                            decimalColumnVector.set(row, hiveDecimal);
                        } else {
                            decimalColumnVector.set(row, (HiveDecimal) null);
                        }
                    } else if (cv instanceof Decimal64ColumnVector) {
                        Decimal64ColumnVector decimalColumnVector = (Decimal64ColumnVector) cv;
                        if (StringUtils.isNotEmpty(val)) {
                            HiveDecimal hiveDecimal = HiveDecimal.create(val);
                            if (hiveDecimal == null) {
                                throw new IllegalArgumentException(appendExMsg(Category.DECIMAL, fieldNames.get(col), val));
                            }
                            decimalColumnVector.set(row, hiveDecimal);
                        } else {
                            decimalColumnVector.set(row, (HiveDecimal) null);
                        }
                    } else if (cv instanceof BytesColumnVector) {
                        BytesColumnVector bytesColumnVector = (BytesColumnVector) cv;
                        if (val == null) {
                            bytesColumnVector.setVal(row, new byte[0]);
                            bytesColumnVector.isNull[row] = true;
                            bytesColumnVector.noNulls = false;
                        } else {
                            bytesColumnVector.setVal(row, val.getBytes(StandardCharsets.UTF_8));
                            bytesColumnVector.isNull[row] = false;
                        }
                    } else if (cv instanceof LongColumnVector) {
                        LongColumnVector longColumnVector = (LongColumnVector) cv;
                        if (StringUtils.isNotEmpty(val)) {
                            long value;
                            try {
                                value = Long.parseLong(val);
                            } catch (Exception e) {
                                throw new IllegalArgumentException(appendExMsg(Category.LONG, fieldNames.get(col), val));
                            }
                            if (schemaCategoryArray[col] == Category.INT) {
                                try {
                                    value = Integer.parseInt(val);
                                } catch (Exception e) {
                                    throw new IllegalArgumentException(appendExMsg(Category.INT, fieldNames.get(col), val));
                                }
                            }
                            longColumnVector.vector[row] = value;
                            longColumnVector.isNull[row] = false;
                        } else {
                            longColumnVector.vector[row] = LongColumnVector.NULL_VALUE;
                            longColumnVector.isNull[row] = true;
                            longColumnVector.noNulls = false;
                        }
                    } else if (cv instanceof DoubleColumnVector) {
                        DoubleColumnVector doubleColumnVector = (DoubleColumnVector) cv;
                        if (StringUtils.isNotEmpty(val)) {
                            try {
                                doubleColumnVector.vector[row] = Double.parseDouble(val);
                                doubleColumnVector.isNull[row] = false;
                            } catch (Exception e) {
                                throw new IllegalArgumentException(appendExMsg(Category.DOUBLE, fieldNames.get(col), val));
                            }
                        } else {
                            doubleColumnVector.vector[row] = DoubleColumnVector.NULL_VALUE;
                            doubleColumnVector.isNull[row] = true;
                            doubleColumnVector.noNulls = false;
                        }
                    }
                }
                row++;
            } catch (Exception e) {
                log.error("ORC数据转换失败" + e.getMessage(), e);
                failedRecordList.add(Data2Orc.buildFailedRecord(next, e.getMessage()));
            }
        }
        batch.size = row;
    }

    public void init(Configuration config) {
//        if (mappingMap == null) {
//            // support single table so that get the first column list.
//            List<Object> mappings = config.getList(CoreConstant.DATA_JOB_CONTENT_READER_0_PARAMETER + ".column");
//            if (CollectionUtils.isEmpty(mappings)) {
//                log.error("映射字段为空");
//                throw new IllegalArgumentException("映射字段为空");
//            }
//            mappingMap = new LinkedHashMap<>();
//            // hive data-type map
//            TaskBuild.initHiveType(mappings, mappingMap);
//        }
//
//        hiveTable = Optional.ofNullable(hiveTable).orElse(config.getString(CoreConstant.DATA_JOB_CONTENT_WRITER_PARAMETER_TARGET + ".name"));
//
//        // "hdfs://10.83.192.7:8020"
//        hdfsAddress = Optional.ofNullable(hdfsAddress).orElse(config.getString(CoreConstant.DATA_JOB_CONTENT_WRITER_PARAMETER_TARGET + ".hdfsAddress"));
//        // "/user/hive/warehouse/dmw_test.db/"
//        dbPath = Optional.ofNullable(dbPath).orElse(config.getString(CoreConstant.DATA_JOB_CONTENT_WRITER_PARAMETER_TARGET + ".dbPath"));
//
//        final String configFilePathPrefix = config.getString(CoreConstant.DATA_JOB_CONTENT_WRITER_PARAMETER_TARGET + ".filePathPrefix", "");
//        if (StringUtils.isEmpty(configFilePathPrefix)) {
//            final File tempDirectory = FileUtils.getTempDirectory();
//            final File rootDirectory = new File(tempDirectory, "jdbc-2—hive");
//            filePathPrefix = new File(rootDirectory, Objects.toString(task.getTaskContext().getJobId())).getPath();
//        } else {
//            filePathPrefix = new File(configFilePathPrefix).getAbsolutePath();
//        }
//        filePathPrefix = filePathPrefix.replace('\\', '/');
//
//        final JsonNode setting = JsonNodeUtils.readTree(config.getString(CoreConstant.DATA_JOB_SETTING_PROPERTIES));
//        maxSize = JsonNodeUtils.asLong(setting, CoreConstant.DATA_JOB_SETTING_HIVE_INTERVAL_SIZE, 128);
//        maxSize = Math.max(maxSize * 0.8, 2L);
//        maxSize = maxSize * 1024 * 1024;
//
//        if (StringUtils.isAnyEmpty(hiveTable, hdfsAddress, dbPath)) {
//            log.error("HDFS 信息为空");
//            throw new IllegalArgumentException("HDFS 信息为空");
//        }
//
//        transformationChainMap = Optional.ofNullable(transformationChainMap).orElse(TransformerUtil
//                .buildTransformerInfoMap(config.getConfiguration(CoreConstant.JOB_CONTENT)));
//
//        schema = Data2Orc.getHiveSchema(mappingMap);
//        final List<TypeDescription> children = schema.getChildren();
//        schemaCategoryArray = new TypeDescription.Category[children.size()];
//        for (int i = 0; i < schemaCategoryArray.length; i++) {
//            schemaCategoryArray[i] = children.get(0).getCategory();
//        }
//        fieldNames = schema.getFieldNames();
    }

    public void closeWriter() throws InterruptedException {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ignored) {
            }
            writer = null;
        }
        if (StringUtils.isNotEmpty(fileName)) {
            final ArrayDeque<String> deque = new ArrayDeque<>();
            deque.add(fileName);
            Orc2Hdfs.putFile2HDFS(task, deque, filePathPrefix, hdfsAddress, dbPath, true);
            fileName = null;

            log.warn("[WriterHelper][closeWriter][{}][{}][{}]", readSucceedRecords, writeSucceedRecords, writeFailedRecords);
//            if (writeFailedRecords > 0L) {
//                communication.increaseCounter(CommunicationTool.WRITE_FAILED_RECORDS, writeFailedRecords);
//                writeFailedRecords = 0L;
//            }
//            if (writeSucceedRecords > 0L) {
//                communication.increaseCounter(CommunicationTool.WRITE_SUCCEED_RECORDS, writeSucceedRecords);
//                writeSucceedRecords = 0L;
//            }
//            if (readSucceedRecords > 0L) {
//                communication.increaseCounter(CommunicationTool.READ_SUCCEED_RECORDS, readSucceedRecords);
//                readSucceedRecords = 0L;
//            }
//            if (!failedRecordList.isEmpty()) {
//                task.getCollectFailedInfoUtil().remoteReport(new LinkedList<>(failedRecordList));
//                failedRecordList.clear();
//            }
        }
        rowBatchMap.clear();
    }

    private String appendExMsg(Category category, String columnName, Object value) {
        return category.name() + " 类型转换异常，列为：" + columnName + "值为：" + value;
    }
}

