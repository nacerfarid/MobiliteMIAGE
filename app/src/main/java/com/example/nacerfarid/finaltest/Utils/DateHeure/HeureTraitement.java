package com.example.nacerfarid.finaltest.Utils.DateHeure;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nacer FARID on 24/03/2018.
 */

public class HeureTraitement {
    public HeureTraitement(){

    }
    public String TimeDifference(Integer delay) {

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long milliseconds = delay * 1000;
        cal.add(Calendar.MILLISECOND, (int) milliseconds);
        //cal.add(Calendar.HOUR,-1);
        Date d1 = cal.getTime();

        Timestamp ts = new Timestamp(new Date().getTime());
        Timestamp ts1 = new Timestamp(d1.getTime());

        long milli = Math.abs(ts1.getTime() - ts.getTime());
        int sec = (int) milli/1000;
        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = (sec % 3600) % 60;

        String time;
        if(minutes<30 && hours<1){
            time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            if(minutes<1){
                time = "A l'approche";
            }
        }else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            time = dateFormat.format(d1);

        }
        return time;
    }
}
