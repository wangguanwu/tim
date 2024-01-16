package com.gw.tim.client.handle;

import com.gw.tim.client.service.CustomMsgHandleListener;

/**
 * 消息回调 bean
 *
 * @since JDK 1.8
 */
public class MsgHandleCaller {

    /**
     * 回调接口
     */
    private CustomMsgHandleListener msgHandleListener ;

    public MsgHandleCaller(CustomMsgHandleListener msgHandleListener) {
        this.msgHandleListener = msgHandleListener;
    }

    public CustomMsgHandleListener getMsgHandleListener() {
        return msgHandleListener;
    }

    public void setMsgHandleListener(CustomMsgHandleListener msgHandleListener) {
        this.msgHandleListener = msgHandleListener;
    }
}
