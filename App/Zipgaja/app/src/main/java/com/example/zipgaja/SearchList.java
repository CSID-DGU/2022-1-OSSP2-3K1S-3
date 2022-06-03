package com.example.zipgaja;

import java.io.Serializable;

public class SearchList implements Serializable {
    private RouteData[] data;

    private String status;

    public RouteData[] getData ()
    {
        return data;
    }

    public void setData (RouteData[] data)
    {
        this.data = data;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "[data = "+data+", status = "+status+"]";
    }
}
