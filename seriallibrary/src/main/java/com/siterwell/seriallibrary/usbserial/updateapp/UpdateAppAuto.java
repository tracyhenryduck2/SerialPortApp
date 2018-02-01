package com.siterwell.seriallibrary.usbserial.updateapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.siterwell.seriallibrary.R;
import com.siterwell.seriallibrary.usbserial.dialog.ECAlertDialog;
import com.siterwell.seriallibrary.usbserial.view.SettingItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


/**
 * ClassName:UpdateAppAuto
 * 作者：Henry on 2017/3/30 13:52
 * 邮箱：xuejunju_4595@qq.com
 * 描述:自动更新，HTT访问服务器地址文件中的版本号大小，与APP的版本号进行比较，若大于本地版本则弹出对话框
 */
public class UpdateAppAuto {
    private final String TAG = "UpdateAppAuto";
    private Context context;
    private Handler handlerUpdate;
    private final static int DOWN_UPDATE = 11;
    private int progress = 0;
    private static boolean flag_checkupdate = false;
    private int count=0;
    private SettingItem updateSetitem;
    private boolean isFlag_checkupdate;
    public UpdateAppAuto(Context context) {
        Log.i(TAG,"UpdateAppAuto create");
        this.context = context;
        handlerUpdate = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        getUpdateInfo();
                        break;
                    case 3:
                        confirm((Config.UpdateInfo) msg.obj);
                        break;
                }
            }
        };
    }


    public UpdateAppAuto(Context context, SettingItem updateSetitem2,boolean flag_checkupdate) {
        Log.i(TAG,"UpdateAppAuto create");
        this.context = context;
        this.updateSetitem = updateSetitem2;
        this.isFlag_checkupdate = flag_checkupdate;
        handlerUpdate = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        getUpdateInfo();
                        break;
                    case 3:
                        updateSetitem.setNewUpdateVisibility(true);
                        updateSetitem.setTag((Config.UpdateInfo) msg.obj);
                        break;
                }
            }
        };

        if(isFlag_checkupdate){
            updateSetitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                            if(updateSetitem.getNewUpdateVisibility()==View.VISIBLE && !UpdateService.flag_updating){


                                Config.UpdateInfo info = (Config.UpdateInfo)v.getTag();
                                confirm(info);
                            }


                }
            });
        }

    }


    private void confirm(Config.UpdateInfo info) {
        String appname =  context.getPackageName();
        Log.i(TAG,"appname:"+appname);

        int verCode = Config.getVerCode(context, context.getPackageName());
        String verName = Config.getVerName(context, context.getPackageName());

        String ds = String.format(context.getResources().getString(R.string.update_alert),verName,verCode,info.name,info.code);

        try {
            ECAlertDialog dialog = ECAlertDialog.buildAlert(context,ds,context.getResources().getString(R.string.dialog_btn_cancel), context.getResources().getString(R.string.dialog_btn_confim) , new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context,context.getResources().getString(R.string.background_update),Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    Intent intent = new Intent(context,UpdateService.class);
                    intent.putExtra("Key_App_Name",Config.UPDATE_APKNAME);
                    intent.putExtra("Key_Down_Url",Config.ApkUrl);
                    context.startService(intent);
                }
            });
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    private void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), Config.UPDATE_APKNAME)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public void getUpdateInfo(){
        String appname =  context.getPackageName();
        Log.i(TAG,"appname:"+appname);
        Config.getUpdateInfo(context, new UpdateListenr() {
            @Override
            public void onSuccessUpdate(String str) {
                try {
                    JSONObject object = new JSONObject(str);
                    int code = object.getInt("code");
                    String name = object.getString("name");
                    Config.UpdateInfo ds = new Config.UpdateInfo();
                    ds.setCode(code);
                    ds.setName(name);
                    if (Config.getVerCode(context, context.getPackageName()) < code) {
                        handlerUpdate.sendMessage(handlerUpdate.obtainMessage(3, ds));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int errorCode) {
                com.litesuits.android.log.Log.i(TAG,"更新消息获取失败");
            }
        });

    }

    public void initCheckUpate(){

        if(!flag_checkupdate) {
            flag_checkupdate = true;
            new Thread() {
                public void run() {

                    while(count<2)
                    {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        count++;
                    }
                    count = 0;
                    handlerUpdate.sendMessage(handlerUpdate.obtainMessage(1));

                }
            }.start();
        }

    }

}
