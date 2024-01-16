package com.gw.tim.server.api;

import com.gw.tim.server.api.vo.req.SendMsgReqVO;

/**
 *
 * @since JDK 1.8
 */
public interface ServerApi {

    /**
     * Push msg to client
     * @param sendMsgReqVO
     * @return
     * @throws Exception
     */
    Object sendMsg(SendMsgReqVO sendMsgReqVO) throws Exception;
}
