package com.gw.tim.server.dao;

import com.gw.tim.common.pojo.TimUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author guanwu
 * @created 2024/1/16 21:39
 */

@Repository
public class TimUserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TimUser> getUserList() {
        String sql = "select user_name as userName, gender, user_nick as userNick ,id  from tim_user";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(TimUser.class));
    }

    public int insertUser(TimUser user) {
        //SQL
        String sql = " INSERT INTO tim_user (user_name , gender , user_nick ) values(?,?,?)";
        //执行写入
        return jdbcTemplate.update(sql, user.getUserName(), user.getGender(), user.getUserNick());
    }

}
