package com.example.zipgaja;

public class StationData
{
    private Bus[] bus;

    private Bike[] bike;

    public Bus[] getBus ()
    {
        return bus;
    }

    public void setBus (Bus[] bus)
    {
        this.bus = bus;
    }

    public Bike[] getBike ()
    {
        return bike;
    }

    public void setBike (Bike[] bike)
    {
        this.bike = bike;
    }

    @Override
    public String toString()
    {
        return "[bus = "+bus+", bike = "+bike+"]";
    }
}