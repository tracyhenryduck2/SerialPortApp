package com.siterwell.seriallibrary.usbserial.Modbus;

import com.siterwell.seriallibrary.R;

/**
 * Created by TracyHenry on 2018/1/4.
 */

public enum ModbusErrcode {
    ERROR_NORMAL(0,R.string.normal),
    ERROR_FUNCTION(1,R.string.illegal_function),//非法功能
    ERROR_READ_COIL_ADDRESS(2,R.string.illegal_address),//非法线圈地址
    ERROR_READ_REGISTER_ADDRESS(2,R.string.illegal_address),//非法寄存器地址
    ERROR_READ_COIL_COUNT(3,R.string.illegal_coil_count),  //非法线圈数量
    ERROR_READ_REGISTER_COUNT(3,R.string.illegal_coil_count),  //非法寄存器数量
    ERROR_SLAVE(4,R.string.slave_error),  //从机故障
    ERROR_RECEIVE_DATALENGTH(5,R.string.illegal_data_length),//非法数据
    ERROR_WRITE_COIL_VALUE(6,R.string.illegal_coil_value),//非法线圈写入值
    ERROR_WRITE_REGISTER_VALUE(6,R.string.illegal_register_value),//非法寄存器写入值
    ERROR_RECEIVE_CRC(7,R.string.illegal_crc);       //CRC校验错误

    private int code;
    private int desc;


    ModbusErrcode(int code, int res_desc) {
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
