package com.gw.tim.gateway.api;

import com.gw.tim.common.res.BaseResponse;
import com.gw.tim.gateway.api.vo.req.*;
import com.gw.tim.gateway.api.vo.res.RegisterInfoResVO;

import java.util.List;
import java.util.Map;

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
    Object groupRoute(GroupMessageReqVO groupReqVO) throws Exception;

    /**
     * Point to point chat
     *
     * @param p2pRequest
     * @return
     * @throws Exception
     */
    Object p2pRoute(SingleMessageReqVO p2pRequest) throws Exception;


    /**
     * Offline account
     *
     * @param reqVo
     * @return
     * @throws Exception
     */
    Object offLine(ChatReqVO reqVo) throws Exception;

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

    BaseResponse<List<String>> getAllServerList() throws Exception;

    Object getUserInfo(Map<String, Object> map) throws Exception;
}
