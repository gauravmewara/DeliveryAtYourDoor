package com.collectorate.deliveryatyourdoor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.R;

import java.util.ArrayList;

public class DeliveryWardZoneAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> list;
    LayoutInflater inflator;

    public DeliveryWardZoneAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
        inflator = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflator.inflate(R.layout.delivery_warzone_spinner_item, null);
        TextView names = (TextView) view.findViewById(R.id.spinner_singleItem);
        /*TextView phone = (TextView) view.findViewById(R.id.spinner_phone_code);*/
       /* phone.setText(list.get(i).getCountryphonename());*/
        names.setText(list.get(i));
        return view;
    }




}
