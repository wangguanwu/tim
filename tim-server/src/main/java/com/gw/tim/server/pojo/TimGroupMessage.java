package com.gw.tim.server.pojo;

import com.gw.tim.common.req.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 *
 */

@Data
public class TimGroupMessage extends BaseRequest implements Serializable {

    private static final long serialVersionUID = -9222454377496805671L;

    private Long userId;

    //消息接收者的 userId
    private Long groupId;

    private String msg;
}
