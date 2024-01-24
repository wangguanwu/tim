package com.gw.tim.client.service.impl;

import com.gw.tim.client.config.AppConfiguration;
import com.gw.tim.client.service.*;
import com.gw.tim.client.util.SnowflakeIdWorker;
import com.gw.tim.client.client.TIMClient;
import com.gw.tim.common.util.StringUtil;
import com.gw.tim.gateway.api.vo.req.GroupMessageReqVO;
import com.gw.tim.gateway.api.vo.req.SingleMessageReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @since JDK 1.8
 */
@Service
public class MsgHandler implements MsgHandle {
    private final static Logger LOGGER = LoggerFactory.getLogger(MsgHandler.class);
    @Autowired
    private RouteRequest routeRequest;

    @Autowired
    private AppConfiguration configuration;

    @Resource(name = "callBackThreadPool")
    private ThreadPoolExecutor executor;

    @Autowired
    private TIMClient timClient;

    @Autowired
    private MsgLogger msgLogger;

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private InnerCommandContext innerCommandContext;

    @Resource
    private SnowflakeIdWorker msgIdGenerator;


    @Resource
    private SnowflakeIdWorker requestIdGenerator;

    private boolean aiModel = false;

    @Override
    public void sendMsg(String msg) {
        if (aiModel) {
            aiChat(msg);
        } else {
            normalChat(msg);
        }
    }

    /**
     * 正常聊天
     *
     * @param msg
     */
    private void normalChat(String msg) {
        String[] totalMsg = msg.split("::");
        long msgId = msgIdGenerator.nextId();
        int timeStamp = (int)(System.currentTimeMillis()/1000);
        Long requestId = requestIdGenerator.nextId();
        if (totalMsg.length > 1) {
            //私聊
            SingleMessageReqVO singleMessageReqVO = new SingleMessageReqVO();
            singleMessageReqVO.setUserId(configuration.getUserId());
            singleMessageReqVO.setToUserId(Long.parseLong(totalMsg[0]));
            singleMessageReqVO.setMsg(totalMsg[1]);
            singleMessageReqVO.setMsgId(msgId);
            singleMessageReqVO.setTimeStamp(timeStamp);
            singleMessageReqVO.setReqNo(requestId.toString());
            try {
                p2pChat(singleMessageReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }

        } else {
            //群聊
            GroupMessageReqVO groupMessageReqVO = GroupMessageReqVO.builder()
                    .groupId(0L)
                    .msg(msg)
                    .userId(configuration.getUserId())
                    .build();
            groupMessageReqVO.setReqNo(requestId.toString());
            groupMessageReqVO.setMsgId(msgId);
            groupMessageReqVO.setTimeStamp(timeStamp);
            try {
                groupChat(groupMessageReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }
        }
    }

    /**
     * AI model
     *
     * @param msg
     */
    private void aiChat(String msg) {
        msg = msg.replace("吗", "");
        msg = msg.replace("嘛", "");
        msg = msg.replace("?", "!");
        msg = msg.replace("？", "!");
        msg = msg.replace("你", "我");
        System.out.println("AI:\033[31;4m" + msg + "\033[0m");
    }

    @Override
    public void groupChat(GroupMessageReqVO groupMessageReqVO) throws Exception {
        routeRequest.sendGroupMsg(groupMessageReqVO);
    }

    @Override
    public void p2pChat(SingleMessageReqVO singleMessageReqVO) throws Exception {
        routeRequest.sendP2PMsg(singleMessageReqVO);
    }

    @Override
    public boolean checkMsg(String msg) {
        if (StringUtil.isEmpty(msg)) {
            LOGGER.warn("不能发送空消息！");
            return true;
        }
        return false;
    }

    @Override
    public boolean innerCommand(String msg) {

        if (msg.startsWith(":")) {

            InnerCommand instance = innerCommandContext.getInstance(msg);
            instance.process(msg);

            return true;

        } else {
            return false;
        }


    }

    /**
     * 关闭系统
     */
    @Override
    public void shutdown() {
        LOGGER.info("系统关闭中。。。。");
        routeRequest.offLine();
        msgLogger.stop();
        executor.shutdown();
        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("线程池关闭中。。。。");
            }
            timClient.close();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
        System.exit(0);
    }

    @Override
    public void openAIModel() {
        aiModel = true;
    }

    @Override
    public void closeAIModel() {
        aiModel = false;
    }

}
