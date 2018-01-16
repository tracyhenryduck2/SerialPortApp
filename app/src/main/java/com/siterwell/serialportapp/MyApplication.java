package com.siterwell.serialportapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.siterwell.seriallibrary.usbserial.Modbus.ModbusResolve;
import com.siterwell.seriallibrary.usbserial.util.CrashHandler;


/**
 * Created by hekr_jds on 6/30 0030.
 **/
public class MyApplication extends Application {

    private static MyApplication mApp;
    public static Activity sActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        ModbusResolve.getInstance().init(this);
        mApp = this;
        this.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                sActivity=activity;

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        CrashHandler.getInstance().init(getApplicationContext());
    }



    public static Context getAppContext() {
        return mApp;
    }

    public static Resources getAppResources() {
        return mApp.getResources();
    }

    public static Activity getActivity(){
        return sActivity;
    }



}
