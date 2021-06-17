package com.lingbao.hive;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.orc.OrcConf;
import org.apache.orc.OrcFile;
import org.apache.orc.TypeDescription;
import org.apache.orc.Writer;

import java.io.File;
import java.io.IOException;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 3/22/21 19:50
 **/
@Slf4j
public class WriterHelper2 {

    public static Writer createWriter(File file, TypeDescription schema) {
        try {
            final Configuration configuration = new Configuration();
            //每100行刷新一下到磁盘（flush）
            configuration.setInt(OrcConf.ROWS_BETWEEN_CHECKS.getAttribute(), 100);
            final OrcFile.WriterOptions writerOptions = OrcFile.writerOptions(configuration);
            writerOptions.setSchema(schema);
            writerOptions.overwrite(true);
            // 设置为10M，默认 64M 条带大小更少内存（建议使用默认大小）
            // writerOptions.stripeSize(10 * _1M);
            // 禁用索引，减少内存
            writerOptions.rowIndexStride(0);
            return OrcFile.createWriter(new Path(file.toURI()), writerOptions);
        } catch (IOException e) {
            log.error("创建Writer错误：{}", e);
        }
        return null;
    }
}
