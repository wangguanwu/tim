package com.gw.tim.server.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author guanwu
 */

@Data
public class TimSingleMessage implements Serializable {

    private static final long serialVersionUID = -6066549328721524468L;

    private Long msgId;

    private Long userId;

    //消息接收者的 userId
    private Long toUserId;

    private String msg;
}
