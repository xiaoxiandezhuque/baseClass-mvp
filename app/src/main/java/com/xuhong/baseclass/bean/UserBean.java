package com.xuhong.baseclass.bean;

import org.litepal.annotation.Column;

/**
 * Created by xuhong on 2016/8/4.
 */

public class UserBean extends BaseBean {

    private int id = 1;
    @Column(unique = true)
    private String uid;
    private String username;
    private String userhead;
    private String userlevel;
    private String userphone;
    private String userscore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public String getUserlevel() {
        return userlevel;
    }

    public void setUserlevel(String userlevel) {
        this.userlevel = userlevel;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUserscore() {
        return userscore;
    }

    public void setUserscore(String userscore) {
        this.userscore = userscore;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", userhead='" + userhead + '\'' +
                ", userlevel='" + userlevel + '\'' +
                ", userphone='" + userphone + '\'' +
                ", userscore='" + userscore + '\'' +
                '}';
    }
}
