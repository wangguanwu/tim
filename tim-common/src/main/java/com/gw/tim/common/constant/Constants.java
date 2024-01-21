package com.gw.tim.common.constant;

/**
 *
 * @since JDK 1.8
 */
public class Constants {


    /**
     * 服务端手动 push 次数
     */
    public static final String COUNTER_SERVER_PUSH_COUNT = "counter.server.push.count" ;


    /**
     * 客户端手动 push 次数
     */
    public static final String COUNTER_CLIENT_PUSH_COUNT = "counter.client.push.count" ;


    /**
     * 自定义报文类型
     */
    public static class CommandType{
        /**
         * 登录
         */
        public static final int LOGIN = 1 ;
        /**
         * 业务消息
         */
        public static final int MSG = 2 ;

        /**
         * ping
         */
        public static final int PING = 3 ;
    }

    public static enum ChatType {

        SINGLE(1),

        GROUP(2);

        private final int type;

        ChatType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public static final String MQ_MESSAGE_P2P_TOPIC = "tim-message-p2p";

    public static final String MQ_MESSAGE_GROUP_TOPIC = "tim-message-group";

    public static final String MQ_MESSAGE_CONSUMER_TYPE_PERSISTENT = "tim-persistent-consumer";

    public static final String MQ_MESSAGE_CONSUMER_TYPE_FORWARD = "tim-forward-consumer";



}
