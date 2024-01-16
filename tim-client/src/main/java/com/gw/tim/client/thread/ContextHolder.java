package com.gw.tim.client.thread;

/**
 * Something about of client runtime sign.
 *
 * @since JDK 1.8
 */
public class ContextHolder {
    private static final ThreadLocal<Boolean> IS_RECONNECT = new ThreadLocal<>() ;

    public static void setReconnect(boolean reconnect){
        IS_RECONNECT.set(reconnect);
    }

    public static Boolean getReconnect(){
        return IS_RECONNECT.get() ;
    }

    public static void clear(){
        IS_RECONNECT.remove();
    }
}
