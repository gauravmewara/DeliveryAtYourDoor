package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Modal.OrderModal;
import com.collectorate.deliveryatyourdoor.R;

public class OrderConfirmation extends AppCompatActivity {
    TextView pagetitle,deliverycode,orderid,orderdate,orderamt,backtohome;
    OrderModal order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        order = (OrderModal) getIntent().getSerializableExtra("order");
        backtohome = (TextView) findViewById(R.id.tv_orderconfirm_submit);
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderConfirmation.this, PlaceOrder.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        deliverycode = (TextView)findViewById(R.id.tv_orderconfirm_deliverycode);
        deliverycode.setText(order.getDeliveryCode());
        orderid = (TextView)findViewById(R.id.tv_orderconfirm_orderid);
        orderid.setText(order.getOrder_id());
        orderdate = (TextView)findViewById(R.id.tv_orderconfirm_orderdate);
        orderdate.setText(order.getOrderDate());
        orderamt = (TextView)findViewById(R.id.tv_orderconfirm_orderamt);
        orderamt.setText("Rs "+order.getOrder_price());
    }

}
