package com.udacity.capstone.models.response;

/**
 * Created by chyupa on 05-May-16.
 */
public class BasicResponse {

    private boolean success;
    private String msg;

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
}
