package com.siterwell.seriallibrary.usbserial.event;

/**
 * Created by TracyHenry on 2018/1/9.
 */

public class SerialReceiveEvent {
    private byte[] receive_data;

    public byte[] getReceive_data() {
        return receive_data;
    }

    public void setReceive_data(byte[] receive_data) {
        this.receive_data = receive_data;
    }
}
