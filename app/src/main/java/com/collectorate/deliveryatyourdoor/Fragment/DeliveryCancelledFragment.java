package com.collectorate.deliveryatyourdoor.Fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Adapter.DeliveryItemAdapter;
import com.collectorate.deliveryatyourdoor.Modal.DeliveryOrderModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.collectorate.deliveryatyourdoor.Utils.PaginationScrollListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryCancelledFragment extends Fragment {



    RecyclerView recyclerView;
    ProgressBar progreassIndicator;
    TextView noItems;
    Context context;
    private DeliveryItemAdapter mAdapter;
    ArrayList<DeliveryOrderModal> requestlist;
    LinearLayoutManager mLayoutManager;
    private final int PAGE_START  = 1;
    private int TOTAL_PAGES = 1;
    private static int page_size = 7;
    private int totaltxnCount;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    boolean isListNull = true;


    public DeliveryCancelledFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_delivery_cancelled, container, false);

        recyclerView = (RecyclerView)root.findViewById(R.id.rv_fg_itemlist);
        progreassIndicator=(ProgressBar)root.findViewById(R.id.progreassIndicator);
        noItems=(TextView)root.findViewById(R.id.noItems);
        mAdapter = new DeliveryItemAdapter(context,getActivity(), Constants.ITEM_DETAILS);

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
}
