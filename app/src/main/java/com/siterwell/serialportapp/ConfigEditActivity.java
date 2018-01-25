package com.siterwell.serialportapp;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.siterwell.seriallibrary.usbserial.Modbus.ModbusResolve;
import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;
import com.siterwell.seriallibrary.usbserial.bean.TypeModbusAddress;
import com.siterwell.seriallibrary.usbserial.dialog.ECAlertDialog;
import com.siterwell.seriallibrary.usbserial.util.TopbarSuperActivity;
import com.siterwell.seriallibrary.usbserial.util.UtilTools;
import com.siterwell.seriallibrary.usbserial.view.SpinnerSelectView;
import com.siterwell.serialportapp.adapter.ModbusAdapter;

import java.util.List;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public class ConfigEditActivity extends TopbarSuperActivity {
    private static final String TAG = ConfigEditActivity.class.getName();
    private EditText editText_name;
    private SpinnerSelectView spinnerSelectView;
    private ModbusAddressBean modbusAddressBean;
    private ECAlertDialog ecAlertDialog;
    @Override
    protected void onCreateInit() {
        initdata();
        getTopBarView().setTopBarStatus(R.drawable.back, getResources().getString(R.string.ok), modbusAddressBean == null ? getResources().getString(R.string.add_config) : getResources().getString(R.string.edit_config), 1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        }, R.color.blue);
        editText_name = (EditText)findViewById(R.id.tet);
        spinnerSelectView = (SpinnerSelectView)findViewById(R.id.spinner_type);
      if(modbusAddressBean!=null){
            editText_name.setText(modbusAddressBean.getName());
            editText_name.setSelection(modbusAddressBean.getName().length());
            spinnerSelectView.setmChoiceItemPosition(modbusAddressBean.getType().getCode());
            spinnerSelectView.setEnabled(false);

        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_modbus;
    }


    private void initdata(){
        modbusAddressBean = (ModbusAddressBean)getIntent().getSerializableExtra("modebusbean");
    }


    private void submit(){


        if(spinnerSelectView.getChoiceItemPosition()==-1){
            ecAlertDialog = ECAlertDialog.buildPositiveAlert(this,R.string.please_choose_type,null);
            ecAlertDialog.show();
            return;
        }

        if(TextUtils.isEmpty(editText_name.getText().toString().trim())){
            ecAlertDialog = ECAlertDialog.buildPositiveAlert(this,R.string.name_is_null,null);
            ecAlertDialog.show();
            return;
        }

        ModbusAddressBean modbusAddressBean2 = new ModbusAddressBean();
        modbusAddressBean2.setType(spinnerSelectView.getChoiceItemPosition()==0?TypeModbusAddress.TYPE_MODBUS_COIL:TypeModbusAddress.TYPE_MODBUS_REGISTER);
        modbusAddressBean2.setName(editText_name.getText().toString().trim());
        if(modbusAddressBean!=null){
            modbusAddressBean2.setAddress(modbusAddressBean.getAddress());
        }
        Intent intent = new Intent();
        intent.putExtra("modbus",modbusAddressBean2);
        setResult(RESULT_OK, intent);
        finish();
    }
}
