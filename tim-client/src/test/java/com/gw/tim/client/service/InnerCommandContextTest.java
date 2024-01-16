package com.gw.tim.client.service;

import com.gw.tim.client.TIMClientApplication;
import com.gw.tim.common.enums.SystemCommandEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TIMClientApplication.class)
@RunWith(SpringRunner.class)
public class InnerCommandContextTest {

    @Autowired
    private InnerCommandContext context;

    @Test
    public void execute() {
        String msg = ":all";
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute3() {
        String msg = SystemCommandEnum.ONLINE_USER.getCommandType();
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute4() {
        String msg = ":q 天气";
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute5() {
        String msg = ":q tuling";
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute6() {
        String msg = SystemCommandEnum.AI.getCommandType();
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute7() {
        String msg = SystemCommandEnum.QAI.getCommandType();
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute8() {
        String msg = ":pu tuling";
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute9() {
        String msg = SystemCommandEnum.INFO.getCommandType();
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }

    @Test
    public void execute10() {
        String msg = "dsds";
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }


    // @Test
    public void quit() {
        String msg = ":q!";
        InnerCommand execute = context.getInstance(msg);
        execute.process(msg);
    }
}