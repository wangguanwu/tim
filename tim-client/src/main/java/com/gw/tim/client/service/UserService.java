package com.gw.tim.client.service;

import com.gw.tim.client.vo.UserInfo;
import com.gw.tim.client.vo.res.OnlineUsersResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guanwu
 **/

@Service("userService")
public class UserService {

    @Autowired
    private RouteRequest routeRequest;

    private static final Map<Long, String> userMap = new ConcurrentHashMap<>(16);

    public UserInfo getUserInfo(Long userId) {
        final String name = userMap.get(userId);

        if (null != name) {
            return new UserInfo(userId, name);
        }
        final OnlineUsersResVO userInfo = routeRequest.getUserInfo(userId);

        if (null == userInfo) {
            return null;
        }
        final OnlineUsersResVO.DataBodyBean dataBodyBean = userInfo.getDataBody().get(0);
        userMap.put(userId, dataBodyBean.getUserName());
        return  new UserInfo(userId, dataBodyBean.getUserName());
    }
}
