package com.gw.tim.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author guanwu
 * @created 2024/1/16 20:05
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/redis/set")
    public String setKey(@RequestParam("key")String key, @RequestParam("val") String val) {
        redisTemplate.opsForValue().set(key, val);
        return "ok";
    }

    @GetMapping("/redis/get")
    public String getKey(@RequestParam("key")String key) {
        Object  res = redisTemplate.opsForValue().get(key);
        return String.valueOf(res);
    }
}
