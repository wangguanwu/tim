package com.gw.tim.gateway.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.gw.tim.common.route.algorithm.RouteHandle;
import com.gw.tim.common.route.algorithm.consistenthash.AbstractConsistentHash;
import com.gw.tim.gateway.service.PushMessageService;
import com.gw.tim.gateway.service.impl.HttpPushMessgeServiceImpl;
import com.gw.tim.gateway.service.impl.MqPushMessageServiceImpl;
import okhttp3.OkHttpClient;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @since JDK 1.8
 */
@Configuration
public class BeanConfig {

    private static Logger logger = LoggerFactory.getLogger(BeanConfig.class);

    @Autowired
    private AppConfiguration appConfiguration;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(appConfiguration.getZkAddr(), appConfiguration.getZkConnectTimeout());
    }

    @Bean
    public LoadingCache<String, String> buildCache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return null;
                    }
                });
    }


    /**
     * Redis bean
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
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

    @Bean
    public RouteHandle buildRouteHandle() throws Exception {
        String routeWay = appConfiguration.getRouteWay();
        RouteHandle routeHandle = (RouteHandle) Class.forName(routeWay).newInstance();
        logger.info("Current route algorithm is [{}]", routeHandle.getClass().getSimpleName());
        if (routeWay.contains("ConsistentHash")) {
            //一致性 hash 算法
            Method method = Class.forName(routeWay).getMethod("setHash", AbstractConsistentHash.class);
            AbstractConsistentHash consistentHash = (AbstractConsistentHash)
                    Class.forName(appConfiguration.getConsistentHashWay()).newInstance();
            method.invoke(routeHandle, consistentHash);
            return routeHandle;
        } else {

            return routeHandle;

        }

    }

    @Bean
    @ConditionalOnProperty(prefix = "app.push", name = "type", havingValue = "http")
    public PushMessageService httpPushMessageService() {
        return new HttpPushMessgeServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.push", name = "type", havingValue = "mq")
    public PushMessageService mqPushMessageService() {
        return new MqPushMessageServiceImpl();
    }
}
