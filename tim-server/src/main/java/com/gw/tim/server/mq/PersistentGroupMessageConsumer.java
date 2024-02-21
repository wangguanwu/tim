package com.gw.tim.server.mq;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.server.api.vo.mq.MqGroupMessageReqVO;
import com.gw.tim.server.dao.TimGroupMessageDao;
import com.gw.tim.server.pojo.TimGroupMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * persistent group msg to db
 *
 **/

@Slf4j
@Service
@RocketMQMessageListener(topic = Constants.MQ_MESSAGE_GROUP_TOPIC, consumerGroup = Constants.MQ_GROUP_MESSAGE_CONSUMER_TYPE_PERSISTENT,
messageModel = MessageModel.CLUSTERING)
public class PersistentGroupMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private TimGroupMessageDao groupMessageDao;

    @Override
    public void onMessage(String msg) {
        log.info("received msg:{}", msg);
        MqGroupMessageReqVO groupMessageReq = JsonUtil.fromJson(msg, MqGroupMessageReqVO.class);
        TimGroupMessage groupMessage = new TimGroupMessage();
        BeanUtils.copyProperties(groupMessageReq, groupMessage);
        if (groupMessageReq.getMsgId() == null) {
            log.error("msgId is null");
            throw new RuntimeException("msg is null from req " + groupMessageReq.getReqNo());
        }
        groupMessageDao.insertMessage(groupMessage);
    }
}
