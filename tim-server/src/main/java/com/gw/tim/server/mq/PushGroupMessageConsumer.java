package com.gw.tim.server.mq;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.server.api.vo.mq.MqGroupMessageReqVO;
import com.gw.tim.server.api.vo.mq.MqSingleMessageReqVO;
import com.gw.tim.server.api.vo.req.SendMsgReqVO;
import com.gw.tim.server.server.TIMServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * push msg consumer
 */

@Slf4j
@Service
@RocketMQMessageListener(topic = Constants.MQ_MESSAGE_GROUP_TOPIC, consumerGroup = Constants.MQ_GROUP_MESSAGE_CONSUMER_TYPE_FORWARD,
messageModel = MessageModel.BROADCASTING)
public class PushGroupMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private TIMServer timServer;

    @Override
    public void onMessage(String message) {
        log.info("receive msg :{}", message);

        MqGroupMessageReqVO mqGroupMessageVOReq = JsonUtil.fromJson(message, MqGroupMessageReqVO.class);

        SendMsgReqVO sendMsgReqVO = new SendMsgReqVO();

        BeanUtils.copyProperties(mqGroupMessageVOReq, sendMsgReqVO);

        sendMsgReqVO.setUserId(mqGroupMessageVOReq.getUserId());
        sendMsgReqVO.setToUserId(mqGroupMessageVOReq.getGroupId());
        sendMsgReqVO.setType(Constants.ChatType.SINGLE.getType());
        timServer.sendGroupMsg(sendMsgReqVO);
    }
}
