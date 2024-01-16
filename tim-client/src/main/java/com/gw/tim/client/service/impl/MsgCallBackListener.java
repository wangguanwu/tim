package com.gw.tim.client.service.impl;

import com.gw.tim.client.service.CustomMsgHandleListener;
import com.gw.tim.client.service.MsgLogger;
import com.gw.tim.client.util.SpringBeanFactory;

/**
 * 自定义收到消息回调
 *
 * @since JDK 1.8
 */
public class MsgCallBackListener implements CustomMsgHandleListener {


    private MsgLogger msgLogger ;

    public MsgCallBackListener() {
        this.msgLogger = SpringBeanFactory.getBean(MsgLogger.class) ;
    }

    @Override
    public void handle(String msg) {
        msgLogger.log(msg) ;
    }
}
