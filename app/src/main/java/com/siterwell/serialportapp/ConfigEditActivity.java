package com.siterwell.serialportapp;

import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;

import com.siterwell.seriallibrary.usbserial.Modbus.ModbusResolve;
import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;
import com.siterwell.seriallibrary.usbserial.dialog.ECAlertDialog;
import com.siterwell.seriallibrary.usbserial.util.TopbarSuperActivity;
import com.siterwell.seriallibrary.usbserial.util.UtilTools;
import com.siterwell.serialportapp.adapter.ModbusAdapter;

import java.util.List;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public class ConfigEditActivity extends TopbarSuperActivity {
    private static final String TAG = ConfigEditActivity.class.getName();
    private List<ModbusAddressBean> list;
    private List<ModbusAddressBean> list_register;
    private ModbusAdapter modbusAdapter;
    private ModbusAdapter modbusAdapter_reg;
    private ListView listView_coil;
    private ListView listView_register;
    private ECAlertDialog alertDialog;
    private ScrollView scrollView;

    @Override
    protected void onCreateInit() {
        getTopBarView().setTopBarStatus(R.drawable.back, getResources().getString(R.string.add), "编辑配置文件", 1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        },R.color.blue);
        scrollView = (ScrollView)findViewById(R.id.demoScroller);
        listView_coil = (ListView)findViewById(R.id.coillist);

        listView_register = (ListView)findViewById(R.id.registerlist);

        list = ModbusResolve.getInstance().listcoil;
        list_register = ModbusResolve.getInstance().listregister;
        Log.i(TAG,list.get(0).getName());
        modbusAdapter = new ModbusAdapter(list,this);
        modbusAdapter_reg = new ModbusAdapter(list_register,this);
        listView_coil.setAdapter(modbusAdapter);
        listView_register.setAdapter(modbusAdapter_reg);

        UtilTools.setListViewHeightBasedOnChildren(listView_coil);
        UtilTools.setListViewHeightBasedOnChildren(listView_register);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_config;
    }


    private void showAlert(){

        alertDialog = ECAlertDialog.buildAlert(ConfigEditActivity.this, getResources().getString(R.string.add),getResources().getString(R.string.cancel),getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });
        alertDialog.setContentView(R.layout.edit_modbus);
        alertDialog.setTitle(getResources().getString(R.string.add));
        EditText text = (EditText) alertDialog.getContent().findViewById(R.id.tet);


        alertDialog.show();


    }
}
