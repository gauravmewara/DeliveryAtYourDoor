package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Adapter.DeliveryWardZoneAdapter;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.SharedPrefUtil;

import java.util.ArrayList;

public class DeliverySelectYourWard extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    RelativeLayout menu,filter;
    TextView pageTitle;
    LinearLayout proceed;
    Spinner wardZoneSpin,wardNoSpin ;
    DeliveryWardZoneAdapter wzadapter;
   // DileveryWardNumberSpinnerAdapter wnadapter;
    ArrayList<String> wardzoneList;
    ArrayList<String>wardNumberList;
    String dir,wardnumber;
    String[] values;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_select_your_ward);
        pageTitle=findViewById(R.id.tv_toolbar_heading);
        pageTitle.setText(R.string.selectYourWard);
        menu=findViewById(R.id.rl_toolbar_menu);
       filter=findViewById(R.id.rl_toolbar_filterLayout);
       filter.setVisibility(View.GONE);
        proceed=findViewById(R.id.lv_proceedLayout);
        proceed.setOnClickListener(this);
        wardZoneSpin=findViewById(R.id.wardZone_spinner);

        wardzoneList=new ArrayList<String>();
        wardzoneList.add("North");
        wardzoneList.add("South");

        wardZoneSpin.setAdapter(new ArrayAdapter<String>(DeliverySelectYourWard.this,android.R.layout.simple_spinner_dropdown_item,wardzoneList));
        wardZoneSpin.setOnItemSelectedListener(DeliverySelectYourWard.this);
        Log.i("providers",wardzoneList.toString());


        /*wzadapter=new DeliveryWardZoneAdapter(DeliverySelectYourWard.this,wardzoneList);
        wardZoneSpin.setAdapter(wzadapter);
        wardZoneSpin.setOnItemSelectedListener(DeliverySelectYourWard.this);*/
        SharedPrefUtil.setPreferences(DeliverySelectYourWard.this, Constants.SHARED_PREF_NOTICATION_TAG,Constants.SHARED_NOTIFICATION_COUNT_KEY,"0");
        wardNoSpin = findViewById(R.id.wardno_spinner);
        addWardNumber();



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i ;
                switch (view.getId()){
                    case R.id.rl_toolbar_menu:
                        Log.i("profileclicked","profile_menu_image_clicked()");
                        PopupMenu popupMenu = new PopupMenu(DeliverySelectYourWard.this,view);
                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.delivery_profile_menu,popupMenu.getMenu());
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i;
                                switch (item.getItemId()) {
                                    case R.id.mi_selectward:
                                        /*i = new Intent(DeliverySelectYourWard.this, ChangePassword.class);
                                        startActivity(i);*/
                                        return true;
                                    case R.id.mi_dashbord:
                                        /*i = new Intent(DeliverySelectYourWard.this, EditProfile.class);
                                        startActivityForResult(i, 2);*/
                                        return true;

                                    case R.id.mi_logout:
                                        /*i = new Intent(DeliverySelectYourWard.this,getApplicationContext(), EditProfile.class);
                                        startActivityForResult(i, 2);*/
                                        return true;
                                    default:
                                        return onOptionsItemSelected(item);
                                }
                            }
                        });
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent i ;
        switch (view.getId()){
            case R.id.lv_proceedLayout:
               SharedPrefUtil.setPreferences(DeliverySelectYourWard.this,Constants.SHARED_PREF_WARDZONE_TAG,Constants.SHARED_WARDZONE_KEY,dir);
                SharedPrefUtil.setPreferences(DeliverySelectYourWard.this,Constants.SHARED_PREF_WARD_NUMBER_TAG,Constants.SHARED_WARDNUMBER_KEY,wardnumber);


                i=new Intent(DeliverySelectYourWard.this,DeliveryDashboard.class);
                startActivity(i);
                break;
        }

    }

    public void addWardNumber(){
        wardNumberList=new ArrayList<>();
      /* for (int i =0;i<81;i++){
           wardNumberList.add(values[i]);
       }*/
         wardNumberList.add("1");
        wardNumberList.add("2");
        wardNumberList.add("3");
        wardNumberList.add("4");
        wardNumberList.add("5");
        wardNumberList.add("6");
        wardNumberList.add("7");
        wardNumberList.add("8");
        wardNumberList.add("9");
        wardNumberList.add("10");

        wardNoSpin.setAdapter(new ArrayAdapter<String>(DeliverySelectYourWard.this,android.R.layout.simple_spinner_dropdown_item,wardNumberList));
        wardNoSpin.setOnItemSelectedListener(DeliverySelectYourWard.this);


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.wardZone_spinner)
        {
            dir=wardzoneList.get(position);
            //do this
        }
        else if(parent.getId() == R.id.wardno_spinner)
        {
            wardnumber=wardNumberList.get(position);
            //do this
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
