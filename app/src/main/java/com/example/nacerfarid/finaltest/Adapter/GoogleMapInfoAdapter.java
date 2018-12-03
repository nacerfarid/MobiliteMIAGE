package com.example.nacerfarid.finaltest.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nacerfarid.finaltest.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class GoogleMapInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public GoogleMapInfoAdapter(Context ctx){
        context = ctx;
    }

    public View GoogleMapInfoAdapter(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.pic);

        TextView hotel_tv = view.findViewById(R.id.hotels);
        TextView food_tv = view.findViewById(R.id.food);
        TextView transport_tv = view.findViewById(R.id.transport);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        //InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();


        return view;
    }
}