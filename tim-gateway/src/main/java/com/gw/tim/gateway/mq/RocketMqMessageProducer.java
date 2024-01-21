package com.gw.tim.gateway.mq;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author guanwu
 * @created 2024/1/21 15:23
 */

@Component
public class RocketMqMessageProducer {


    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public boolean syncSendMsg(String topic, String msg) {
        SendResult sendResult = this.rocketMQTemplate.syncSend(topic, msg);
        return sendResult.getSendStatus() == SendStatus.SEND_OK;
    }

}
