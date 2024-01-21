package com.gw.tim.server.dao;

import com.gw.tim.server.pojo.TimGroupMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 *
 *
 */

@Repository
public class TimGroupMessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int insertMessage(TimGroupMessage timGroupMessage)  {
        //SQL
        String sql = " INSERT INTO tim_group_message (msg_id , user_id , groupId, msg) values(?,?,?,?)";
        //执行写入
        return jdbcTemplate.update(sql, timGroupMessage.getMsgId(), timGroupMessage.getUserId(),
                timGroupMessage.getGroupId(), timGroupMessage.getMsg());
    }
}
