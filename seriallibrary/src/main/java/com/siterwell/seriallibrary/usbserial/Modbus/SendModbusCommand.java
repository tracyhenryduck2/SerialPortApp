package com.siterwell.seriallibrary.usbserial.Modbus;

/**
 * Created by TracyHenry on 2018/1/8.
 */

public interface SendModbusCommand {



        void sendCoil(int deivce_address, int address,boolean flag);

        void sendRegister(int deivce_address,int address);

        void SuccessSendCoil(byte[] reivce_data);

        void ErrorSendCoil(byte[] reivce_data);
}
