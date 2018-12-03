package com.example.nacerfarid.finaltest.Remote;

import com.example.nacerfarid.finaltest.Model.Horaire;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nacer FARID on 17/03/2018.
 */

public interface RouteByCodeService
{
    @GET("/api/ficheHoraires/json")
    Call<Horaire> getRouteByCode(@Query("route") String route);
}
