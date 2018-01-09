package com.siterwell.seriallibrary.usbserial.Modbus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.siterwell.seriallibrary.usbserial.driver.UsbSerialDriver;
import com.siterwell.seriallibrary.usbserial.event.PacketEndEvent;
import com.siterwell.seriallibrary.usbserial.event.SerialReceiveEvent;
import com.siterwell.seriallibrary.usbserial.event.SerialSendEvent;
import com.siterwell.seriallibrary.usbserial.util.HexDump;
import com.siterwell.seriallibrary.usbserial.util.SerialInputOutputManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by TracyHenry on 2018/1/9.
 */

public class ModusService extends Service implements SerialInputOutputManager.Listener{
    private static ArrayList<Byte> receive_data;
    private Timer timer;
    private MyTimerTask myTimerTask;
    private final String TAG = ModusService.class.getName();
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager mSerialIoManager;
    private int count;
    private boolean flag_timer;
    private final int MaX_INTERVAL = 500;  //最大时间间隔为500ms 表示500ms没收到数据则认为缓冲数据为一个完整的包

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        receive_data = new ArrayList<Byte>();
//        timer = new Timer();
//        myTimerTask = new MyTimerTask();
//        timer.schedule(myTimerTask,0l);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().register(this);
        Log.d(TAG, "sDriver=" + ModbusResolve.getInstance().sDriver);
        if (ModbusResolve.getInstance().sDriver == null) {
            Log.i(TAG,"No serial device.");
        } else {
            try {
                ModbusResolve.getInstance().sDriver.open();
                ModbusResolve.getInstance().sDriver.setParameters(9600, 8, UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
                Toast.makeText(this,"sDriver.open();",Toast.LENGTH_LONG).show();
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
        }
        stopIoManager();
        startIoManager();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        receive_data= null;
        EventBus.getDefault().unregister(this);
        stopIoManager();
    }

    @Override
    public void onNewData(byte[] data) {
        Log.i(TAG,"收到的数据为:"+ HexDump.dumpHexString(data));
        count = 0;
//        for(int i=0;i<data.length;i++){
//            Byte ds2 = new Byte(String.valueOf(data[i]));
//            receive_data.add(ds2);
//        }
        SerialReceiveEvent serialReceiveEvent = new SerialReceiveEvent();
        serialReceiveEvent.setReceive_data(data);
         EventBus.getDefault().post(serialReceiveEvent);

        //Toast.makeText(this,HexDump.dumpHexString(data),Toast.LENGTH_LONG).show();
        flag_timer = true;
    }

    @Override
    public void onRunError(Exception e) {

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
         Toast.makeText(this,"收到数据包："+HexDump.dumpHexString(ds2),Toast.LENGTH_LONG).show();

        receive_data.clear();
    }


    private class  MyTimerTask extends TimerTask{

        @Override
        public void run() {
           if(flag_timer){
               count ++;
               if(count>=MaX_INTERVAL){
                   count = 0;
                   EventBus.getDefault().post(new PacketEndEvent());
               }

           }
        }
    }
}
