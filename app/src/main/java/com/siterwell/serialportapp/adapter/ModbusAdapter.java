package com.siterwell.serialportapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.siterwell.seriallibrary.usbserial.bean.ModbusAddressBean;
import com.siterwell.serialportapp.R;

import java.util.List;

/**
 * Created by TracyHenry on 2018/1/22.
 */

public class ModbusAdapter extends BaseAdapter {
    private List<ModbusAddressBean> lists;
    private Context context;
    private ViewHolder holder;
    public ModbusAdapter(List<ModbusAddressBean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

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
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {


        ModbusAddressBean ac =lists.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_config_coil,null);
            holder.textView_name = (TextView) convertView.findViewById(R.id.title);
            holder.textView_address = (TextView) convertView.findViewById(R.id.summary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView_address.setText(String.valueOf(ac.getAddress()));
        holder.textView_name.setText(ac.getName());
        return convertView;
    }

    class ViewHolder{
        TextView textView_name;
        TextView textView_address;
    }


    public void refreshLists(List<ModbusAddressBean> mlists){
        this.lists = mlists;
        notifyDataSetChanged();
    }
}
