package com.collectorate.deliveryatyourdoor.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collectorate.deliveryatyourdoor.Activity.Cart;
import com.collectorate.deliveryatyourdoor.Adapter.ProductAdapter;
import com.collectorate.deliveryatyourdoor.Interface.ItemUpdateListener;
import com.collectorate.deliveryatyourdoor.Modal.CartedProductModal;
import com.collectorate.deliveryatyourdoor.Modal.ProductsModal;
import com.collectorate.deliveryatyourdoor.R;
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

public class OrderPlaceFragment extends Fragment implements View.OnClickListener, ItemUpdateListener {
    View view;
    RelativeLayout cartbtn;
    TextView totalprice,nodata;
    ProgressBar progress;
    private final int PAGE_START  = 1;
    private int TOTAL_PAGES = 1;
    private static int page_size = 15;
    RecyclerView itemlistview;
    ProductAdapter adapter;
    HashMap<String,ProductsModal> cartMap;
    float orderPrice=0;
    ArrayList<ProductsModal> cartitems;
    ArrayList<ProductsModal> cartedItems;
    ArrayList<CartedProductModal> cartApiData;
    LinearLayoutManager mLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private int totalBookingCount;
    boolean isListNull = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_place_order,container,false);
        cartbtn = (RelativeLayout)view.findViewById(R.id.rl_fg_placeorder_cart);
        cartbtn.setOnClickListener(this);
        totalprice = (TextView)view.findViewById(R.id.tv_fgplaceorder_cart_rs);
        itemlistview = (RecyclerView)view.findViewById(R.id.rv_fg_placeorder_list);
        nodata = (TextView)view.findViewById(R.id.tv_placeorder_nodata);
        progress = (ProgressBar)view.findViewById(R.id.pg_placeorder);
        nodata.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        totalprice.setText("0");

        adapter = new ProductAdapter(getContext(),this);
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
        loadProductData();
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.rl_fg_placeorder_cart:
                if(orderPrice>0){
                    if(cartMap!=null){
                        if(cartMap.size()>0){
                            if(cartedItems==null)
                                cartedItems =  new ArrayList<>();
                            if(cartApiData==null)
                                cartApiData = new ArrayList<>();
                            cartApiData.clear();
                            cartedItems.clear();
                            for (ProductsModal temp:cartMap.values()) {
                                int num = Integer.parseInt(temp.getPurchaseQty());
                                CartedProductModal cpm = new CartedProductModal();
                                cpm.setProductId(temp.getProductId());
                                cpm.setQty(temp.getPurchaseQty());
                                cartApiData.add(cpm);
                                for(int i=0;i<num;i++){
                                    cartedItems.add(temp);
                                }
                            }
                            intent = new Intent(getActivity(), Cart.class);
                            intent.putExtra("cartitems",cartedItems);
                            intent.putExtra("cartamount",String.valueOf(orderPrice));
                            intent.putExtra("cartapidata",cartApiData);
                            startActivity(intent);

                        }
                    }
                }
                break;
        }
    }


    public void loadProductData(){
        try{
            GETAPIRequest getapiRequest=new GETAPIRequest();
            String url = URLs.BASE_URL+ URLs.PRODUCT_LIST_URL+"?page_size="+String.valueOf(page_size)+"&page=1";
            Log.i("url", String.valueOf(url));
            String token = SessionManagement.getUserToken(getActivity());
            HeadersUtil headparam = new HeadersUtil(token);
            getapiRequest.request(getActivity(),loadProductListener,url,headparam);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    FetchDataListener loadProductListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject response) {
            try {
                if (response != null) {
                    if (response.getInt("error")==0) {
                        ArrayList<ProductsModal> tmodalList=new ArrayList<>();
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
                                    JSONObject jsonObject = (JSONObject) array.get(i);
                                    Log.i("Order List", jsonObject.toString());
                                    ProductsModal bmod = new ProductsModal();
                                    bmod.setProductId(jsonObject.getString("_id"));
                                    bmod.setName(jsonObject.getString("name"));
                                    bmod.setPackaging(jsonObject.getString("packaging"));
                                    bmod.setUnit(jsonObject.getString("unit"));
                                    bmod.setUnitprice(jsonObject.getString("unitPrice"));
                                    bmod.setProductstock(jsonObject.getString("stock"));
                                    if(jsonObject.getString("available").equals("false")){
                                        bmod.setProductAvailable(false);
                                    }else{
                                        bmod.setProductAvailable(true);
                                    }
                                    tmodalList.add(bmod);
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
            String url = URLs.BASE_URL+URLs.PRODUCT_LIST_URL+"?page_size="+page_size+"&page="+currentPage;
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
                        ArrayList<ProductsModal> tmodalList=new ArrayList<>();
                        JSONArray array = mydata.getJSONArray("data");
                        if(array!=null) {
                            if(array.length()!=0) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) array.get(i);
                                    Log.i("Order List", jsonObject.toString());
                                    ProductsModal bmod = new ProductsModal();
                                    bmod.setProductId(jsonObject.getString("_id"));
                                    bmod.setName(jsonObject.getString("name"));
                                    bmod.setPackaging(jsonObject.getString("packaging"));
                                    bmod.setUnit(jsonObject.getString("unit"));
                                    bmod.setUnitprice(jsonObject.getString("unitPrice"));
                                    bmod.setProductstock(jsonObject.getString("stock"));
                                    if(jsonObject.getString("available").equals("false")){
                                        bmod.setProductAvailable(false);
                                    }else{
                                        bmod.setProductAvailable(true);
                                    }
                                    tmodalList.add(bmod);
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

    @Override
    public void onItemUpdate(Object... values) {
        Float price;
        String process;
        price = (float)values[0];
        process = (String)values[1] ;
        cartMap = (HashMap<String, ProductsModal>) values[2];
        if(process.equals("add")){
            orderPrice = orderPrice+price;
        }else{
            if(orderPrice!=0){
                orderPrice-=price;
            }
        }
        totalprice.setText(String.valueOf(orderPrice));
    }
}
