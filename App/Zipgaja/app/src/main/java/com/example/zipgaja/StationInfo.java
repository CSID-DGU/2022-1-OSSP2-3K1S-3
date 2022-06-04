package com.example.zipgaja;

public class StationInfo
{
    private StationData[] data;

    private int status;

    public StationData[] getData ()
    {
        return data;
    }

    public void setData (StationData[] data)
    {
        this.data = data;
    }

    public int getStatus ()
    {
        return status;
    }

    public void setStatus (int status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "[data = "+data+", status = "+status+"]";
    }
}

