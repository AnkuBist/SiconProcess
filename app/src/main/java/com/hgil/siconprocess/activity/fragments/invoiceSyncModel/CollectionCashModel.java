package com.hgil.siconprocess.activity.fragments.invoiceSyncModel;

/**
 * Created by mohan.giri on 20-02-2017.
 */

public class CollectionCashModel {
    private String invoice_no;
    private String bill_no;
    private String customer_id;
    private double opening;
    private double sale;
    private double receive;
    private double balance;
    private String upi_reference_id;
    private double upi_amount;
    private String imei_no;
    private String lat_lng;
    private String time_stamp;
    private String login_id;
    private String date;

    public String getUpi_reference_id() {
        return upi_reference_id;
    }

    public void setUpi_reference_id(String upi_reference_id) {
        this.upi_reference_id = upi_reference_id;
    }

    public double getUpi_amount() {
        return upi_amount;
    }

    public void setUpi_amount(double upi_amount) {
        this.upi_amount = upi_amount;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public double getOpening() {
        return opening;
    }

    public void setOpening(double opening) {
        this.opening = opening;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getReceive() {
        return receive;
    }

    public void setReceive(double receive) {
        this.receive = receive;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
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

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }
}
