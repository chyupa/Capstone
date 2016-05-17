package com.udacity.capstone.models.response;

import java.lang.reflect.Array;

/**
 * Created by chyupa on 05-May-16.
 */
public class UserResponse {
    private boolean success;
    private int id;
    private String msg;
    private String[] name;
    private String[] email;
    private String[] password;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String[] getName() {
        return name;
    }

    public String getNameError() {
        return name[0];
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public String[] getEmail() {
        return email;
    }

    public String getEmailError() {
        return email[0];
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String[] getPassword() {
        return password;
    }

    public String getPasswordError() {
        return password[0];
    }

    public void setPassword(String[] password) {
        this.password = password;
    }
}
