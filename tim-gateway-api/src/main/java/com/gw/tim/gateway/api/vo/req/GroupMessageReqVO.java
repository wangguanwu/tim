package com.gw.tim.gateway.api.vo.req;


import com.gw.tim.common.req.BaseRequest;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 群发请求
 *
 * @since JDK 1.8
 */

@Data
@ToString
@Builder
@AllArgsConstructor
public class GroupMessageReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    //消息发送者的 userId
    private Long userId;

    @NotNull(message = "msg 不能为空")
    private String msg;

    private Long groupId;

    private Long msgId;

    public GroupMessageReqVO() {

    }

}
