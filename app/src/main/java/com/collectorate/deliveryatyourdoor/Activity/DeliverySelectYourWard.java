package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Adapter.WarZoneAdapter;
import com.collectorate.deliveryatyourdoor.Adapter.WardNumberSpinnerAdapter;
import com.collectorate.deliveryatyourdoor.Modal.WardZoneModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.GETAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.RequestQueueService;
import com.collectorate.deliveryatyourdoor.Utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeliverySelectYourWard extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout menu;
    TextView pageTitle;
    LinearLayout proceed;
    Spinner wardZoneSpin,wardNoSpin ;
    WarZoneAdapter wzadapter;
    WardNumberSpinnerAdapter wnadapter;
    ArrayList<WardZoneModal> wardzoneList;
    ArrayList<String>wardNumberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_select_your_ward);
        pageTitle=findViewById(R.id.tv_toolbar_heading);
        pageTitle.setText(R.string.selectYourWard);
        menu=findViewById(R.id.rl_toolbar_menu);
        proceed=findViewById(R.id.lv_proceedLayout);
        proceed.setOnClickListener(this);
        wardZoneSpin = findViewById(R.id.wardZone_spinner);
        wardNoSpin = findViewById(R.id.wardno_spinner);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i ;
                switch (view.getId()){
                    case R.id.rl_toolbar_menu:
                        Log.i("profileclicked","profile_menu_image_clicked()");
                        PopupMenu popupMenu = new PopupMenu(DeliverySelectYourWard.this,view);
                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.profile_menu,popupMenu.getMenu());
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
                i=new Intent(DeliverySelectYourWard.this,DeliveryDashboard.class);
                startActivity(i);
                break;
        }

    }





  /*  private void wardZoneApiCalling(){
        try{
            GETAPIRequest getapiRequest=new GETAPIRequest();
            String url = URLs.BASE_URL+URLs.COUNTRIES_URL;
            getapiRequest.request(this,fetchGetResultListener,url);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    FetchDataListener fetchGetResultListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    String countries = data.getString("countries");
                    Log.i("Countries",countries);
                    //spinnerlist.add("Select");
                    JSONArray array = new JSONArray(countries);
                    for (int i =0;i<array.length();i++){
                        CountryModal cm = new CountryModal();
                        JSONObject jsonPart = array.getJSONObject(i);
                        Log.i("id",jsonPart.getString("id"));
                        Log.i("countryName",jsonPart.getString("countryName"));
                        cm.setCountryname(jsonPart.getString("countryName"));
                        cm.setCountryshortname(jsonPart.getString("countryCode"));
                        cm.setCountryphonename(jsonPart.getString("phoneCode"));
                        String x = cm.getCountryphonename()+" | "+cm.getCountryshortname();
                        countrycodes.add(cm);
                        //spinnerlist.add(x);
                    }
                    sadapter = new CountrySpinnerAdapter(Login.this,countrycodes);
                    spinner.setAdapter(sadapter);
                    spinner.setOnItemSelectedListener(Login.this);
                    login.setClickable(true);
                }
                else{
                    RequestQueueService.showAlert("Error! No data fetched", Login.this);
                }
            }catch (Exception e){
                RequestQueueService.showAlert("Something went wrong", Login.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();

            RequestQueueService.showAlert(msg,Login.this);
        }

        @Override
        public void onFetchStart() {

            //RequestQueueService.showProgressDialog(Login.this);
        }

    };*/




    /*  private void wardNumberApiCalling(){
        try{
            GETAPIRequest getapiRequest=new GETAPIRequest();
            String url = URLs.BASE_URL+URLs.COUNTRIES_URL;
            getapiRequest.request(this,fetchGetResultListener,url);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    FetchDataListener fetchGetResultListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    String countries = data.getString("countries");
                    Log.i("Countries",countries);
                    //spinnerlist.add("Select");
                    JSONArray array = new JSONArray(countries);
                    for (int i =0;i<array.length();i++){
                        CountryModal cm = new CountryModal();
                        JSONObject jsonPart = array.getJSONObject(i);
                        Log.i("id",jsonPart.getString("id"));
                        Log.i("countryName",jsonPart.getString("countryName"));
                        cm.setCountryname(jsonPart.getString("countryName"));
                        cm.setCountryshortname(jsonPart.getString("countryCode"));
                        cm.setCountryphonename(jsonPart.getString("phoneCode"));
                        String x = cm.getCountryphonename()+" | "+cm.getCountryshortname();
                        countrycodes.add(cm);
                        //spinnerlist.add(x);
                    }
                    sadapter = new CountrySpinnerAdapter(Login.this,countrycodes);
                    spinner.setAdapter(sadapter);
                    spinner.setOnItemSelectedListener(Login.this);
                    login.setClickable(true);
                }
                else{
                    RequestQueueService.showAlert("Error! No data fetched", Login.this);
                }
            }catch (Exception e){
                RequestQueueService.showAlert("Something went wrong", Login.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();

            RequestQueueService.showAlert(msg,Login.this);
        }

        @Override
        public void onFetchStart() {

            //RequestQueueService.showProgressDialog(Login.this);
        }

    };*/
}
