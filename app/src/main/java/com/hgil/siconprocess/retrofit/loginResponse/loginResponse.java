package com.hgil.siconprocess.retrofit.loginResponse;

/**
 * Created by mohan.giri on 24-01-2017.
 */

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class loginResponse implements Serializable {

    @SerializedName("returnCode")
    @Expose
    private Boolean returnCode;
    @SerializedName("strMessage")
    @Expose
    private String strMessage;
    @Nullable
    @SerializedName("objLoginResponse")
    @Expose
    private ObjLoginResponse objLoginResponse;

    public Boolean getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Boolean returnCode) {
        this.returnCode = returnCode;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public ObjLoginResponse getObjLoginResponse() {
        return objLoginResponse;
    }

    public void setObjLoginResponse(ObjLoginResponse objLoginResponse) {
        this.objLoginResponse = objLoginResponse;
    }

}

