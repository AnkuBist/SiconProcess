package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CrateCollectionModel implements Serializable {

    @SerializedName("Route_id")
    @Expose
    private String routeId;
    @SerializedName("Route_Management_id")
    @Expose
    private String routeManagementId;
    @SerializedName("Route_Management_Date")
    @Expose
    private String routeManagementDate;
    @SerializedName("Customer_id")
    @Expose
    private String customerId;
    @SerializedName("Invoice_No")
    @Expose
    private String invoiceNo;
    @SerializedName("Invoice_date")
    @Expose
    private String invoiceDate;
    @SerializedName("Crate_Id")
    @Expose
    private String crateId;
    @SerializedName("Crate_Qty")
    @Expose
    private float crateQty;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteManagementId() {
        return routeManagementId;
    }

    public void setRouteManagementId(String routeManagementId) {
        this.routeManagementId = routeManagementId;
    }

    public String getRouteManagementDate() {
        return routeManagementDate;
    }

    public void setRouteManagementDate(String routeManagementDate) {
        this.routeManagementDate = routeManagementDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCrateId() {
        return crateId;
    }

    public void setCrateId(String crateId) {
        this.crateId = crateId;
    }

    public float getCrateQty() {
        return crateQty;
    }

    public void setCrateQty(float crateQty) {
        this.crateQty = crateQty;
    }

}
