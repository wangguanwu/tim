package com.gw.tim.client.service.impl;

import com.gw.tim.client.thread.ContextHolder;
import com.gw.tim.client.client.TIMClient;
import com.gw.tim.common.kit.HeartBeatHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @since JDK 1.8
 */
@Service
public class ClientHeartBeatHandlerImpl implements HeartBeatHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHeartBeatHandlerImpl.class);

    @Autowired
    private TIMClient timClient;


    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        //重连
        ContextHolder.setReconnect(true);
        timClient.reconnect();
    }


}
