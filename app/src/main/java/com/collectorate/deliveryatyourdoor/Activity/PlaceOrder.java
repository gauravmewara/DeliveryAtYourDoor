package com.collectorate.deliveryatyourdoor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.collectorate.deliveryatyourdoor.Fragment.ChangePassword;
import com.collectorate.deliveryatyourdoor.Fragment.OrderHistory;
import com.collectorate.deliveryatyourdoor.Fragment.OrderPlaceFragment;
import com.collectorate.deliveryatyourdoor.Fragment.Profile;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.POSTAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceOrder extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout toolbar_notification,noticountlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    RelativeLayout toolbar_toggle,logout;
    RelativeLayout homemenu,ordermenu,profilemenu,changepwdmenu,logoutmenu;
    FrameLayout fragmentPages;
    FragmentManager fm;
    FragmentTransaction ft;
    Fragment placeorder,orders,profile,chngpwd;
    TextView name,location,notiCount,pagetitle;
    DrawerLayout navdrawer;
    static String notificationCount;
    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        navdrawer = (DrawerLayout)findViewById(R.id.nav_drawer_mainpage);
        toolbar_toggle = (RelativeLayout) findViewById(R.id.rl_toolbar_menu);
        toolbar_notification = (RelativeLayout) findViewById(R.id.rl_toolbar_notification_view);
        toolbar_notification.setOnClickListener(this);
        noticountlayout = (RelativeLayout)findViewById(R.id.rl_toolbar_notificationcount);
        notiCount = (TextView)findViewById(R.id.tv_toolbar_notificationcount);
        fragmentPages = (FrameLayout)findViewById(R.id.fl_placeorder_content);
        pagetitle=(TextView)findViewById(R.id.tv_toolbar_heading);
        pagetitle.setText("Place Order");
        //homemenu = (RelativeLayout)findViewById(R.id.rl_nav_home);
        homemenu.setOnClickListener(this);
       // ordermenu = (RelativeLayout)findViewById(R.id.rl_nav_order);
        ordermenu.setOnClickListener(this);
        profilemenu = (RelativeLayout)findViewById(R.id.rl_nav_profile);
        profilemenu.setOnClickListener(this);
        changepwdmenu = (RelativeLayout)findViewById(R.id.rl_nav_changepwd);
        changepwdmenu.setOnClickListener(this);
        logoutmenu = (RelativeLayout)findViewById(R.id.rl_nav_logout);
        logoutmenu.setOnClickListener(this);
        context = this;
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, navdrawer,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        placeorder =  new OrderPlaceFragment();
        orders = new OrderHistory();
        profile = new Profile();
        chngpwd = new ChangePassword();
        logout = (RelativeLayout)findViewById(R.id.rl_nav_logout);
        logout.setOnClickListener(this);
        context = this;
        navdrawer.setScrimColor(Color.TRANSPARENT);
        navdrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navdrawer.setDrawerElevation(0f);
        toolbar_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerMenu(view);
            }
        });
        loadFragment(placeorder);
    }

    @Override
    public void onClick(View view) {
        navdrawer.closeDrawers();
        switch(view.getId()) {
            /*case R.id.rl_nav_home:
                loadFragment(placeorder);
                pagetitle.setText("Place Order");
                break;
            case R.id.rl_nav_order:
                loadFragment(orders);
                pagetitle.setText("Order History");
                break;*/
            case R.id.rl_nav_profile:
                loadFragment(profile);
                pagetitle.setText("Profile");
                break;
            case R.id.rl_nav_changepwd:
                loadFragment(chngpwd);
                pagetitle.setText("Change Password");
                break;
            case R.id.rl_nav_logout:
                logout.setClickable(false);
                logoutApiCalling();
                break;
        }
    }
    public void drawerMenu (View view ){
        navdrawer.openDrawer(Gravity.LEFT);
    }

    public void logoutApiCalling(){
        try {
            POSTAPIRequest getapiRequest = new POSTAPIRequest();
            String url = URLs.BASE_URL + URLs.SIGN_OUT_URL;
            Log.i("url", String.valueOf(url));
            Log.i("Request", String.valueOf(getapiRequest));
            //String token = FirebaseInstanceId.getInstance().getToken();
            String token = SessionManagement.getUserToken(this);
            Log.i("Token:",token);
            HeadersUtil headparam = new HeadersUtil(token);
            getapiRequest.request(PlaceOrder.this,logoutListener,url,headparam);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    FetchDataListener logoutListener = new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            try {
                if (data != null) {
                    if (data.getInt("error") == 0) {
                        //FirebaseAuth.getInstance().signOut();
                        SessionManagement.logout(logoutListener, PlaceOrder.this);
                        Intent i = new Intent(PlaceOrder.this, Login.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        Toast.makeText(PlaceOrder.this, "You are now logout", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {
            logout.setClickable(true);
        }

        @Override
        public void onFetchStart() {

        }
    };

    private void loadFragment(Fragment fragment) {
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_placeorder_content, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Constants.SUMMARY_CANCEL_REQUEST_CODE) {
                if (getSupportFragmentManager().findFragmentById(R.id.fl_placeorder_content) instanceof OrderHistory)
                    orders.onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(!(getSupportFragmentManager().findFragmentById(R.id.fl_placeorder_content) instanceof OrderPlaceFragment)){
            loadFragment(placeorder);
        }else{
            showExitAlert();
        }
    }

    public void showExitAlert() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(PlaceOrder.this);
            builder.setTitle("Exit");
            builder.setMessage("Do you want to exit Application?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
