package com.collectorate.deliveryatyourdoor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.GETAPIRequest;
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

import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

    View view;
    EditText otp,pwd,mobile;
    TextView submit,sendotp;
    RelativeLayout backbtn;
    String generatedOtp;
    String mobileno;
    private FirebaseAuth mAuth;
    private boolean otpsent = false;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mobile = (EditText)view.findViewById(R.id.et_fragment_forgot_mobile);
        otp = (EditText)view.findViewById(R.id.et_fragment_forgot_otp);
        pwd = (EditText)view.findViewById(R.id.et_fragment_forgot_newpwd);
        submit = (TextView)view.findViewById(R.id.tv_fragment_forgot_submit);
        sendotp = (TextView)view.findViewById(R.id.tv_fragment_forgot_sendotp);
        backbtn = (RelativeLayout) view.findViewById(R.id.rl_forgotpwd_back);
        mAuth = FirebaseAuth.getInstance();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpsent=true){
                    if(validData()) {
                        verifyCode(otp.getText().toString().trim());
                    } else
                        Toast.makeText(ForgotPassword.this,"Fill all details",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPassword.this,"Send OTP to Mobile",Toast.LENGTH_LONG).show();
                }
            }
        });

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.getText().toString().equals("")){
                    mobile.setError("Enter your mobile number ");
                }else if (mobile.getText().toString().length()!=10){
                    mobile.setError("Invalid Mobile No.");
                }else {
                    checkPhone();
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
                mCallback,
                mResendToken);
    }


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
                            Toast.makeText(ForgotPassword.this, "Verification Success", Toast.LENGTH_SHORT).show();
                            resetPasswordApiCall();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ForgotPassword.this, "Otp Verification Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //String otpCode = phoneAuthCredential.getSmsCode();
            Toast.makeText(ForgotPassword.this, "Verification Complete", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i("OtpScreen", e.getMessage());
            Toast.makeText(ForgotPassword.this, "Verification Failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(ForgotPassword.this, "Code Sent", Toast.LENGTH_SHORT).show();
            otpsent = true;
            mVerificationId=s;
            Log.i("chck ver", mVerificationId);
        }
    };



    public boolean validData(){
        boolean datavalid = true;
        if(pwd.getText().toString()!= null && datavalid){
            if(pwd.getText().toString().trim().equals("") || pwd.getText().toString().trim().length()>5){
                datavalid = false;
            }else{
                datavalid = true;
            }
        }else{
            datavalid = false;
        }

        return datavalid;
    }

    private void resetPasswordApiCall(){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("password", pwd.getText().toString().trim());
            jsonBodyObj.put("phone", mobile.getText().toString().trim());
            String token = SessionManagement.getUserToken(ForgotPassword.this);
            POSTAPIRequest postapiRequest=new POSTAPIRequest();
            String url = URLs.BASE_URL+ URLs.RESET_PASSWORD_URL;
            Log.i("url",String.valueOf(url));
            HeadersUtil headparam = new HeadersUtil(token);
            postapiRequest.request(ForgotPassword.this, chngPwdListener,url,headparam,jsonBodyObj);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    FetchDataListener chngPwdListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    if (data.getInt("error") == 0) {
                        Log.i("Login", "Password Changed Successfully");
                        Toast.makeText(ForgotPassword.this,data.getString("message"),Toast.LENGTH_LONG).show();
                        submit.setClickable(true);
                    }else {
                        RequestQueueService.showAlert(data.getString("message"), ForgotPassword.this);
                        submit.setClickable(true);
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", ForgotPassword.this);
                    submit.setClickable(true);
                }
            } catch (Exception e) {
                RequestQueueService.showAlert("Something went wrong", ForgotPassword.this);
                submit.setClickable(true);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlert(msg,ForgotPassword.this);
            submit.setClickable(true);
        }

        @Override
        public void onFetchStart() {
            //RequestQueueService.showProgressDialog(Login.this);
        }
    };

    public void checkPhone() {
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("phone", mobile.getText().toString().trim());
            GETAPIRequest postapiRequest = new GETAPIRequest();
            String url = URLs.BASE_URL + URLs.PHONE_EXIST_URL;
            Log.i("url", String.valueOf(url));
            HeadersUtil headparam = new HeadersUtil();
            postapiRequest.request(ForgotPassword.this, chkPhnListener, url, headparam, jsonBodyObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    FetchDataListener chkPhnListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    if (data.getInt("error") == 0) {
                        Log.i("ChkPhn", "Phone Exist");
                        sendVerificationCode("+91"+mobile.getText().toString().trim());
                        //Toast.makeText(ForgotPassword.this,data.getString("message"),Toast.LENGTH_LONG).show();
                    }else {
                        RequestQueueService.showAlert("No User With This Mobile", ForgotPassword.this);
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", ForgotPassword.this);
                }
            } catch (Exception e) {
                RequestQueueService.showAlert("Something went wrong", ForgotPassword.this);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlert(msg,ForgotPassword.this);
        }

        @Override
        public void onFetchStart() {
            //RequestQueueService.showProgressDialog(Login.this);
        }
    };
}
