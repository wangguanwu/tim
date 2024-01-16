package com.gw.tim.server.init;

import com.gw.tim.common.protocol.ObjDecoder;
import com.gw.tim.common.protocol.ObjEncoder;
import com.gw.tim.common.protocol.TIMReqMsg;
import com.gw.tim.server.handle.TIMServerHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @since JDK 1.8
 */
public class TIMServerInitializer extends ChannelInitializer<Channel> {

    private final TIMServerHandle timServerHandle = new TIMServerHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline()
                // 20秒没有收到客户端发送消息或心跳就触发读空闲，执行TIMServerHandle的userEventTriggered方法关闭客户端连接
                .addLast(new IdleStateHandler(20, 0, 0))
                .addLast(new ObjEncoder(TIMReqMsg.class))
                .addLast(new ObjDecoder(TIMReqMsg.class))
                .addLast(timServerHandle);
    }
}
