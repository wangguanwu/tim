package com.gw.tim.client.service;

/**
 *
 * @since JDK 1.8
 */
public interface CustomMsgHandleListener {

    /**
     * 消息回调
     * @param msg
     */
    void handle(String msg);
}
