package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.POSTAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.RequestQueueService;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    EditText name,phone,address,pincode,mobile,otp,password;
    Spinner zonespin,wardspin;
    TextView sendotp,knowward,submit;
    String zoneval,wardval;
    boolean wardselected = false,zoneselected=false;
    ArrayList<String> zonedata;
    ArrayList<String> warddata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText)findViewById(R.id.et_signup_name);
        phone = (EditText)findViewById(R.id.et_signup_mobile);
        address = (EditText)findViewById(R.id.et_signup_address);
        pincode = (EditText)findViewById(R.id.et_signup_pincode);
        mobile = (EditText)findViewById(R.id.et_signup_mobile);
        otp = (EditText)findViewById(R.id.et_signup_otp);
        password = (EditText)findViewById(R.id.et_signup_password);
        zonespin = (Spinner)findViewById(R.id.sp_signup_zone);
        wardspin = (Spinner)findViewById(R.id.sp_signup_ward);
        sendotp =(TextView)findViewById(R.id.tv_signup_sendotp);
        sendotp.setOnClickListener(this);
        knowward = (TextView)findViewById(R.id.tv_signup_knwward);
        knowward.setOnClickListener(this);
        submit = (TextView)findViewById(R.id.tv_signup_submit);
        submit.setOnClickListener(this);
        zonedata = new ArrayList<>();
        zonedata.add("Select Zone");
        zonedata.add("North");
        zonedata.add("South");
        warddata = new ArrayList<>();
        for(int i=0;i<82;i++){
            if(i==0){
                warddata.add("Select Ward");
            }else{
                warddata.add("Ward "+i);
            }
        }
        zonespin.setOnItemSelectedListener(this);
        wardspin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter zoneadapter = new ArrayAdapter(this,R.layout.spinner_single_item,zonedata);
        ArrayAdapter wardadapter = new ArrayAdapter(this,R.layout.spinner_single_item,warddata);
        zoneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wardadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        zonespin.setAdapter(zoneadapter);
        wardspin.setAdapter(wardadapter);

    }




    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Log.d("Spinner Id:",String.valueOf(arg1.getId()));
        switch (arg1.getId()){
            case R.id.sp_signup_zone:
                if(position==0){
                    zoneselected = false;
                }else{
                    zoneselected = true;
                }
                zoneval = zonedata.get(position);
                break;
            case R.id.sp_signup_ward:
                if(position==0){
                    wardselected = false;
                }else{
                    wardselected = true;
                }
                wardval = warddata.get(position);
                break;
        }
        //Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_signup_submit:
                break;
            case R.id.tv_signup_sendotp:
                break;
            case R.id.tv_signup_knwward:
                break;
        }
    }

    public boolean validData(){
        boolean datavalid = true;
        if(name.getText().toString()!= null && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        if(mobile.getText().toString()!= null && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        if(password.getText().toString()!= null && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        if(otp.getText().toString()!= null && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        if(pincode.getText().toString()!= null && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        if(address.getText().toString()!= null && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        if(!zoneselected && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        if(!wardselected && datavalid){
            if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }
        return datavalid;
    }

    private void signUpApiCall(){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("name", name.getText().toString().trim());
            jsonBodyObj.put("phone", mobile.getText().toString().trim());
            jsonBodyObj.put("password", password.getText().toString().trim());
            jsonBodyObj.put("pincode", pincode.getText().toString().trim());
            jsonBodyObj.put("address", address.getText().toString().trim());
            jsonBodyObj.put("wardNo", wardval);
            jsonBodyObj.put("wardZone", zoneval);
            jsonBodyObj.put("otp", otp.getText().toString().trim());
            jsonBodyObj.put("device_type", "a");
            POSTAPIRequest postapiRequest=new POSTAPIRequest();
            String url = URLs.BASE_URL+ URLs.SIGN_UP_URL;
            Log.i("url",String.valueOf(url));
            HeadersUtil headparam = new HeadersUtil();
            postapiRequest.request(this, signUpApiListener,url,headparam,jsonBodyObj);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    FetchDataListener signUpApiListener;
    {
        signUpApiListener = new FetchDataListener() {
            @Override
            public void onFetchComplete(JSONObject data) {
                //RequestQueueService.cancelProgressDialog();
                try {
                    if (data != null) {
                        if (data.getInt("error") == 0) {
                            Log.i("Login", "Login Successfull");
                            JSONObject userdetail = data.getJSONObject("data");
                            if (userdetail != null) {
                                Intent i = new Intent(SignUp.this, Login.class);
                                startActivity(i);
                                finish();
                            } else {
                                RequestQueueService.showAlert("Error! No data fetched", SignUp.this);
                                submit.setClickable(true);
                            }
                        }
                    } else {
                        RequestQueueService.showAlert("Error! No data fetched", SignUp.this);
                        submit.setClickable(true);
                    }
                } catch (Exception e) {
                    RequestQueueService.showAlert("Something went wrong", SignUp.this);
                    submit.setClickable(true);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFetchFailure(String msg) {
                //RequestQueueService.cancelProgressDialog();
                RequestQueueService.showAlert(msg, SignUp.this);
                submit.setClickable(true);
            }
            @Override
            public void onFetchStart() {
                //RequestQueueService.showProgressDialog(Login.this);
            }
        };
    }

}
