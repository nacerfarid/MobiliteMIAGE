package com.example.nacerfarid.finaltest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nacer FARID on 20/03/2018.
 */

public class Arret
{
    @SerializedName("stopId")
    @Expose
    private String stopId;
    @SerializedName("trips")
    @Expose
    private List<Integer> trips = null;
    @SerializedName("stopName")
    @Expose
    private String stopName;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("parentStation")
    @Expose
    private String parentStation;

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public List<Integer> getTrips() {
        return trips;
    }

    public void setTrips(List<Integer> trips) {
        this.trips = trips;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getParentStation() {
        return parentStation;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }
}
