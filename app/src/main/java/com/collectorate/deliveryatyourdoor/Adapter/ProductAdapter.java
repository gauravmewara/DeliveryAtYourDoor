package com.collectorate.deliveryatyourdoor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.collectorate.deliveryatyourdoor.Fragment.OrderPlaceFragment;
import com.collectorate.deliveryatyourdoor.Interface.ItemUpdateListener;
import com.collectorate.deliveryatyourdoor.Modal.ProductsModal;
import com.collectorate.deliveryatyourdoor.R;
import com.collectorate.deliveryatyourdoor.Utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<ProductsModal> productlist;
    HashMap<String,ProductsModal> cartMap;
    ItemUpdateListener updateitemlistener;
    public ProductAdapter(Context context, OrderPlaceFragment listenercontext){
        this.context=context;
        this.updateitemlistener = (ItemUpdateListener)listenercontext;
        productlist = new ArrayList<>();
    }

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public ProductAdapter(Context context,ArrayList<ProductsModal> productlist){
        this.productlist = productlist;
        this.context = context;
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView productname,productpackaging,productprice,add,sub,qty;
        public ProductViewHolder(View view){
            super(view);
            productname = (TextView)view.findViewById(R.id.tv_adapter_product_name);
            productpackaging = (TextView)view.findViewById(R.id.tv_adapter_product_packaging);
            productprice = (TextView)view.findViewById(R.id.tv_adapter_product_unit_price);
            add = (TextView)view.findViewById(R.id.tv_adapter_product_qty_add);
            add.setOnClickListener(this);
            sub = (TextView)view.findViewById(R.id.tv_adapter_product_qty_sub);
            sub.setOnClickListener(this);
            qty = (TextView)view.findViewById(R.id.tv_adapter_product_qty);
        }

        @Override
        public void onClick(View view) {
            int prev;
            float unitprice;
            ProductsModal temp;
            switch (view.getId()) {
                case R.id.tv_adapter_product_qty_add:
                    if(cartMap==null)
                        cartMap = new HashMap<>();
                    prev = Integer.parseInt(qty.getText().toString());
                    if(!((prev+1)>Constants.MAX_ORDER_LIMIT)) {
                        prev++;
                        qty.setText(String.valueOf(prev));
                        temp = productlist.get(getAdapterPosition());
                        unitprice = Float.parseFloat(temp.getUnitprice());
                        if(cartMap.containsKey(temp.getProductId())){
                            cartMap.remove(temp.getProductId());
                            temp.setPurchaseQty(String.valueOf(prev));
                            temp.setQtyPrice(String.valueOf(prev*unitprice));
                            cartMap.put(temp.getProductId(),temp);
                        }else{
                            temp.setPurchaseQty(String.valueOf(prev));
                            temp.setQtyPrice(String.valueOf(prev*unitprice));
                            cartMap.put(temp.getProductId(),temp);
                        }
                        updateitemlistener.onItemUpdate(unitprice, "add",cartMap);
                    }
                    break;
                case R.id.tv_adapter_product_qty_sub:
                    prev = Integer.parseInt(qty.getText().toString());
                    if((prev-1)>=0) {
                        prev--;
                        qty.setText(String.valueOf(prev));
                        temp = productlist.get(getAdapterPosition());
                        unitprice = Float.parseFloat(temp.getUnitprice());
                        if(cartMap.containsKey(temp.getProductId())){
                            cartMap.remove(temp.getProductId());
                            if(prev!=0) {
                                temp.setPurchaseQty(String.valueOf(prev));
                                temp.setQtyPrice(String.valueOf(prev * unitprice));
                                cartMap.put(temp.getProductId(), temp);
                            }
                        }
                        updateitemlistener.onItemUpdate(unitprice, "sub",cartMap);
                    }
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return productlist == null ? 0 : productlist.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = new ProductViewHolder(inflater.inflate(R.layout.product_adapter_item,parent,false));
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
                final ProductViewHolder mVH = (ProductViewHolder) holder;
                mVH.productname.setText(productlist.get(position).getName());
                mVH.productpackaging.setText(productlist.get(position).getPackaging()+" "+
                        productlist.get(position).getUnit()+ " Pack "+productlist.get(position).getAdditional());
                mVH.productprice.setText(productlist.get(position).getUnitprice()+" Rs");
                mVH.qty.setText("0");
                break;
            case LOADING:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == productlist.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(ProductsModal r) {
        productlist.add(r);
        notifyItemInserted(productlist.size() - 1);
    }

    public void addAll(List<ProductsModal> moveResults) {
        for (ProductsModal result : moveResults) {
            add(result);
        }
    }

    public void remove(ProductsModal r) {
        int position = productlist.indexOf(r);
        if (position > -1) {
            productlist.remove(position);
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
        add(new ProductsModal());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = productlist.size() - 1;
        ProductsModal result = getItem(position);
        if (result != null) {
            productlist.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ProductsModal getItem(int position) {
        return productlist.get(position);
    }

}
