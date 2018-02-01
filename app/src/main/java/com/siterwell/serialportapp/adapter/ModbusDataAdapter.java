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

public class ModbusDataAdapter extends BaseAdapter {
    private List<ModbusAddressBean> lists;
    private Context context;
    private ViewHolder holder;
    public ModbusDataAdapter(List<ModbusAddressBean> lists, Context context) {
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
            holder.textView_address = (TextView)convertView.findViewById(R.id.sub_title);
            holder.textView_data = (TextView) convertView.findViewById(R.id.summary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textView_address.setText(String.valueOf(ac.getAddress()));
        holder.textView_address.setVisibility(View.VISIBLE);
        holder.textView_name.setText(ac.getName());
       switch (ac.getData()){
           case -1:
               holder.textView_data.setVisibility(View.GONE);
               break;
           case 0:
               holder.textView_data.setVisibility(View.VISIBLE);
               holder.textView_data.setText(context.getResources().getString(R.string.disabled));
               break;
           case 1:
               holder.textView_data.setVisibility(View.VISIBLE);
               holder.textView_data.setText(context.getResources().getString(R.string.enabled));
               break;
               default:
                   holder.textView_data.setVisibility(View.GONE);
                   break;
       }
        return convertView;
    }

    class ViewHolder{
        TextView textView_name;
        TextView textView_address;
        TextView textView_data;
    }


    public void refreshLists(List<ModbusAddressBean> mlists){
        this.lists = mlists;
        notifyDataSetChanged();
    }
}
