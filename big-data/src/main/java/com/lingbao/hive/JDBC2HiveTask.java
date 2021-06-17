package com.lingbao.hive;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.lingbao.hive.model.Configuration;
import com.lingbao.hive.model.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lingbao08
 * @desc
 * @date 3/27/21 11:07
 **/
@Slf4j
public class JDBC2HiveTask {
    public static final String DEFAULT_HIVE_USER = "admin";


    private Integer threadNum = 1;

    private Statistics statistics = new Statistics();

    private Configuration config = new Configuration();


    public void execute() throws InterruptedException {
        log.info("【JDBC2HiveTask】【call】 OfflineSource 作业开始");

        System.setProperty("HADOOP_USER_NAME", DEFAULT_HIVE_USER);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        threadNum = Math.max(threadNum, 1);
        threadNum = Math.min(threadNum, 10);
        final WriterHelper[] pools = new WriterHelper[threadNum];


        for (int i = 0; i < threadNum; i++) {
            pools[i] = new WriterHelper(this, statistics);
            pools[i].init(config);
        }

        final AtomicInteger counter = new AtomicInteger();
        try {
//            masterReader.read(query, (list) -> {
//                //batch size is 200
//                if (isStopping()) {
//                    logger.info("[JDBC2HiveTask][call]作业停止时退出循环");
//                    throw ApplicationExceptionUtils.newUnknownException("作业停止时退出循环");
//                }
//                pools[counter.incrementAndGet() % threadNum].writer(list);
//            }, 200);

            ArrayList<ObjectNode> list = Lists.newArrayList();
            pools[counter.incrementAndGet() % threadNum].writer(list);

            for (WriterHelper pool : pools) {
                try {
                    pool.closeWriter();
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }

            stopWatch.stop();
            if (log.isInfoEnabled()) {
                log.info("【JDBC2HiveTask】【execute】 OfflineSource 作业结束，耗时 {} 毫秒，读取数量 {} 条，" +
                        "写入数量 {} 条。", stopWatch.getTime(), 1, 1);
            }
        } catch (Exception e) {
            log.info("[JDBC2HiveTask][execute] " + e.getMessage(), e);
        } finally {

            for (WriterHelper pool : pools) {
                pool.closeWriter();
            }
        }
    }


}
