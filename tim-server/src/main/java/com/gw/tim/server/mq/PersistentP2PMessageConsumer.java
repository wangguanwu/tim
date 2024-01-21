package com.gw.tim.server.mq;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.server.api.vo.mq.MqSingleMessageReqVO;
import com.gw.tim.server.dao.TimSingleMessageDao;
import com.gw.tim.server.pojo.TimSingleMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * persistent p2p msg to db
 *
 **/

@Slf4j
@Service
@RocketMQMessageListener(topic = Constants.MQ_MESSAGE_P2P_TOPIC, consumerGroup = Constants.MQ_MESSAGE_CONSUMER_TYPE_PERSISTENT)
public class PersistentP2PMessageConsumer implements RocketMQListener<String> {

    @Autowired
    private TimSingleMessageDao timSingleMessageDao;


    @Override
    public void onMessage(String msg) {
        log.info("received msg:{}", msg);
        MqSingleMessageReqVO mqSingleMessageReqVO = JsonUtil.fromJson(msg, MqSingleMessageReqVO.class);
        TimSingleMessage  timSingleMessage = new TimSingleMessage();
        BeanUtils.copyProperties(mqSingleMessageReqVO, timSingleMessage);
        if (mqSingleMessageReqVO.getMsgId() == null) {
            log.error("msgId is null");
            throw new RuntimeException("msg is null from req " + mqSingleMessageReqVO.getReqNo());
        }
        timSingleMessageDao.insertMessage(timSingleMessage);
    }
}
