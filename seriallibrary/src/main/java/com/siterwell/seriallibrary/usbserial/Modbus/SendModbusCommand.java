package com.siterwell.seriallibrary.usbserial.Modbus;

/**
 * Created by TracyHenry on 2018/1/8.
 */

public interface SendModbusCommand {


        void ErrorReadCoil(ModbusErrcode modbusErrcode);


        void ErrorReadRegister(ModbusErrcode modbusErrcode);
}
