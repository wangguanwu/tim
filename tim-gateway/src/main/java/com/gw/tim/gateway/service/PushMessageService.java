package com.gw.tim.gateway.service;

import com.gw.tim.gateway.api.vo.req.GroupMessageReqVO;
import com.gw.tim.gateway.api.vo.req.SingleMessageReqVO;

/**
 *
 *  push message service
 */
public interface PushMessageService {

    void sendP2pMsg(SingleMessageReqVO singleMessageReqVO) throws Exception;

    void sendGroupMsg(GroupMessageReqVO groupMessageReqVO) throws Exception;

}
