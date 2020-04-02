package com.collectorate.deliveryatyourdoor.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.collectorate.deliveryatyourdoor.Activity.Login;
import com.collectorate.deliveryatyourdoor.Activity.SignUp;
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

public class Profile extends Fragment {
    View view;
    TextView name,mobile,submit;
    EditText address,pincode;
    Spinner wardspin,zonespin;
    ArrayList<String> zonedata,warddata;
    boolean wardselected = true,zoneselected = true;
    String wardval,zoneval;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profile,container,false);
        name = (TextView)view.findViewById(R.id.tv_fragment_profile_name);
        mobile = (TextView)view.findViewById(R.id.tv_fragment_profile_mobile);
        address = (EditText)view.findViewById(R.id.et_fragment_profile_address);
        pincode = (EditText)view.findViewById(R.id.et_fragment_profile_pincode);
        wardspin = (Spinner)view.findViewById(R.id.sp_fragment_profile_ward);
        zonespin = (Spinner)view.findViewById(R.id.sp_fragment_profile_zone);
        submit = (TextView)view.findViewById(R.id.tv_fragment_profile_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setClickable(false);
                if(validData())
                    profileUpdateApiCall();
                else
                    Toast.makeText(getActivity(),"Fill all details",Toast.LENGTH_LONG).show();
            }
        });
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

        ArrayAdapter<String> zoneadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, zonedata);
        zoneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> wardadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, warddata);
        wardadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        zonespin.setAdapter(zoneadapter);
        wardspin.setAdapter(wardadapter);

        name.setText(SessionManagement.getName(getActivity()));
        mobile.setText(SessionManagement.getPhoneNo(getActivity()));
        address.setText(SessionManagement.getAddress(getActivity()));
        pincode.setText(SessionManagement.getPincode(getActivity()));

        if(SessionManagement.getZone(getActivity()).equalsIgnoreCase("north")){
            zonespin.setSelection(1);
        }else{
            zonespin.setSelection(2);
        }

        int ward = Integer.parseInt(SessionManagement.getWard(getActivity()));
        wardspin.setSelection(ward);


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

        return view;
    }

    public boolean validData(){
        boolean datavalid = true;
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

    private void profileUpdateApiCall(){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("pincode", pincode.getText().toString().trim());
            jsonBodyObj.put("address", address.getText().toString().trim());
            jsonBodyObj.put("wardNo", wardval);
            jsonBodyObj.put("wardZone", zoneval);
            String token = SessionManagement.getUserToken(getActivity());
            POSTAPIRequest postapiRequest=new POSTAPIRequest();
            String url = URLs.BASE_URL+ URLs.UPDATE_PROFILE;
            Log.i("url",String.valueOf(url));
            HeadersUtil headparam = new HeadersUtil(token);
            postapiRequest.request(getActivity(), updateApiListener,url,headparam,jsonBodyObj);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    FetchDataListener updateApiListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    if (data.getInt("error") == 0) {
                        Log.i("Login", "Update Successfull");
                        SessionManagement.setAddress(getActivity(),address.getText().toString().trim());
                        SessionManagement.setWard(getActivity(),wardval);
                        SessionManagement.setZone(getActivity(),zoneval);
                        SessionManagement.setPincode(getActivity(),pincode.getText().toString().trim());
                        Toast.makeText(getActivity(),"Profile Updated Successfully",Toast.LENGTH_LONG).show();
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
}
