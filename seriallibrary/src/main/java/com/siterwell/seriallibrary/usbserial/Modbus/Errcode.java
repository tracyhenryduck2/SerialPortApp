package com.siterwell.seriallibrary.usbserial.Modbus;

import android.content.Context;

import com.siterwell.seriallibrary.R;

/**
 * Created by TracyHenry on 2018/1/4.
 */

public enum Errcode {
    ERROR_NORMAL(0,R.string.normal),
    ERROR_FUNCTION(1,R.string.illegal_function),//非法功能
    ERROR_ADDRESS(2,R.string.illegal_address),//非法数据地址
    ERROR_VALUE(3,R.string.illegal_data),  //非法数据值
    ERROR_SLAVE(4,R.string.slave_error),  //从机故障

    //上面四种是从错误码中接收到的上位机自身的错误

    ERROR_RECEIVE_DATALENGTH(5,R.string.illegal_data_length),//数据长度有误
    ERROR_RECEIVE_ADDRESS(6,R.string.illegal_data_address),   //数据地址有误
    ERROR_RECEIVE_FUNCTION(7,R.string.illegal_function_code),  //功能码有误
    ERROR_RECEIVE_CRC(8,R.string.illegal_crc);       //CRC校验错误

    private int code;
    private int desc;


    Errcode(int code, int res_desc) {
        this.code = code;
        this.desc = res_desc;
    }

    public int getCode() {
        return code;
    }

    public int getDesc() {
        return desc;
    }
}
