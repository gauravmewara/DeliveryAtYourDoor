package com.collectorate.deliveryatyourdoor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.collectorate.deliveryatyourdoor.Activity.Cart;
import com.collectorate.deliveryatyourdoor.Interface.CartItemRemoveListener;
import com.collectorate.deliveryatyourdoor.Modal.ProductsModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<ProductsModal> itemlist;
    CartItemRemoveListener removeListener;
    String init_type;

    public CartItemAdapter(Context context,ArrayList<ProductsModal> itemlist,String init_type){
        this.context=context;
        if(init_type.equals(Constants.CART_INIT))
            this.removeListener = (CartItemRemoveListener)context;
        this.itemlist=itemlist;
        this.init_type = init_type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout cancel;
        TextView itemname,itempackaging,amount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cancel = (RelativeLayout)itemView.findViewById(R.id.rl_cartitem_remove);
            cancel.setOnClickListener(this);
            itemname = (TextView)itemView.findViewById(R.id.tv_cartitem_itemname);
            itempackaging = (TextView)itemView.findViewById(R.id.tv_cartitem_itempackaging);
            amount = (TextView)itemView.findViewById(R.id.tv_cartitem_amt);
            if(init_type.equals(Constants.CART_INIT)){
                cancel.setVisibility(View.VISIBLE);
                cancel.setClickable(true);
            }else{
                cancel.setVisibility(View.INVISIBLE);
                cancel.setClickable(false);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_cartitem_remove:
                    removeListener.onItemRemoved(itemlist.get(getAdapterPosition()),getAdapterPosition());
                    remove(getAdapterPosition());
                    break;
            }
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_single_item,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder mvH = (MyViewHolder)holder;
        mvH.itemname.setText(itemlist.get(position).getName());
        mvH.itempackaging.setText(itemlist.get(position).getPackaging()+" "+itemlist.get(position).getUnit());
        mvH.amount.setText(itemlist.get(position).getUnitprice());
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    public void remove(int position) {
        if (position > -1) {
            itemlist.remove(position);
            notifyItemRemoved(position);
        }
    }
}
