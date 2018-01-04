package com.siterwell.seriallibrary.usbserial.driver;

/*
@class UsbSerialRuntimeException
@autor Administrator
@time 2017/12/18 10:25
@email xuejunju_4595@qq.com
*/
@SuppressWarnings("serial")
public class UsbSerialRuntimeException extends RuntimeException {

    public UsbSerialRuntimeException() {
        super();
    }

    public UsbSerialRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public UsbSerialRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public UsbSerialRuntimeException(Throwable throwable) {
        super(throwable);
    }

}
