package com.siterwell.seriallibrary.usbserial.bean;

/**
 * Created by TracyHenry on 2018/1/16.
 */

public class ModbusAddressBean {
    private int address;
    private String name;
    private TypeModbusAddress type;  //

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

    @Override
    public String toString() {
        return "ModbusAddressBean{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
