package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.R;

public class DeliveryFilterScreen extends AppCompatActivity {

    TextView tv_date,tv_fromDate,tv_to,tv_toDate,wardZoneSpinnerText,wardNoSpinnerText;
    ImageView imageView,iv_fromCalender;
    Spinner wardZoneSpinnerfilter,wardNoSpinnerfilter;
    LinearLayout lhwardZone_spinnerLayout,lhwardNo_spinnerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_filter_screen);
        initView();
    }
    public void initView(){
        tv_date =(TextView)findViewById(R.id.tv_date);
        tv_fromDate =(TextView)findViewById(R.id.tv_fromDate);
        tv_to =(TextView)findViewById(R.id.tv_to);
        tv_toDate =(TextView)findViewById(R.id.tv_toDate);
        wardZoneSpinnerText =(TextView)findViewById(R.id.wardZoneSpinnerText);
        wardNoSpinnerText =(TextView)findViewById(R.id.wardNoSpinnerText);
        imageView =(ImageView) findViewById(R.id.imageView);
        iv_fromCalender =(ImageView)findViewById(R.id.iv_fromCalender);
        wardZoneSpinnerfilter =(Spinner)findViewById(R.id.wardZoneSpinnerfilter);
        wardNoSpinnerfilter =(Spinner)findViewById(R.id.wardNoSpinnerfilter);
        lhwardZone_spinnerLayout =(LinearLayout)findViewById(R.id.lhwardZone_spinnerLayout);
        lhwardNo_spinnerLayout =(LinearLayout)findViewById(R.id.lhwardNo_spinnerLayout);
    }

}
