package com.gw.tim.gateway.api;

import com.gw.tim.common.res.BaseResponse;
import com.gw.tim.gateway.api.vo.req.ChatReqVO;
import com.gw.tim.gateway.api.vo.req.LoginReqVO;
import com.gw.tim.gateway.api.vo.req.P2PReqVO;
import com.gw.tim.gateway.api.vo.req.RegisterInfoReqVO;
import com.gw.tim.gateway.api.vo.res.RegisterInfoResVO;

/**
 * Route Api
 *
 * @since JDK 1.8
 */
public interface RouteApi {

    /**
     * group chat
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object groupRoute(ChatReqVO groupReqVO) throws Exception;

    /**
     * Point to point chat
     *
     * @param p2pRequest
     * @return
     * @throws Exception
     */
    Object p2pRoute(P2PReqVO p2pRequest) throws Exception;


    /**
     * Offline account
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object offLine(ChatReqVO groupReqVO) throws Exception;

    /**
     * Login account
     *
     * @param loginReqVO
     * @return
     * @throws Exception
     */
    Object login(LoginReqVO loginReqVO) throws Exception;

    /**
     * Register account
     *
     * @param registerInfoReqVO
     * @return
     * @throws Exception
     */
    BaseResponse<RegisterInfoResVO> registerAccount(RegisterInfoReqVO registerInfoReqVO) throws Exception;

    /**
     * Get all online users
     *
     * @return
     * @throws Exception
     */
    Object onlineUser() throws Exception;
}
