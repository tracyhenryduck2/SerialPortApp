package com.siterwell.seriallibrary.usbserial.Modbus;

/**
 * Created by TracyHenry on 2018/1/4.
 */

public class ModbusResolve {

    public final int FUNCTION_READ_COIL=1; //功能码：读取线圈寄存器
    public final int FUNCTION_READ_REGISTER = 3;//功能码:读取保存寄存器
    public final int FUNCTION_WRITE_COIL=5;//功能码：写线圈寄存器
    public final int FUNCTION_WRITE_REGISTER=6;//功能码:写保存寄存器


    public ModbusResolve() {

    }

    public byte[] SendReadcoil(int address,int start,int length){

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
    public byte[] sendCommandOfReadRegister(int address,int start,int length){
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
    public byte[]  sendCommandOfWriteCoil(int address,int start,boolean flag){
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
}
