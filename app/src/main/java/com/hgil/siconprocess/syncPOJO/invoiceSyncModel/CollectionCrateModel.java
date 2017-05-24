package com.hgil.siconprocess.syncPOJO.invoiceSyncModel;

/**
 * Created by mohan.giri on 20-02-2017.
 */

public class CollectionCrateModel {

    private String invoice_no;
    private String bill_no;
    private String customer_id;
    private int opening;
    private int issued;
    private int receive;
    private int balance;
    private String imei_no;
    private String lat_lng;
    private String time_stamp;
    private String login_id;
    private String ddate;

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getBill_no() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public int getOpening() {
        return opening;
    }

    public void setOpening(int opening) {
        this.opening = opening;
    }

    public int getIssued() {
        return issued;
    }

    public void setIssued(int issued) {
        this.issued = issued;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getDdate() {
        return ddate;
    }

    public void setDdate(String ddate) {
        this.ddate = ddate;
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
