package com.example.zipgaja;

import java.io.Serializable;
import java.util.Arrays;

public class Route implements Serializable {
    private String routeID;

    private int cost;

    private String[] routeDetail;

    private String busNum;

    private String recommend;

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
        return routeDetail;
    }

    public void setRouteDetail (String[] routeDetail)
    {
        this.routeDetail = routeDetail;
    }

    public String getBusNum ()
    {
        return busNum;
    }

    public void setBusNum (String busNum)
    {
        this.busNum = busNum;
    }


    public String getRecommend ()
    {
        return recommend;
    }

    public void setRecommend (String recommend)
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
        return "[routeID = "+routeID+", cost = "+cost+", route = "+ Arrays.toString(routeDetail) +", busNum = "+busNum+", recommend = "+recommend+", time = "+time+", type = "+type+"]";
    }
}
