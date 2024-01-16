package com.gw.tim.common.route.algorithm.consistenthash;

import com.gw.tim.common.route.algorithm.RouteHandle;

import java.util.List;

/**
 * @since JDK 1.8
 */
public class ConsistentHashHandle implements RouteHandle {
    private AbstractConsistentHash hash;

    public void setHash(AbstractConsistentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values, key);
    }
}
