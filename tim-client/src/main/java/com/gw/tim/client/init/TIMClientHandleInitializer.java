package com.gw.tim.client.init;

import com.gw.tim.client.handle.TIMClientHandle;
import com.gw.tim.common.protocol.ObjDecoder;
import com.gw.tim.common.protocol.ObjEncoder;
import com.gw.tim.common.protocol.TIMReqMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @since JDK 1.8
 */
public class TIMClientHandleInitializer extends ChannelInitializer<Channel> {

    private final com.gw.tim.client.handle.TIMClientHandle TIMClientHandle = new TIMClientHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 15秒客户端没给服务端主动发送消息就触发写空闲，执行TIMClientHandle的userEventTriggered方法给服务端发送一次心跳
                .addLast(new IdleStateHandler(0, 15, 0))
                .addLast(new ObjEncoder(TIMReqMsg.class))
                .addLast(new ObjDecoder(TIMReqMsg.class))
                .addLast(TIMClientHandle)
        ;
    }
}
