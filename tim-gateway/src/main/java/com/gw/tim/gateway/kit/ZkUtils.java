package com.gw.tim.gateway.kit;

import com.alibaba.fastjson.JSON;
import com.gw.tim.gateway.cache.ServerCache;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Zookeeper kit
 *
 * @since JDK 1.8
 */
@Component
public class ZkUtils {

    private static Logger logger = LoggerFactory.getLogger(ZkUtils.class);


    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ServerCache serverCache;


    /**
     * 监听事件
     *
     * @param path
     */
    public void subscribeEvent(String path) {
        zkClient.subscribeChildChanges(path, (parentPath, currentChildren) -> {
            logger.info("Clear and update local cache parentPath=[{}],currentChildren=[{}]", parentPath, currentChildren.toString());

            //update local cache, delete and save.
            serverCache.updateCache(currentChildren);
        });


    }


    /**
     * get all server node from zookeeper
     *
     * @return
     */
    public List<String> getAllNode() {
        List<String> children = zkClient.getChildren("/route");
        logger.info("Query all node =[{}] success.", JSON.toJSONString(children));
        return children;
    }


}
