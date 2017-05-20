package com.hgil.siconprocess.activity.fragments.invoiceSyncModel;

/**
 * Created by mohan.giri on 20-02-2017.
 */

public class SyncInvoiceDetailModel {

    private String bill_no;
    private String invoice_no;
    private String invoice_date;
    private String customer_id;
    private String route_id;
    //private String route_management_id;
    private String cashier_code;
    private String item_id;
    private int loading_count;
    private int sale_count;
    private int fresh_rej;
    private int market_rej;
    private int sample;

    private double item_price;
    private double disc_price;
    private double disc_percentage;
    private String disc_type;
    private double discounted_price;
    private double total_sale_amount;
    private double total_disc_amount;

    private String imei_no;
    private String lat_lng;
    private String time_stamp;
    private String login_id;

    private int actual_sale_count;
    private double f_rej_amount;
    private double m_rej_amount;

    public int getActual_sale_count() {
        return actual_sale_count;
    }

    public void setActual_sale_count(int actual_sale_count) {
        this.actual_sale_count = actual_sale_count;
    }

    public double getF_rej_amount() {
        return f_rej_amount;
    }

    public void setF_rej_amount(double f_rej_amount) {
        this.f_rej_amount = f_rej_amount;
    }

    public double getM_rej_amount() {
        return m_rej_amount;
    }

    public void setM_rej_amount(double m_rej_amount) {
        this.m_rej_amount = m_rej_amount;
    }

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

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getCashier_code() {
        return cashier_code;
    }

    public void setCashier_code(String cashier_code) {
        this.cashier_code = cashier_code;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getLoading_count() {
        return loading_count;
    }

    public void setLoading_count(int loading_count) {
        this.loading_count = loading_count;
    }

    public int getSale_count() {
        return sale_count;
    }

    public void setSale_count(int sale_count) {
        this.sale_count = sale_count;
    }

    public int getFresh_rej() {
        return fresh_rej;
    }

    public void setFresh_rej(int fresh_rej) {
        this.fresh_rej = fresh_rej;
    }

    public int getMarket_rej() {
        return market_rej;
    }

    public void setMarket_rej(int market_rej) {
        this.market_rej = market_rej;
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public double getDisc_price() {
        return disc_price;
    }

    public void setDisc_price(double disc_price) {
        this.disc_price = disc_price;
    }

    public double getDisc_percentage() {
        return disc_percentage;
    }

    public void setDisc_percentage(double disc_percentage) {
        this.disc_percentage = disc_percentage;
    }

    public String getDisc_type() {
        return disc_type;
    }

    public void setDisc_type(String disc_type) {
        this.disc_type = disc_type;
    }

    public double getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(double discounted_price) {
        this.discounted_price = discounted_price;
    }

    public double getTotal_sale_amount() {
        return total_sale_amount;
    }

    public void setTotal_sale_amount(double total_sale_amount) {
        this.total_sale_amount = total_sale_amount;
    }

    public double getTotal_disc_amount() {
        return total_disc_amount;
    }

    public void setTotal_disc_amount(double total_disc_amount) {
        this.total_disc_amount = total_disc_amount;
    }
}
