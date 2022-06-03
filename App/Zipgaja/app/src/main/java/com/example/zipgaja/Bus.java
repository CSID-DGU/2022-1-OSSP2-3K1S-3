package com.example.zipgaja;

public class Bus
{
    private String ID;

    private String name;

    private String station_num;

    private String node_id;

    private String ars_id;

    private String station_name;

    private float latitude;

    private float longitude;

    public String getStation_name ()
    {
        return station_name;
    }

    public void setStation_name (String station_name)
    {
        this.station_name = station_name;
    }

    public String getStation_num ()
    {
        return station_num;
    }

    public void setStation_num (String station_num)
    {
        this.station_num = station_num;
    }

    public float getLatitude ()
    {
        return latitude;
    }

    public void setLatitude (float latitude)
    {
        this.latitude = latitude;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getArs_id ()
    {
        return ars_id;
    }

    public void setArs_id (String ars_id)
    {
        this.ars_id = ars_id;
    }

    public String getID ()
    {
        return ID;
    }

    public void setID (String ID)
    {
        this.ID = ID;
    }

    public String getNode_id ()
    {
        return node_id;
    }

    public void setNode_id (String node_id)
    {
        this.node_id = node_id;
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
        return "[station_name = "+station_name+", station_num = "+station_num+", latitude = "+latitude+", name = "+name+", ars_id = "+ars_id+", ID = "+ID+", node_id = "+node_id+", longitude = "+longitude+"]";
    }
}