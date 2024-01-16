package com.gw.tim.client.vo.res;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @since JDK 1.8
 */

@Data
@ToString
public class SendMsgResVO {
    private String msg ;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
