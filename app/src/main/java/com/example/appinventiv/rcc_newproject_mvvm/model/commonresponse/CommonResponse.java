package com.example.appinventiv.rcc_newproject_mvvm.model.commonresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("CODE")
    @Expose
    private Integer cODE;
    @SerializedName("MESSAGE")
    @Expose
    private String mESSAGE;
    @SerializedName("RESULT")
    @Expose
    private RESULT rESULT;

    public Integer getCODE() {
        return cODE;
    }

    public void setCODE(Integer cODE) {
        this.cODE = cODE;
    }

    public String getMESSAGE() {
        return mESSAGE;
    }

    public void setMESSAGE(String mESSAGE) {
        this.mESSAGE = mESSAGE;
    }

    public RESULT getRESULT() {
        return rESULT;
    }

    public void setRESULT(RESULT rESULT) {
        this.rESULT = rESULT;
    }

}
