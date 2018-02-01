package com.siterwell.seriallibrary.usbserial.bean;

/**
 * Created by TracyHenry on 2018/1/16.
 */

public enum TypeModbusAddress {
    TYPE_MODBUS_COIL(0),
    TYPE_MODBUS_REGISTER(1);

    TypeModbusAddress(int code) {
       this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
