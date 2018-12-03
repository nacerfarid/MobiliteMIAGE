package com.example.nacerfarid.finaltest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nacer FARID on 20/03/2018.
 */

public class Ligne
{
    @SerializedName("arrets")
    @Expose
    private List<Arret> arrets = null;
    @SerializedName("prevTime")
    @Expose
    private long prevTime;
    @SerializedName("nextTime")
    @Expose
    private long nextTime;

    public List<Arret> getArrets() {
        return arrets;
    }

    public void setArrets(List<Arret> arrets) {
        this.arrets = arrets;
    }

    public long getPrevTime() {
        return prevTime;
    }

    public void setPrevTime(long prevTime) {
        this.prevTime = prevTime;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }
}
