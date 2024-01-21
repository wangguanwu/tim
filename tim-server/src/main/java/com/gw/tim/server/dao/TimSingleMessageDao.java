package com.gw.tim.server.dao;

import com.gw.tim.server.pojo.TimSingleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * dao
 */

@Repository
public class TimSingleMessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int insertMessage(TimSingleMessage singleMessage)  {
        //SQL
        String sql = "INSERT INTO tim_single_message (msg_id , user_id , to_user_id, msg) values(?,?,?,?)";
        //执行写入
        return jdbcTemplate.update(sql, singleMessage.getMsgId(), singleMessage.getUserId(),
                singleMessage.getToUserId(), singleMessage.getMsg());
    }
}
