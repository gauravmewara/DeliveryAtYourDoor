package com.collectorate.deliveryatyourdoor.Modal;

import java.io.Serializable;

public class CartedProductModal implements Serializable {
    String productId;
    String qty;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

}
