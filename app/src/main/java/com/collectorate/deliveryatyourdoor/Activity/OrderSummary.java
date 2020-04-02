package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.collectorate.deliveryatyourdoor.Adapter.CartItemAdapter;
import com.collectorate.deliveryatyourdoor.Modal.OrderModal;
import com.collectorate.deliveryatyourdoor.Modal.ProductsModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.POSTAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderSummary extends AppCompatActivity {
    TextView orderstatus,deliverycode,orderid,orderdate,deliverydate,deliverydatetext,rsntxt,rsn,cancel,orderamt;
    TextView pagetitle;
    RelativeLayout orderstatuslayout,menuback;
    LinearLayout deliverycodeview,deliverydateview;
    RecyclerView cartlistview;
    boolean reasonselected = false,otherselected=false;
    String cancelreason;
    int selecteposition;
    boolean cancelled=false;
    BottomSheetDialog cancelorder;
    EditText other;
    Spinner reason;
    ArrayList<String> reasonList;
    TextView yes,no;
    OrderModal order;
    ArrayList<ProductsModal> cartlistdata;
    CartItemAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        order = (OrderModal)getIntent().getSerializableExtra("order");
        selecteposition = (int)getIntent().getIntExtra("position",0);
        pagetitle = (TextView)findViewById(R.id.tv_toolbar2_heading);
        pagetitle.setText("Order Details");
        menuback=(RelativeLayout)findViewById(R.id.rl_toolbar2_menu);
        menuback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        orderstatus = (TextView)findViewById(R.id.tv_ordersummary_orderstatus);
        deliverycode = (TextView)findViewById(R.id.tv_ordersummary_deliverycode);
        orderid = (TextView)findViewById(R.id.tv_ordersummary_orderid);
        orderdate = (TextView)findViewById(R.id.tv_ordersummary_orderdate);
        deliverydate = (TextView)findViewById(R.id.tv_ordersummary_deliverydate);
        deliverydatetext = (TextView)findViewById(R.id.tv_ordersummary_deliverydatetext);
        rsntxt = (TextView)findViewById(R.id.tv_ordersummary_rsntitle);
        rsn = (TextView)findViewById(R.id.tv_ordersummary_rsn);
        cancel = (TextView)findViewById(R.id.tv_ordersummary_submit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelorder = new BottomSheetDialog(OrderSummary.this);
                View sheetView = LayoutInflater.from(OrderSummary.this).inflate(R.layout.order_cancel_dialog,null);
                cancelorder.setContentView(sheetView);
                reasonList = new ArrayList<>();
                reasonList.add("Select Reason");
                reasonList.add("Product not satisfactory");
                reasonList.add("Delivery time is large");
                reasonList.add("Other");


                reason = (Spinner)sheetView.findViewById(R.id.sp_order_cancel_rsnlist);
                other = (EditText)sheetView.findViewById(R.id.et_fragment_cancel_rsn_txt);

                ArrayAdapter<String> reasondapter = new ArrayAdapter<String>(OrderSummary.this, android.R.layout.simple_spinner_item, reasonList);
                reasondapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reason.setAdapter(reasondapter);

                //ArrayAdapter reasondapter = new ArrayAdapter(OrderSummary.this,R.layout.spinner_single_item,reasonList);
                reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i==0) {
                            reasonselected = false;
                            other.setVisibility(View.GONE);
                            otherselected = false;
                        }
                        else {
                            cancelreason = reasonList.get(i);
                            if(cancelreason.equals("Other")){
                                other.setVisibility(View.VISIBLE);
                                otherselected = true;
                            }else{
                                other.setVisibility(View.GONE);
                                otherselected = false;
                            }
                            reasonselected = true;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                yes = (TextView)sheetView.findViewById(R.id.tv_canceldialog_accept);
                no = (TextView)sheetView.findViewById(R.id.tv_canceldialog_reject);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        yes.setClickable(false);
                        boolean otherempty=false;
                        if(otherselected){
                            if(other.getText()!=null) {
                                otherempty = other.getText().toString().trim().equals("");
                            }else{
                                otherempty = true;
                            }
                        }
                        if(reasonselected && !otherempty) {
                            cancelOrder(order.getOrder_id());
                            //cancelled = true;
                            //onBackPressed();
                        }
                        else
                            Toast.makeText(OrderSummary.this,"Select Reason",Toast.LENGTH_LONG).show();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelorder.dismiss();
                    }
                });
                cancelorder.show();
            }
        });
        orderamt = (TextView)findViewById(R.id.tv_ordersummary_orderamt);

        orderstatuslayout = (RelativeLayout) findViewById(R.id.rl_ordersummary_orderstatuslayout);
        deliverycodeview = (LinearLayout) findViewById(R.id.ll_ordersummary_deliverycode);
        deliverydateview = (LinearLayout) findViewById(R.id.ll_ordersummary_deliverydate);

        cartlistview = (RecyclerView) findViewById(R.id.rv_ordersummary_cartlist);

        String status =order.getStatus();
        if(status.equals("Pending")){
            cancel.setVisibility(View.VISIBLE);
            deliverycodeview.setVisibility(View.VISIBLE);
            deliverycode.setText(order.getDeliveryCode());
            rsntxt.setVisibility(View.GONE);
            rsn.setVisibility(View.GONE);
            orderstatuslayout.setBackground(getDrawable(R.drawable.statusbackgroundblue));
            deliverydateview.setVisibility(View.GONE);
        }else if(status.equals("Delivered")){
            cancel.setVisibility(View.INVISIBLE);
            deliverycodeview.setVisibility(View.GONE);
            rsntxt.setVisibility(View.GONE);
            rsn.setVisibility(View.GONE);
            deliverydateview.setVisibility(View.VISIBLE);
            deliverydatetext.setText("Delivery Date: ");
            deliverydate.setText(order.getUpdateDate());
            orderstatuslayout.setBackground(getDrawable(R.drawable.statusbackgroundgreen));
        }else if(status.equals("Cancelled")){
            cancel.setVisibility(View.INVISIBLE);
            deliverycodeview.setVisibility(View.GONE);
            rsntxt.setVisibility(View.VISIBLE);
            rsn.setVisibility(View.VISIBLE);
            rsn.setText(order.getCancel_reason());
            deliverydateview.setVisibility(View.VISIBLE);
            deliverydatetext.setText("Cancel Date: ");
            deliverydate.setText(order.getUpdateDate());
            orderstatuslayout.setBackground(getDrawable(R.drawable.statusbackgroundred));
        }
        orderstatus.setText(order.getStatus());
        orderid.setText(order.getOrder_id());
        orderdate.setText(order.getOrderDate());
        orderamt.setText(order.getOrder_price());
        cartlistdata = new ArrayList<>();
        ArrayList<ProductsModal> itemdata = order.getOrderItems();
        for (ProductsModal temp:itemdata) {
            int qty = Integer.parseInt(temp.getPurchaseQty());
            for(int i=0;i<qty;i++){
                cartlistdata.add(temp);
            }
        }
        layoutManager = new LinearLayoutManager(this);
        cartlistview.setLayoutManager(layoutManager);
        adapter = new CartItemAdapter(this,cartlistdata, Constants.SUMMARY_INIT);
        cartlistview.setAdapter(adapter);
    }

    public void cancelOrder(String orderid){
        try{
            JSONObject jsonBody = new JSONObject();
            if(otherselected) {
                jsonBody.put("reason",other.getText().toString().trim() );
            }else{
                jsonBody.put("reason",cancelreason);
            }
            POSTAPIRequest getapiRequest=new POSTAPIRequest();
            String url = URLs.BASE_URL+URLs.CANCEL_ORDER_URL+orderid;
            Log.i("url", String.valueOf(url));
            String token = SessionManagement.getUserToken(OrderSummary.this);
            HeadersUtil headparam = new HeadersUtil(token);
            getapiRequest.request(getApplicationContext(),cancelOrderListener,url,headparam,jsonBody);
        }catch (Exception e){
            e.printStackTrace();
            yes.setClickable(true);
            cancelorder.dismiss();
        }
    }

    FetchDataListener cancelOrderListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            try{
                if(data!=null){
                    if(data.getInt("error")==0){
                        Toast.makeText(OrderSummary.this,data.getString("message"),Toast.LENGTH_LONG).show();
                        yes.setClickable(true);
                        cancelled = true;
                        cancelorder.dismiss();
                        onBackPressed();
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
                yes.setClickable(true);
                cancelorder.dismiss();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            yes.setClickable(true);
            cancelorder.dismiss();
        }

        @Override
        public void onFetchStart() {

        }
    };

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        Bundle b = new Bundle();
        if(cancelled) {
            b.putString("cancelled", "yes");
            OrderModal neword = order;
            neword.setStatus("Cancelled");
            neword.setActionbyid(SessionManagement.getUserId(OrderSummary.this));
            neword.setActionbyname("Customer");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            String currentDateandTime = sdf.format(new Date());
            neword.setActiondate(currentDateandTime);
            b.putInt("oldorder", selecteposition);
            b.putSerializable("neworder", neword);
        } else {
            b.putString("cancelled", "no");
        }
        resultIntent.putExtras(b);
        setResult(Activity.RESULT_OK,resultIntent);
        finish();
    }
}
