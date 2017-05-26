package com.hgil.siconprocess.retrofit.loginResponse.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mohan.giri on 26-05-2017.
 */

public class RcReason {

    @SerializedName("reason_id")
    @Expose
    private Integer reasonId;
    @SerializedName("reason")
    @Expose
    private String reason;

    private boolean status;

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
