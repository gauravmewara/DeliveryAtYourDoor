package com.collectorate.deliveryatyourdoor.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.collectorate.deliveryatyourdoor.Activity.OrderSummary;
import com.collectorate.deliveryatyourdoor.Fragment.OrderHistory;
import com.collectorate.deliveryatyourdoor.Interface.OrderCancelListener;
import com.collectorate.deliveryatyourdoor.Modal.OrderModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<OrderModal> orderlist;
    Activity activity;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public OrderAdapter(Context context, Activity activity){
        this.context=context;
        orderlist = new ArrayList<>();
        this.activity = activity;
    }


    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView orderid,orderdate,orderprice,orderstatus;
        RelativeLayout orderstatuslayout,orderitemlayout;
        public OrderViewHolder(View view){
            super(view);
            orderid = (TextView)view.findViewById(R.id.tv_adapter_order_orderid);
            orderdate = (TextView)view.findViewById(R.id.tv_adapter_order_orderdate);
            orderprice = (TextView)view.findViewById(R.id.tv_adapter_order_orderprice);
            orderstatus = (TextView)view.findViewById(R.id.tv_adapter_order_orderstatus);
            orderstatuslayout = (RelativeLayout) view.findViewById(R.id.rl_adapter_order_orderstatuslayout);
            orderitemlayout = (RelativeLayout) view.findViewById(R.id.rl_adapter_order_mainlayout);
            orderitemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderSummary.class);
                    intent.putExtra("order",orderlist.get(getAdapterPosition()));
                    intent.putExtra("position",getAdapterPosition());
                    activity.startActivityForResult(intent,Constants.SUMMARY_CANCEL_REQUEST_CODE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderlist == null ? 0 : orderlist.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = new OrderViewHolder(inflater.inflate(R.layout.adapter_order_item,parent,false));
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case ITEM:
                final OrderViewHolder mVH = (OrderViewHolder) holder;
                mVH.orderid.setText(orderlist.get(position).getOrder_id());
                mVH.orderdate.setText(orderlist.get(position).getOrderDate());
                mVH.orderprice.setText(orderlist.get(position).getOrder_price()+" Rs");
                String status = orderlist.get(position).getStatus();
                if(status.equals("Pending")){
                    mVH.orderstatus.setText("Pending");
                    mVH.orderstatuslayout.setBackground(context.getDrawable( R.drawable.statusbackgroundblue));
                }else if(status.equals("Delivered")){
                    mVH.orderstatus.setText("Delivered");
                    mVH.orderstatuslayout.setBackground(context.getDrawable( R.drawable.statusbackgroundgreen));
                }else if(status.equals("Cancelled")){
                    mVH.orderstatus.setText("Cancelled");
                    mVH.orderstatuslayout.setBackground(context.getDrawable( R.drawable.statusbackgroundred));
                }
                //mVH.orderstatuslayout.setBackground(context.getResources( R.color.colorBlack));
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == orderlist.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(OrderModal r) {
        orderlist.add(r);
        notifyItemInserted(orderlist.size() - 1);
    }

    public void addAll(List<OrderModal> moveResults) {
        for (OrderModal result : moveResults) {
            add(result);
        }
    }

    public void remove(OrderModal r) {
        int position = orderlist.indexOf(r);
        if (position > -1) {
            orderlist.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new OrderModal());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = orderlist.size() - 1;
        OrderModal result = getItem(position);
        if (result != null) {
            orderlist.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void upadteList(int position,OrderModal neword){

            orderlist.remove(position);
            orderlist.add(position,neword);
            notifyDataSetChanged();

    }


    public OrderModal getItem(int position) {
        return orderlist.get(position);
    }
}
