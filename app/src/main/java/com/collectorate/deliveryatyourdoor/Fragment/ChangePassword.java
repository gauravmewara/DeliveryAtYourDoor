package com.collectorate.deliveryatyourdoor.Fragment;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.POSTAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.RequestQueueService;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends Fragment {
    View view;
    EditText otp,pwd;
    TextView submit,sendotp;
    String generatedOtp;
    String mobileno;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_change_password,container,false);
        otp = (EditText)view.findViewById(R.id.et_fragment_changepwd_otp);
        pwd = (EditText)view.findViewById(R.id.et_fragment_changepwd_newpwd);
        submit = (TextView)view.findViewById(R.id.tv_fragment_changepwd_submit);
        sendotp = (TextView)view.findViewById(R.id.tv_fragment_changepwd_sendotp);
        mobileno = SessionManagement.getPhoneNo(getActivity());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyOtp()){
                    if(validData()){
                        changePasswordApiCall();
                    }else
                        Toast.makeText(getActivity(),"Enter Password",Toast.LENGTH_LONG).show();
                }
            }
        });

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

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

    private void changePasswordApiCall(){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("password", pwd.getText().toString().trim());
            String token = SessionManagement.getUserToken(getActivity());
            POSTAPIRequest postapiRequest=new POSTAPIRequest();
            String url = URLs.BASE_URL+ URLs.CHANGE_PASSWORD_URL;
            Log.i("url",String.valueOf(url));
            HeadersUtil headparam = new HeadersUtil(token);
            postapiRequest.request(getActivity(), chngPwdListener,url,headparam,jsonBodyObj);
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
                        Toast.makeText(getActivity(),data.getString("message"),Toast.LENGTH_LONG).show();
                        submit.setClickable(true);
                    }else {
                        RequestQueueService.showAlert(data.getString("message"), getActivity());
                        submit.setClickable(true);
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", getActivity());
                    submit.setClickable(true);
                }
            } catch (Exception e) {
                RequestQueueService.showAlert("Something went wrong", getActivity());
                submit.setClickable(true);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlert(msg, getActivity());
            submit.setClickable(true);
        }

        @Override
        public void onFetchStart() {
            //RequestQueueService.showProgressDialog(Login.this);
        }
    };
    public boolean verifyOtp(){
        boolean otpcorrect=false;
        if(otp.getText().toString()!= null){
            if(otp.getText().toString().trim().equals("")){
                otpcorrect = false;
            }else{
                if(otp.getText().toString().trim().equals(generatedOtp))
                    otpcorrect = true;
            }
        }else{
            otpcorrect = false;
        }
        return otpcorrect;
    }

    public void sendOtp(){

    }
}
