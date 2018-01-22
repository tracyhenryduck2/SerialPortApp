package com.siterwell.seriallibrary.usbserial.Modbus;

/**
 * Created by TracyHenry on 2018/1/8.
 */

public interface SendModbusCommand {


        void ErrorSendCoil(ModbusErrcode modbusErrcode);

        void ErrorReadCoil(ModbusErrcode modbusErrcode);

        void ErrorSendRegister(ModbusErrcode modbusErrcode);

        void ErrorReadRegister(ModbusErrcode modbusErrcode);
}
