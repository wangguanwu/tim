package com.gw.tim.client.service.impl;

import com.gw.tim.client.service.MsgLogger;
import com.gw.tim.client.TIMClientApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = TIMClientApplication.class)
@RunWith(SpringRunner.class)
public class AsyncMsgLoggerTest {


    @Autowired
    private MsgLogger msgLogger;

    @Test
    public void writeLog() throws Exception {
        for (int i = 0; i < 10; i++) {
            msgLogger.log("zhangsan:【asdsd】" + i);
        }

        TimeUnit.SECONDS.sleep(2);
    }


    @Test
    public void query() {
        String tuling = msgLogger.query("tuling");
        System.out.println(tuling);
    }

}