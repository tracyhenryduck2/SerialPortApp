package com.siterwell.seriallibrary.usbserial.Modbus;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.siterwell.seriallibrary.usbserial.driver.UsbSerialDriver;
import com.siterwell.seriallibrary.usbserial.event.InitSerialEvent;
import com.siterwell.seriallibrary.usbserial.event.PacketEndEvent;
import com.siterwell.seriallibrary.usbserial.event.SerialReceiveEvent;
import com.siterwell.seriallibrary.usbserial.event.SerialSendEvent;
import com.siterwell.seriallibrary.usbserial.util.HexDump;
import com.siterwell.seriallibrary.usbserial.util.SerialInputOutputManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by TracyHenry on 2018/1/9.
 */

public class ModbusService extends Service implements SerialInputOutputManager.Listener{
    private static ArrayList<Byte> receive_data;
    private Timer timer;
    private MyTimerTask myTimerTask;
    private Timer timer_read;
    private ModbusReadTimerTask modbusReadTimerTask;
    private final String TAG = ModbusService.class.getName();
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    private int count;    //用来做接收数据的分包标识
    private int count_data; //用来做分包发送线圈和寄存器命令；
    private AtomicBoolean flag_timer;
    private final int MaX_INTERVAL = 100;  //最大时间间隔为500ms 表示500ms没收到数据则认为缓冲数据为一个完整的包
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        flag_timer = new AtomicBoolean(false);
        receive_data = new ArrayList<Byte>();
        timer = new Timer();
        timer_read=new Timer();
        myTimerTask = new MyTimerTask();
        modbusReadTimerTask = new ModbusReadTimerTask();
        timer.schedule(myTimerTask,0l,1l);
        timer_read.schedule(modbusReadTimerTask,20000l,20000l);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().register(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        receive_data= null;
        EventBus.getDefault().unregister(this);
        stopIoManager();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        if(myTimerTask!=null){
            myTimerTask = null;
        }
        if(handler!=null){
            handler.removeCallbacks(null);
        }
    }

    @Override
    public void onNewData(byte[] data) {

        synchronized (data){
            Log.i(TAG,"收到的数据为:"+ HexDump.dumpHexString(data));

            for(int i=0;i<data.length;i++){
                Byte ds2 = new Byte(String.valueOf(data[i]));
                receive_data.add(ds2);
            }
            SerialReceiveEvent serialReceiveEvent = new SerialReceiveEvent();
            serialReceiveEvent.setReceive_data(data);
            EventBus.getDefault().post(serialReceiveEvent);

            flag_timer.set(true);
        }

    }

    @Override
    public void onRunError(Exception e) {

        e.printStackTrace();
    }


    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (ModbusResolve.getInstance().sDriver != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(ModbusResolve.getInstance().sDriver, this);
            mExecutor.submit(mSerialIoManager);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InitSerialEvent event) {

        if (event.getUsbSerialDriver() == null) {
            Log.i(TAG,"No serial device.");
        } else {
            try {
                ModbusResolve.getInstance().sDriver = event.getUsbSerialDriver();
                Log.d(TAG, "sDriver=" + ModbusResolve.getInstance().sDriver);
                ModbusResolve.getInstance().sDriver.open();
                ModbusResolve.getInstance().sDriver.setParameters(9600, 8, UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);

                try {
                    ModbusResolve.getInstance().sDriver.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                ModbusResolve.getInstance().sDriver = null;
            }
            Log.i(TAG,"Serial device: " + ModbusResolve.getInstance().sDriver.getClass().getSimpleName());
            stopIoManager();
            startIoManager();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SerialSendEvent event) {
          byte[] ds = event.getContent();
          if(mSerialIoManager!=null){
              mSerialIoManager.writeAsync(ds);
          }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PacketEndEvent event) {
        Byte[] ds = (Byte[])receive_data.toArray();

        byte ds2[] = new byte[ds.length];
        for(int i=0;i<ds.length;i++){
            ds2[i] = ds[i];
        }
         Toast.makeText(this,"收到数据包："+HexDump.toHexString(ds2),Toast.LENGTH_LONG).show();

        receive_data.clear();
        flag_timer.set(false);
    }


    private class  MyTimerTask extends TimerTask{

        @Override
        public void run() {
           if(flag_timer.get()){
               count ++;
               if(count>=MaX_INTERVAL){
                   count = 0;
                   handler.sendEmptyMessage(1);
               }

           }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    if(receive_data.size()>0){
                        byte ds2[] = new byte[receive_data.size()];
                        for(int i=0;i<receive_data.size();i++){
                            ds2[i] = receive_data.get(i).byteValue();
                        }
                        Toast.makeText(ModbusService.this,"收到数据包："+HexDump.toHexString(ds2),Toast.LENGTH_LONG).show();

                        receive_data.clear();
                        flag_timer.set(false);
                        if(count_data%2==1){
                            ModbusErrcode modbusErrcode = ModbusResolve.getInstance().checkReceiveReadCoil(ds2);
                            if(ModbusResolve.getInstance().getSendModbusCommand()!=null){
                                ModbusResolve.getInstance().getSendModbusCommand().ErrorReadCoil(modbusErrcode);
                            }
                        }else{

                        }

                    }

                    break;

                case 2:
                    if(ModbusResolve.getInstance().listcoil!=null && ModbusResolve.getInstance().listcoil.size()>0){
                        byte[] data_coil = ModbusResolve.getInstance().sendReadcoil(ModbusResolve.DEVICE_ADDRESS,ModbusResolve.getInstance().listcoil.get(0).getAddress(),ModbusResolve.getInstance().listcoil.size());
                        SerialSendEvent serialSendEvent = new SerialSendEvent();
                        serialSendEvent.setContent(data_coil);
                        EventBus.getDefault().post(serialSendEvent);
                    }

                    break;
                case 3:
                    if(ModbusResolve.getInstance().listregister!=null && ModbusResolve.getInstance().listregister.size()>0) {
                        byte[] data_register =   ModbusResolve.getInstance().sendCommandOfReadRegister(ModbusResolve.DEVICE_ADDRESS, ModbusResolve.getInstance().listregister.get(0).getAddress(), ModbusResolve.getInstance().listregister.size());
                        SerialSendEvent serialSendEvent = new SerialSendEvent();
                        serialSendEvent.setContent(data_register);
                        EventBus.getDefault().post(serialSendEvent);
                      }
                      break;
            }
        }
    };


    private class  ModbusReadTimerTask extends TimerTask{

        @Override
        public void run() {
                count_data ++;
                if(count_data%2==1){
                    handler.sendEmptyMessage(2);
                }else{
                    handler.sendEmptyMessage(3);
                }

        }
    }
}
