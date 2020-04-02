package com.collectorate.deliveryatyourdoor.Modal;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderModal implements Serializable {
    String order_id,status,cancel_reason,deliveryCode,orderDate,updateDate,order_price;
    String actionbyid,actionbyname,actiondate;
    ArrayList<ProductsModal> orderItems;

    public String getActiondate() {
        return actiondate;
    }

    public void setActiondate(String actiondate) {
        this.actiondate = actiondate;
    }

    public String getActionbyid() {
        return actionbyid;
    }

    public void setActionbyid(String actionbyid) {
        this.actionbyid = actionbyid;
    }

    public String getActionbyname() {
        return actionbyname;
    }

    public void setActionbyname(String actionbyname) {
        this.actionbyname = actionbyname;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getDeliveryCode() {
        return deliveryCode;
    }

    public void setDeliveryCode(String deliveryCode) {
        this.deliveryCode = deliveryCode;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public ArrayList<ProductsModal> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<ProductsModal> orderItems) {
        this.orderItems = orderItems;
    }
}
