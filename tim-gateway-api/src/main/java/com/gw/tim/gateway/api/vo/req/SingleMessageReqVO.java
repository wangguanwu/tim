package com.gw.tim.gateway.api.vo.req;

import com.gw.tim.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * 单聊请求
 *
 * @since JDK 1.8
 */

@Data
@ToString
@AllArgsConstructor
public class SingleMessageReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    //消息发送者的 userId
    private Long userId;


    @NotNull(message = "userId 不能为空")
    //消息接收者的 userId
    private Long toUserId;


    @NotNull(message = "msg 不能为空")
    private String msg;

    private Long msgId;

    public SingleMessageReqVO() {
    }
}
