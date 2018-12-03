package com.example.nacerfarid.finaltest.Remote;

import com.example.nacerfarid.finaltest.Model.ArretHoraire;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Nacer FARID on 20/03/2018.
 */

public interface HoraireByArretService {
    @GET("/api/routers/default/index/clusters/{station}/stoptimes")
    Call<List<ArretHoraire>> getHoraireByArret(@Path("station") String station);
}
