package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel implements Serializable {

    @SerializedName("ITEMSEQUENCE")
    @Expose
    private int iTEMSEQUENCE;
    @SerializedName("Item_id")
    @Expose
    private String itemId;
    @SerializedName("Item_Name")
    @Expose
    private String itemName;
    @SerializedName("Target_Rej")
    @Expose
    private float targetRej;
    @SerializedName("ITEMGROUPID")
    @Expose
    private String iTEMGROUPID;

    public int getITEMSEQUENCE() {
        return iTEMSEQUENCE;
    }

    public void setITEMSEQUENCE(int iTEMSEQUENCE) {
        this.iTEMSEQUENCE = iTEMSEQUENCE;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getTargetRej() {
        return targetRej;
    }

    public void setTargetRej(float targetRej) {
        this.targetRej = targetRej;
    }

    public String getITEMGROUPID() {
        return iTEMGROUPID;
    }

    public void setITEMGROUPID(String iTEMGROUPID) {
        this.iTEMGROUPID = iTEMGROUPID;
    }

}