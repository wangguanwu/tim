package com.gw.tim.common.protocol;

/**
 * @since JDK 1.8
 */
public class TIMReqMsg {

    private Long requestId;
    private String reqMsg;
    private Integer type;

    public TIMReqMsg() {
    }

    public TIMReqMsg(Long requestId, String reqMsg, Integer type) {
        this.requestId = requestId;
        this.reqMsg = reqMsg;
        this.type = type;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getReqMsg() {
        return reqMsg;
    }

    public void setReqMsg(String reqMsg) {
        this.reqMsg = reqMsg;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
