package com.example.zipgaja;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {
    private String routeID;

    private int cost;

    private String[] route;

    private String busNum;

    private int recommend;

    private float time;

    private String type;

    public String getRouteID ()
    {
        return routeID;
    }

    public void setRouteID (String routeID)
    {
        this.routeID = routeID;
    }

    public int getCost ()
    {
        return cost;
    }

    public void setCost (int cost)
    {
        this.cost = cost;
    }

    public String[] getRouteDetail ()
    {
        return route;
    }

    public void setRouteDetail (String[] route)
    {
        this.route = route;
    }

    public String getBusNum ()
    {
        return busNum;
    }

    public void setBusNum (String busNum)
    {
        this.busNum = busNum;
    }


    public int getRecommend ()
    {
        return recommend;
    }

    public void setRecommend (int recommend)
    {
        this.recommend = recommend;
    }

    public float getTime ()
    {
        return time;
    }

    public void setTime (float time)
    {
        this.time = time;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "[routeID = "+routeID+", cost = "+cost+", routeDetail = "+ route +", busNum = "+busNum+", recommend = "+recommend+", time = "+time+", type = "+type+"]";
    }
}
