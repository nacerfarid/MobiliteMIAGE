package com.example.nacerfarid.finaltest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nacerfarid.finaltest.Model.Ligne;
import com.example.nacerfarid.finaltest.Model.NearbyStop;
import com.example.nacerfarid.finaltest.Remote.NearbyStopsService;
import com.example.nacerfarid.finaltest.Utils.ServicesTag;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcheActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int REQUEST_LOCATION=1;
    private double latitude;
    private double longitude;
    NearbyStopsService nearbyStopsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proche);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        nearbyStopsService = ServicesTag.getNearbyPlaces();
    }

    private void getNearbyStops() {
        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longitude);
        String dist = "1000";
        String details = "true";
        nearbyStopsService.getNearbyPlaces(lon,lat,dist,details).enqueue(new Callback<List<NearbyStop>>() {
            @Override
            public void onResponse(Call<List<NearbyStop>> call, Response<List<NearbyStop>> response) {
                for(int i=0;i<response.body().size();i++){
                    for(NearbyStop nearbyStop : response.body()){
                        if(nearbyStop.getId().startsWith("SEM")) {
                            LatLng currentLoc = new LatLng(nearbyStop.getLat(), nearbyStop.getLon());
                            mMap.addMarker(new MarkerOptions().position(currentLoc).title(nearbyStop.getId() + "|" + nearbyStop.getName().substring(nearbyStop.getName().indexOf(", ")+2,nearbyStop.getName().length())).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bus)));
                            for (String ligne : nearbyStop.getLines()) {
                                Log.e("/////////////////////", ligne);
                            }
                        }
                    }
                }
                mMap.setOnInfoWindowClickListener(MyOnInfoWindowClickListener);
            }
            @Override
            public void onFailure(Call<List<NearbyStop>> call, Throwable t) {
                Log.e("ERROR_Nearby_Activity",t.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusCheck();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_LOCATION);
            return;
        }else{
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                LatLng currentLoc = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 12));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                getNearbyStops();
            }
        }


    }
    GoogleMap.OnInfoWindowClickListener MyOnInfoWindowClickListener
            = new GoogleMap.OnInfoWindowClickListener(){
        @Override
        public void onInfoWindowClick(Marker marker) {
            Toast.makeText(ProcheActivity.this,
                    "onInfoWindowClick():\n" +
                            marker.getPosition().latitude + "\n" +
                            marker.getPosition().longitude,
                    Toast.LENGTH_LONG).show();
        }
    };

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
