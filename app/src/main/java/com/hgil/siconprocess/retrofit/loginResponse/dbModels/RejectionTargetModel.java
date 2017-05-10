package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RejectionTargetModel implements Serializable {

    @SerializedName("Item_id")
    @Expose
    private String itemId;
    @SerializedName("Target_Rej")
    @Expose
    private float targetRej;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public float getTargetRej() {
        return targetRej;
    }

    public void setTargetRej(float targetRej) {
        this.targetRej = targetRej;
    }

}