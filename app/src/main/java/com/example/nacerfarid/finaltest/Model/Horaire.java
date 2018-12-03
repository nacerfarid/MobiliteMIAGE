package com.example.nacerfarid.finaltest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Nacer FARID on 20/03/2018.
 */

public class Horaire
{
    @SerializedName("0")
    @Expose
    private Ligne ligne_1;
    @SerializedName("1")
    @Expose
    private Ligne ligne_2;

    public Ligne getLigne_1() {
        return ligne_1;
    }

    public void setLigne_1(Ligne ligne_1) {
        this.ligne_1 = ligne_1;
    }

    public Ligne getLigne_2() {
        return ligne_2;
    }

    public void setLigne_2(Ligne ligne_2) {
        this.ligne_2 = ligne_2;
    }
}
