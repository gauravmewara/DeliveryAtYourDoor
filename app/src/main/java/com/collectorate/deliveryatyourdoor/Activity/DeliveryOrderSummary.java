package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Adapter.DeliveryOrderAdapter;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.PaginationScrollListener;

public class DeliveryOrderSummary extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout back,otpLayout;
    RecyclerView recyclerView;
    TextView pageTitle,orderidValue,dateValue,totalValue,nodataProducts,askCustomer;
    ProgressBar tripDetProgressBar;
    LinearLayout buttonLayout,completedLayout;
    EditText Otp;
    Button cancel,complete;
    DeliveryOrderAdapter orderAdapter;
    LinearLayoutManager mLayoutManager;
    private final int PAGE_START  = 1;
    private int TOTAL_PAGES = 1;
    private static int page_size = 15;
    private int totaltxnCount;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    boolean isListNull = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_summary);
        initViews();
    }
    public void initViews(){
        back=(RelativeLayout)findViewById(R.id.rl_toolbar_back);
        back.setOnClickListener(this);
        pageTitle=(TextView)findViewById(R.id.tv_toolbar_heading);
        pageTitle.setText("Order Summary");
        orderidValue=(TextView)findViewById(R.id.tv_orderid_value);
        dateValue=(TextView)findViewById(R.id.tv_date_value);
        recyclerView=(RecyclerView)findViewById(R.id.rv_orderSummary);
        nodataProducts=(TextView)findViewById(R.id.nodataProducts);
        totalValue=(TextView)findViewById(R.id.tv_total_value);
        askCustomer=(TextView)findViewById(R.id.askCustomer);
        otpLayout=(RelativeLayout)findViewById(R.id.OtpLayout);
        Otp=(EditText) findViewById(R.id.et_OTP);
        buttonLayout=(LinearLayout)findViewById(R.id.lh_buttonsLayout);
        completedLayout=(LinearLayout)findViewById(R.id.completedLayout);
        cancel=(Button)findViewById(R.id.orderSummaryCancelBtn);
        cancel.setOnClickListener(this);
        complete=(Button)findViewById(R.id.orderSummaryCompleteBtn);
        complete.setOnClickListener(this);

        orderAdapter = new DeliveryOrderAdapter(this,DeliveryOrderSummary.this, Constants.PRODUCT_DETAILS);

        mLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(orderAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       // loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //tripDetailsListApiCalling();
    }

    @Override
    public void onClick(View view) {
        Intent i ;
        switch (view.getId()){
            case R.id.orderSummaryCancelBtn:
                break;
            case R.id.orderSummaryCompleteBtn:
                otpLayout.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.GONE);
                completedLayout.setVisibility(View.VISIBLE);
                askCustomer.setVisibility(View.GONE);
                break;
            case R.id.rl_toolbar_back:
                onBackPressed();
        }
    }

    public void setRecyclerView(){
        if(isListNull){
            tripDetProgressBar.setVisibility(View.GONE);
            nodataProducts.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            tripDetProgressBar.setVisibility(View.GONE);
            nodataProducts.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

