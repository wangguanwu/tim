package com.gw.tim.common.core.proxy;

import com.alibaba.fastjson.JSONObject;
import com.gw.tim.common.enums.StatusEnum;
import com.gw.tim.common.exception.TIMException;
import com.gw.tim.common.util.HttpClient;
import com.gw.tim.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *
 * @since JDK 1.8
 */

@Slf4j
public final class ProxyManager<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyManager.class);

    private Class<T> clazz;

    private String url;

    private OkHttpClient okHttpClient;

    /**
     *
     * @param clazz Proxied interface
     * @param url server provider url
     * @param okHttpClient http client
     */
    public ProxyManager(Class<T> clazz, String url, OkHttpClient okHttpClient) {
        this.clazz = clazz;
        this.url = url;
        this.okHttpClient = okHttpClient;
    }

    /**
     * Get proxy instance of api.
     * @return
     */
    public T getInstance() {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new ProxyInvocation());
    }


    private class ProxyInvocation implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String serverUrl = url + "/" + method.getName() ;

            if (args != null && args.length > 1) {
                throw new TIMException(StatusEnum.VALIDATION_FAIL);
            }
            log.info("start invoke http request");
            String bodyJson  = "{}";
            if (method.getParameterTypes().length > 0){
                Object para = args[0];
                bodyJson = JsonUtil.toJson(para);
            }
            log.info("request url :{}, request body:{}", url, bodyJson);

            return HttpClient.call(okHttpClient, bodyJson, serverUrl);
        }
    }
}
