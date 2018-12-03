package com.example.nacerfarid.finaltest.Service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.nacerfarid.finaltest.FavorisActivity;
import com.example.nacerfarid.finaltest.Model.ArretHoraire;
import com.example.nacerfarid.finaltest.R;
import com.example.nacerfarid.finaltest.Remote.HoraireByArretService;
import com.example.nacerfarid.finaltest.Utils.DateHeure.HeureTraitement;
import com.example.nacerfarid.finaltest.Utils.ServicesTag;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class notificationService extends Service
{
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    String notifTitle= "";
    int notifIcon = 0;
    String message;
    private int NOTIFICATION_ID = 1;

    private HeureTraitement heureTraitement;
    StringBuilder stringBuilder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        heureTraitement = new HeureTraitement();
        stringBuilder = new StringBuilder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String codeArret = intent.getStringExtra("codeArret");
        String numTransport = intent.getStringExtra("numTransport");
        String nomStation = intent.getStringExtra("nomStation");
        stringBuilder.setLength(0);
        showNotification(codeArret,numTransport,nomStation);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public void showNotification(final String codeArret, final String numTransport, final String nomStation)
    {
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //call the horaireService

                HoraireByArretService horaireService = ServicesTag.getHoraireByArretService();
                horaireService.getHoraireByArret(codeArret).enqueue(new Callback<List<ArretHoraire>>()
                {
                    @Override
                    public void onResponse(Call<List<ArretHoraire>> call, Response<List<ArretHoraire>> response)
                    {
                        stringBuilder.setLength(0);
                        String date1 = "";
                        String date2 = "";
                        String destination1 = "";
                        String destination2 = "";
                        ArretHoraire ah = response.body().get(0);
                        ArretHoraire arretHoraire = response.body().get(1);
                            if(ah.getPattern().getId().contains(numTransport + ":")&& ah.getPattern().getId().startsWith("SEM")) {

                                if (ah.getTimes().get(0).getRealtimeDeparture() != null) {
                                    date1 = heureTraitement.TimeDifference(ah.getTimes().get(0).getRealtimeDeparture());
                                    destination1 = ah.getPattern().getDesc();
                                }
                            }
                            if(arretHoraire.getPattern().getId().contains(numTransport + ":") && arretHoraire.getPattern().getId().startsWith("SEM")) {
                                if (arretHoraire.getTimes().get(0).getRealtimeDeparture() != null) {
                                    date2 = heureTraitement.TimeDifference(arretHoraire.getTimes().get(0).getRealtimeDeparture());
                                    destination2 = arretHoraire.getPattern().getDesc();
                                }
                            }
                            stringBuilder.append(numTransport + " | " + nomStation + "\n");
                            stringBuilder.append("* " + date1 + " vers " +destination1 + "\n");
                            stringBuilder.append("* " + date2 + " vers " +destination2 + "\n");
                        buildNotification(numTransport,nomStation,stringBuilder);
                    }

                    @Override
                    public void onFailure(Call<List<ArretHoraire>> call, Throwable t)
                    {
                        Log.e("bg_Service_Error",t.getMessage());
                    }
                });


            }
        },5000);
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                showNotification(codeArret,numTransport,nomStation);
                mHandler.postDelayed(this,3000);
            }
        },3000);
    }


    private void buildNotification(String numTransport,String nomStation,StringBuilder message)
    {
        StringBuilder test = new StringBuilder();
        test = message;
        Intent intent = new Intent(notificationService.this, FavorisActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(notificationService.this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NOTIFICATION_SERVICE)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Prochains passages:")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(test));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
