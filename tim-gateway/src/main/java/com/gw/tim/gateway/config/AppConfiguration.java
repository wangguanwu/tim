package com.gw.tim.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @since JDK 1.8
 */
@Component
public class AppConfiguration {

    @Value("${app.zk.root}")
    private String zkRoot;

    @Value("${app.zk.addr}")
    private String zkAddr;


    @Value("${server.port}")
    private int port;

    @Value("${app.zk.connect.timeout}")
    private int zkConnectTimeout;

    @Value("${app.route.way}")
    private String routeWay;

    @Value("${app.route.way.consitenthash:com.tuling.tim.common.route.algorithm.consistenthash.TreeMapConsistentHash}")
    private String consistentHashWay;

    @Value("${app.push.type}")
    private String pushType;

    @Value("${app.user.status.timeoutSec}")
    private int userStatusTimeoutSec = 3600;

    public int getZkConnectTimeout() {
        return zkConnectTimeout;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getZkRoot() {
        return zkRoot;
    }

    public void setZkRoot(String zkRoot) {
        this.zkRoot = zkRoot;
    }

    public String getZkAddr() {
        return zkAddr;
    }

    public void setZkAddr(String zkAddr) {
        this.zkAddr = zkAddr;
    }

    public String getRouteWay() {
        return routeWay;
    }

    public void setRouteWay(String routeWay) {
        this.routeWay = routeWay;
    }

    public String getConsistentHashWay() {
        return consistentHashWay;
    }

    public void setConsistentHashWay(String consistentHashWay) {
        this.consistentHashWay = consistentHashWay;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushType() {
        return this.pushType;
    }

    public int getUserStatusTimeoutSec() {
        return userStatusTimeoutSec;
    }

    public void setUserStatusTimeoutSec(int userStatusTimeoutSec) {
        this.userStatusTimeoutSec = userStatusTimeoutSec;
    }
}
