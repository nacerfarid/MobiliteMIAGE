package com.example.nacerfarid.finaltest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.buttonTRAM) Button buttonTRAM;
    @BindView(R.id.buttonPROXIMO) Button buttonPROXIMO;
    @BindView(R.id.buttonFLEXO) Button buttonFLEXO;
    @BindView(R.id.buttonCHRONO) Button buttonCHRONO;
    @BindView(R.id.buttonFAVORIS) ImageButton buttonFavorites;
    @BindView(R.id.markerMAP) ImageButton buttonMAP;
    @BindView(R.id.historyStops) ImageButton buttonHistory;


    Drawable couleur;

    String type = "type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isNetworkAvailable()){
            showAlertDialog(this);
        }

        setContentView(R.layout.activity_main);

        final Intent intentDonnes = new Intent(MainActivity.this,LigneActivity.class);
        final Bundle bundle = new Bundle();


        ButterKnife.bind(this);
        buttonCHRONO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur = (buttonCHRONO.getBackground());
                fillIntent(bundle, intentDonnes, "CHRONO");
            }
        });
        buttonFLEXO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur = (buttonFLEXO.getBackground());
                fillIntent(bundle, intentDonnes, "FLEXO");
            }
        });
        buttonPROXIMO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur = (buttonPROXIMO.getBackground());
                fillIntent(bundle, intentDonnes, "PROXIMO");
            }
        });
        buttonTRAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couleur = (buttonTRAM.getBackground());
                fillIntent(bundle, intentDonnes, "TRAM");
            }
        });
        buttonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FavorisActivity.class);
                startActivity(intent);
            }
        });

        buttonMAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProcheActivity.class);
                startActivity(intent);
            }
        });
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void fillIntent(Bundle bundle, Intent intentDonnes, String Type) {
        bundle.putString(type, Type);
        intentDonnes.putExtras(bundle);
        startActivity(intentDonnes);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected;
        if (activeNetworkInfo != null && (activeNetworkInfo.isConnected() || activeNetworkInfo.isConnectedOrConnecting())) {
            // we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }

    private void showAlertDialog(Context context){
        AlertDialog alertDialog = new AlertDialog.Builder( new ContextThemeWrapper(this, R.style.Theme_AppCompat_Dialog_Alert)).create();
        //AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Pas d'internet");

        alertDialog.setMessage("Votre téléphone n'est pas connecté, veuillez vérifier votre connexion internet et réessayer");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Réessayer", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                finish();
                startActivity(getIntent());
            } });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            }});
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
