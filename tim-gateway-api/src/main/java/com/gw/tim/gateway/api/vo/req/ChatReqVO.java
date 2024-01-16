package com.gw.tim.gateway.api.vo.req;

import com.gw.tim.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * Google Protocol 编解码发送
 *
 * @since JDK 1.8
 */
public class ChatReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    private Long userId;


    @NotNull(message = "msg 不能为空")
    private String msg;

    public ChatReqVO() {
    }

    public ChatReqVO(Long userId, String msg) {
        this.userId = userId;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GroupReqVO{" +
                "userId=" + userId +
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
