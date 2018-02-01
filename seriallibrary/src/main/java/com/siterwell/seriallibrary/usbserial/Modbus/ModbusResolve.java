package com.siterwell.seriallibrary.usbserial.Modbus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.siterwell.seriallibrary.usbserial.bean.ErrorReadCofig;
import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;
import com.siterwell.seriallibrary.usbserial.bean.TypeModbusAddress;
import com.siterwell.seriallibrary.usbserial.driver.UsbSerialDriver;
import com.siterwell.seriallibrary.usbserial.util.FileUtils;
import com.siterwell.seriallibrary.usbserial.util.FunPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TracyHenry on 2018/1/4.
 */

public class ModbusResolve {
    private final static String TAG = "ModbusResolve";
    public static byte[] receive_data;
    public final static int FUNCTION_READ_COIL=1; //功能码：读取线圈寄存器
    public final static int FUNCTION_READ_REGISTER = 3;//功能码:读取保存寄存器
    public final static int FUNCTION_WRITE_COIL=5;//功能码：写线圈寄存器
    public final static int FUNCTION_WRITE_REGISTER=6;//功能码:写保存寄存器

    public final static int MAX_READ_COIL = 2000; //线圈数量最大值

    public final static int MAX_READ_REGISTER = 125; //寄存器数量最大值

    public final static int DEVICE_ADDRESS = 3; //写死设备地址为3

    public UsbSerialDriver sDriver = null;

    private static ModbusResolve modbusResolve;

    public List<ModbusAddressBean> listcoil;
    public List<ModbusAddressBean> listregister;
    public ErrorReadCofig errorReadCofig;
    private SendModbusCommand sendModbusCommand;
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

    private ModbusResolve() {

    }


    public void init(Context context){
        listcoil = new ArrayList<ModbusAddressBean>();
        listregister =new ArrayList<ModbusAddressBean>();
        FunPath.init(context,context.getPackageName());
        load(context);
        Intent intent = new Intent(context, ModbusService.class);
        context.startService(intent);
    }


    private void load(Context context) {
        // 导入之前使用过的设备登录密码
        try {
            String path = FunPath.getMediaPath(context);
            String addres  = path+ File.separator + "config.txt";
            String text = FileUtils.readFromFile(addres);
            Log.i(TAG,"读取到的文件为："+text);
            JSONObject jsonObject = new JSONObject(text);

                JSONArray jsonObj_coil     = jsonObject.getJSONArray("coil");
                JSONArray jsonObj_register = jsonObject.getJSONArray("register");

                for(int i=0;i<jsonObj_coil.length();i++){
                    ModbusAddressBean bean = new ModbusAddressBean();

                    bean.setName(jsonObj_coil.getJSONObject(i).getString("name"));
                    bean.setAddress(i+1);
                    bean.setType(TypeModbusAddress.TYPE_MODBUS_COIL);
                    listcoil.add(bean);
                }


                for(int i=0;i<jsonObj_register.length();i++){
                    ModbusAddressBean bean = new ModbusAddressBean();

                    bean.setName(jsonObj_register.getJSONObject(i).getString("name"));
                    bean.setAddress(i+1);
                    bean.setType(TypeModbusAddress.TYPE_MODBUS_COIL);
                    listregister.add(bean);
                }

                Log.i(TAG,"dddL:"+listcoil.toString());
            errorReadCofig = ErrorReadCofig.Success;
        } catch (NullPointerException e) {
            e.printStackTrace();
            errorReadCofig = ErrorReadCofig.ERROR_READ_COFIG_NULL;
        }catch (JSONException e){
            e.printStackTrace();
            errorReadCofig = ErrorReadCofig.ERROR_READ_COFIG_FORMAT;
        }
    }

