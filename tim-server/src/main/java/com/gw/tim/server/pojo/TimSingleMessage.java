package com.gw.tim.server.pojo;

import com.gw.tim.common.req.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author guanwu
 */

@Data
public class TimSingleMessage extends BaseRequest implements Serializable {

    private static final long serialVersionUID = -6066549328721524468L;

    private Long userId;

    //消息接收者的 userId
    private Long receiveUserId;

    private String msg;
}
