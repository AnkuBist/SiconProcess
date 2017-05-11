package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mohan.giri on 07-02-2017.
 */

public class CustomerItemPriceModel implements Serializable {

    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_price")
    @Expose
    private Double itemPrice;
    @SerializedName("discount_price")
    @Expose
    private Double discountPrice;
    @SerializedName("discount_percentage")
    @Expose
    private Double discountPercentage;
    @SerializedName("discount_type")
    @Expose
    private String discountType;
    @SerializedName("discounted_price")
    @Expose
    private Double discountedPrice;
    @SerializedName("target_qty")
    @Expose
    private float target_qty;
    @SerializedName("sample_qty")
    @Expose
    private int sample_qty;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public float getTarget_qty() {
        return target_qty;
    }

    public void setTarget_qty(float target_qty) {
        this.target_qty = target_qty;
    }

    public int getSample_qty() {
        return sample_qty;
    }

    public void setSample_qty(int sample_qty) {
        this.sample_qty = sample_qty;
    }
}