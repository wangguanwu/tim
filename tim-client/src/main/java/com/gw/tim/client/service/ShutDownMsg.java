package com.gw.tim.client.service;

import org.springframework.stereotype.Component;

/**
 *
 * @since JDK 1.8
 */
@Component
public class ShutDownMsg {
    private boolean isCommand ;

    /**
     * 置为用户主动退出状态
     */
    public void shutdown(){
        isCommand = true ;
    }

    public boolean checkStatus(){
        return isCommand ;
    }
}
