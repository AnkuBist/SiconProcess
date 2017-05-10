package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FixedSampleModel implements Serializable {

    @SerializedName("Route")
    @Expose
    private String route;
    @SerializedName("Customer_id")
    @Expose
    private String customerId;
    @SerializedName("Item_id")
    @Expose
    private String itemId;
    @SerializedName("SQty")
    @Expose
    private int sQty;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getSQty() {
        return sQty;
    }

    public void setSQty(int sQty) {
        this.sQty = sQty;
    }

}