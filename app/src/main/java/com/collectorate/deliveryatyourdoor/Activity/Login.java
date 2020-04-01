package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.POSTAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.RequestQueueService;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener {
    EditText mobile,password;
    TextView signin,signup,forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobile = (EditText)findViewById(R.id.et_login_mobile);
        password = (EditText) findViewById(R.id.et_login_pwd);
        signin = (TextView) findViewById(R.id.tv_login_loginttbn);
        signup = (TextView) findViewById(R.id.tv_login_signupbtn);
        forgot = (TextView) findViewById(R.id.tv_login_forgotpassword);
        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgot.setOnClickListener(this);
    }

    public boolean checkData(){
        boolean dataCorrect;
        if(mobile.getText().toString().trim()==null || password.getText().toString().trim()==null){
            dataCorrect = false;
        }else if(mobile.getText().toString().equals("") || password.getText().toString().equals("")){
            dataCorrect = false;
        }else
            dataCorrect = true;
        return dataCorrect;
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.tv_login_loginttbn:
                signin.setClickable(false);
                if(!checkData()){
                    Toast.makeText(this,"Fill Details",Toast.LENGTH_LONG).show();
                }else{
                    loginApiCall();
                }
                break;
            case R.id.tv_login_signupbtn:
                intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
                break;
            case R.id.tv_login_forgotpassword:
                intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
                break;
        }
    }

    private void loginApiCall(){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("phone", mobile.getText().toString().trim());
            jsonBodyObj.put("password", password.getText().toString().trim());
            jsonBodyObj.put("device_type", "a");
            jsonBodyObj.put("userType","1");
            String token = FirebaseInstanceId.getInstance().getToken();
            jsonBodyObj.put("device_token", token);
            POSTAPIRequest postapiRequest=new POSTAPIRequest();
            String url = URLs.BASE_URL+ URLs.SIGN_IN_URL;
            Log.i("url",String.valueOf(url));
            //Log.i("token",String.valueOf(token));
            //Log.i("Request",username.getText().toString()+", "+pwd.getText().toString());
            HeadersUtil headparam = new HeadersUtil();
            postapiRequest.request(this, loginApiListener,url,headparam,jsonBodyObj);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    FetchDataListener loginApiListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    if (data.getInt("error") == 0) {
                        Log.i("Login", "Login Successfull");
                        JSONObject userdetail = data.getJSONObject("data");
                        if (userdetail != null) {
                            SessionManagement.createLoginSession(Login.this,
                                    true,
                                    userdetail.getString("_id"),
                                    userdetail.getString("phone"),
                                    userdetail.getString("name"),
                                    userdetail.getString("token"),
                                    userdetail.getString("address"),
                                    userdetail.getString("pincode"),
                                    userdetail.getString("wardZone"),
                                    userdetail.getString("wardNo"),
                                    userdetail.getString("userType"), "9");
                            Intent i = new Intent(Login.this, PlaceOrder.class);
                            startActivity(i);
                            finish();
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", Login.this);
                            signin.setClickable(true);
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", Login.this);
                    signin.setClickable(true);
                }
            } catch (Exception e) {
                RequestQueueService.showAlert("Something went wrong", Login.this);
                signin.setClickable(true);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlert(msg, Login.this);
            signin.setClickable(true);
        }

        @Override
        public void onFetchStart() {
            //RequestQueueService.showProgressDialog(Login.this);
        }
    };

}
