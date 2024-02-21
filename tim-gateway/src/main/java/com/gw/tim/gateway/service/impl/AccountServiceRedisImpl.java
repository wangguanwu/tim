package com.gw.tim.gateway.service.impl;

import com.gw.tim.common.core.proxy.ProxyManager;
import com.gw.tim.common.enums.StatusEnum;
import com.gw.tim.common.exception.TIMException;
import com.gw.tim.common.pojo.TIMUserInfo;
import com.gw.tim.common.util.RouteInfoParseUtil;
import com.gw.tim.gateway.api.vo.req.ChatReqVO;
import com.gw.tim.gateway.api.vo.req.LoginReqVO;
import com.gw.tim.gateway.api.vo.res.RegisterInfoResVO;
import com.gw.tim.gateway.api.vo.res.TIMServerResVO;
import com.gw.tim.gateway.service.AccountService;
import com.gw.tim.gateway.service.UserInfoCacheService;
import com.gw.tim.server.api.ServerApi;
import com.gw.tim.server.api.vo.req.SendMsgReqVO;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.gw.tim.common.enums.StatusEnum.OFF_LINE;
import static com.gw.tim.gateway.constant.Constant.ACCOUNT_PREFIX;
import static com.gw.tim.gateway.constant.Constant.ROUTE_PREFIX;

/**
 * @since JDK 1.8
 */
@Service
public class AccountServiceRedisImpl implements AccountService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceRedisImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public RegisterInfoResVO register(RegisterInfoResVO info) {
        String key = ACCOUNT_PREFIX + info.getUserId();

        String name = redisTemplate.opsForValue().get(info.getUserName());
        if (null == name) {
            //为了方便查询，冗余一份
            redisTemplate.opsForValue().set(key, info.getUserName());
            redisTemplate.opsForValue().set(info.getUserName(), key);
        } else {
            long userId = Long.parseLong(name.split(":")[1]);
            info.setUserId(userId);
            info.setUserName(info.getUserName());
        }

        return info;
    }

    @Override
    public StatusEnum login(LoginReqVO loginReqVO) throws Exception {
        //再去Redis里查询
        String key = ACCOUNT_PREFIX + loginReqVO.getUserId();
        String userName = redisTemplate.opsForValue().get(key);
        if (null == userName) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        if (!userName.equals(loginReqVO.getUserName())) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        //登录成功，保存登录状态
        if (!userInfoCacheService.saveAndCheckUserLoginStatus(loginReqVO.getUserId())) {
            //重复登录
            LOGGER.warn("repeat login {}", loginReqVO.getUserName());
            return StatusEnum.SUCCESS;
        }

        return StatusEnum.SUCCESS;
    }

    @Override
    public void saveRouteInfo(LoginReqVO loginReqVO, String msg) throws Exception {
        String key = ROUTE_PREFIX + loginReqVO.getUserId();
        redisTemplate.opsForValue().set(key, msg);
    }

    @Override
    public Map<Long, TIMServerResVO> loadRouteRelated() {

        Map<Long, TIMServerResVO> routes = new HashMap<>(64);


        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions()
                .match(ROUTE_PREFIX + "*")
                .build();
        Cursor<byte[]> scan = connection.scan(options);

        while (scan.hasNext()) {
            byte[] next = scan.next();
            String key = new String(next, StandardCharsets.UTF_8);
            LOGGER.info("key={}", key);
            parseServerInfo(routes, key);

        }
        try {
            scan.close();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }

        return routes;
    }

    @Override
    public TIMServerResVO loadRouteRelatedByUserId(Long userId) {
        String value = redisTemplate.opsForValue().get(ROUTE_PREFIX + userId);

        if (value == null) {
            throw new TIMException(OFF_LINE);
        }

        TIMServerResVO TIMServerResVO = new TIMServerResVO(RouteInfoParseUtil.parse(value));
        return TIMServerResVO;
    }

    private void parseServerInfo(Map<Long, TIMServerResVO> routes, String key) {
        long userId = Long.valueOf(key.split(":")[1]);
        String value = redisTemplate.opsForValue().get(key);
        TIMServerResVO TIMServerResVO = new TIMServerResVO(RouteInfoParseUtil.parse(value));
        routes.put(userId, TIMServerResVO);
    }


    @Override
    public void pushMsg(TIMServerResVO serverInfo, long sendUserId, ChatReqVO groupReqVO) throws Exception {
        TIMUserInfo timUserInfo = userInfoCacheService.loadUserInfoByUserId(sendUserId);

        String url = "http://" + serverInfo.getIp() + ":" + serverInfo.getHttpPort();
        ServerApi serverApi = new ProxyManager<>(ServerApi.class, url, okHttpClient).getInstance();
        SendMsgReqVO sendMsgReqVO = new SendMsgReqVO();
        BeanUtils.copyProperties(groupReqVO, sendMsgReqVO);
        String msg = timUserInfo.getUserName() + ":" + groupReqVO.getMsg();
        sendMsgReqVO.setMsg(msg);
        Response response = null;
        try {
            LOGGER.info("start push message to server:{}", url);
            response = (Response) serverApi.sendMsg(sendMsgReqVO);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        } finally {
            if (null != response && null != response.body()) {
                response.body().close();
            }
        }
    }

    @Override
    public void offLine(Long userId) throws Exception {

        // TODO 这里需要用lua保证原子性

        //删除路由
        redisTemplate.delete(ROUTE_PREFIX + userId);

        //删除登录状态
        userInfoCacheService.removeLoginStatus(userId);
    }

    @Override
    public RegisterInfoResVO getUserInfo(Long userId) {
        String key = ACCOUNT_PREFIX + userId;

        String name = redisTemplate.opsForValue().get(key);

        if (null == name) {
            throw new RuntimeException("user not exist");
        }
        return new RegisterInfoResVO(userId, name);
    }
}
