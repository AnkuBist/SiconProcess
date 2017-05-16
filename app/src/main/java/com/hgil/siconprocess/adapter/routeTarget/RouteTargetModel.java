package com.hgil.siconprocess.adapter.routeTarget;

/**
 * Created by mohan.giri on 06-03-2017.
 */

public class RouteTargetModel {

    private String itemId;
    private String item_name;
    private int itemSequence;
    private int target;
    private int achieved;
    private int variance;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getAchieved() {
        return achieved;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }

    public int getVariance() {
        return variance;
    }

    public void setVariance(int variance) {
        this.variance = variance;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemSequence() {
        return itemSequence;
    }

    public void setItemSequence(int itemSequence) {
        this.itemSequence = itemSequence;
    }
}
