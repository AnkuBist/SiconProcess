package com.hgil.siconprocess.syncPOJO.invoiceSyncModel;

/**
 * Created by mohan.giri on 08-04-2017.
 */

public class RejectionDetailModel {

    private String routeManagementId;
    private String bill_no;
    private String invoice_no;
    private String route_id;
    private String customer_id;
    private String invoice_date;
    private String item_id;
    private int fresh_m_shaped;
    private int fresh_torn_polly;
    private int fresh_fungus;
    private int fresh_wet_bread;
    private int fresh_others;
    private int market_damaged;
    private int market_expired;
    private int market_rat_eaten;

    public String getRouteManagementId() {
        return routeManagementId;
    }

    public void setRouteManagementId(String routeManagementId) {
        this.routeManagementId = routeManagementId;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getFresh_m_shaped() {
        return fresh_m_shaped;
    }

    public void setFresh_m_shaped(int fresh_m_shaped) {
        this.fresh_m_shaped = fresh_m_shaped;
    }

    public int getFresh_torn_polly() {
        return fresh_torn_polly;
    }

    public void setFresh_torn_polly(int fresh_torn_polly) {
        this.fresh_torn_polly = fresh_torn_polly;
    }

    public int getFresh_fungus() {
        return fresh_fungus;
    }

    public void setFresh_fungus(int fresh_fungus) {
        this.fresh_fungus = fresh_fungus;
    }

    public int getFresh_wet_bread() {
        return fresh_wet_bread;
    }

    public void setFresh_wet_bread(int fresh_wet_bread) {
        this.fresh_wet_bread = fresh_wet_bread;
    }

    public int getFresh_others() {
        return fresh_others;
    }

    public void setFresh_others(int fresh_others) {
        this.fresh_others = fresh_others;
    }

    public int getMarket_damaged() {
        return market_damaged;
    }

    public void setMarket_damaged(int market_damaged) {
        this.market_damaged = market_damaged;
    }

    public int getMarket_expired() {
        return market_expired;
    }

    public void setMarket_expired(int market_expired) {
        this.market_expired = market_expired;
    }

    public int getMarket_rat_eaten() {
        return market_rat_eaten;
    }

    public void setMarket_rat_eaten(int market_rat_eaten) {
        this.market_rat_eaten = market_rat_eaten;
    }
}
