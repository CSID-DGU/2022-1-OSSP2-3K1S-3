package com.example.zipgaja.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LessMoneyResponse {
    @SerializedName("status")
    int status;

    @SerializedName("data")
    ArrayList<LessMoneyResponseData> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<LessMoneyResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<LessMoneyResponseData> data) {
        this.data = data;
    }

}
