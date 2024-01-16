package com.gw.tim.common.pojo;

/**
 * @since JDK 1.8
 */
public final class RouteInfo {

    private String ip;
    private Integer timServerPort;
    private Integer httpPort;

    public RouteInfo(String ip, Integer timServerPort, Integer httpPort) {
        this.ip = ip;
        this.timServerPort = timServerPort;
        this.httpPort = httpPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getTimServerPort() {
        return timServerPort;
    }

    public void setTimServerPort(Integer timServerPort) {
        this.timServerPort = timServerPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
}
