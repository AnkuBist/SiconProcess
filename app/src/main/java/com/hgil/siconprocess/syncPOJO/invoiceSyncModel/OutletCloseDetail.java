package com.hgil.siconprocess.syncPOJO.invoiceSyncModel;

/**
 * Created by mohan.giri on 29-05-2017.
 */

public class OutletCloseDetail {

    private String route_id;
    private String route_name;
    private String customer_id;
    private String customer_name;
    private double sale_amount;
    private double received_amount;
    private String reason_id;
    private String reason;
    private String close_time;

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public double getSale_amount() {
        return sale_amount;
    }

    public void setSale_amount(double sale_amount) {
        this.sale_amount = sale_amount;
    }

    public double getReceived_amount() {
        return received_amount;
    }

    public void setReceived_amount(double received_amount) {
        this.received_amount = received_amount;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }
}
