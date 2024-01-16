package com.gw.tim.client.service.impl.command;

import com.gw.tim.client.service.InnerCommand;
import com.gw.tim.client.service.EchoService;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @since JDK 1.8
 */
@Service
public class EmojiCommand implements InnerCommand {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmojiCommand.class);

    @Autowired
    private EchoService echoService ;


    @Override
    public void process(String msg) {
        if (msg.split(" ").length <=1){
            echoService.echo("incorrect commond, :emoji [option]") ;
            return ;
        }
        String value = msg.split(" ")[1];
        if (value != null) {
            Integer index = Integer.parseInt(value);
            List<Emoji> all = (List<Emoji>) EmojiManager.getAll();
            all = all.subList(5 * index, 5 * index + 5);

            for (Emoji emoji : all) {
                echoService.echo(EmojiParser.parseToAliases(emoji.getUnicode()) + "--->" + emoji.getUnicode());
            }
        }

    }
}
