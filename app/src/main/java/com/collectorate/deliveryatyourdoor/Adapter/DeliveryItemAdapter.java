package com.collectorate.deliveryatyourdoor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collectorate.deliveryatyourdoor.Modal.DeliveryOrderModal;
import com.collectorate.deliveryatyourdoor.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class DeliveryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<DeliveryOrderModal> orderlist;
    FragmentActivity activity;
    private String init_type;
    private static final int ITEM=0;
    private static final int LOADING=1;
    private boolean isLoadingAdded = false;


    public DeliveryItemAdapter(Context context, FragmentActivity activity, String init_type) {
        this.context = context;
        this.activity = activity;
        this.init_type = init_type;
        this.orderlist = new ArrayList<DeliveryOrderModal>();

    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bookingid, distance, fromlocation, fromtime, tolocation, totime, bookingactiontext,fromaddress,toaddress;
        RelativeLayout bookingview;
        ConstraintLayout itemlayout;

        public OrderViewHolder(View view) {

            super(view);
           /* bookingid = (TextView) view.findViewById(R.id.tv_bookingitem_bookingid);
            distance = (TextView) view.findViewById(R.id.tv_bookingitem_distance);
            fromaddress = (TextView) view.findViewById(R.id.tv_bookingitem_fromlocation);
            fromtime = (TextView) view.findViewById(R.id.tv_bookingitem_fromtime);
            toaddress = (TextView) view.findViewById(R.id.tv_bookingitem_tolocation);
            totime = (TextView) view.findViewById(R.id.tv_bookingitem_totime);
            bookingactiontext=(TextView)view.findViewById(R.id.tv_bookingitem_viewaction) ;
            *//*itemlayout = (ConstraintLayout)view.findViewById(R.id.cl_triplist_itemlayout);
            itemlayout.setOnClickListener(this);*//*
            bookingview = (RelativeLayout) view.findViewById(R.id.rl_bookingitem_view);

            bookingview.setOnClickListener(this);
            bookingactiontext.setOnClickListener(this);*/
        }

        @Override
        public void onClick(View view) {
          /*  switch (view.getId()) {

                case R.id.tv_bookingitem_viewaction:
                    Intent i = new Intent(context, RequestDetails.class);
                    i.putExtra("init_type", init_type);
                    context.startActivity(i);
                    break;
            }*/
        }
    }

    @Override
    public int getItemCount() {
        return orderlist==null?0:orderlist.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM:
                viewHolder = new OrderViewHolder(inflater.inflate(R.layout.single_delivery_order_item,parent,false));
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
        final DeliveryOrderModal current = orderlist.get(position);

        switch (getItemViewType(position)){


            /*case ITEM:
                final BookingViewHolder mVH = (BookingViewHolder)holder;
                mVH.bookingid.setText(orderlist.get(position).getBookingid());
                mVH.distance.setText(orderlist.get(position).getDistance());
                mVH.fromtime.setText(orderlist.get(position).getFromtime());
                mVH.totime.setText(orderlist.get(position).getTotime());
                mVH.fromaddress.setText(orderlist.get(position).getFromaddress());
                mVH.toaddress.setText(orderlist.get(position).getToaddress());
                mVH.bookingactiontext.setOnClickListener(new View.OnClickListener() {
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
        return (position==orderlist.size()-1&&isLoadingAdded)?LOADING:ITEM ;
    }


    public void add(DeliveryOrderModal r) {
        orderlist.add(r);
        notifyItemInserted(orderlist.size() - 1);
    }
    public void addAll(List<DeliveryOrderModal> moveResults) {
        for (DeliveryOrderModal result : moveResults) {
            add(result);
        }
    }

    public void remove(DeliveryOrderModal r){
        int position = orderlist.indexOf(r);
        if(position>-1){
            orderlist.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void clearAll(){
        orderlist.clear();
        notifyDataSetChanged();
    }



    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new DeliveryOrderModal());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = orderlist.size() - 1;
        DeliveryOrderModal result = getItem(position);
        if (result != null) {
            orderlist.remove(position);
            notifyItemRemoved(position);
        }
    }

    public DeliveryOrderModal getItem(int position) {
        return orderlist.get(position);
    }



}


