package com.gw.tim.client.vo.req;

import com.gw.tim.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * Google Protocol 编解码发送
 *
 * @since JDK 1.8
 */
public class GoogleProtocolVO extends BaseRequest {
    @NotNull(message = "requestId 不能为空")
    private Long requestId;

    @NotNull(message = "msg 不能为空")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "GoogleProtocolVO{" +
                "requestId=" + requestId +
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
