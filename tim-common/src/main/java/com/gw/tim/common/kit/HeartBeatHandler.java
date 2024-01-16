package com.gw.tim.common.kit;

import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @since JDK 1.8
 */
public interface HeartBeatHandler {

    /**
     * 处理心跳
     * @param ctx
     * @throws Exception
     */
    void process(ChannelHandlerContext ctx) throws Exception ;
}
