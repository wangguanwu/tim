package com.gw.tim.gateway.api.vo.req;

import com.gw.tim.common.req.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Google Protocol 编解码发送
 *
 * @since JDK 1.8
 */

@Data
@ToString
@AllArgsConstructor
public class ChatReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    private Long toUserId;

    @NotNull(message = "toUserId 不能为空")
    private Long userId;


    @NotNull(message = "msg 不能为空")
    private String msg;

    private int type;

    public ChatReqVO() {
    }

    public ChatReqVO(Long toUserId, String msg) {
        this.toUserId = toUserId;
        this.msg = msg;
    }

}
