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
    @SerializedName("Sub_Company_id")
    @Expose
    private String subCompanyId;
    @SerializedName("Depot_id")
    @Expose
    private String depotId;
    @SerializedName("SubDepot_id")
    @Expose
    private String subDepotId;
    @SerializedName("Route_Id")
    @Expose
    private String routeId;
    @SerializedName("Route_Name")
    @Expose
    private String routeName;
    @SerializedName("Route_Description")
    @Expose
    private String routeDescription;
    @SerializedName("Sale_Date_Parameter")
    @Expose
    private String saleDateParameter;
    @SerializedName("Loading_Type")
    @Expose
    private String loadingType;
    @SerializedName("TCC")
    @Expose
    private int tCC;
    @SerializedName("Flag")
    @Expose
    private int flag;
    @SerializedName("arrCustomerRouteMap")
    @Expose
    private List<CustomerRouteMapModel> arrCustomerRouteMap;
    @SerializedName("arrRouteCustomerInfo")
    @Expose
    private List<CustomerInfoModel> arrRouteCustomerInfo;
    @SerializedName("arrItemsMaster")
    @Expose
    private List<ProductModel> arrItemsMaster;
    @SerializedName("arrCreditOpening")
    @Expose
    private List<CreditOpeningModel> arrCreditOpening;
    @SerializedName("arrCrateOpening")
    @Expose
    private List<CrateOpeningModel> arrCrateOpening;
    @SerializedName("arrCrateCollection")
    @Expose
    private List<CrateCollectionModel> arrCrateCollection;
    @SerializedName("arrInvoiceDetails")
    @Expose
    private List<InvoiceDetailModel> arrInvoiceDetails;
    @SerializedName("arrDemandTarget")
    @Expose
    private List<DemandTargetModel> arrDemandTarget;
    @SerializedName("arrFixedSample")
    @Expose
    private List<FixedSampleModel> arrFixedSample;
    @SerializedName("arrRejectionTarget")
    @Expose
    private List<RejectionTargetModel> arrRejectionTarget;
    @SerializedName("arrEmployees")
    @Expose
    private List<EmployeeModel> arrEmployees;
   /* @SerializedName("arrCustomerItemPrice")
    @Expose
    private List<CustomerItemPriceModel> arrCustomerItemPrice = null;*/

    @SerializedName("arrItemDiscountPrice")
    @Expose
    private List<CustomerItemPriceModel> arrItemDiscountPrice;
    /*@SerializedName("expectedLastInvoiceNo")
    @Expose
    private String expectedLastInvoiceNo;*/
    @SerializedName("expectedLastBillNo")
    @Expose
    private String expectedLastBillNo;

    public List<CustomerItemPriceModel> getArrItemDiscountPrice() {
        return arrItemDiscountPrice;
    }

    public void setArrItemDiscountPrice(List<CustomerItemPriceModel> arrItemDiscountPrice) {
        this.arrItemDiscountPrice = arrItemDiscountPrice;
    }

    /*public String getExpectedLastInvoiceNo() {
        return expectedLastInvoiceNo;
    }

    public void setExpectedLastInvoiceNo(String expectedLastInvoiceNo) {
        this.expectedLastInvoiceNo = expectedLastInvoiceNo;
    }*/

    public String getExpectedLastBillNo() {
        return expectedLastBillNo;
    }

    public void setExpectedLastBillNo(String expectedLastBillNo) {
        this.expectedLastBillNo = expectedLastBillNo;
    }

    /*public List<CustomerItemPriceModel> getArrCustomerItemPrice() {
        return arrCustomerItemPrice;
    }

    public void setArrCustomerItemPrice(List<CustomerItemPriceModel> arrCustomerItemPrice) {
        this.arrCustomerItemPrice = arrCustomerItemPrice;
    }*/

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

    public String getSubCompanyId() {
        return subCompanyId;
    }

    public void setSubCompanyId(String subCompanyId) {
        this.subCompanyId = subCompanyId;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public String getSubDepotId() {
        return subDepotId;
    }

    public void setSubDepotId(String subDepotId) {
        this.subDepotId = subDepotId;
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

    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public String getSaleDateParameter() {
        return saleDateParameter;
    }

    public void setSaleDateParameter(String saleDateParameter) {
        this.saleDateParameter = saleDateParameter;
    }

    public String getLoadingType() {
        return loadingType;
    }

    public void setLoadingType(String loadingType) {
        this.loadingType = loadingType;
    }

    public int getTCC() {
        return tCC;
    }

    public void setTCC(int tCC) {
        this.tCC = tCC;
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

    public List<CreditOpeningModel> getArrCreditOpening() {
        return arrCreditOpening;
    }

    public void setArrCreditOpening(List<CreditOpeningModel> arrCreditOpening) {
        this.arrCreditOpening = arrCreditOpening;
    }

    public List<CrateOpeningModel> getArrCrateOpening() {
        return arrCrateOpening;
    }

    public void setArrCrateOpening(List<CrateOpeningModel> arrCrateOpening) {
        this.arrCrateOpening = arrCrateOpening;
    }

    public List<CrateCollectionModel> getArrCrateCollection() {
        return arrCrateCollection;
    }

    public void setArrCrateCollection(List<CrateCollectionModel> arrCrateCollection) {
        this.arrCrateCollection = arrCrateCollection;
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

    public List<RejectionTargetModel> getArrRejectionTarget() {
        return arrRejectionTarget;
    }

    public void setArrRejectionTarget(List<RejectionTargetModel> arrRejectionTarget) {
        this.arrRejectionTarget = arrRejectionTarget;
    }

    public List<EmployeeModel> getArrEmployees() {
        return arrEmployees;
    }

    public void setArrEmployees(List<EmployeeModel> arrEmployees) {
        this.arrEmployees = arrEmployees;
    }

    public List<CustomerInfoModel> getArrRouteCustomerInfo() {
        return arrRouteCustomerInfo;
    }

    public void setArrRouteCustomerInfo(List<CustomerInfoModel> arrRouteCustomerInfo) {
        this.arrRouteCustomerInfo = arrRouteCustomerInfo;
    }
}
