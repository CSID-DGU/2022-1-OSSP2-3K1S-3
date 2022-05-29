package com.example.zipgaja;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Station {
    Retrofit instance;
    String BASEURL = "http://ec2-107-23-186-215.compute-1.amazonaws.com:5000";
    public Retrofit getInstance() {
        if(instance == null) {
            // instance = new Station();
            instance = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }

    // bike
    String num;
    String name;
    float latitude;
    float longitude;

    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }
    public void setName(String num) {
        this.num = num;
    }
    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


    // bus
    String stationId;
    String stationNm;
    float gpsX;
    float gpsY;
    float posX;
    float posY;
    String stationTp;
    String arsId;
    String dist;

    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
    public String getStationNm() {
        return stationNm;
    }
    public void setStationNm(String stationNm) {
        this.stationNm = stationNm;
    }
    public float getGpsX() {
        return gpsX;
    }
    public void setGpsX(float gpsX) {
        this.gpsX = gpsX;
    }
    public float getGpsY() {
        return gpsY;
    }
    public void setGpsY(float gpsY) {
        this.gpsY = gpsY;
    }


}
