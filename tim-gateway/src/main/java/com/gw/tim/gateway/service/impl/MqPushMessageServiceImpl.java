package com.gw.tim.gateway.service.impl;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.gateway.api.vo.req.GroupMessageReqVO;
import com.gw.tim.gateway.api.vo.req.SingleMessageReqVO;
import com.gw.tim.gateway.mq.RocketMqMessageProducer;
import com.gw.tim.gateway.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

/**
 * @author guanwu
 * @created 2024/1/21 15:17
 */

@Slf4j
public class MqPushMessageServiceImpl implements PushMessageService {

    @Autowired
    private RocketMqMessageProducer rocketMqMessageProducer;

    @Override
    public void sendP2pMsg(SingleMessageReqVO singleMessageReqVO) throws Exception {

        boolean res = rocketMqMessageProducer.syncSendMsg(Constants.MQ_MESSAGE_P2P_TOPIC, JsonUtil.toJson(singleMessageReqVO));
        if (!res) {
            throw new RuntimeException("Send p2p msg failed." + JsonUtil.toJson(singleMessageReqVO));
        }

    }

    @Override
    @Async
    public void sendGroupMsg(GroupMessageReqVO groupMessageReqVO) throws Exception {
        log.info("start push message to mq:{}", JsonUtil.toJson(groupMessageReqVO));
        boolean res =  rocketMqMessageProducer.syncSendMsg(Constants.MQ_MESSAGE_GROUP_TOPIC, JsonUtil.toJson(groupMessageReqVO));

        if (!res) {
            throw new RuntimeException("Send msg failed." + JsonUtil.toJson(groupMessageReqVO));
        }
    }
}
