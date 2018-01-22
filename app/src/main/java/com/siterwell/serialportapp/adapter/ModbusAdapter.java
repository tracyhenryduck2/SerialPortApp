package com.siterwell.serialportapp.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;

import java.util.List;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public class ModbusAdapter extends BaseAdapter {
    private List<ModbusAddressBean> lists;

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public ModbusAddressBean getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
