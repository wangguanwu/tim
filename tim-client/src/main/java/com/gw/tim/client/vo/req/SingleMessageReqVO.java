package com.gw.tim.client.vo.req;

import com.gw.tim.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * 单聊请求
 *
 * @since JDK 1.8
 */
public class SingleMessageReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    //消息发送者的 userId
    private Long userId;


    @NotNull(message = "userId 不能为空")
    //消息接收者的 userId
    private Long toUserId;


    @NotNull(message = "msg 不能为空")
    private String msg;

    public SingleMessageReqVO() {
    }

    public SingleMessageReqVO(Long userId, Long toUserId, String msg) {
        this.userId = userId;
        this.toUserId = toUserId;
        this.msg = msg;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
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
