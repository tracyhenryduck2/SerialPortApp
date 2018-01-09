package com.siterwell.seriallibrary.usbserial.Modbus;

import com.siterwell.seriallibrary.usbserial.driver.UsbSerialDriver;

/**
 * Created by TracyHenry on 2018/1/4.
 */

public class ModbusResolve {

    public static byte[] receive_data;
    public final static int FUNCTION_READ_COIL=1; //功能码：读取线圈寄存器
    public final static int FUNCTION_READ_REGISTER = 3;//功能码:读取保存寄存器
    public final static int FUNCTION_WRITE_COIL=5;//功能码：写线圈寄存器
    public final static int FUNCTION_WRITE_REGISTER=6;//功能码:写保存寄存器

    public final static int MAX_READ_COIL = 2000; //线圈数量最大值

    public final static int MAX_READ_REGISTER = 125; //寄存器数量最大值

    public UsbSerialDriver sDriver = null;

    private static ModbusResolve modbusResolve;

    public static ModbusResolve getInstance() {
        if (modbusResolve == null) {
            synchronized (ModbusResolve.class) {
                if (modbusResolve == null) {
                    modbusResolve = new ModbusResolve();
                }
            }
        }
        return modbusResolve;
    }

    public static byte[] SendReadcoil(int address,int start,int length){

        byte[] send = new byte[8];
        send[0]=Algorithm.toByteArray(address, 1)[0];
        send[1]=FUNCTION_READ_COIL;
        send[2]=Algorithm.toByteArray(start, 2)[1];
        send[3]=Algorithm.toByteArray(start, 2)[0];
        send[4]=Algorithm.toByteArray(length, 2)[1];
        send[5]=Algorithm.toByteArray(length, 2)[0];

        byte[] crc=Algorithm.get_crc16(send,6);
        send[6]=crc[0];
        send[7]=crc[1];
        return send;
    }


    /*
    @method sendCommandOfReadRegister
    @autor TracyHenry
    @time 2018/1/4 下午2:05
    @email xuejunju_4595@qq.com
    读取寄存器命令
    */
    public static byte[] sendCommandOfReadRegister(int address,int start,int length){
        byte[] send = new byte[8];
        send[0]=Algorithm.toByteArray(address, 1)[0];
        send[1]=FUNCTION_READ_REGISTER;
        send[2]=Algorithm.toByteArray(start, 2)[1];
        send[3]=Algorithm.toByteArray(start, 2)[0];
        send[4]=Algorithm.toByteArray(length, 2)[1];
        send[5]=Algorithm.toByteArray(length, 2)[0];

        byte[] crc=Algorithm.get_crc16(send,6);
        send[6]=crc[0];
        send[7]=crc[1];
        return send;
    }

    /**
     * 写单个线圈
     * @param address
     * @param start
     * @param flag
     * @return
     */
    public static byte[]  sendCommandOfWriteCoil(int address,int start,boolean flag){
        byte[] send = new byte[8];
        send[0]=Algorithm.toByteArray(address, 1)[0];
        send[1]=FUNCTION_WRITE_COIL;
        send[2]=Algorithm.toByteArray(start, 2)[1];
        send[3]=Algorithm.toByteArray(start, 2)[0];

        if(flag==true)
        {
            send[4]=-1;
            send[5]=0;
        }
        else
        {
            send[4]=0;
            send[5]=0;
        }
        byte[] crc=Algorithm.get_crc16(send,6);
        send[6]=crc[0];
        send[7]=crc[1];
        return send;
    }


    /*
    @method sendCommandOfWriteRegister
    @autor TracyHenry
    @time 2018/1/8 下午2:09
    @email xuejunju_4595@qq.com
     地址，寄存器地址，值
    */
    public static byte[] sendCommandOfWriteRegister(int address,int register,int value){

        byte[] send = new byte[8];
        send[0]=Algorithm.toByteArray(address, 1)[0];
        send[1]=FUNCTION_WRITE_REGISTER;
        send[2]=Algorithm.toByteArray(register, 2)[1];
        send[3]=Algorithm.toByteArray(register, 2)[0];
        send[4]=Algorithm.toByteArray(value, 2)[1];
        send[5]=Algorithm.toByteArray(value, 2)[0];

        byte[] crc=Algorithm.get_crc16(send,6);
        send[6]=crc[0];
        send[7]=crc[1];
        return send;
    }
}
