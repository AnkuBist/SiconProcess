package com.hgil.siconprocess.adapter.invoice;

import java.io.Serializable;

/**
 * Created by mohan.giri on 30-01-2017.
 */

public class InvoiceModel implements Serializable {

    private String routeManagemnetDate;
    private String bill_no;

    private String invoiceNo;
    private String invoiceDate;
    private String customerId;
    private String routeId;
    private String vehicleNo;
    private String driverCode;
    private String cashierCode;
    private String itemId;
    private int itemSequence;
    private String crateId;
    private float invQtyPs;
    private float itemRate;
    private float itemDiscount;
    private float discountAmount;
    private float totalAmount;

    // new added value
    private int fixedSample;
    //  private float demandTargetQty;
    private double orderAmount;
    private int stockAvail;
    private int tempStock;
    private String itemName;
    private String imei_no;
    private String lat_lng;
    private String login_id;

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getImei_no() {
        return imei_no;
    }

    public void setImei_no(String imei_no) {
        this.imei_no = imei_no;
    }

    public String getLat_lng() {
        return lat_lng;
    }

    public void setLat_lng(String lat_lng) {
        this.lat_lng = lat_lng;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public int getTempStock() {
        return tempStock;
    }

    public void setTempStock(int tempStock) {
        this.tempStock = tempStock;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStockAvail() {
        return stockAvail;
    }

    public void setStockAvail(int stockAvail) {
        this.stockAvail = stockAvail;
    }

    public int getFixedSample() {
        return fixedSample;
    }

    public void setFixedSample(int fixedSample) {
        this.fixedSample = fixedSample;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

   /* public float getDemandTargetQty() {
        return demandTargetQty;
    }

    public void setDemandTargetQty(float demandTargetQty) {
        this.demandTargetQty = demandTargetQty;
    }*/

    public String getRouteManagemnetDate() {
        return routeManagemnetDate;
    }

    public void setRouteManagemnetDate(String routeManagemnetDate) {
        this.routeManagemnetDate = routeManagemnetDate;
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

    public float getInvQtyPs() {
        return invQtyPs;
    }

    public void setInvQtyPs(float invQtyPs) {
        this.invQtyPs = invQtyPs;
    }

    public int getItemSequence() {
        return itemSequence;
    }

    public void setItemSequence(int itemSequence) {
        this.itemSequence = itemSequence;
    }
}
