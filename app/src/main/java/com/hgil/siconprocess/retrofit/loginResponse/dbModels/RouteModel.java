package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RouteModel implements Serializable {

    @SerializedName("Rec_Id")
    @Expose
    private int recId;
    @SerializedName("Depot_id")
    @Expose
    private String depotId;
    @SerializedName("Route_Id")
    @Expose
    private String routeId;
    @SerializedName("Route_Name")
    @Expose
    private String routeName;
    @SerializedName("Flag")
    @Expose
    private int flag;
    @SerializedName("arrCustomerRouteMap")
    @Expose
    private List<CustomerRouteMapModel> arrCustomerRouteMap;
    @SerializedName("arrItemsMaster")
    @Expose
    private List<ProductModel> arrItemsMaster;
    @SerializedName("arrInvoiceDetails")
    @Expose
    private List<InvoiceDetailModel> arrInvoiceDetails;
    @SerializedName("arrDemandTarget")
    @Expose
    private List<DemandTargetModel> arrDemandTarget;
    @SerializedName("arrFixedSample")
    @Expose
    private List<FixedSampleModel> arrFixedSample;
    @SerializedName("arrEmployees")
    @Expose
    private List<EmployeeModel> arrEmployees;

    @SerializedName("arrItemDiscountPrice")
    @Expose
    private List<CustomerItemPriceModel> arrItemDiscountPrice;

    @SerializedName("expectedLastBillNo")
    @Expose
    private String expectedLastBillNo;

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

    public List<CustomerItemPriceModel> getArrItemDiscountPrice() {
        return arrItemDiscountPrice;
    }

    public void setArrItemDiscountPrice(List<CustomerItemPriceModel> arrItemDiscountPrice) {
        this.arrItemDiscountPrice = arrItemDiscountPrice;
    }

    public String getExpectedLastBillNo() {
        return expectedLastBillNo;
    }

    public void setExpectedLastBillNo(String expectedLastBillNo) {
        this.expectedLastBillNo = expectedLastBillNo;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public List<CustomerRouteMapModel> getArrCustomerRouteMap() {
        return arrCustomerRouteMap;
    }

    public void setArrCustomerRouteMap(List<CustomerRouteMapModel> arrCustomerRouteMap) {
        this.arrCustomerRouteMap = arrCustomerRouteMap;
    }

    public List<ProductModel> getArrItemsMaster() {
        return arrItemsMaster;
    }

    public void setArrItemsMaster(List<ProductModel> arrItemsMaster) {
        this.arrItemsMaster = arrItemsMaster;
    }

    public List<InvoiceDetailModel> getArrInvoiceDetails() {
        return arrInvoiceDetails;
    }

    public void setArrInvoiceDetails(List<InvoiceDetailModel> arrInvoiceDetails) {
        this.arrInvoiceDetails = arrInvoiceDetails;
    }

    public List<DemandTargetModel> getArrDemandTarget() {
        return arrDemandTarget;
    }

    public void setArrDemandTarget(List<DemandTargetModel> arrDemandTarget) {
        this.arrDemandTarget = arrDemandTarget;
    }

    public List<FixedSampleModel> getArrFixedSample() {
        return arrFixedSample;
    }

    public void setArrFixedSample(List<FixedSampleModel> arrFixedSample) {
        this.arrFixedSample = arrFixedSample;
    }

    public List<EmployeeModel> getArrEmployees() {
        return arrEmployees;
    }

    public void setArrEmployees(List<EmployeeModel> arrEmployees) {
        this.arrEmployees = arrEmployees;
    }
}
