package com.hgil.siconprocess.syncPOJO.vanCloseModel;

import java.io.Serializable;

/**
 * Created by mohan.giri on 23-05-2017.
 */

public class ItemStockCheck implements Serializable {

    private String item_id;
    private String item_name;
    private double item_price;
    private int loadQty;
    private int gross_sale;
    private int sample;
    private int market_rejection;
    private int fresh_rejection;
    private int actual_leftover;
    private int physical_leftover;
    private int item_variance;

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

    public int getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(int loadQty) {
        this.loadQty = loadQty;
    }

    public int getGross_sale() {
        return gross_sale;
    }

    public void setGross_sale(int gross_sale) {
        this.gross_sale = gross_sale;
    }

    public int getSample() {
        return sample;
    }

    public void setSample(int sample) {
        this.sample = sample;
    }

    public int getMarket_rejection() {
        return market_rejection;
    }

    public void setMarket_rejection(int market_rejection) {
        this.market_rejection = market_rejection;
    }

    public int getFresh_rejection() {
        return fresh_rejection;
    }

    public void setFresh_rejection(int fresh_rejection) {
        this.fresh_rejection = fresh_rejection;
    }

    public int getActual_leftover() {
        return actual_leftover;
    }

    public void setActual_leftover(int actual_leftover) {
        this.actual_leftover = actual_leftover;
    }

    public int getPhysical_leftover() {
        return physical_leftover;
    }

    public void setPhysical_leftover(int physical_leftover) {
        this.physical_leftover = physical_leftover;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }

    public int getItem_variance() {
        return item_variance;
    }

    public void setItem_variance(int item_variance) {
        this.item_variance = item_variance;
    }
}
