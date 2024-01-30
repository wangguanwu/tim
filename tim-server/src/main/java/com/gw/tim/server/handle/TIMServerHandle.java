package com.gw.tim.server.handle;

import com.gw.tim.common.constant.Constants;
import com.gw.tim.common.exception.TIMException;
import com.gw.tim.common.kit.HeartBeatHandler;
import com.gw.tim.common.pojo.TIMUserInfo;
import com.gw.tim.common.protocol.TIMReqMsg;
import com.gw.tim.common.util.JsonUtil;
import com.gw.tim.common.util.NettyAttrUtil;
import com.gw.tim.server.kit.RouteHandler;
import com.gw.tim.server.kit.ServerHeartBeatHandlerImpl;
import com.gw.tim.server.service.UserStatusService;
import com.gw.tim.server.util.SessionSocketHolder;
import com.gw.tim.server.util.SpringBeanFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * @since JDK 1.8
 */
@ChannelHandler.Sharable
public class TIMServerHandle extends SimpleChannelInboundHandler<TIMReqMsg> {

    private final static Logger LOGGER = LoggerFactory.getLogger(TIMReqMsg.class);

    private static final long REFRESH_STATUS_TIME_INTERVAL_MS = 15 * 1000L;



    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //可能出现业务判断离线后再次触发 channelInactive
        TIMUserInfo userInfo = SessionSocketHolder.getUserId((NioSocketChannel) ctx.channel());
        if (userInfo != null) {
            LOGGER.warn("[{}] trigger channelInactive offline!", userInfo.getUserName());

            //Clear route info and offline.
            RouteHandler routeHandler = SpringBeanFactory.getBean(RouteHandler.class);
            routeHandler.userOffLine(userInfo, (NioSocketChannel) ctx.channel());

            ctx.channel().close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                //TIMUserInfo userInfo = SessionSocketHolder.getUserId((NioSocketChannel) ctx.channel());
                //System.out.println("定时检测客户端是否存活:" + userInfo.getUserName());
                HeartBeatHandler heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class);
                heartBeatHandler.process(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TIMReqMsg msg) throws Exception {

        if (msg.getType() == Constants.CommandType.LOGIN) {
            //保存客户端与 Channel 之间的关系
            SessionSocketHolder.put(msg.getRequestId(), (NioSocketChannel) ctx.channel());
            SessionSocketHolder.saveSession(msg.getRequestId(), msg.getReqMsg());
            LOGGER.info("client [{}] online success!!", msg.getReqMsg());
        }

        //心跳更新时间
        if (msg.getType() == Constants.CommandType.PING) {
            Long lastTime = NettyAttrUtil.getReaderTime(ctx.channel());
            long currentTime = System.currentTimeMillis();
            if (msg.getRequestId() == null) {
               LOGGER.error("PING error. requestId is null");
                SessionSocketHolder.remove((NioSocketChannel) ctx.channel());
                ctx.channel().close();
                return;
            }
            if (null != lastTime &&  (currentTime - lastTime) > REFRESH_STATUS_TIME_INTERVAL_MS) {
                //超过REFRESH_STATUS_TIME_INTERVAL_MS会更新redis状态
                UserStatusService userStatusService = SpringBeanFactory.getBean(UserStatusService.class);
                userStatusService.refreshUserOnlineStatus(msg.getRequestId());
            }
            NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
            //向客户端响应 pong 消息
            TIMReqMsg heartBeat = SpringBeanFactory.getBean("heartBeat",
                    TIMReqMsg.class);
            ctx.writeAndFlush(heartBeat).addListeners((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    LOGGER.error("IO error,close Channel");
                    future.channel().close();
                }
            });
        } else {
            LOGGER.info("received msg: {}", JsonUtil.toJson(msg));
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (TIMException.isResetByPeer(cause.getMessage())) {
            return;
        }
        LOGGER.error(cause.getMessage(), cause);

    }

}
