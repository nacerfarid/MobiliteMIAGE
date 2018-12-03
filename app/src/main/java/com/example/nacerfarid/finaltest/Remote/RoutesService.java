package com.example.nacerfarid.finaltest.Remote;

import com.example.nacerfarid.finaltest.Model.Route;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Nacer FARID on 18/03/2018.
 */

public interface RoutesService
{
    @GET("/api/routers/default/index/routes/")
    Call<List<Route>> getRoutes();
}
