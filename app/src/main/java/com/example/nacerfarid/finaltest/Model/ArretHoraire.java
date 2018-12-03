package com.example.nacerfarid.finaltest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nacer FARID on 21/03/2018.
 */

public class ArretHoraire
{
    @SerializedName("pattern")
    @Expose
    private Pattern pattern;
    @SerializedName("times")
    @Expose
    private List<Delay> times = null;

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public List<Delay> getTimes() {
        return times;
    }

    public void setTimes(List<Delay> times) {
        this.times = times;
    }
}
