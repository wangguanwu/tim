package com.gw.tim.server.api.vo.mq;

import com.gw.tim.common.req.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author guanwu
 * @created 2024/1/21 16:12
 */

@Data
public class MqGroupMessageReqVO extends BaseRequest implements Serializable {

    private static final long serialVersionUID = -4830943113745630784L;

    private Long userId;

    //消息接收者的 userId
    private Long groupId;

    private String msg;

    private Long msgId;

}
