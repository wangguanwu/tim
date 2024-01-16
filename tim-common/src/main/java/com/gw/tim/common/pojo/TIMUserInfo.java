package com.gw.tim.common.pojo;

/**
 * 用户信息
 *
 * @since JDK 1.8
 */
public class TIMUserInfo {
    private Long userId ;
    private String userName ;

    public TIMUserInfo(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "TIMUserInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
