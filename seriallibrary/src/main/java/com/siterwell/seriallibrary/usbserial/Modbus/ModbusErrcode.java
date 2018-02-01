package com.siterwell.seriallibrary.usbserial.Modbus;

import com.siterwell.seriallibrary.R;

import java.util.HashMap;
import java.util.Map;

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
    ERROR_RECEIVE_CRC(7,R.string.illegal_crc),       //CRC校验错误
    ERROR_UNKNOW(100,R.string.unknown_error);

    private int code;
    private int desc;

    private static Map<Integer, ModbusErrcode> mErrStrMap = new HashMap<Integer, ModbusErrcode>();
    static {
        mErrStrMap.put(ERROR_NORMAL.getCode(),ERROR_NORMAL);
        mErrStrMap.put(ERROR_FUNCTION.getCode(),ERROR_FUNCTION);
        mErrStrMap.put(ERROR_READ_COIL_ADDRESS.getCode(),ERROR_READ_COIL_ADDRESS);
        mErrStrMap.put(ERROR_READ_REGISTER_ADDRESS.getCode(),ERROR_READ_REGISTER_ADDRESS);
        mErrStrMap.put(ERROR_READ_COIL_COUNT.getCode(),ERROR_READ_COIL_COUNT);
        mErrStrMap.put(ERROR_READ_REGISTER_COUNT.getCode(),ERROR_READ_REGISTER_COUNT);
        mErrStrMap.put(ERROR_SLAVE.getCode(),ERROR_SLAVE);
        mErrStrMap.put(ERROR_RECEIVE_DATALENGTH.getCode(),ERROR_RECEIVE_DATALENGTH);
        mErrStrMap.put(ERROR_WRITE_COIL_VALUE.getCode(),ERROR_WRITE_COIL_VALUE);
        mErrStrMap.put(ERROR_WRITE_REGISTER_VALUE.getCode(),ERROR_WRITE_REGISTER_VALUE);
        mErrStrMap.put(ERROR_RECEIVE_CRC.getCode(),ERROR_RECEIVE_CRC);
        mErrStrMap.put(ERROR_UNKNOW.getCode(),ERROR_UNKNOW);
    }


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

    public static ModbusErrcode getErrorStr(Integer errCode) {
        ModbusErrcode errStrId = mErrStrMap.get(errCode);
        if ( null != errStrId ) {
            return errStrId;
        }
        return ModbusErrcode.ERROR_UNKNOW;
    }
}
