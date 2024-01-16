package com.gw.tim.common.route.algorithm.loop;

import com.gw.tim.common.enums.StatusEnum;
import com.gw.tim.common.exception.TIMException;
import com.gw.tim.common.route.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @since JDK 1.8
 */
public class LoopHandle implements RouteHandle {
    private AtomicLong index = new AtomicLong();

    @Override
    public String routeServer(List<String> values,String key) {
        if (values.size() == 0) {
            throw new TIMException(StatusEnum.SERVER_NOT_AVAILABLE) ;
        }
        Long position = index.incrementAndGet() % values.size();
        if (position < 0) {
            position = 0L;
        }

        return values.get(position.intValue());
    }
}
