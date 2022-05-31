package com.example.zipgaja.api;

import com.google.gson.annotations.SerializedName;

public class LessMoneyResponseData {
    @SerializedName("route")
    String route = "";

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTimev() {
        return timev;
    }

    public void setTimev(float timev) {
        this.timev = timev;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("name")
    String name = "";

    @SerializedName("time")
    float timev = 0.0f;

    @SerializedName("cost")
    int cost = 0;

    @SerializedName("type")
    String type = "";
}
