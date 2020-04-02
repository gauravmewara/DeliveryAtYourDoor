package com.collectorate.deliveryatyourdoor.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Adapter.DeliveryItemAdapter;
import com.collectorate.deliveryatyourdoor.Modal.DeliveryOrderModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.FetchDataListener;
import com.collectorate.deliveryatyourdoor.Utils.GETAPIRequest;
import com.collectorate.deliveryatyourdoor.Utils.HeadersUtil;
import com.collectorate.deliveryatyourdoor.Utils.PaginationScrollListener;
import com.collectorate.deliveryatyourdoor.Utils.SessionManagement;
import com.collectorate.deliveryatyourdoor.Utils.SharedPrefUtil;
import com.collectorate.deliveryatyourdoor.Utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */


public class DeliveryPendingFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progreassIndicator;
    TextView noItems;
    Context context;
    private DeliveryItemAdapter mAdapter;
    ArrayList<DeliveryOrderModal> orderList;
    LinearLayoutManager mLayoutManager;
    private final int PAGE_START  = 1;
    private int TOTAL_PAGES = 1;
    private static int page_size = 7;
    private int totaltxnCount;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    boolean isListNull = true;
    String getwardzone, getwardNumber;





    public DeliveryPendingFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_delivery_pending, container, false);


        recyclerView = (RecyclerView)root.findViewById(R.id.rv_fg_itemlist);
        progreassIndicator=(ProgressBar)root.findViewById(R.id.progreassIndicator);
        noItems=(TextView)root.findViewById(R.id.noItems);
        mAdapter = new DeliveryItemAdapter(context,getActivity(), Constants.ITEM_DETAILS);

        getwardzone=SharedPrefUtil.getStringPreferences(getActivity(),Constants.SHARED_PREF_WARDZONE_TAG,Constants.SHARED_WARDZONE_KEY);
        getwardNumber=SharedPrefUtil.getStringPreferences(getActivity(),Constants.SHARED_PREF_WARDZONE_TAG,Constants.SHARED_WARDZONE_KEY);


        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
          //              loadNextPage();
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


        //requestlistApiCalling();
        return root;
    }

    public void setRecyclerView(){
        if(isListNull){
            progreassIndicator.setVisibility(View.GONE);
            noItems.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            progreassIndicator.setVisibility(View.GONE);
            noItems.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

   /* private void requestlistApiCalling(){
        JSONObject jsonBodyObj = new JSONObject();
        try{
            GETAPIRequest getapiRequest=new GETAPIRequest();
            String url = URLs.BASE_URL+URLs.ORDER_FILTER;
            *//*+"?page_size="+String.valueOf(page_size)+"&page="+String.valueOf(PAGE_START);*//*
            jsonBodyObj.put("listType",Constants.PENDING);

            Log.i("url", String.valueOf(url));
            Log.i("Request", String.valueOf(getapiRequest));
            *//*String token = SessionManagement.getUserToken(getContext());*//*
            String token = Constants.Token;
            HeadersUtil headparam = new HeadersUtil(token);
            getapiRequest.request(getActivity().getApplicationContext(),fetchListener,url,headparam,jsonBodyObj);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    FetchDataListener fetchListener=new FetchDataListener() {
        @Override
        public void onFetchComplete(JSONObject data) {
            //RequestQueueService.cancelProgressDialog();
            try {
                if (data != null) {
                    if (data.getInt("error")==0){
                        orderList=new ArrayList<>();
                        JSONArray array = data.getJSONArray("data");
                        if (array!=null){
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = (JSONObject) array.get(i);

                            }

                            isListNull = false;
                        }



                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onFetchFailure(String msg) {

        }

        @Override
        public void onFetchStart() {

        }
    };
*/

}
