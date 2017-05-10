package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreditOpeningModel implements Serializable {

    @SerializedName("Route_id")
    @Expose
    private String routeId;
    @SerializedName("Customer_id")
    @Expose
    private String customerId;
    @SerializedName("DDate")
    @Expose
    private String dDate;
    @SerializedName("Opening")
    @Expose
    private float opening;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDDate() {
        return dDate;
    }

    public void setDDate(String dDate) {
        this.dDate = dDate;
    }

    public float getOpening() {
        return opening;
    }

    public void setOpening(float opening) {
        this.opening = opening;
    }

}