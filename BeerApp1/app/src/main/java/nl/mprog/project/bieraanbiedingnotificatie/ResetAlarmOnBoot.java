package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Alex on 21-1-2016.
 *
 * The app is sheduled to update its discount information evey night
 * This stops when the phone reboots, but this class listens for a reboot and then starts the alarm
 * again
 */
public class ResetAlarmOnBoot extends BroadcastReceiver{
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

//        // Set the alarm to start at 8:30 a.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.MINUTE, 30);

//// setRepeating() lets you specify a precise custom interval--in this case,
//// 20 minutes.
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 60 * 20, alarmPendingIntent);

        // 10 seconds
        int interval = 20 * 1000;

        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, alarmPendingIntent);
        Toast.makeText(appContext, "Alarm Set, end of SetupdateAndNotifyAlarm", Toast.LENGTH_SHORT).show();
    }
}
