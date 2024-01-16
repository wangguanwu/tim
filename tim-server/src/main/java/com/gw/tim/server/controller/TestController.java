package com.gw.tim.server.controller;

import com.gw.tim.common.pojo.TimUser;
import com.gw.tim.server.dao.TimUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author guanwu
 * @created 2024/1/16 21:42
 */

@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private TimUserDao timUserDao;


    @GetMapping("/queryAllUser")
    public List<TimUser> queryAllUser() {
        return timUserDao.getUserList();
    }

    @PostMapping("/addUser")
    public boolean addTimUser(@RequestBody TimUser timUser) {
        return timUserDao.insertUser(timUser) > 0;
    }
}
