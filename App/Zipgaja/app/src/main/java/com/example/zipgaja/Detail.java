package com.example.zipgaja;

import java.io.Serializable;

public class Detail implements Serializable {
    private DetailData data;

    private String status;

    public DetailData getData ()
    {
        return data;
    }

    public void setData (DetailData data)
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
