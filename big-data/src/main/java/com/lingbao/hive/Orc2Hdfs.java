package com.lingbao.hive;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;

/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 11:06
 **/

@Slf4j
public class Orc2Hdfs {


    /**
     * @param task
     * @param deque
     * @param filePathPrefix
     * @param hdfsAddress
     * @param dbPath
     * @param b
     * @throws InterruptedException don't change it.
     */
    public static void putFile2HDFS(JDBC2HiveTask task, ArrayDeque<String> deque,
                                    String filePathPrefix, String hdfsAddress, String dbPath, boolean b) throws InterruptedException {

        for (int i = 1; /*!task.isStopping()*/; i++) {
            try {
                if (deque.isEmpty()) {
                    log.info("队列为空");
                    return;
                }
                putFiles(deque, filePathPrefix, hdfsAddress, dbPath);
            } catch (IOException e) {
                String msg = String.format("上传ORC文件异常，第%s次IO异常，文件名：%s", i, deque.getFirst());
                log.error(msg + "{}", e);
                Thread.sleep(5000L);
                continue;
            } catch (Exception e) {
                String first = deque.pollFirst();
                log.error("上传ORC文件出错，文件名：{}，{}", first, e);
                throw new IllegalArgumentException("上传ORC文件出错，文件名：" + first, e);
            }
            deque.pollFirst();
            if (b) {
                return;
            }
        }

    }

    private static void putFiles(ArrayDeque<String> deque, String filePathPrefix,
                                 String hdfsAddress, String dbPath) throws Exception {
        try (FileSystem fs = FileSystem.get(getConf(hdfsAddress))) {
            // table/etl_date/filename
            String first = deque.getFirst();
            if (StringUtils.isNotEmpty(first)) {
                String localFilePath = filePathPrefix + first;
                log.info("本地路径：{}", localFilePath);
                if (new File(localFilePath).exists()) {
                    fs.copyFromLocalFile(new Path(localFilePath), new Path(dbPath + first));
                    LocalFileSystem.get(new Configuration()).delete(new Path(localFilePath), false);
                }
            }
        }
    }


    private static Configuration getConf(String hdfsAddress) {

        Configuration conf = new Configuration();
//
        String[] split = hdfsAddress.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String nn = "nn" + i;
            conf.set("dfs.namenode.rpc-address.bigbigworld." + nn, split[i]);
            sb.append(nn).append(",");
        }

        conf.set("dfs.nameservices", "bigbigworld");
        conf.set("fs.defaultFS", "hdfs://bigbigworld");
//        conf.set("fs.defaultFS", "10.83.192.7:8020");
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        conf.set("dfs.ha.namenodes.bigbigworld", sb.subSequence(0, sb.length() - 1).toString());
        conf.set("dfs.ha.automatic-failover.enabled.bigbigworld", "true");
        conf.set("dfs.client.failover.proxy.provider.bigbigworld",
                "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        return conf;
    }
}
