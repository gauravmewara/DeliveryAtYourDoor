package com.collectorate.deliveryatyourdoor.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collectorate.deliveryatyourdoor.Adapter.OrderAdapter;
import com.collectorate.deliveryatyourdoor.Adapter.ProductAdapter;
import com.collectorate.deliveryatyourdoor.Interface.OrderCancelListener;
import com.collectorate.deliveryatyourdoor.Modal.OrderModal;
import com.collectorate.deliveryatyourdoor.Modal.ProductsModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.GETAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.PaginationScrollListener;
import com.collectorate.deliveryatyourdoor.Utils.RequestQueueService;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistory extends Fragment {

    View view;
    RelativeLayout menuback,menunotification;
    TextView nodata;
    ProgressBar progress;
    int cancelledposition;
    private final int PAGE_START  = 1;
    private int TOTAL_PAGES = 1;
    private static int page_size = 15;
    RecyclerView itemlistview;
    OrderAdapter adapter;
    float orderPrice=0;
    ArrayList<OrderModal> cartitems;
    LinearLayoutManager mLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private int totalBookingCount;
    boolean isListNull = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_order_history,container,false);
        itemlistview = (RecyclerView)view.findViewById(R.id.rv_fg_orderhistory_list);
        nodata = (TextView)view.findViewById(R.id.tv_placeorder_nodata);
        progress = (ProgressBar)view.findViewById(R.id.pg_placeorder);
        nodata.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        adapter = new OrderAdapter(getContext(),getActivity());
        mLayoutManager = new LinearLayoutManager(getContext());
        itemlistview.setLayoutManager(mLayoutManager);
        itemlistview.setItemAnimator(new DefaultItemAnimator());
        itemlistview.setAdapter(adapter);
        itemlistview.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
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
        loadOrderData();
        return view;
    }

    public void loadOrderData(){
        try{
            GETAPIRequest getapiRequest=new GETAPIRequest();
            String url = URLs.BASE_URL+ URLs.ORDER_LIST_URL+"?page_size="+String.valueOf(page_size)+"&page=1";
            Log.i("url", String.valueOf(url));
            String token = SessionManagement.getUserToken(getActivity());
            HeadersUtil headparam = new HeadersUtil(token);
            getapiRequest.request(getActivity(),loadProductListener,url,headparam);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Constants.SUMMARY_CANCEL_REQUEST_CODE){
            if(data.getStringExtra("cancelled").equals("yes")){
                cancelledposition = data.getIntExtra("position",0);
                adapter.remove(cancelledposition);
            }
        }

    }

    FetchDataListener loadProductListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject response) {
            try {
                if (response != null) {
                    if (response.getInt("error")==0) {
                        ArrayList<OrderModal> tmodalList=new ArrayList<>();
                        JSONArray array = response.getJSONArray("data");
                        totalBookingCount = response.getInt("total");
                        if(totalBookingCount>page_size) {
                            if (totalBookingCount % page_size == 0) {
                                TOTAL_PAGES = totalBookingCount / page_size;
                            } else {
                                TOTAL_PAGES = (totalBookingCount / page_size) + 1;
                            }
                        }
                        if(array!=null) {
                            if(array.length()!=0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject orderdetail = (JSONObject) array.get(i);
                                    Log.i("Order List", orderdetail.toString());
                                    OrderModal order = new OrderModal();
                                    order.setOrder_id(orderdetail.getString("_id"));
                                    order.setOrder_price(orderdetail.getString("total"));
                                    order.setOrderDate(orderdetail.getString("createdAt"));
                                    order.setDeliveryCode(orderdetail.getString("deliveryCode"));
                                    String status = orderdetail.getString("status");
                                    if(status.equals("1")){
                                        status = "Pending";
                                    }else if(status.equals("2")){
                                        status = "Delivered";
                                    }else if(status.equals("3")){
                                        status = "Cancelled";
                                    }
                                    order.setStatus(status);
                                    order.setUpdateDate(orderdetail.getString("updatedAt"));
                                    order.setCancel_reason(orderdetail.getString("reason"));
                                    JSONArray items = orderdetail.getJSONArray("items");
                                    ArrayList<ProductsModal> itemList = new ArrayList<>();
                                    for(int j=0;j<itemList.size();j++){
                                        ProductsModal bmod = new ProductsModal();
                                        JSONObject product = items.getJSONObject(j).getJSONObject("product");
                                        bmod.setProductId(product.getString("_id"));
                                        bmod.setName(product.getString("name"));
                                        bmod.setPackaging(product.getString("packaging"));
                                        bmod.setUnit(product.getString("unit"));
                                        bmod.setUnitprice(product.getString("unitPrice"));
                                        bmod.setProductstock(product.getString("stock"));
                                        if(product.getString("available").equals("false")){
                                            bmod.setProductAvailable(false);
                                        }else{
                                            bmod.setProductAvailable(true);
                                        }
                                        bmod.setPurchaseQty(items.getJSONObject(j).getString("qty"));
                                        bmod.setQtyPrice(items.getJSONObject(j).getString("price"));
                                        itemList.add(bmod);
                                    }
                                    order.setOrderItems(itemList);

                                    tmodalList.add(order);
                                }
                                isListNull = false;
                            }
                        }
                        Log.d("OrderPlaceFragment:",array.toString());
                        setRecyclerView();
                        //progressBar.setVisibility(View.GONE);
                        adapter.addAll(tmodalList);
                        if (currentPage < TOTAL_PAGES)
                            adapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                setRecyclerView();
            }
        }
        @Override
        public void onFetchFailure(String msg) {
            RequestQueueService.showAlert(msg, getActivity());
            setRecyclerView();
        }
        @Override
        public void onFetchStart() {
        }
    };

    public void setRecyclerView(){
        if(isListNull){
            progress.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
            itemlistview.setVisibility(View.GONE);
        }else{
            progress.setVisibility(View.GONE);
            nodata.setVisibility(View.GONE);
            itemlistview.setVisibility(View.VISIBLE);
        }
    }

    public void loadNextPage(){
        Log.d("loadNextPage: ", String.valueOf(currentPage));
        JSONObject jsonBodyObj = new JSONObject();
        try{
            GETAPIRequest getapiRequest=new GETAPIRequest();
            String url = URLs.BASE_URL+URLs.ORDER_LIST_URL+"?page_size="+page_size+"&page="+currentPage;
            Log.i("url", String.valueOf(url));
            //Log.i("Request", String.valueOf(getapiRequest));
            String token = SessionManagement.getUserToken(getActivity());
            HeadersUtil headparam = new HeadersUtil(token);
            getapiRequest.request(getActivity(),nextListener,url,headparam);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    FetchDataListener nextListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject mydata) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (mydata != null) {
                    if (mydata.getInt("error")==0) {
                        ArrayList<OrderModal> tmodalList=new ArrayList<>();
                        JSONArray array = mydata.getJSONArray("data");
                        if(array!=null) {
                            if(array.length()!=0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject orderdetail = (JSONObject) array.get(i);
                                    Log.i("Order List", orderdetail.toString());
                                    OrderModal order = new OrderModal();
                                    order.setOrder_id(orderdetail.getString("_id"));
                                    order.setOrder_price(orderdetail.getString("total"));
                                    order.setOrderDate(orderdetail.getString("createdAt"));
                                    order.setDeliveryCode(orderdetail.getString("deliveryCode"));
                                    String status = orderdetail.getString("status");
                                    if(status.equals("1")){
                                        status = "Pending";
                                    }else if(status.equals("2")){
                                        status = "Delivered";
                                    }else if(status.equals("3")){
                                        status = "Cancelled";
                                    }
                                    order.setStatus(status);
                                    order.setUpdateDate(orderdetail.getString("updatedAt"));
                                    order.setCancel_reason(orderdetail.getString("reason"));
                                    JSONArray items = orderdetail.getJSONArray("items");
                                    ArrayList<ProductsModal> itemList = new ArrayList<>();
                                    for(int j=0;j<itemList.size();j++){
                                        ProductsModal bmod = new ProductsModal();
                                        JSONObject product = items.getJSONObject(j).getJSONObject("product");
                                        bmod.setProductId(product.getString("_id"));
                                        bmod.setName(product.getString("name"));
                                        bmod.setPackaging(product.getString("packaging"));
                                        bmod.setUnit(product.getString("unit"));
                                        bmod.setUnitprice(product.getString("unitPrice"));
                                        bmod.setProductstock(product.getString("stock"));
                                        if(product.getString("available").equals("false")){
                                            bmod.setProductAvailable(false);
                                        }else{
                                            bmod.setProductAvailable(true);
                                        }
                                        bmod.setPurchaseQty(items.getJSONObject(j).getString("qty"));
                                        bmod.setQtyPrice(items.getJSONObject(j).getString("price"));
                                        itemList.add(bmod);
                                    }
                                    order.setOrderItems(itemList);

                                    tmodalList.add(order);
                                }
                            }
                        }
                        Log.d("Order List", mydata.toString());
                        adapter.removeLoadingFooter();
                        isLoading = false;
                        adapter.addAll(tmodalList);
                        if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlert(msg,getActivity());
        }

        @Override
        public void onFetchStart() {
            //RequestQueueService.showProgressDialog(Login.this);
        }

    };


}
