package com.gw.tim.server.config;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.protocol.TIMReqMsg;
import okhttp3.OkHttpClient;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @since JDK 1.8
 */
@Configuration
public class BeanConfig {

    @Autowired
    private AppConfiguration appConfiguration;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(appConfiguration.getZkAddr(), appConfiguration.getZkConnectTimeout());
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
     * 创建心跳单例
     *
     * @return
     */
    @Bean(value = "heartBeat")
    public TIMReqMsg heartBeat() {
        TIMReqMsg heart = new TIMReqMsg(0L, "pong", Constants.CommandType.PING);
        return heart;
    }
}
