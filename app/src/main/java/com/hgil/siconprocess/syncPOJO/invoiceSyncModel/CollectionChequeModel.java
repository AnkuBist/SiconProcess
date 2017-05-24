package com.hgil.siconprocess.syncPOJO.invoiceSyncModel;

/**
 * Created by mohan.giri on 28-03-2017.
 */

public class CollectionChequeModel {
    private String invoice_no;
    private String bill_no;
    private String route_id;
    private String customer_id;
    private String cheque_no;
    private String cheque_date;
    private double cheque_amount;
    private String bank_name;
    private String branch_name;
    private String imei_no;
    private String lat_lng;
    private String time_stamp;
    private String login_id;
    private String date;

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
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

    public String getCheque_no() {
        return cheque_no;
    }

    public void setCheque_no(String cheque_no) {
        this.cheque_no = cheque_no;
    }

    public String getCheque_date() {
        return cheque_date;
    }

    public void setCheque_date(String cheque_date) {
        this.cheque_date = cheque_date;
    }

    public double getCheque_amount() {
        return cheque_amount;
    }

    public void setCheque_amount(double cheque_amount) {
        this.cheque_amount = cheque_amount;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
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
}
