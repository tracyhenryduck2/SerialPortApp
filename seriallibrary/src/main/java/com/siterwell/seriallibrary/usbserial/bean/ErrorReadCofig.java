package com.siterwell.seriallibrary.usbserial.bean;

import com.siterwell.seriallibrary.R;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public enum ErrorReadCofig {
    Success(0, R.string.read_config_success),
    ERROR_READ_COFIG_NULL(1,R.string.read_config_err_null),
    ERROR_READ_COFIG_FORMAT(2,R.string.read_config_err_format);

    private int code;
    private int desc;

    ErrorReadCofig(int code, int desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getDesc() {
        return desc;
    }

    public void setDesc(int desc) {
        this.desc = desc;
    }
}
