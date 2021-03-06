package com.hgil.siconprocess.syncPOJO.vanCloseModel;

import java.io.Serializable;

/**
 * Created by mohan.giri on 20-02-2017.
 */

public class CrateStockCheck implements Serializable {

    private String routeId;
    private int opening;
    private int issued;
    private int received;       // supervisor enter figures
    private int balance;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
