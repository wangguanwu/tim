package com.gw.tim.client.service;

/**
 *
 * @since JDK 1.8
 */
public interface EchoService {

    /**
     * echo msg to terminal
     * @param msg message
     * @param replace
     */
    void echo(String msg, Object... replace) ;
}
