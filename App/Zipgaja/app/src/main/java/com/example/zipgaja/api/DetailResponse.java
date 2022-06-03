package com.example.zipgaja.api;

import com.google.gson.annotations.SerializedName;

public class DetailResponse {
    @SerializedName("good1")
    String good1 = "";
    @SerializedName("good2")
    String good2 = "";
    @SerializedName("good3")
    String good3 = "";
    @SerializedName("good4")
    String good4 = "";

    @SerializedName("bad1")
    String bad1 = "";
    @SerializedName("bad2")
    String bad2 = "";
    @SerializedName("bad3")
    String bad3 = "";
    @SerializedName("bad4")
    String bad4 = "";

    public String getGood1() {
        return good1;
    }

    public void setGood1(String good1) {
        this.good1 = good1;
    }

    public String getGood2() {
        return good2;
    }

    public void setGood2(String good2) {
        this.good2 = good2;
    }

    public String getGood3() {
        return good3;
    }

    public void setGood3(String good3) {
        this.good3 = good3;
    }

    public String getGood4() {
        return good4;
    }

    public void setGood4(String good4) {
        this.good4 = good4;
    }

    public String getBad1() {
        return bad1;
    }

    public void setBad1(String bad1) {
        this.bad1 = bad1;
    }

    public String getBad2() {
        return bad2;
    }

    public void setBad2(String bad2) {
        this.bad2 = bad2;
    }

    public String getBad3() {
        return bad3;
    }

    public void setBad3(String bad3) {
        this.bad3 = bad3;
    }

    public String getBad4() {
        return bad4;
    }

    public void setBad4(String bad4) {
        this.bad4 = bad4;
    }
}
