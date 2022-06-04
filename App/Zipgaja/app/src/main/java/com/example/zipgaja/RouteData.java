package com.example.zipgaja;

import java.io.Serializable;

public class RouteData implements Serializable {
    private Route[] route;

    public Route[] getRoute ()
    {
        return route;
    }

    public void setRoute (Route[] route)
    {
        this.route = route;
    }

    @Override
    public String toString()
    {
        return "[route = "+route+"]";
    }
}
