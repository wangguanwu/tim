package com.gw.tim.server.kit;

import com.gw.tim.server.config.AppConfiguration;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Zookeeper 工具
 *
 * @since JDK 1.8
 */
@Component
public class ZookeeperUtil {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperUtil.class);

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private AppConfiguration appConfiguration;

    /**
     * 创建父级节点
     */
    public void createRootNode() {
        boolean exists = zkClient.exists(appConfiguration.getZkRoot());
        if (exists) {
            return;
        }

        //创建 root
        zkClient.createPersistent(appConfiguration.getZkRoot());
    }

    /**
     * 写入指定节点 临时目录
     *
     * @param path
     */
    public void createNode(String path) {
        zkClient.createEphemeral(path);
    }

}
