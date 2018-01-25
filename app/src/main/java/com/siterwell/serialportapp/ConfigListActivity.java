package com.siterwell.serialportapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.siterwell.seriallibrary.usbserial.Modbus.ModbusResolve;
import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;
import com.siterwell.seriallibrary.usbserial.bean.TypeModbusAddress;
import com.siterwell.seriallibrary.usbserial.dialog.ECAlertDialog;
import com.siterwell.seriallibrary.usbserial.util.TopbarSuperActivity;
import com.siterwell.seriallibrary.usbserial.util.UtilTools;
import com.siterwell.serialportapp.adapter.ModbusAdapter;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public class ConfigListActivity extends TopbarSuperActivity implements View.OnClickListener{
    private static final String TAG = ConfigListActivity.class.getName();
    private List<ModbusAddressBean> list;
    private List<ModbusAddressBean> list_register;
    private ModbusAdapter modbusAdapter;
    private ModbusAdapter modbusAdapter_reg;
    private ListView listView_coil;
    private ListView listView_register;
    private Button button_add;
    private final int EDIT_CONFIG = 1;

    @Override
    protected void onCreateInit() {
        getTopBarView().setTopBarStatus(R.drawable.back, getResources().getString(R.string.save), getResources().getString(R.string.config_list), 1, new View.OnClickListener() {
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
        button_add = (Button)findViewById(R.id.add);
        button_add.setOnClickListener(this);
        list = ModbusResolve.getInstance().listcoil;
        list_register = ModbusResolve.getInstance().listregister;
        modbusAdapter = new ModbusAdapter(list,this);
        modbusAdapter_reg = new ModbusAdapter(list_register,this);
        listView_coil.setAdapter(modbusAdapter);
        listView_register.setAdapter(modbusAdapter_reg);
        listView_coil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ConfigListActivity.this,ConfigEditActivity.class);
                intent.putExtra("modebusbean",list.get(i));
                startActivityForResult(intent,EDIT_CONFIG);
            }
        });
        listView_register.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ConfigListActivity.this,ConfigEditActivity.class);
                intent.putExtra("modebusbean",list_register.get(i));
                startActivityForResult(intent,EDIT_CONFIG);
            }
        });
        listView_coil.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDeleteAlert(list.get(i));
                return true;
            }
        });

        listView_register.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDeleteAlert(list_register.get(i));
                return true;
            }
        });
        UtilTools.setListViewHeightBasedOnChildren(listView_coil);
        UtilTools.setListViewHeightBasedOnChildren(listView_register);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_config;
    }


    private void showAlert(){

        ECAlertDialog ecAlertDialog = ECAlertDialog.buildAlert(this, R.string.save_or_not, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               ModbusResolve.getInstance().saveList(ConfigListActivity.this,list,list_register);
               finish();
            }
        });
        ecAlertDialog.show();

    }

    private void showDeleteAlert(final ModbusAddressBean modbusAddressBean){
        String d = String.format(getResources().getString(R.string.delete_or_not),modbusAddressBean.getName());
        ECAlertDialog ecAlertDialog = ECAlertDialog.buildAlert(this, d, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    if(modbusAddressBean.getType()==TypeModbusAddress.TYPE_MODBUS_COIL){
                        list.remove(modbusAddressBean.getAddress()-1);
                        for(ModbusAddressBean modbusAddressBean1:list){
                            int ds = modbusAddressBean1.getAddress();
                            modbusAddressBean1.setAddress(ds-1);
                        }
                        modbusAdapter.refreshLists(list);
                        UtilTools.setListViewHeightBasedOnChildren(listView_coil);
                    }else if(modbusAddressBean.getType()==TypeModbusAddress.TYPE_MODBUS_REGISTER){
                        list_register.remove(modbusAddressBean.getAddress()-1);
                        for(ModbusAddressBean modbusAddressBean1:list_register){
                            int ds = modbusAddressBean1.getAddress();
                            modbusAddressBean1.setAddress(ds-1);
                        }
                        modbusAdapter_reg.refreshLists(list_register);
                        UtilTools.setListViewHeightBasedOnChildren(listView_register);
                    }
            }
        });
        ecAlertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) return;

        if(requestCode==EDIT_CONFIG){
         ModbusAddressBean modbusAddressBean = (ModbusAddressBean)data.getSerializableExtra("modbus");

          if(modbusAddressBean!=null){


              if(modbusAddressBean.getType()== TypeModbusAddress.TYPE_MODBUS_COIL){
                  if(modbusAddressBean.getAddress()>0){
                      list.set(modbusAddressBean.getAddress()-1,modbusAddressBean);
                  }else{
                      modbusAddressBean.setAddress(list.size()+1);
                      list.add(modbusAddressBean);
                  }
                  modbusAdapter.refreshLists(list);
                  UtilTools.setListViewHeightBasedOnChildren(listView_coil);
              }else if(modbusAddressBean.getType()==TypeModbusAddress.TYPE_MODBUS_REGISTER){
                  if(modbusAddressBean.getAddress()>0){
                      list_register.set(modbusAddressBean.getAddress()-1,modbusAddressBean);
                  }else{
                      modbusAddressBean.setAddress(list_register.size()+1);
                      list_register.add(modbusAddressBean);
                  }
                  modbusAdapter_reg.refreshLists(list_register);
                  UtilTools.setListViewHeightBasedOnChildren(listView_register);
              }

          }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                Intent intent = new Intent(this,ConfigEditActivity.class);
                startActivityForResult(intent,EDIT_CONFIG);
                break;
        }
    }
}
