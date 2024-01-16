package com.gw.tim.client.vo.req;

import com.gw.tim.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @since JDK 1.8
 */
public class TextMessageReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
