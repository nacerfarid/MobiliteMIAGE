package com.example.nacerfarid.finaltest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nacerfarid.finaltest.Adapter.HoraireDirectionDeuxAdapter;
import com.example.nacerfarid.finaltest.Adapter.HoraireDirectionUneAdapter;
import com.example.nacerfarid.finaltest.Model.ArretHoraire;
import com.example.nacerfarid.finaltest.Model.Delay;
import com.example.nacerfarid.finaltest.Remote.HoraireByArretService;
import com.example.nacerfarid.finaltest.Utils.DateHeure.HeureTraitement;
import com.example.nacerfarid.finaltest.Utils.DataBase.dataBaseTAG;
import com.example.nacerfarid.finaltest.Utils.ServicesTag;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoraireArretActivity extends AppCompatActivity{
    ListView listViewUne;
    ListView listViewDeux;
    dataBaseTAG dbTag;
    SQLiteDatabase db;
    HoraireByArretService horaireByArretService;
    ArrayList<String> arretsZone;
    HoraireDirectionUneAdapter horaireDirectionUneAdapter;
    HoraireDirectionDeuxAdapter horaireDirectionDeuxAdapter;
    String arret,code,arretLabel,shortName;
    List<String> arrets, horaireString;
    List<Integer> horaire;
    List<String> directionUne,directionDeux;
    List<Integer> horaireUne,horaireDeux;
    List<String> horaireUneS,horaireDeuxS;
    HeureTraitement heureTraitement;
    Handler mHandler;
    @BindView(R.id.ligneArretName) TextView textView;
    @BindView(R.id.favoris) ImageButton ajoutFavo;
    @BindView(R.id.progressBar_arret) ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horaire_layout_activity);
        dbTag = new dataBaseTAG(HoraireArretActivity.this);
        db = dbTag.getWritableDatabase();
        ButterKnife.bind(this);
        final Bundle bundle = getIntent().getExtras();
        arret = bundle.getString("arret");
        arretLabel = bundle.getString("arretLabel");
        code = (String) bundle.getSerializable("code");
        horaireByArretService = ServicesTag.getHoraireByArretService();
        shortName = bundle.getString("shortName");

        arrets = new ArrayList<String>();
        horaire = new ArrayList<Integer>();
        horaireString = new ArrayList<String>();
        horaireUne = new ArrayList<Integer>();
        horaireDeux = new ArrayList<Integer>();
        directionUne = new ArrayList<String>();
        directionDeux = new ArrayList<String>();
        horaireDeuxS = new ArrayList<String>();
        horaireUneS = new ArrayList<String>();
        arretsZone = bundle.getStringArrayList("listeArrets");

        progressBar.setVisibility(View.VISIBLE);
        Cursor cursor = null;
        String sql = "SELECT * FROM ARRETS WHERE shortName = ?  AND label = ?;";
        cursor = db.rawQuery(sql,new String[]{shortName,arretLabel});
        if(cursor.getCount()>0){
            ajoutFavo.setImageDrawable(getResources().getDrawable(R.drawable.fav_full));
            ajoutFavo.setTag("full");
        }else{
            ajoutFavo.setImageDrawable(getResources().getDrawable(R.drawable.fav_empty));
            ajoutFavo.setTag("empty");
        }
        ajoutFavo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ajoutFavo.getTag().equals("empty")){
                    addToFavorite(shortName,arretLabel,bundle.getString("color"),bundle.getString("txtColor"),arret);
                    ajoutFavo.setTag("full");
                    ajoutFavo.setImageDrawable(getResources().getDrawable(R.drawable.fav_full));
                    Toast.makeText(getApplicationContext(),"Ajouté au favoris",Toast.LENGTH_SHORT).show();
                }else if(ajoutFavo.getTag().equals("full")){
                    removeFromFavorite(shortName,arretLabel);
                    ajoutFavo.setTag("empty");
                    ajoutFavo.setImageDrawable(getResources().getDrawable(R.drawable.fav_empty));
                    Toast.makeText(getApplicationContext(),"Retiré du favoris",Toast.LENGTH_SHORT).show();
                }
            }
        });


        addToLastVisisted(shortName,arretLabel,bundle.getString("color"),bundle.getString("txtColor"),
                arret,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        getArretHoraireByParentStation(arret);
        heureTraitement = new HeureTraitement();
        listViewUne = findViewById(R.id.DirectionUneView);
        listViewDeux = findViewById(R.id.DirectionDeuxView);

        textView.setText(bundle.getString("shortName")+"|"+arretLabel);
        textView.setBackgroundColor(Color.parseColor("#"+bundle.getString("color")));
        textView.setTextColor(Color.parseColor("#"+bundle.getString("txtColor")));
    }

    private void getArretHoraireByParentStation(final String arret)
    {
        horaireByArretService.getHoraireByArret(arret).enqueue(new Callback<List<ArretHoraire>>()
        {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<ArretHoraire>> call, Response<List<ArretHoraire>> response)
            {
                for(int i=0;i<response.body().size();i++)
                {
                    for(Delay delay : response.body().get(i).getTimes()) {
                        if (response.body().get(i).getPattern().getId().contains(code +":")==true && response.body().get(i).getPattern().getId().startsWith("SEM")) {
                            if(response.body().get(i).getPattern().getDir()==1)
                            {
                                if(delay.getRealtimeDeparture()!=null) {
                                    horaireDeux.add(delay.getRealtimeDeparture());
                                    String date = heureTraitement.TimeDifference(delay.getRealtimeDeparture());
                                    horaireDeuxS.add(date);
                                    directionDeux.add(response.body().get(i).getPattern().getDesc());
                                }else{
                                    horaireDeuxS.add("");
                                    directionDeux.add("Terminus " + response.body().get(i).getPattern().getDesc());
                                }
                            }
                            if(response.body().get(i).getPattern().getDir()==2)
                            {
                                if(delay.getRealtimeDeparture()!=null) {
                                    horaireUne.add(delay.getRealtimeDeparture());
                                    String date = heureTraitement.TimeDifference(delay.getRealtimeDeparture());
                                    horaireUneS.add(date);
                                    directionUne.add(response.body().get(i).getPattern().getDesc());
                                }else{
                                    horaireUneS.add("");
                                    directionUne.add("Terminus " + response.body().get(i).getPattern().getDesc());
                                }
                            }
                        }
                    }
                }
                horaireDirectionUneAdapter = new HoraireDirectionUneAdapter(HoraireArretActivity.this,directionUne,horaireUneS);
                horaireDirectionDeuxAdapter = new HoraireDirectionDeuxAdapter(HoraireArretActivity.this,directionDeux,horaireDeuxS);
                listViewUne.setAdapter(horaireDirectionUneAdapter);
                listViewDeux.setAdapter(horaireDirectionDeuxAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ArretHoraire>> call, Throwable t)
            {
                Log.e("ERROR_3",t.getMessage());
                progressBar.setVisibility(View.GONE);
                showAlertDialog(getApplicationContext());
            }
        });
    }

    private boolean addToLastVisisted(String shortName, String arretLabel, String color, String txtColor,String arretCode,String lastVisited) {
        dbTag.insertToLastVisited(db,shortName,arretLabel,color,txtColor,arretCode,lastVisited);
        return true;
    }

    private boolean addToFavorite(String shortName, String arretLabel, String color, String txtColor,String arretCode) {
        dbTag.insertToFavorite(db,shortName,arretLabel,color,txtColor,arretCode);
        return true;
    }
    private boolean removeFromFavorite(String shortName, String arretLabel) {
        dbTag.removeFromFavorite(db,shortName,arretLabel);
        return true;
    }

    private void showAlertDialog(Context context){
        AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper(this, R.style.Theme_AppCompat_Dialog_Alert)).create();
        alertDialog.setTitle("Problème API");

        alertDialog.setMessage("Les données de l'API sont momentanément indisponibles, réessayer ultérierement");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Page d'accueil", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(HoraireArretActivity.this,MainActivity.class);
                startActivity(intent);
            }});
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
