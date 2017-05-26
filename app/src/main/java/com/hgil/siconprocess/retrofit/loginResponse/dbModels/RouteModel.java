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
    @SerializedName("subCompanyId")
    @Expose
    private String subCompanyId;
    @SerializedName("Route_Id")
    @Expose
    private String routeId;
    @SerializedName("Route_Name")
    @Expose
    private String routeName;
    @SerializedName("routeManagementId")
    @Expose
    private String routeManagementId;
    @SerializedName("CashierCode")
    @Expose
    private String cashierCode;
    @SerializedName("crateLoading")
    @Expose
    private int crateLoading;
    @SerializedName("Flag")
    @Expose
    private int flag;
    @SerializedName("arrRouteCloseReason")
    @Expose
    private List<RcReason> arrRcReason;
    @SerializedName("arrCustomerRouteMap")
    @Expose
    private List<CustomerRouteMapModel> arrCustomerRouteMap;
    @SerializedName("arrItemsMaster")
    @Expose
    private List<ProductModel> arrItemsMaster;
    @SerializedName("arrInvoiceDetails")
    @Expose
    private List<InvoiceDetailModel> arrInvoiceDetails;
    @SerializedName("arrEmployees")
    @Expose
    private List<EmployeeModel> arrEmployees;

    @SerializedName("arrItemDiscountPrice")
    @Expose
    private List<CustomerItemPriceModel> arrItemDiscountPrice;

    @SerializedName("expectedLastBillNo")
    @Expose
    private String expectedLastBillNo;

    public String getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getRouteManagementId() {
        return routeManagementId;
    }

    public void setRouteManagementId(String routeManagementId) {
        this.routeManagementId = routeManagementId;
    }

    public String getCashierCode() {
        return cashierCode;
    }

    public void setCashierCode(String cashierCode) {
        this.cashierCode = cashierCode;
    }

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

    public List<EmployeeModel> getArrEmployees() {
        return arrEmployees;
    }

    public void setArrEmployees(List<EmployeeModel> arrEmployees) {
        this.arrEmployees = arrEmployees;
    }

    public int getCrateLoading() {
        return crateLoading;
    }

    public void setCrateLoading(int crateLoading) {
        this.crateLoading = crateLoading;
    }

    public List<RcReason> getArrRcReason() {
        return arrRcReason;
    }

    public void setArrRcReason(List<RcReason> arrRcReason) {
        this.arrRcReason = arrRcReason;
    }
}
