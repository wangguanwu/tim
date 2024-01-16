package com.gw.tim.server.test;

import com.gw.tim.client.TIMClientApplication;
import com.gw.tim.client.service.RouteRequest;
import com.gw.tim.client.vo.req.LoginReqVO;
import com.gw.tim.client.vo.res.TIMServerResVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @since JDK 1.8
 */
@SpringBootTest(classes = TIMClientApplication.class)
@RunWith(SpringRunner.class)
public class RouteTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteTest.class);

    @Value("${tim.user.id}")
    private long userId;

    @Value("${tim.user.userName}")
    private String userName;

    @Autowired
    private RouteRequest routeRequest ;

    @Test
    public void test() throws Exception {
        LoginReqVO vo = new LoginReqVO(userId,userName) ;
        TIMServerResVO.ServerInfo timServer = routeRequest.getTIMServer(vo);
        LOGGER.info("timServer=[{}]",timServer.toString());
    }
}
