package com.gw.tim.server.mq;

import com.gw.tim.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 消息持久化消息组
 *
 **/

@Slf4j
@Service
@RocketMQMessageListener(topic = Constants.MQ_MESSAGE_TOPIC, consumerGroup = Constants.MQ_MESSAGE_CONSUMER_TYPE_PERSISTENT)
public class PersistentMessageConsumer implements RocketMQListener<String> {


    @Override
    public void onMessage(String message) {
        log.info("received msg:{}", message);
    }
}
