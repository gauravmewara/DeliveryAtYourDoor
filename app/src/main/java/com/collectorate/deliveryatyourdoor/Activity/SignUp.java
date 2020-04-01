package com.collectorate.deliveryatyourdoor.Activity;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.POSTAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.RequestQueueService;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    EditText name,phone,address,pincode,mobile,otp,password;
    Spinner zonespin,wardspin;
    TextView sendotp,knowward,submit;
    String zoneval,wardval;
    boolean wardselected = false,zoneselected=false;
    ArrayList<String> zonedata;
    ArrayList<String> warddata;
    boolean otpverified=false;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private String authToken;
    String verification_code;
    private boolean otpsent =false;
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
        for(int i=0;i<81;i++){
            if(i==0){
                warddata.add("Select Ward");
            }else{
                warddata.add("Ward "+i);
            }
        }
        zonespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    zoneselected = false;
                }else{
                    zoneselected = true;
                }
                zoneval = zonedata.get(i).toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        wardspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    wardselected = false;
                }else{
                    wardselected = true;
                }
                wardval = String.valueOf((i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> zoneadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zonedata);
        zoneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> wardadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, warddata);
        wardadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        zonespin.setAdapter(zoneadapter);
        wardspin.setAdapter(wardadapter);

        mAuth = FirebaseAuth.getInstance();

    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //String otpCode = phoneAuthCredential.getSmsCode();
            Toast.makeText(SignUp.this, "Verification Complete", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i("OtpScreen", e.getMessage());
            Toast.makeText(SignUp.this, "Verification Failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(SignUp.this, "Code Sent", Toast.LENGTH_SHORT).show();
            otpsent = true;
            mVerificationId=s;
            Log.i("chck ver", mVerificationId);
        }
    };


    private void verifyCode(String otpCode) {
        Log.i("chck ver", mVerificationId);
        Log.i("check_code", otpCode);
        //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp.getText().toString().trim());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Verification Success", Toast.LENGTH_SHORT).show();
                            signUpApiCall();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUp.this, "Otp Verification Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode(String mobileno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileno,
                60,
                TimeUnit.SECONDS,
                this,
                /*TaskExecutors.MAIN_THREAD,*/
                mCallback);
    }


    private void reSendVerificationCode(String mobileno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileno,
                2,
                TimeUnit.MINUTES,
                this,
                mCallbacks,
                mResendToken);
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_signup_submit:
                if(otpsent=true){
                    if(validData()) {
                        verifyCode(otp.getText().toString().trim());
                    } else
                        Toast.makeText(SignUp.this,"Fill all details",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SignUp.this,"Send OTP to Mobile",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.tv_signup_sendotp:
                if (mobile.getText().toString().equals("")){
                    mobile.setError("Enter your mobile number");
                }else if (mobile.getText().toString().length()!=10){
                    mobile.setError("Invalid Mobile No.");
                }else {
                    sendVerificationCode("+91"+mobile.getText().toString().trim());
                }
                break;
            case R.id.tv_signup_knwward:
                Intent intent = new Intent(SignUp.this,PdfViewActivity.class);
                startActivity(intent);
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
        if(zoneselected && datavalid){
            datavalid = true;
            /*if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }*/
        }else{
            datavalid = false;
        }
        if(wardselected && datavalid){
           datavalid = true;
            /* if(name.getText().toString().trim().equals("")){
                datavalid = false;
            }else{
                datavalid = true;
            }*/
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

    FetchDataListener signUpApiListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    if (data.getInt("error") == 0) {
                        Log.i("SignUp", "Signup Successfull");
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
