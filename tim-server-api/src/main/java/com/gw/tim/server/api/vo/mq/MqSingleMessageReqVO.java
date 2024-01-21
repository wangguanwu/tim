package com.gw.tim.server.api.vo.mq;

import com.gw.tim.common.req.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author guanwu
 * @created 2024/1/21 16:11
 */

@Data
public class MqSingleMessageReqVO extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 6281389161988406734L;

    private Long msgId;

    private Long userId;

    //消息接收者的 groupId

    private Long toUserId;

    private String msg;
}
