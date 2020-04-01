package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Adapter.CartItemAdapter;
import com.collectorate.deliveryatyourdoor.Interface.CartItemRemoveListener;
import com.collectorate.deliveryatyourdoor.Modal.CartedProductModal;
import com.collectorate.deliveryatyourdoor.Modal.OrderModal;
import com.collectorate.deliveryatyourdoor.Modal.ProductsModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.POSTAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.RequestQueueService;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity implements View.OnClickListener, CartItemRemoveListener {
    RelativeLayout menuback,menunotification;
    TextView totalamounttext,pagetitle,placeorder;
    RecyclerView cartlistview;
    ArrayList<ProductsModal> cartItemList;
    HashMap<String,String> mapApiData;
    ArrayList<CartedProductModal> cartApiData;
    CartItemAdapter adapter;
    float totalamount;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Bundle b = getIntent().getExtras();
        cartItemList = (ArrayList<ProductsModal>) b.getSerializable("cartitems");
        totalamount = Float.parseFloat(b.getString("cartamount"));
        cartApiData = (ArrayList<CartedProductModal>)b.getSerializable("cartapidata");
        pagetitle = (TextView)findViewById(R.id.tv_toolbar2_heading);
        pagetitle.setText("Cart");
        totalamounttext = (TextView)findViewById(R.id.tv_cart_total_amt);
        totalamounttext.setText(String.valueOf(totalamount));
        menuback = (RelativeLayout)findViewById(R.id.rl_toolbar2_menu);
        menuback.setOnClickListener(this);
        menunotification = (RelativeLayout)findViewById(R.id.rl_toolbar2_notification_view);
        menunotification.setOnClickListener(this);
        placeorder = (TextView)findViewById(R.id.tv_cart_actionbtn);
        placeorder.setOnClickListener(this);
        cartlistview = (RecyclerView)findViewById(R.id.rv_cart_selecteditems);
        layoutManager = new LinearLayoutManager(this);
        cartlistview.setLayoutManager(layoutManager);
        adapter = new CartItemAdapter(this,cartItemList, Constants.CART_INIT);
        cartlistview.setAdapter(adapter);
        if(mapApiData==null)
           mapApiData= new HashMap<>();
        for (CartedProductModal temp:cartApiData) {
            mapApiData.put(temp.getProductId(),temp.getQty());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_toolbar2_menu:
                onBackPressed();
                break;
            case R.id.rl_toolbar2_notification_view:
                break;
            case R.id.tv_cart_actionbtn:
                placeOrderApiCall();
                placeorder.setClickable(false);
                break;
        }
    }

    @Override
    public void onItemRemoved(Object... values) {
        ProductsModal item = (ProductsModal)values[0];
        int qty = Integer.parseInt(mapApiData.get(item.getProductId()));
        qty--;
        mapApiData.remove(item.getProductId());
        if(qty>0){
            mapApiData.put(item.getProductId(),String.valueOf(qty));
        }
        cartItemList.remove(values[1]);
        totalamount = totalamount-Float.parseFloat(item.getUnitprice());
        totalamounttext.setText(String.valueOf(totalamount));

    }

    public void placeOrderApiCall(){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            JSONArray jsonArray = new JSONArray();
            for (String key:mapApiData.keySet()) {
                JSONObject single = new JSONObject();
                single.put("productId",key);
                single.put("qty",mapApiData.get(key));
                jsonArray.put(single);
            }
            jsonBodyObj.put("cart",jsonArray);
            POSTAPIRequest postapiRequest=new POSTAPIRequest();
            String url = URLs.BASE_URL+ URLs.PLACE_ORDER_URL;
            Log.i("url",String.valueOf(url));
            String token = SessionManagement.getUserToken(Cart.this);
            HeadersUtil headparam = new HeadersUtil(token);
            postapiRequest.request(this, placeOrderListener,url,headparam,jsonBodyObj);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    FetchDataListener placeOrderListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    if (data.getInt("error") == 0) {
                        //Log.i("Login", "Login Successfull");
                        JSONObject orderdetail = data.getJSONObject("data");
                        if (orderdetail != null) {
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

                            for(int i=0;i<itemList.size();i++){
                                ProductsModal bmod = new ProductsModal();
                                JSONObject product = items.getJSONObject(i).getJSONObject("product");
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
                                bmod.setPurchaseQty(items.getJSONObject(i).getString("qty"));
                                bmod.setQtyPrice(items.getJSONObject(i).getString("price"));
                                itemList.add(bmod);
                            }
                            order.setOrderItems(itemList);
                            Intent i = new Intent(Cart.this, OrderConfirmation.class);
                            i.putExtra("order",order);
                            startActivity(i);
                            finish();
                        } else {
                            RequestQueueService.showAlert("Error! No data fetched", Cart.this);
                            placeorder.setClickable(true);
                        }
                    }
                } else {
                    RequestQueueService.showAlert("Error! No data fetched", Cart.this);
                    placeorder.setClickable(true);
                }
            } catch (Exception e) {
                RequestQueueService.showAlert("Something went wrong", Cart.this);
                placeorder.setClickable(true);
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            //RequestQueueService.cancelProgressDialog();
            RequestQueueService.showAlert(msg, Cart.this);
            placeorder.setClickable(true);
        }

        @Override
        public void onFetchStart() {
            //RequestQueueService.showProgressDialog(Login.this);
        }
    };
}
