package com.example.nacerfarid.finaltest.Remote;

import com.example.nacerfarid.finaltest.Model.NearbyStop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NearbyStopsService {
    @GET("/api/linesNear/json")
    Call<List<NearbyStop>> getNearbyPlaces(@Query("x") String longitude,
                                           @Query("y") String latitude,
                                           @Query("dist") String distance,
                                           @Query("details") String value);
}
