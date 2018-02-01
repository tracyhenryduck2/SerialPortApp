package com.siterwell.seriallibrary.usbserial.updateapp;

/**
 * Created by TracyHenry on 2018/2/1.
 */

public interface UpdateListenr {

    void onSuccessUpdate(String data);
    void onError(int code);

}
