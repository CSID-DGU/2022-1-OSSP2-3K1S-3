package com.example.zipgaja;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StationInfo {
    @SerializedName("status")
    int status;

    public int getStatus() {
        return status;
    }

    public StationInfo (int st) {
        this.status = st;
    }

    public List<stationData> sdata = new ArrayList<>();
    public List<stationData> getStationData() {
        return sdata;
    }

    public static class stationData {
        public List<bikeInfo> bikeStation = new ArrayList<>();
        public List<bikeInfo> getBikeStation() { return bikeStation; }

        public static class bikeInfo {
            @SerializedName("num")
            String num;
            @SerializedName("name")
            String name;
            @SerializedName("latitude")
            float latitude;
            @SerializedName("longitude")
            float longitude;

            public String getNum() {
                return num;
            }
            public String getName() {
                return name;
            }
            public float getLatitude() {
                return latitude;
            }
            public float getLongitude() {
                return longitude;
            }
        }

        public List<busInfo> busStation = new ArrayList<>();
        public List<busInfo> getBusStation() { return busStation; }

        public static class busInfo {
            @SerializedName("stationId")
            String stationId;
            @SerializedName("stationNm")
            String stationNm;
            @SerializedName("gpsX")
            float gpsX;
            @SerializedName("gpsY")
            float gpsY;
//            @SerializedName("posX")
//            float posX;
//            @SerializedName("posY")
//            float posY;
//            @SerializedName("stationTp")
//            String stationTp;
//            @SerializedName("arsId")
//            String arsId;
//            @SerializedName("dist")
//            String dist;

            public String getStationId() {
                return stationId;
            }
            public String getStationNm() {
                return stationNm;
            }
            public float getGpsX() {
                return gpsX;
            }
            public float getGpsY() {
                return gpsY;
            }
        }
    }

}
