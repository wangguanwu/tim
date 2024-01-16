package com.gw.tim.client.controller;

import com.gw.tim.client.service.RouteRequest;
import com.gw.tim.client.vo.req.GoogleProtocolVO;
import com.gw.tim.client.vo.req.GroupReqVO;
import com.gw.tim.client.vo.req.StringReqVO;
import com.gw.tim.client.vo.res.SendMsgResVO;
import com.gw.tim.client.client.TIMClient;
import com.gw.tim.client.vo.req.SendMsgReqVO;
import com.gw.tim.common.enums.StatusEnum;
import com.gw.tim.common.res.BaseResponse;
import com.gw.tim.common.res.NULLBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @since JDK 1.8
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private TIMClient heartbeatClient;


    @Autowired
    private RouteRequest routeRequest;


    /**
     * 向服务端发消息 字符串
     *
     * @param stringReqVO
     * @return
     */
    @RequestMapping(value = "sendStringMsg", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<NULLBody> sendStringMsg(@RequestBody StringReqVO stringReqVO) {
        BaseResponse<NULLBody> res = new BaseResponse();

        for (int i = 0; i < 100; i++) {
            heartbeatClient.sendStringMsg(stringReqVO.getMsg());
        }

        SendMsgResVO sendMsgResVO = new SendMsgResVO();
        sendMsgResVO.setMsg("OK");
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 向服务端发消息 Google ProtoBuf
     *
     * @param googleProtocolVO
     * @return
     */
    @RequestMapping(value = "sendProtoBufMsg", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<NULLBody> sendProtoBufMsg(@RequestBody GoogleProtocolVO googleProtocolVO) {
        BaseResponse<NULLBody> res = new BaseResponse();

        for (int i = 0; i < 100; i++) {
            heartbeatClient.sendGoogleProtocolMsg(googleProtocolVO);
        }

        SendMsgResVO sendMsgResVO = new SendMsgResVO();
        sendMsgResVO.setMsg("OK");
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }


    /**
     * 群发消息
     *
     * @param sendMsgReqVO
     * @return
     */
    @RequestMapping(value = "sendGroupMsg", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse sendGroupMsg(@RequestBody SendMsgReqVO sendMsgReqVO) throws Exception {
        BaseResponse<NULLBody> res = new BaseResponse();

        GroupReqVO groupReqVO = new GroupReqVO(sendMsgReqVO.getUserId(), sendMsgReqVO.getMsg());
        routeRequest.sendGroupMsg(groupReqVO);

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }
}
