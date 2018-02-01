package com.siterwell.seriallibrary.usbserial.bean;

import java.io.Serializable;

/**
 * Created by TracyHenry on 2018/1/16.
 */

public class ModbusAddressBean implements Serializable{
    private int address;
    private String name;
    private TypeModbusAddress type;  //
    private int data = -1;

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeModbusAddress getType() {
        return type;
    }

    public void setType(TypeModbusAddress type) {
        this.type = type;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ModbusAddressBean{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", data=" + data +
                '}';
    }
}
