package com.example.nacerfarid.finaltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.nacerfarid.finaltest.Adapter.GridViewAdapter;
import com.example.nacerfarid.finaltest.Model.Arret;
import com.example.nacerfarid.finaltest.Model.Horaire;
import com.example.nacerfarid.finaltest.Model.Route;
import com.example.nacerfarid.finaltest.Remote.RouteByCodeService;
import com.example.nacerfarid.finaltest.Remote.RoutesService;
import com.example.nacerfarid.finaltest.Utils.ServicesTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nacer FARID on 19/03/2018.
 */

public class LigneActivity extends AppCompatActivity
{
    @BindView(R.id.gridview) GridView itemsGridView;
    @BindView(R.id.TypeTransport) TextView textView;
    RoutesService routeService;
    List<String> routesNames,bgColors, textColors,parentStationList,zonesArrets;
    RouteByCodeService routeByCodeService;
    HashMap<String,String> idShortName;
    String type;
    Bundle bundle1 = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_arret);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        routesNames = new ArrayList<String>();
        zonesArrets = new ArrayList<String>();
        bgColors = new ArrayList<String>();
        textColors = new ArrayList<String>();
        idShortName = new HashMap<>();

        parentStationList = new ArrayList<String>();

        textView.setText("Lignes : " + type);
        //textView.setBackgroundColor(bundle.getInt("couleur"));
        routeService = ServicesTag.getRouteService();
        //Get All Routes List
        getRoutesList();

        routeByCodeService = ServicesTag.getRouteByCodeService();

        itemsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                //Get SelectedItem id from idShortName hashMap
                String shortName =  parent.getItemAtPosition(position).toString();
                String code = idShortName.get(shortName);


                bundle1.putString("shortName",parent.getItemAtPosition(position).toString());
                bundle1.putString("code",code);
                bundle1.putString("color",bgColors.get(position));
                bundle1.putString("txtColor",textColors.get(position));
                getArretsByCode(code);

//                    Log.e("ERROR",code);



            }
        });
    }

    private void getArretsByCode(final String code)
    {
        routeByCodeService.getRouteByCode(code).enqueue(new Callback<Horaire>()
        {
            @Override
            public void onResponse(Call<Horaire> call, Response<Horaire> response)
            {
                zonesArrets.clear();
                for(Arret arret : response.body().getLigne_1().getArrets())
                {
                    zonesArrets.add(arret.getStopName());
                    parentStationList.add(arret.getParentStation());
                }

                //Bundle bundle = new Bundle();
                bundle1.putSerializable("zonesList",new ArrayList<String>(zonesArrets));
                bundle1.putSerializable("parentStationList",new ArrayList<String>(parentStationList));
                bundle1.putString("code",code);

                Intent arretZonesIntent = new Intent(LigneActivity.this, ArretZonesActivity.class);
                arretZonesIntent.putExtras(bundle1);

                startActivity(arretZonesIntent);
                finish();
            }

            @Override
            public void onFailure(Call<Horaire> call, Throwable t)
            {
                Log.e("ERROR_2",t.getMessage());
            }
        });
    }
    HashMap<String,HashMap<String,String>> routeProperties = new HashMap<>();

    Map<String,HashMap<String,String>> map;

    private void getRoutesList()
    {
        routeService.getRoutes().enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response)
            {
                for(Route route : response.body())
                {
                    if(route.getId().startsWith("SEM")&&route.getType().equals(type))
                    {
                        HashMap<String,String> Color_TextColor = new HashMap<>();
                        Color_TextColor.put(route.getColor(),route.getTextColor());
                        idShortName.put(route.getShortName(), route.getId());
                        routeProperties.put(route.getShortName(),Color_TextColor);
                    }
                }
                map = new TreeMap<String,HashMap<String,String>>(routeProperties);
                Set set2 = map.entrySet();
                routesNames.addAll(map.keySet());
                for (Map.Entry<String, HashMap<String, String>> letterEntry : map.entrySet()) {
                    for (Map.Entry<String, String> nameEntry : letterEntry.getValue().entrySet()) {
                        bgColors.add(nameEntry.getKey());
                        textColors.add(nameEntry.getValue());
                    }
                }
                GridViewAdapter gridViewAdapter = new GridViewAdapter(LigneActivity.this,routesNames,bgColors,textColors);
                itemsGridView.setAdapter(gridViewAdapter);
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t)
            {
                Log.e("ERROR",t.getMessage());
            }
        });
    }

}
