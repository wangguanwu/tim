package com.gw.tim.server.service;

import com.gw.tim.common.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author guanwu
 * @created 2024/1/24 22:31
 */

@Service("userStatusService")
public class UserStatusService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public boolean refreshUserOnlineStatus(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().
                add(Constants.USER_LOGIN_STATUS_PREFIX, userId.toString(), System.currentTimeMillis()));
    }

}