    public byte[] sendReadcoil(int address, int start, int length){

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


    /*
    @method sendCommandOfWriteRegister
    @autor TracyHenry
    @time 2018/1/8 下午2:09
    @email xuejunju_4595@qq.com
     地址，寄存器地址，值
    */
    public byte[] sendCommandOfWriteRegister(int address,int register,int value){

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

    public void saveList(Context context,List<ModbusAddressBean> listcoil,List<ModbusAddressBean> listreg){
        try {
        this.listcoil = listcoil;
        this.listregister = listreg;
        JSONArray jsonObject = new JSONArray();
        for(int i=0;i<this.listcoil.size();i++){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name",this.listcoil.get(i).getName());
                jsonObject.put(jsonObject1);
        }


            JSONArray jsonObject2 = new JSONArray();
            for(int i=0;i<this.listregister.size();i++){
                JSONObject jsonObject3 = new JSONObject();
                jsonObject3.put("name",this.listregister.get(i).getName());
                jsonObject2.put(jsonObject3);
            }

            JSONObject jsonObject_al = new JSONObject();
            jsonObject_al.put("coil",jsonObject);
            jsonObject_al.put("register",jsonObject2);
            String path = FunPath.getMediaPath(context);
            String addres  = path+ File.separator + "config.txt";
            FileUtils.saveToFile(addres,jsonObject_al.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析收到的读线圈信息
     */
    public ModbusErrcode checkReceiveReadCoil(byte[] data){

        byte[] data1 = {0x03,0x01,0x02,0x03,0x01};

        try{
            if(data1.length == 3){
                if(data1[0]!=DEVICE_ADDRESS) return ModbusErrcode.ERROR_UNKNOW;
                else if(data1[1]!=FUNCTION_READ_COIL) return ModbusErrcode.ERROR_UNKNOW;
                else{
                    int code = data1[2];
                    return ModbusErrcode.getErrorStr(code);
                }
            }else{
                if(data1[0]!=DEVICE_ADDRESS) return ModbusErrcode.ERROR_UNKNOW;
                else if(data1[1]!=FUNCTION_READ_COIL) return ModbusErrcode.ERROR_UNKNOW;
                else {
                    int leng1 = data1[2];
                    if(data1.length!=leng1+3){
                        return ModbusErrcode.ERROR_UNKNOW;
                    }else {

                        if((listcoil.size()%8==0&&(leng1 == (listcoil.size()/8)))
                                || listcoil.size()%8!=0&&(leng1 == (listcoil.size()/8+1))){

                            for(int i=0;i<listcoil.size();i++){
                                int d = i/8;
                                byte x = (byte)((0x01 << (i%8)) & data1[3+d]);
                                if(x!=0){
                                    ModbusResolve.getInstance().listcoil.get(i).setData(1);
                                }else{
                                    ModbusResolve.getInstance().listcoil.get(i).setData(0);
                                }
                            }
                        }else {
                            return ModbusErrcode.ERROR_UNKNOW;
                        }




                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }



        return ModbusErrcode.ERROR_NORMAL;
    }


    /**
     * 解析收到的读线圈信息
     */
    public ModbusErrcode checkReceiveReadRegister(byte[] data){


        try{
            if(data.length == 3){
                if(data[0]!=DEVICE_ADDRESS) return ModbusErrcode.ERROR_UNKNOW;
                else if(data[1]!=FUNCTION_READ_COIL) return ModbusErrcode.ERROR_UNKNOW;
                else{
                    int code = data[2];
                    return ModbusErrcode.getErrorStr(code);
                }
            }else{
                if(data[0]!=DEVICE_ADDRESS) return ModbusErrcode.ERROR_UNKNOW;
                else if(data[1]!=FUNCTION_READ_COIL) return ModbusErrcode.ERROR_UNKNOW;
                else {

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }



        return ModbusErrcode.ERROR_NORMAL;
    }

    public SendModbusCommand getSendModbusCommand() {
        return sendModbusCommand;
    }

    public void addSendModbusCommandListenr(SendModbusCommand sendModbusCommand) {
        this.sendModbusCommand = sendModbusCommand;
    }

    public void removeSendModbusCommandListenr(){
         this.sendModbusCommand = null;
    }
}
