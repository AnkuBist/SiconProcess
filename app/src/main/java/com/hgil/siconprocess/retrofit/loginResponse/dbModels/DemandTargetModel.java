package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DemandTargetModel implements Serializable {

    @SerializedName("DDate")
    @Expose
    private String dDate;
    @SerializedName("Route_ID")
    @Expose
    private String routeID;
    @SerializedName("Customer_ID")
    @Expose
    private String customerID;
    @SerializedName("Item_id")
    @Expose
    private String itemId;
    @SerializedName("Target_Qty")
    @Expose
    private float targetQty;

    public String getDDate() {
        return dDate;
    }

    public void setDDate(String dDate) {
        this.dDate = dDate;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(float targetQty) {
        this.targetQty = targetQty;
    }

}