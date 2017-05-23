package com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync;

/**
 * Created by mohan.giri on 23-05-2017.
 */

public class CashCheckModel {

    private String route_id;
    private double total_amount;        //net sale amount
    private double f_rej_amount;
    private double m_rej_amount;
    private double leftover_amount;     //supervisor leftover amount..,...not mandatory
    private double sample_amount;       //  not manadatory
    private double receive_amount;      // cashier collected amount upi+cash....supervisor entered figure
    private double balance_amount;      //
    private double discount_amount;

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
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

    public double getLeftover_amount() {
        return leftover_amount;
    }

    public void setLeftover_amount(double leftover_amount) {
        this.leftover_amount = leftover_amount;
    }

    public double getSample_amount() {
        return sample_amount;
    }

    public void setSample_amount(double sample_amount) {
        this.sample_amount = sample_amount;
    }

    public double getReceive_amount() {
        return receive_amount;
    }

    public void setReceive_amount(double receive_amount) {
        this.receive_amount = receive_amount;
    }

    public double getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(double balance_amount) {
        this.balance_amount = balance_amount;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }



}
