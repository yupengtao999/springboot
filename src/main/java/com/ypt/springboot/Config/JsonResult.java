package com.ypt.springboot.Config;

import java.io.Serializable;

public class JsonResult implements Serializable {
    private static final long serialVersionUID = 652951263305881145L;
    private boolean result;
    private Object msg;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public JsonResult(boolean result, Object msg) {
        this.result = result;
        this.msg = msg;
    }
}
