package com.gw.tim.server.api.vo.req;

import com.gw.tim.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @since JDK 1.8
 */
public class SendMsgReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    private String msg;

    @NotNull(message = "userId 不能为空")
    private Long userId;

    public SendMsgReqVO() {
    }

    public SendMsgReqVO(String msg, Long userId) {
        this.msg = msg;
        this.userId = userId;
    }

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

    @Override
    public String toString() {
        return "SendMsgReqVO{" +
                "msg='" + msg + '\'' +
                ", userId=" + userId +
                "} " + super.toString();
    }
}
