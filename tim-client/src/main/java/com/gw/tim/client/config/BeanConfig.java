package com.gw.tim.client.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.gw.tim.client.handle.MsgHandleCaller;
import com.gw.tim.client.service.impl.MsgCallBackListener;
import com.gw.tim.client.util.SnowflakeIdWorker;
import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.data.construct.RingBufferWheel;
import com.gw.tim.common.protocol.TIMReqMsg;
import lombok.Data;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @since JDK 1.8
 */
@Configuration
@Data
public class BeanConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(BeanConfig.class);


    @Value("${tim.user.id}")
    private long userId;

    @Value("${tim.callback.thread.queue.size}")
    private int queueSize;

    @Value("${tim.callback.thread.pool.size}")
    private int poolSize;

    @Value("${tim.worker.id}")
    private int workerId;


    @Value("${tim.dateCenter.id}")
    private int dataCenterId;

    /**
     * 创建心跳单例
     *
     * @return
     */
    @Bean(value = "heartBeat")
    public TIMReqMsg heartBeat() {
        TIMReqMsg heart = new TIMReqMsg(userId, "ping", Constants.CommandType.PING);
        return heart;
    }


    /**
     * http client
     *
     * @return okHttp
     */
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        return builder.build();
    }


    /**
     * 创建回调线程池
     *
     * @return
     */
    @Bean("callBackThreadPool")
    public ThreadPoolExecutor buildCallerThread() {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue(queueSize);
        ThreadFactory product = new ThreadFactoryBuilder()
                .setNameFormat("msg-callback-%d")
                .setDaemon(true)
                .build();
        ThreadPoolExecutor productExecutor = new ThreadPoolExecutor(poolSize, poolSize, 1, TimeUnit.MILLISECONDS, queue, product);
        return productExecutor;
    }


    @Bean("scheduledTask")
    public ScheduledExecutorService buildSchedule() {
        ThreadFactory sche = new ThreadFactoryBuilder()
                .setNameFormat("reConnect-job-%d")
                .setDaemon(true)
                .build();
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, sche);
        return scheduledExecutorService;
    }

    /**
     * 回调 bean
     *
     * @return
     */
    @Bean
    public MsgHandleCaller buildCaller() {
        MsgHandleCaller caller = new MsgHandleCaller(new MsgCallBackListener());

        return caller;
    }


    @Bean
    public RingBufferWheel bufferWheel() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        return new RingBufferWheel(executorService);
    }

    @Bean
    public SnowflakeIdWorker msgIdGenerator() {
        return new SnowflakeIdWorker(workerId, dataCenterId);
    }

    @Bean
    public SnowflakeIdWorker requestIdGenerator() {
        return new SnowflakeIdWorker(workerId, dataCenterId);
    }
}
