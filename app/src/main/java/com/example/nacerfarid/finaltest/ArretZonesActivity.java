package com.example.nacerfarid.finaltest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nacerfarid.finaltest.Remote.DestinationsByArretService;
import com.example.nacerfarid.finaltest.Utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArretZonesActivity extends AppCompatActivity{
    ArrayList<String> arretZonesList,parentStationList;
    ArrayList<String> arretZonesFinalList;
    DestinationsByArretService horaireDest;
    HashMap<String,String> parentStationMap;
    String code;
    @BindView(R.id.zonesListView) ListView arretZones;
    @BindView(R.id.ligneName) TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arretzones);
        ButterKnife.bind(this);
        arretZonesList = new ArrayList<String>();
        arretZonesFinalList = new ArrayList<String>();
        parentStationList = new ArrayList<String>();
        parentStationMap = new HashMap<String, String>();

        final Bundle bundle = getIntent().getExtras();
        arretZonesFinalList.clear();
        parentStationList.clear();
        parentStationMap.clear();
        horaireDest = ServicesTag.getDestinationsByArret();
        arretZonesList = (ArrayList)bundle.getSerializable("zonesList");
        parentStationList = (ArrayList)bundle.getSerializable("parentStationList");
        code = (String) bundle.getSerializable("code");

        for(String stopName : arretZonesList)
        {
            String[] output = stopName.split(",");
            arretZonesFinalList.add(output[1]);
        }

        for(int i=0; i < parentStationList.size(); i++ )
        {
            parentStationMap.put(arretZonesFinalList.get(i),parentStationList.get(i));
        }

        ArrayAdapter<String> arretZonesAdapter = new ArrayAdapter<String>(ArretZonesActivity.this,android.R.layout.simple_list_item_1,arretZonesFinalList);
        arretZones.setAdapter(arretZonesAdapter);

        textView.setText(bundle.getString("shortName"));
        textView.setBackgroundColor(Color.parseColor("#"+bundle.getString("color")));
        textView.setTextColor(Color.parseColor("#"+bundle.getString("txtColor")));

        arretZones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                String arret = adapterView.getAdapter().getItem(position).toString();
                Intent intent = new Intent(ArretZonesActivity.this,HoraireArretActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("shortName",bundle.getString("shortName"));
                bundle1.putString("color",bundle.getString("color"));
                bundle1.putString("txtColor",bundle.getString("txtColor"));
                bundle1.putString("arret",parentStationMap.get(arret));
                bundle1.putString("arretLabel",arret);
                bundle1.putStringArrayList("listeArrets", arretZonesFinalList);
                bundle1.putSerializable("code",bundle.getSerializable("code"));
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }
}
