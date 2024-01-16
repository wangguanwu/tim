package com.gw.tim.gateway.api.vo.req;

import com.gw.tim.common.req.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @since JDK 1.8
 */
public class SendMsgReqVO extends BaseRequest {

    @NotNull(message = "msg 不能为空")
    private String msg;

    @NotNull(message = "id 不能为空")
    private long id;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
