package com.gw.tim.server.server;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.protocol.TIMReqMsg;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.server.init.TIMServerInitializer;
import com.gw.tim.server.util.SessionSocketHolder;
import com.gw.tim.server.api.vo.req.SendMsgReqVO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Set;

/**
 * @since JDK 1.8
 */
@Component
public class TIMServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(TIMServer.class);

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();


    @Value("${app.server.port}")
    private int nettyPort;


    /**
     * 启动 tim server
     *
     * @return
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(nettyPort))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new TIMServerInitializer());

        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            LOGGER.info("Start tim server success!!!");
        }
    }


    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        LOGGER.info("Close tim server success!!!");
    }


    /**
     * Push msg to client.
     *
     * @param sendMsgReqVO 消息
     */
    public void sendMsg(SendMsgReqVO sendMsgReqVO) {
        NioSocketChannel socketChannel = SessionSocketHolder.get(sendMsgReqVO.getToUserId());

        if (null == socketChannel) {
            LOGGER.warn("client {} offline or not current service", sendMsgReqVO.getToUserId());
            return;
        }
        TIMReqMsg protocol = new TIMReqMsg(sendMsgReqVO.getToUserId(), JsonUtil.toJson(sendMsgReqVO), Constants.CommandType.MSG);

        ChannelFuture future = socketChannel.writeAndFlush(protocol);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("server push msg:[{}]", sendMsgReqVO));
    }

    public void sendGroupMsg(SendMsgReqVO sendMsgReqVO) {
        Set<NioSocketChannel> socketChannelSet = SessionSocketHolder.getAllChannel();
        Long fromUserId = sendMsgReqVO.getUserId();

        // remove  sender channel to avoid sending the same msg to sender
        NioSocketChannel nioSocketChannel = SessionSocketHolder.get(fromUserId);
        socketChannelSet.remove(nioSocketChannel);

        TIMReqMsg protocol = new TIMReqMsg(sendMsgReqVO.getToUserId(), JsonUtil.toJson(sendMsgReqVO), Constants.CommandType.MSG);

        socketChannelSet.parallelStream().forEach(socketChannel -> {
            ChannelFuture future = socketChannel.writeAndFlush(protocol);
            future.addListener((ChannelFutureListener) channelFuture ->
                    LOGGER.info("server push msg:[{}]", sendMsgReqVO));
        });
    }
}
