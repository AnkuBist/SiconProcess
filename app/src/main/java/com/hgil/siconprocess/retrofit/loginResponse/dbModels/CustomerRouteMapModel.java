package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerRouteMapModel implements Serializable {

    @SerializedName("Route_id")
    @Expose
    private String routeId;
    @SerializedName("Route_Name")
    @Expose
    private String routeName;
    @SerializedName("Sale_Date_Parameter")
    @Expose
    private String saleDateParameter;
    @SerializedName("PSMID")
    @Expose
    private String pSMID;
    @SerializedName("Customer_id")
    @Expose
    private String customerId;
    @SerializedName("Customer_Name")
    @Expose
    private String customerName;
    @SerializedName("PRICEGROUP")
    @Expose
    private String pRICEGROUP;
    @SerializedName("LINEDISC")
    @Expose
    private String lINEDISC;
    @SerializedName("C_Type")
    @Expose
    private String cType;
    @SerializedName("CUSTCLASSIFICATIONID")
    @Expose
    private String cUSTCLASSIFICATIONID;
    @SerializedName("Crate_Loading")
    @Expose
    private int crateLoading;
    @SerializedName("Crate_Credit")
    @Expose
    private int crateCredit;
    @SerializedName("Amount_Credit")
    @Expose
    private double amountCredit;
    @SerializedName("cust_status")
    @Expose
    private String custStatus;

    public String getCustStatus() {
        return custStatus;
    }

    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getSaleDateParameter() {
        return saleDateParameter;
    }

    public void setSaleDateParameter(String saleDateParameter) {
        this.saleDateParameter = saleDateParameter;
    }

    public String getPSMID() {
        return pSMID;
    }

    public void setPSMID(String pSMID) {
        this.pSMID = pSMID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPRICEGROUP() {
        return pRICEGROUP;
    }

    public void setPRICEGROUP(String pRICEGROUP) {
        this.pRICEGROUP = pRICEGROUP;
    }

    public String getLINEDISC() {
        return lINEDISC;
    }

    public void setLINEDISC(String lINEDISC) {
        this.lINEDISC = lINEDISC;
    }

    public String getCType() {
        return cType;
    }

    public void setCType(String cType) {
        this.cType = cType;
    }

    public String getCUSTCLASSIFICATIONID() {
        return cUSTCLASSIFICATIONID;
    }

    public void setCUSTCLASSIFICATIONID(String cUSTCLASSIFICATIONID) {
        this.cUSTCLASSIFICATIONID = cUSTCLASSIFICATIONID;
    }

    public double getAmountCredit() {
        return amountCredit;
    }

    public void setAmountCredit(double amountCredit) {
        this.amountCredit = amountCredit;
    }

    public int getCrateCredit() {
        return crateCredit;
    }

    public void setCrateCredit(int crateCredit) {
        this.crateCredit = crateCredit;
    }

    public int getCrateLoading() {
        return crateLoading;
    }

    public void setCrateLoading(int crateLoading) {
        this.crateLoading = crateLoading;
    }
}
