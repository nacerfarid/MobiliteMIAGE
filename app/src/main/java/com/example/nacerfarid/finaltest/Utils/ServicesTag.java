package com.example.nacerfarid.finaltest.Utils;

import com.example.nacerfarid.finaltest.Remote.DestinationsByArretService;
import com.example.nacerfarid.finaltest.Remote.HoraireByArretService;
import com.example.nacerfarid.finaltest.Remote.NearbyStopsService;
import com.example.nacerfarid.finaltest.Remote.RetrofitClient;
import com.example.nacerfarid.finaltest.Remote.RouteByCodeService;
import com.example.nacerfarid.finaltest.Remote.RoutesService;

public class ServicesTag {
    private static final String BASE_URL = "http://data.metromobilite.fr";

    public static RoutesService getRouteService()
    {
        return RetrofitClient.getClient(BASE_URL).create(RoutesService.class);
    }

    public static RouteByCodeService getRouteByCodeService()
    {
        return RetrofitClient.getClient(BASE_URL).create(RouteByCodeService.class);
    }

    public static HoraireByArretService getHoraireByArretService()
    {
        return RetrofitClient.getClient(BASE_URL).create(HoraireByArretService.class);
    }

    public static DestinationsByArretService getDestinationsByArret()
    {
        return RetrofitClient.getClient(BASE_URL).create(DestinationsByArretService.class);
    }
    public static NearbyStopsService getNearbyPlaces()
    {
        return RetrofitClient.getClient(BASE_URL).create(NearbyStopsService.class);
    }

}
