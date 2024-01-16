package com.gw.tim.client.vo.req;

import com.gw.tim.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @since JDK 1.8
 */
public class SendMessageReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    private String msg;

    @NotNull(message = "userId 不能为空")
    private Long userId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
