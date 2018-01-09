package com.siterwell.seriallibrary.usbserial.event;

/**
 * Created by TracyHenry on 2018/1/9.
 */

public class SerialSendEvent {
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
