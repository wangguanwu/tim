package com.gw.tim.server.mq;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.server.api.vo.mq.MqSingleMessageReqVO;
import com.gw.tim.server.api.vo.req.SendMsgReqVO;
import com.gw.tim.server.server.TIMServer;
import lombok.extern.slf4j.Slf4j;
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
@RocketMQMessageListener(topic = Constants.MQ_MESSAGE_GROUP_TOPIC, consumerGroup = Constants.MQ_MESSAGE_CONSUMER_TYPE_FORWARD)
public class PushP2PMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private TIMServer timServer;

    @Override
    public void onMessage(String message) {
        log.info("receive msg :{}", message);

        MqSingleMessageReqVO mqSingleMessageReqVO = JsonUtil.fromJson(message, MqSingleMessageReqVO.class);

        SendMsgReqVO sendMsgReqVO = new SendMsgReqVO();

        BeanUtils.copyProperties(mqSingleMessageReqVO, sendMsgReqVO);

        sendMsgReqVO.setUserId(mqSingleMessageReqVO.getUserId());
        sendMsgReqVO.setToUserId(mqSingleMessageReqVO.getToUserId());
        sendMsgReqVO.setType(Constants.ChatType.SINGLE.getType());
        timServer.sendMsg(sendMsgReqVO);
    }
}
