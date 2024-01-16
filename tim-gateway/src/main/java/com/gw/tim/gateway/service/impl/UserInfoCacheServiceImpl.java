package com.gw.tim.gateway.service.impl;

import com.gw.tim.common.pojo.TIMUserInfo;
import com.gw.tim.gateway.service.UserInfoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.gw.tim.gateway.constant.Constant.ACCOUNT_PREFIX;
import static com.gw.tim.gateway.constant.Constant.LOGIN_STATUS_PREFIX;

/**
 * @since JDK 1.8
 */
@Service
public class UserInfoCacheServiceImpl implements UserInfoCacheService {

    /**
     * todo 本地缓存，为了防止内存撑爆，后期可换为 LRU。
     */
    private final static Map<Long, TIMUserInfo> USER_INFO_MAP = new ConcurrentHashMap<>(64);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public TIMUserInfo loadUserInfoByUserId(Long userId) {

        //优先从本地缓存获取
        TIMUserInfo timUserInfo = USER_INFO_MAP.get(userId);
        if (timUserInfo != null) {
            return timUserInfo;
        }

        //load redis
        String sendUserName = redisTemplate.opsForValue().get(ACCOUNT_PREFIX + userId);
        if (sendUserName != null) {
            timUserInfo = new TIMUserInfo(userId, sendUserName);
            USER_INFO_MAP.put(userId, timUserInfo);
        }

        return timUserInfo;
    }

    @Override
    public boolean saveAndCheckUserLoginStatus(Long userId) throws Exception {

        Long add = redisTemplate.opsForSet().add(LOGIN_STATUS_PREFIX, userId.toString());
        if (add == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void removeLoginStatus(Long userId) throws Exception {
        redisTemplate.opsForSet().remove(LOGIN_STATUS_PREFIX, userId.toString());
    }

    @Override
    public Set<TIMUserInfo> onlineUser() {
        Set<TIMUserInfo> set = null;
        Set<String> members = redisTemplate.opsForSet().members(LOGIN_STATUS_PREFIX);
        for (String member : members) {
            if (set == null) {
                set = new HashSet<>(64);
            }
            TIMUserInfo timUserInfo = loadUserInfoByUserId(Long.valueOf(member));
            set.add(timUserInfo);
        }

        return set;
    }

}
