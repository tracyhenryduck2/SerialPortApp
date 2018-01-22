package com.siterwell.seriallibrary.usbserial.event;

import com.siterwell.seriallibrary.usbserial.driver.UsbSerialDriver;

/**
 * Created by TracyHenry on 2018/1/17.
 */

public class InitSerialEvent {
    private UsbSerialDriver usbSerialDriver;

    public UsbSerialDriver getUsbSerialDriver() {
        return usbSerialDriver;
    }

    public void setUsbSerialDriver(UsbSerialDriver usbSerialDriver) {
        this.usbSerialDriver = usbSerialDriver;
    }
}
