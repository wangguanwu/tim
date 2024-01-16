package com.gw.tim.client.service.impl.command;

import com.gw.tim.client.service.InnerCommand;
import com.gw.tim.client.service.EchoService;
import com.gw.tim.common.enums.SystemCommandEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 * @since JDK 1.8
 */
@Service
public class PrintAllCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(PrintAllCommand.class);

    @Autowired
    private EchoService echoService ;

    @Override
    public void process(String msg) {
        Map<String, String> allStatusCode = SystemCommandEnum.getAllStatusCode();
        echoService.echo("====================================");
        for (Map.Entry<String, String> stringStringEntry : allStatusCode.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            echoService.echo(key + "----->" + value);
        }
        echoService.echo("====================================");
    }
}
