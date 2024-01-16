package com.gw.tim.server.api.vo.req;

import com.gw.tim.common.req.BaseRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @since JDK 1.8
 */

@ToString
@Data
@RequiredArgsConstructor
public class SendMsgReqVO extends BaseRequest {

    @NotNull(message = "userId 不能为空")
    private Long toUserId;

    @NotNull(message = "toUserId 不能为空")
    private Long fromUserId;


    @NotNull(message = "msg 不能为空")
    private String msg;

    private int type;

}
