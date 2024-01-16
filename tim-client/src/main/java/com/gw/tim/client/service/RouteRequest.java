package com.gw.tim.client.service;

import com.gw.tim.client.vo.req.GroupMessageReqVO;
import com.gw.tim.client.vo.res.TIMServerResVO;
import com.gw.tim.client.vo.req.LoginReqVO;
import com.gw.tim.client.vo.req.SingleMessageReqVO;
import com.gw.tim.client.vo.res.OnlineUsersResVO;

import java.util.List;

/**
 *
 * @since JDK 1.8
 */
public interface RouteRequest {

    /**
     * 群发消息
     * @param groupMessageReqVO 消息
     * @throws Exception
     */
    void sendGroupMsg(GroupMessageReqVO groupMessageReqVO) throws Exception;


    /**
     * 私聊
     * @param singleMessageReqVO
     * @throws Exception
     */
    void sendP2PMsg(SingleMessageReqVO singleMessageReqVO)throws Exception;

    /**
     * 获取服务器
     * @return 服务ip+port
     * @param loginReqVO
     * @throws Exception
     */
    TIMServerResVO.ServerInfo getTIMServer(LoginReqVO loginReqVO) throws Exception;

    /**
     * 获取所有在线用户
     * @return
     * @throws Exception
     */
    List<OnlineUsersResVO.DataBodyBean> onlineUsers()throws Exception ;


    void offLine() ;

}
