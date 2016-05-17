package com.udacity.capstone.models.response;

import com.udacity.capstone.models.User;

/**
 * Created by chyupa on 09-May-16.
 */
public class LoginResponse {

    private boolean success;
    private String msg;
    private int userId;
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
