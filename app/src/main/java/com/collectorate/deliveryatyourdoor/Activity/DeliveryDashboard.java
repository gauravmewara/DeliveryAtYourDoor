package com.collectorate.deliveryatyourdoor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.collectorate.deliveryatyourdoor.Adapter.DeliveryViewPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Fragment.DeliveryCancelledFragment;
import com.collectorate.deliveryatyourdoor.Fragment.DeliveryCompletedFragment;
import com.collectorate.deliveryatyourdoor.Fragment.DeliveryPendingFragment;
import com.collectorate.deliveryatyourdoor.R;

public class DeliveryDashboard extends AppCompatActivity implements View.OnClickListener {

   ViewPager viewPager;
   static FragmentManager fragmentManager;
   DeliveryViewPagerAdapter adapter;
   Bundle b;
   RelativeLayout menu,filter;
   Spinner orderIdSpinner;
   EditText searchText;
   Button btn_search,pending,completed,cancelled;
    TextView pageTitle,wardNo,totalOrders,orderValues,totalAmount,amountValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_dashboard);
        initViews();
    }
    public void initViews(){
       menu =(RelativeLayout) findViewById(R.id.rl_toolbar_menu);
       pageTitle=(TextView) findViewById(R.id.tv_toolbar_heading);
        pageTitle.setText("DashBoard");
       //filter=(RelativeLayout) findViewById(R.id.rl_toolbar_notification_view);
       wardNo=(TextView) findViewById(R.id.tv_dashboardHead);
        totalOrders=(TextView) findViewById(R.id.tv_totalOrders);
        orderValues=(TextView) findViewById(R.id.tv_totalorders_value);
        totalAmount=(TextView) findViewById(R.id.tv_totalAmounts);
        amountValues=(TextView)findViewById(R.id.tv_totalAmounts_value);
        orderIdSpinner=(Spinner)findViewById(R.id.orderIdSpinner);
        searchText=(EditText)findViewById(R.id.et_search);
        btn_search=(Button)findViewById(R.id.dashbordBtn_search);
        btn_search.setOnClickListener(this);
        viewPager=(ViewPager)findViewById(R.id.vp_dashboard);
        pending=(Button)findViewById(R.id.btn_pending);
        pending.setOnClickListener(this);
        completed=(Button)findViewById(R.id.btn_complete);
        completed.setOnClickListener(this);
        cancelled=(Button)findViewById(R.id.btn_cancelled);
        cancelled.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        setupViewPager(viewPager);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i ;
                switch (view.getId()){
                    case R.id.rl_toolbar_menu:
                        Log.i("profileclicked","profile_menu_image_clicked()");
                        PopupMenu popupMenu = new PopupMenu(DeliveryDashboard.this,view);
                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.delivery_profile_menu,popupMenu.getMenu());
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent i;
                                switch (item.getItemId()) {
                                    case R.id.mi_selectward:
                                        /*i = new Intent(DeliverySelectYourWard.this, ChangePassword.class);
                                        startActivity(i);*/
                                        return true;
                                    case R.id.mi_dashbord:
                                        /*i = new Intent(DeliverySelectYourWard.this, EditProfile.class);
                                        startActivityForResult(i, 2);*/
                                        return true;

                                    case R.id.mi_logout:
                                        /*i = new Intent(DeliverySelectYourWard.this,getApplicationContext(), EditProfile.class);
                                        startActivityForResult(i, 2);*/
                                        return true;
                                    default:
                                        return onOptionsItemSelected(item);
                                }
                            }
                        });
                        break;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                //highLightCurrentTab(position);

                //pagetitle.setText(tabTitle[position]);
                if(position==0){
                    pending.setBackground(getResources().getDrawable( R.drawable.delivery_bg_tab_bar_selected));
                    completed.setBackground(getResources().getDrawable(android.R.color.transparent));
                    cancelled.setBackground(getResources().getDrawable(android.R.color.transparent));

                }else if (position==1){
                    completed.setBackground(getResources().getDrawable( R.drawable.delivery_bg_tab_bar_selected));
                    cancelled.setBackground(getResources().getDrawable(android.R.color.transparent));
                    pending.setBackground(getResources().getDrawable(android.R.color.transparent));

                }else {
                    cancelled.setBackground(getResources().getDrawable( R.drawable.delivery_bg_tab_bar_selected));
                    pending.setBackground(getResources().getDrawable(android.R.color.transparent));
                    completed.setBackground(getResources().getDrawable(android.R.color.transparent));

                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setCurrentTab();



    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.btn_pending:
                viewPager.setCurrentItem(0);
                break;
            case R.id.btn_complete:
                viewPager.setCurrentItem(1);
                break;
            case R.id.btn_cancelled:
                viewPager.setCurrentItem(2);
                break;
            case R.id.dashbordBtn_search:
                i=new Intent(DeliveryDashboard.this, DeliveryOrderSummary.class);
                startActivity(i);
                break;
        }



    }
    private void setCurrentTab(){
        b= getIntent().getExtras();
        if(b != null){
            int currentTab = b.getInt("curretTab", 0);
            viewPager.setCurrentItem(currentTab);
            if(currentTab==0){


                pending.setBackground(getResources().getDrawable( R.drawable.delivery_bg_tab_bar_selected));
                completed.setBackground(getResources().getDrawable(android.R.color.transparent));
                cancelled.setBackground(getResources().getDrawable(android.R.color.transparent));

            }else if (currentTab==1){
                completed.setBackground(getResources().getDrawable( R.drawable.delivery_bg_tab_bar_selected));
                pending.setBackground(getResources().getDrawable(android.R.color.transparent));
                cancelled.setBackground(getResources().getDrawable(android.R.color.transparent));

            }else {
                cancelled.setBackground(getResources().getDrawable(R.drawable.delivery_bg_tab_bar_selected));
                pending.setBackground(getResources().getDrawable(android.R.color.transparent));
                completed.setBackground(getResources().getDrawable(android.R.color.transparent));
            }
        } else {
            pending.setBackground(getResources().getDrawable( R.drawable.delivery_bg_tab_bar_selected));
            completed.setBackground(getResources().getDrawable(android.R.color.transparent));
            cancelled.setBackground(getResources().getDrawable(android.R.color.transparent));

        }
    }






    private  void setupViewPager(ViewPager viewPager){
        adapter = new DeliveryViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DeliveryPendingFragment(DeliveryDashboard.this));
        adapter.addFragment(new DeliveryCompletedFragment(DeliveryDashboard.this));
        adapter.addFragment(new DeliveryCancelledFragment(DeliveryDashboard.this));
        viewPager.setAdapter(adapter);

    }






}
