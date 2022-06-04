package com.example.zipgaja;

public class Bike
{
    private String num;

    private String name;

    private float latitude;

    private float longitude;

    public String getNum ()
    {
        return num;
    }

    public void setNum (String num)
    {
        this.num = num;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public float getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (float latitude)
    {
        this.latitude = latitude;
    }

    public float getLongitude ()
    {
        return longitude;
    }

    public void setLongitude (float longitude)
    {
        this.longitude = longitude;
    }

    @Override
    public String toString()
    {
        return "[num = "+num+", name = "+name+", latitude = "+latitude+", longitude = "+longitude+"]";
    }
}