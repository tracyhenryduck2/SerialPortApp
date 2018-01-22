package com.siterwell.serialportapp;

import android.view.View;

import com.siterwell.seriallibrary.usbserial.util.TopbarSuperActivity;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public class ConfigEditActivity extends TopbarSuperActivity {
    @Override
    protected void onCreateInit() {
        getTopBarView().setTopBarStatus(R.drawable.back, getResources().getString(R.string.dialog_btn_confim), "编辑配置文件", 1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        },R.color.white);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_config;
    }
}
