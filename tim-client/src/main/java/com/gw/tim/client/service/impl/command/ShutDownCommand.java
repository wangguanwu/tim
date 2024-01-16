package com.gw.tim.client.service.impl.command;

import com.gw.tim.client.client.TIMClient;
import com.gw.tim.client.service.EchoService;
import com.gw.tim.client.service.InnerCommand;
import com.gw.tim.client.service.MsgLogger;
import com.gw.tim.client.service.RouteRequest;
import com.gw.tim.client.service.ShutDownMsg;
import com.gw.tim.common.data.construct.RingBufferWheel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @since JDK 1.8
 */
@Service
public class ShutDownCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(ShutDownCommand.class);

    @Autowired
    private RouteRequest routeRequest ;

    @Autowired
    private TIMClient timClient;

    @Autowired
    private MsgLogger msgLogger;

    @Resource(name = "callBackThreadPool")
    private ThreadPoolExecutor callBackExecutor;

    @Autowired
    private EchoService echoService ;


    @Autowired
    private ShutDownMsg shutDownMsg ;

    @Autowired
    private RingBufferWheel ringBufferWheel ;

    @Override
    public void process(String msg) {
        echoService.echo("tim client closing...");
        shutDownMsg.shutdown();
        routeRequest.offLine();
        msgLogger.stop();
        callBackExecutor.shutdown();
        ringBufferWheel.stop(false);
        try {
            while (!callBackExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                echoService.echo("thread pool closing");
            }
            timClient.close();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
        echoService.echo("tim close success!");
        System.exit(0);
    }
}
