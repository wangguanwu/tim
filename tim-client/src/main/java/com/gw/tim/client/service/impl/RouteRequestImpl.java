package com.gw.tim.client.service.impl;

import com.alibaba.fastjson.JSON;
import com.gw.tim.client.config.AppConfiguration;
import com.gw.tim.client.service.RouteRequest;
import com.gw.tim.client.thread.ContextHolder;
import com.gw.tim.client.vo.req.GroupMessageReqVO;
import com.gw.tim.client.vo.req.SingleMessageReqVO;
import com.gw.tim.client.vo.res.TIMServerResVO;
import com.gw.tim.client.service.EchoService;
import com.gw.tim.client.vo.req.LoginReqVO;
import com.gw.tim.client.vo.res.OnlineUsersResVO;
import com.gw.tim.common.core.proxy.ProxyManager;
import com.gw.tim.common.enums.StatusEnum;
import com.gw.tim.common.exception.TIMException;
import com.gw.tim.common.res.BaseResponse;
import com.gw.tim.gateway.api.RouteApi;
import com.gw.tim.gateway.api.vo.req.ChatReqVO;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @since JDK 1.8
 */
@Service
public class RouteRequestImpl implements RouteRequest {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteRequestImpl.class);

    @Autowired
    private OkHttpClient okHttpClient;

    @Value("${tim.gateway.url}")
    private String gatewayUrl;

    @Autowired
    private EchoService echoService;


    @Autowired
    private AppConfiguration appConfiguration;

    @Override
    public void sendGroupMsg(GroupMessageReqVO groupMessageReqVO) throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, gatewayUrl, okHttpClient).getInstance();
        ChatReqVO chatReqVO = new ChatReqVO(groupMessageReqVO.getUserId(), groupMessageReqVO.getMsg());
        Response response = null;
        try {
            response = (Response) routeApi.groupRoute(chatReqVO);
        } catch (Exception e) {
            LOGGER.error("exception", e);
        } finally {
            response.body().close();
        }
    }

    @Override
    public void sendP2PMsg(SingleMessageReqVO singleMessageReqVO) throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, gatewayUrl, okHttpClient).getInstance();
        com.gw.tim.gateway.api.vo.req.SingleMessageReqVO vo = new com.gw.tim.gateway.api.vo.req.SingleMessageReqVO();
        vo.setMsg(singleMessageReqVO.getMsg());
        vo.setReceiveUserId(singleMessageReqVO.getReceiveUserId());
        vo.setUserId(singleMessageReqVO.getUserId());

        Response response = null;
        try {
            response = (Response) routeApi.p2pRoute(vo);
            String json = response.body().string();
            BaseResponse baseResponse = JSON.parseObject(json, BaseResponse.class);

            // account offline.
            if (baseResponse.getCode().equals(StatusEnum.OFF_LINE.getCode())) {
                LOGGER.error(singleMessageReqVO.getReceiveUserId() + ":" + StatusEnum.OFF_LINE.getMessage());
            }

        } catch (Exception e) {
            LOGGER.error("exception", e);
        } finally {
            response.body().close();
        }
    }

    @Override
    public TIMServerResVO.ServerInfo getTIMServer(LoginReqVO loginReqVO) throws Exception {

        RouteApi routeApi = new ProxyManager<>(RouteApi.class, gatewayUrl, okHttpClient).getInstance();
        com.gw.tim.gateway.api.vo.req.LoginReqVO vo = new com.gw.tim.gateway.api.vo.req.LoginReqVO();
        vo.setUserId(loginReqVO.getUserId());
        vo.setUserName(loginReqVO.getUserName());

        Response response = null;
        TIMServerResVO TIMServerResVO = null;
        try {
            response = (Response) routeApi.login(vo);
            String json = response.body().string();
            TIMServerResVO = JSON.parseObject(json, TIMServerResVO.class);

            //重复失败
            if (!TIMServerResVO.getCode().equals(StatusEnum.SUCCESS.getCode())) {
                echoService.echo(TIMServerResVO.getMessage());

                // when client in reConnect state, could not exit.
                if (ContextHolder.getReconnect()) {
                    echoService.echo("###{}###", StatusEnum.RECONNECT_FAIL.getMessage());
                    throw new TIMException(StatusEnum.RECONNECT_FAIL);
                }

                System.exit(-1);
            }

        } catch (Exception e) {
            LOGGER.error("exception", e);
        } finally {
            response.body().close();
        }

        return TIMServerResVO.getDataBody();
    }

    @Override
    public List<OnlineUsersResVO.DataBodyBean> onlineUsers() throws Exception {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, gatewayUrl, okHttpClient).getInstance();

        Response response = null;
        OnlineUsersResVO onlineUsersResVO = null;
        try {
            response = (Response) routeApi.onlineUser();
            String json = response.body().string();
            onlineUsersResVO = JSON.parseObject(json, OnlineUsersResVO.class);

        } catch (Exception e) {
            LOGGER.error("exception", e);
        } finally {
            response.body().close();
        }

        return onlineUsersResVO.getDataBody();
    }

    @Override
    public void offLine() {
        RouteApi routeApi = new ProxyManager<>(RouteApi.class, gatewayUrl, okHttpClient).getInstance();
        ChatReqVO vo = new ChatReqVO(appConfiguration.getUserId(), "offLine");
        Response response = null;
        try {
            response = (Response) routeApi.offLine(vo);
        } catch (Exception e) {
            LOGGER.error("exception", e);
        } finally {
            response.body().close();
        }
    }
}
