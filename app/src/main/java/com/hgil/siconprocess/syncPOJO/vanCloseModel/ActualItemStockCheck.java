package com.hgil.siconprocess.syncPOJO.vanCloseModel;

/**
 * Created by mohan.giri on 02-06-2017.
 */

public class ActualItemStockCheck {
    private String item_id;
    private String item_name;
    private double item_price;
    private int stockORejection;
    private int vanStockORejection;
    private int oVariance;
    private double oAmount;
    private int stockLeftover;
    private int vanLeftover;
    private int lVariance;
    private double lAmount;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public int getStockORejection() {
        return stockORejection;
    }

    public void setStockORejection(int stockORejection) {
        this.stockORejection = stockORejection;
    }

    public int getVanStockORejection() {
        return vanStockORejection;
    }

    public void setVanStockORejection(int vanStockORejection) {
        this.vanStockORejection = vanStockORejection;
    }

    public int getoVariance() {
        return oVariance;
    }

    public void setoVariance(int oVariance) {
        this.oVariance = oVariance;
    }

    public double getoAmount() {
        return oAmount;
    }

    public void setoAmount(double oAmount) {
        this.oAmount = oAmount;
    }

    public int getStockLeftover() {
        return stockLeftover;
    }

    public void setStockLeftover(int stockLeftover) {
        this.stockLeftover = stockLeftover;
    }

    public int getVanLeftover() {
        return vanLeftover;
    }

    public void setVanLeftover(int vanLeftover) {
        this.vanLeftover = vanLeftover;
    }

    public int getlVariance() {
        return lVariance;
    }

    public void setlVariance(int lVariance) {
        this.lVariance = lVariance;
    }

    public double getlAmount() {
        return lAmount;
    }

    public void setlAmount(double lAmount) {
        this.lAmount = lAmount;
    }
}
