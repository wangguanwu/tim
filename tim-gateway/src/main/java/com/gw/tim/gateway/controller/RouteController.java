package com.gw.tim.gateway.controller;

import com.gw.tim.common.enums.StatusEnum;
import com.gw.tim.common.exception.TIMException;
import com.gw.tim.common.pojo.RouteInfo;
import com.gw.tim.common.pojo.TIMUserInfo;
import com.gw.tim.common.res.BaseResponse;
import com.gw.tim.common.res.NULLBody;
import com.gw.tim.common.route.algorithm.RouteHandle;
import com.gw.tim.common.util.RouteInfoParseUtil;
import com.gw.tim.gateway.api.RouteApi;
import com.gw.tim.gateway.api.vo.res.RegisterInfoResVO;
import com.gw.tim.gateway.api.vo.res.TIMServerResVO;
import com.gw.tim.gateway.cache.ServerCache;
import com.gw.tim.gateway.service.AccountService;
import com.gw.tim.gateway.service.CommonBizService;
import com.gw.tim.gateway.service.UserInfoCacheService;
import com.gw.tim.gateway.api.vo.req.ChatReqVO;
import com.gw.tim.gateway.api.vo.req.LoginReqVO;
import com.gw.tim.gateway.api.vo.req.P2PReqVO;
import com.gw.tim.gateway.api.vo.req.RegisterInfoReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Set;

/**
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/")
public class RouteController implements RouteApi {
    private final static Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private ServerCache serverCache;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private CommonBizService commonBizService;

    @Autowired
    private RouteHandle routeHandle;

    /**
     * 群聊 API
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "groupRoute", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<NULLBody> groupRoute(@RequestBody ChatReqVO groupReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        LOGGER.info("msg=[{}]", groupReqVO.toString());

        //获取所有的推送列表
        Map<Long, TIMServerResVO> serverResVOMap = accountService.loadRouteRelated();
        for (Map.Entry<Long, TIMServerResVO> timServerResVOEntry : serverResVOMap.entrySet()) {
            Long userId = timServerResVOEntry.getKey();
            TIMServerResVO TIMServerResVO = timServerResVOEntry.getValue();
            if (userId.equals(groupReqVO.getUserId())) {
                //过滤掉自己
                TIMUserInfo timUserInfo = userInfoCacheService.loadUserInfoByUserId(groupReqVO.getUserId());
                LOGGER.warn("过滤掉了发送者 userId={}", timUserInfo.toString());
                continue;
            }

            //推送消息
            ChatReqVO chatVO = new ChatReqVO(userId, groupReqVO.getMsg());
            accountService.pushMsg(TIMServerResVO, groupReqVO.getUserId(), chatVO);

        }

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }


    /**
     * 私聊 API
     *
     * @param p2pRequest
     * @return
     */
    @RequestMapping(value = "p2pRoute", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<NULLBody> p2pRoute(@RequestBody P2PReqVO p2pRequest) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        try {
            //获取接收消息用户的路由信息
            TIMServerResVO TIMServerResVO = accountService.loadRouteRelatedByUserId(p2pRequest.getReceiveUserId());

            //p2pRequest.getReceiveUserId()==>消息接收者的 userID
            ChatReqVO chatVO = new ChatReqVO(p2pRequest.getReceiveUserId(), p2pRequest.getMsg());
            accountService.pushMsg(TIMServerResVO, p2pRequest.getUserId(), chatVO);

            res.setCode(StatusEnum.SUCCESS.getCode());
            res.setMessage(StatusEnum.SUCCESS.getMessage());

        } catch (TIMException e) {
            res.setCode(e.getErrorCode());
            res.setMessage(e.getErrorMessage());
        }
        return res;
    }

    /**
     * 客户端下线
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "offLine", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<NULLBody> offLine(@RequestBody ChatReqVO groupReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        TIMUserInfo timUserInfo = userInfoCacheService.loadUserInfoByUserId(groupReqVO.getUserId());

        LOGGER.info("user [{}] offline!", timUserInfo.toString());
        accountService.offLine(groupReqVO.getUserId());

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 登录并获取一台 TIM server
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<TIMServerResVO> login(@RequestBody LoginReqVO loginReqVO) throws Exception {
        BaseResponse<TIMServerResVO> res = new BaseResponse();

        //登录校验
        StatusEnum status = accountService.login(loginReqVO);
        if (status == StatusEnum.SUCCESS) {

            // 从zookeeper里挑选一台客户端需访问的netty服务器
            String server = routeHandle.routeServer(serverCache.getServerList(), String.valueOf(loginReqVO.getUserId()));
            LOGGER.info("userName=[{}] route server info=[{}]", loginReqVO.getUserName(), server);

            // check server available
            RouteInfo routeInfo = RouteInfoParseUtil.parse(server);
            commonBizService.checkServerAvailable(routeInfo);

            //保存路由信息
            accountService.saveRouteInfo(loginReqVO, server);

            TIMServerResVO vo = new TIMServerResVO(routeInfo);
            res.setDataBody(vo);

        }
        res.setCode(status.getCode());
        res.setMessage(status.getMessage());

        return res;
    }

    /**
     * 注册账号
     *
     * @return
     */
    @RequestMapping(value = "registerAccount", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<RegisterInfoResVO> registerAccount(@RequestBody RegisterInfoReqVO registerInfoReqVO) throws Exception {
        BaseResponse<RegisterInfoResVO> res = new BaseResponse();

        long userId = System.currentTimeMillis();
        RegisterInfoResVO info = new RegisterInfoResVO(userId, registerInfoReqVO.getUserName());
        info = accountService.register(info);

        res.setDataBody(info);
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 获取所有在线用户
     *
     * @return
     */
    @RequestMapping(value = "onlineUser", method = RequestMethod.POST)
    @ResponseBody()
    @Override
    public BaseResponse<Set<TIMUserInfo>> onlineUser() throws Exception {
        BaseResponse<Set<TIMUserInfo>> res = new BaseResponse();

        Set<TIMUserInfo> timUserInfos = userInfoCacheService.onlineUser();
        res.setDataBody(timUserInfos);
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }


}
