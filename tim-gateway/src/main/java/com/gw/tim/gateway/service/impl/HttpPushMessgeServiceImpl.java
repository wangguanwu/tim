package com.gw.tim.gateway.service.impl;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.pojo.TIMUserInfo;
import com.gw.tim.gateway.api.vo.req.ChatReqVO;
import com.gw.tim.gateway.api.vo.req.GroupMessageReqVO;
import com.gw.tim.gateway.api.vo.req.SingleMessageReqVO;
import com.gw.tim.gateway.api.vo.res.TIMServerResVO;
import com.gw.tim.gateway.cache.ServerCache;
import com.gw.tim.gateway.service.AccountService;
import com.gw.tim.gateway.service.PushMessageService;
import com.gw.tim.gateway.service.UserInfoCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * send message by http
 */

@Slf4j
public class HttpPushMessgeServiceImpl implements PushMessageService {

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ServerCache serverCache;

    @Override
    public void sendP2pMsg(SingleMessageReqVO singleMessageReqVO) throws Exception {

        //获取接收消息用户的路由信息
        TIMServerResVO TIMServerResVO = accountService.loadRouteRelatedByUserId(singleMessageReqVO.getToUserId());

        //singleMessageReqVO.getReceiveUserId()==>消息接收者的 userID
        ChatReqVO chatVO = new ChatReqVO(singleMessageReqVO.getToUserId(), singleMessageReqVO.getUserId(), singleMessageReqVO.getMsg(),
                Constants.ChatType.SINGLE.getType());
        accountService.pushMsg(TIMServerResVO, singleMessageReqVO.getUserId(), chatVO);

    }

    @Override
    @Async
    public void sendGroupMsg(GroupMessageReqVO groupReqVO) throws Exception {
        log.info("开始推送消息:req:{}, timestamp:{}", groupReqVO.getReqNo(), groupReqVO.getTimeStamp());
        //获取所有的推送列表
        Set<String> validServer = new HashSet<>(serverCache.getServerList());
        Map<Long, TIMServerResVO> serverResVOMap = accountService.loadRouteRelated();
        for (Map.Entry<Long, TIMServerResVO> entry : serverResVOMap.entrySet()) {
            Long toUserId = entry.getKey();
            TIMServerResVO serverInfo = entry.getValue();
            if (!validServer.contains(serverInfo.toString()))  {
                // 不存在的msg server的用户，是过期用户，需要过滤掉
                log.warn(serverInfo.toString() + " not exist, skip this user:{}", toUserId);
                continue;
            }
            if (toUserId.equals(groupReqVO.getUserId())) {
                //过滤掉自己
                TIMUserInfo timUserInfo = userInfoCacheService.loadUserInfoByUserId(groupReqVO.getUserId());
                log.warn("过滤掉了发送者 userId={}", timUserInfo.toString());
                continue;
            }

            //推送消息
            ChatReqVO chatVO = new ChatReqVO(toUserId, groupReqVO.getUserId(), groupReqVO.getMsg(), Constants.ChatType.GROUP.getType());
            accountService.pushMsg(serverInfo, groupReqVO.getUserId(), chatVO);
        }
    }
}
