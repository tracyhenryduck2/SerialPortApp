package com.siterwell.serialportapp;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.siterwell.seriallibrary.usbserial.Modbus.ModbusErrcode;
import com.siterwell.seriallibrary.usbserial.Modbus.ModbusResolve;
import com.siterwell.seriallibrary.usbserial.Modbus.SendModbusCommand;
import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;
import com.siterwell.seriallibrary.usbserial.util.TopbarSuperActivity;
import com.siterwell.seriallibrary.usbserial.util.UtilTools;
import com.siterwell.serialportapp.adapter.ModbusAdapter;
import com.siterwell.serialportapp.adapter.ModbusDataAdapter;

import java.util.List;

/**
 * Created by TracyHenry on 2018/1/31.
 */

public class DataListConsoleActivity extends TopbarSuperActivity implements SendModbusCommand{
    private final String TAG = DataListConsoleActivity.class.getSimpleName();
    private ModbusDataAdapter modbusAdapter;
    private ModbusDataAdapter modbusAdapter_reg;
    private ListView listView_coil;
    private ListView listView_register;
    @Override
    protected void onCreateInit() {
        getTopBarView().setTopBarStatus(R.drawable.back, -1, getResources().getString(R.string.data_list), 1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }, null,R.color.blue);
        listView_coil = (ListView)findViewById(R.id.coillist);

        listView_register = (ListView)findViewById(R.id.registerlist);
        modbusAdapter = new ModbusDataAdapter(ModbusResolve.getInstance().listcoil,this);
        modbusAdapter_reg = new ModbusDataAdapter(ModbusResolve.getInstance().listregister,this);
        listView_coil.setAdapter(modbusAdapter);
        listView_register.setAdapter(modbusAdapter_reg);
        UtilTools.setListViewHeightBasedOnChildren(listView_coil);
        UtilTools.setListViewHeightBasedOnChildren(listView_register);
        ModbusResolve.getInstance().addSendModbusCommandListenr(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_list;
    }


    @Override
    public void ErrorReadCoil(ModbusErrcode modbusErrcode) {
        Toast.makeText(this,"解析线圈的结果为"+getResources().getString(modbusErrcode.getDesc()),Toast.LENGTH_LONG).show();
        modbusAdapter.refreshLists(ModbusResolve.getInstance().listcoil);
    }


    @Override
    public void ErrorReadRegister(ModbusErrcode modbusErrcode) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ModbusResolve.getInstance().removeSendModbusCommandListenr();
    }
}
