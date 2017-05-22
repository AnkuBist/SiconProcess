package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceDetailModel implements Serializable {

    @SerializedName("Route_managemnet_Date")
    @Expose
    private String routeManagemnetDate;
    @SerializedName("Route_Management_Id")
    @Expose
    private String routeManagementId;
    @SerializedName("Invoice_No")
    @Expose
    private String invoiceNo;
    @SerializedName("Invoice_Date")
    @Expose
    private String invoiceDate;
    @SerializedName("Customer_id")
    @Expose
    private String customerId;
    @SerializedName("Route_Id")
    @Expose
    private String routeId;
    @SerializedName("Vehicle_No")
    @Expose
    private String vehicleNo;
    @SerializedName("Driver_Code")
    @Expose
    private String driverCode;
    @SerializedName("Cashier_Code")
    @Expose
    private String cashierCode;
    @SerializedName("Item_id")
    @Expose
    private String itemId;
    @SerializedName("Crate_id")
    @Expose
    private String crateId;
    @SerializedName("InvQty_ps")
    @Expose
    private float invQtyPs;
    @SerializedName("Item_Rate")
    @Expose
    private float itemRate;
    @SerializedName("Item_Discount")
    @Expose
    private float itemDiscount;
    @SerializedName("Discount_Amount")
    @Expose
    private float discountAmount;
    @SerializedName("Total_Amount")
    @Expose
    private float totalAmount;

    public String getRouteManagemnetDate() {
        return routeManagemnetDate;
    }

    public void setRouteManagemnetDate(String routeManagemnetDate) {
        this.routeManagemnetDate = routeManagemnetDate;
    }

    public String getRouteManagementId() {
        return routeManagementId;
    }

    public void setRouteManagementId(String routeManagementId) {
        this.routeManagementId = routeManagementId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverCode() {
        return driverCode;
    }

    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public String getCashierCode() {
        return cashierCode;
    }

    public void setCashierCode(String cashierCode) {
        this.cashierCode = cashierCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCrateId() {
        return crateId;
    }

    public void setCrateId(String crateId) {
        this.crateId = crateId;
    }

    public float getInvQtyPs() {
        return invQtyPs;
    }

    public void setInvQtyPs(float invQtyPs) {
        this.invQtyPs = invQtyPs;
    }

    public float getItemRate() {
        return itemRate;
    }

    public void setItemRate(float itemRate) {
        this.itemRate = itemRate;
    }

    public float getItemDiscount() {
        return itemDiscount;
    }

    public void setItemDiscount(float itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public float getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

}