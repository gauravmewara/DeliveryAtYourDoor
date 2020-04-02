package com.collectorate.deliveryatyourdoor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Activity.DeliveryOrderSummary;
import com.collectorate.deliveryatyourdoor.Modal.DeliveryProductsModal;
import com.collectorate.deliveryatyourdoor.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    BottomSheetDialog abortsheet;
    private List<DeliveryProductsModal> productList;
    DeliveryOrderSummary activity;
    private String init_type;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;




    public DeliveryOrderAdapter(Context context, DeliveryOrderSummary activity, String init_type) {
        this.context = context;
        this.activity = activity;
        this.init_type = init_type;
        this.productList = new ArrayList<DeliveryProductsModal>();
    }
    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }



    public class OrderSummaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bookingid,distance,fromlocation,fromtime,tolocation,totime,bookingactiontext,from_address,to_address;
        RelativeLayout ongoingView,ongoingAbort,bookingview;
        LinearLayout ongoingaction;
        ConstraintLayout itemlayout;

        public OrderSummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            /*bookingid = (TextView)view.findViewById(R.id.tv_trip_details_bookingid);
            distance = (TextView)view.findViewById(R.id.tv_trip_details_distance1);
            from_address = (TextView)view.findViewById(R.id.tv_trip_details_fromlocation);
            fromtime=(TextView)view.findViewById(R.id.tv_currentDay);

            to_address = (TextView)view.findViewById(R.id.tv_trip_details_tolocation);
            totime = (TextView)view.findViewById(R.id.tv_trip_details_totime);
            itemlayout = (ConstraintLayout)view.findViewById(R.id.cl_triplist_itemlayout);

            bookingactiontext=(TextView)view.findViewById(R.id.tv_bookingitem_viewaction) ;
            bookingview = (RelativeLayout) view.findViewById(R.id.rl_bookingitem_view);*/
        }

        @Override
        public void onClick(View view) {

        }




//            bookingview.setOnClickListener(this);
            //          bookingactiontext.setOnClickListener(this);



        }

    @Override
    public int getItemCount() {
        return productList==null?0:productList  .size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM:
                viewHolder = new OrderSummaryViewHolder(inflater.inflate(R.layout.delivery_product_single_item,parent,false));
                break;
            case LOADING:
                View v = inflater.inflate(R.layout.delivery_progress_indicator,parent,false);
                viewHolder = new LoadingViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final DeliveryProductsModal current = productList.get(position);


        switch (getItemViewType(position)) {



           /* case ITEM:
                final TripDetViewHolder mVH = (TripDetViewHolder) holder;
                mVH.bookingid.setText(tripList.get(position).getBookingid());
                mVH.distance.setText(tripList.get(position).getDistance());
                mVH.fromtime.setText(tripList.get(position).getFromtime());
                mVH.totime.setText(tripList.get(position).getTotime());
                mVH.from_address.setText(tripList.get(position).getFromlocation());
                mVH.to_address.setText(tripList.get(position).getTolocation());
                mVH.itemlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, RequestDetails.class);
                        i.putExtra("init_type", init_type);
                        i.putExtra("booking_id", current.getBookingid());
                        context.startActivity(i);
                    }
                });
                break;
            case LOADING:
                break;*/
        }
    }
    @Override
    public int getItemViewType(int position) {
        return (position==productList.size()-1&&isLoadingAdded)?LOADING:ITEM ;
    }
    public void add(DeliveryProductsModal r) {
        productList.add(r);
        notifyItemInserted(productList.size() - 1);
    }


    public void addAll(List<DeliveryProductsModal> moveResults) {
        for (DeliveryProductsModal result : moveResults) {
            add(result);
        }
    }

    public void remove(DeliveryProductsModal r){
        int position = productList.indexOf(r);
        if(position>-1){
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new DeliveryProductsModal());
    }
    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = productList.size() - 1;
        DeliveryProductsModal result = getItem(position);
        if (result != null) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public DeliveryProductsModal getItem(int position) {
        return productList.get(position);
    }

}







