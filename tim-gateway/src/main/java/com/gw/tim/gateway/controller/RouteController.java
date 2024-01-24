package com.gw.tim.gateway.controller;

import com.gw.tim.common.enums.StatusEnum;
import com.gw.tim.common.exception.TIMException;
import com.gw.tim.common.pojo.RouteInfo;
import com.gw.tim.common.pojo.TIMUserInfo;
import com.gw.tim.common.res.BaseResponse;
import com.gw.tim.common.res.NULLBody;
import com.gw.tim.common.route.algorithm.RouteHandle;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.common.util.RouteInfoParseUtil;
import com.gw.tim.gateway.api.RouteApi;
import com.gw.tim.gateway.api.vo.req.*;
import com.gw.tim.gateway.api.vo.res.RegisterInfoResVO;
import com.gw.tim.gateway.api.vo.res.TIMServerResVO;
import com.gw.tim.gateway.cache.ServerCache;
import com.gw.tim.gateway.kit.ZkUtils;
import com.gw.tim.gateway.service.AccountService;
import com.gw.tim.gateway.service.CommonBizService;
import com.gw.tim.gateway.service.PushMessageService;
import com.gw.tim.gateway.service.UserInfoCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
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

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private ZkUtils zkUtils;

    /**
     * 群聊 API
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "groupRoute", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public BaseResponse<NULLBody> groupRoute(@RequestBody GroupMessageReqVO groupReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        LOGGER.info("msg={}", JsonUtil.toJson(groupReqVO));

        pushMessageService.sendGroupMsg(groupReqVO);
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
    @ResponseBody
    @Override
    public BaseResponse<NULLBody> p2pRoute(@RequestBody SingleMessageReqVO p2pRequest) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        try {
            pushMessageService.sendP2pMsg(p2pRequest);
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
     * @param reqVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "offLine", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public BaseResponse<NULLBody> offLine(@RequestBody ChatReqVO reqVo) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        TIMUserInfo timUserInfo = userInfoCacheService.loadUserInfoByUserId(reqVo.getToUserId());

        LOGGER.info("user [{}] offline!", timUserInfo.toString());
        accountService.offLine(reqVo.getToUserId());

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
    @ResponseBody
    @Override
    public BaseResponse<TIMServerResVO> login(@RequestBody LoginReqVO loginReqVO) throws Exception {
        BaseResponse<TIMServerResVO> res = new BaseResponse();
        LOGGER.info("user start login: {}", JsonUtil.toJson(loginReqVO));
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

        } else {
            LOGGER.error("user login failed  {}", loginReqVO.getUserName());
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


    /**
     * 获取所有可用server
     *
     * @return
     */
    @RequestMapping(value = "getAllServer", method = RequestMethod.GET)
    @ResponseBody()
    @Override
    public BaseResponse<List<String>> getAllServerList() throws Exception {
        BaseResponse<List<String>> res = new BaseResponse();
        res.setDataBody(zkUtils.getAllNode());
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }
}
