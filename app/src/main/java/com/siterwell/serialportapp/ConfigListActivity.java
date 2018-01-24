package com.siterwell.serialportapp;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.siterwell.seriallibrary.usbserial.Modbus.ModbusResolve;
import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;
import com.siterwell.seriallibrary.usbserial.bean.TypeModbusAddress;
import com.siterwell.seriallibrary.usbserial.util.TopbarSuperActivity;
import com.siterwell.seriallibrary.usbserial.util.UtilTools;
import com.siterwell.serialportapp.adapter.ModbusAdapter;

import java.util.List;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public class ConfigListActivity extends TopbarSuperActivity {
    private static final String TAG = ConfigListActivity.class.getName();
    private List<ModbusAddressBean> list;
    private List<ModbusAddressBean> list_register;
    private ModbusAdapter modbusAdapter;
    private ModbusAdapter modbusAdapter_reg;
    private ListView listView_coil;
    private ListView listView_register;
    private final int EDIT_CONFIG = 1;

    @Override
    protected void onCreateInit() {
        getTopBarView().setTopBarStatus(R.drawable.back, getResources().getString(R.string.add), getResources().getString(R.string.config_list), 1, new View.OnClickListener() {
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

        Intent intent = new Intent(this,ConfigEditActivity.class);
        startActivityForResult(intent,EDIT_CONFIG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;

        if(requestCode==EDIT_CONFIG){
         ModbusAddressBean modbusAddressBean = (ModbusAddressBean)data.getSerializableExtra("modbus");

          if(modbusAddressBean!=null){
              if(modbusAddressBean.getType()== TypeModbusAddress.TYPE_MODBUS_COIL){
                  list.add(modbusAddressBean);
                  modbusAdapter.refreshLists(list);
                  UtilTools.setListViewHeightBasedOnChildren(listView_coil);
              }else if(modbusAddressBean.getType()==TypeModbusAddress.TYPE_MODBUS_REGISTER){
                  list_register.add(modbusAddressBean);
                  modbusAdapter_reg.refreshLists(list_register);
                  UtilTools.setListViewHeightBasedOnChildren(listView_register);
              }

          }
        }
    }
}
