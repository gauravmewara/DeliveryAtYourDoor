package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.R;

public class Login extends AppCompatActivity {

     TextView signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = findViewById(R.id.tv_login_loginttbn);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,DeliverySelectYourWard.class);
                startActivity(i);

            }
        });
    }
}
