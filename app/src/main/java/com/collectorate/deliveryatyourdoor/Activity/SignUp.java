package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    EditText name,phone,address,pincode,mobile,otp,password;
    Spinner zonespin,wardspin;
    TextView sendotp,knowward,submit;
    String zoneval,wardval;
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
}
