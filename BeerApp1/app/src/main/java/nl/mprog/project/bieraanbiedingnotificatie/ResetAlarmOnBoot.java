package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Alex on 21-1-2016.
 *
 * The app is sheduled to update its discount information evey night
 * This stops when the phone reboots, but this class listens for a reboot and then starts the alarm
 * again
 */
public class ResetAlarmOnBoot extends BroadcastReceiver{

    // The refresf interval is every TODO: zeg de juiste, nu 2 min maar graag 1 dag
    private static final Integer INTERVAL = 2 * 60 * 1000;

    @Override
    public void onReceive(Context appContext, Intent intent) {
        Toast.makeText(appContext, "In onreceive after boot", Toast.LENGTH_SHORT).show();

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set the alarm again:
            Toast.makeText(appContext, "In if statemtn onReceive", Toast.LENGTH_SHORT).show();

            setUpdateAndNotifyAlarm(appContext);
        }
    }
    private void setUpdateAndNotifyAlarm(Context appContext){
        AlarmManager alarmManager;
        PendingIntent alarmPendingIntent;

        alarmManager = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(appContext, NightUpdate.class);
        alarmPendingIntent = PendingIntent.getBroadcast(appContext, 0, intent, 0);

        // Set the alarm to start at 4:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 30);

        // Make the alarm fire once, now that the device has booted
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), alarmPendingIntent);
        // Set the alarm to fire comming night and then repeat after an interval of one day
        // This commented line will set the alarm for now and then repeat, with an interval of 2 min
//        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), INTERVAL, alarmPendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmPendingIntent);
        Toast.makeText(appContext, "Alarm set again after boot.", Toast.LENGTH_SHORT).show();
    }
}
