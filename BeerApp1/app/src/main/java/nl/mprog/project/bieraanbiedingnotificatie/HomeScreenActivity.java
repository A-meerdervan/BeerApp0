package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String tag = "*C_HomeAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

//        // TODO dit gaat meteen naar naar het volgende scherm
//        TextView nonsens = (TextView)findViewById(R.id.html_content);
//        onClickNotificationButton(nonsens);
        setUpdateAndNotifyAlarm();
    }

    public void onClickNotificationButton(View view){
        // Go to the Notification managing Activity
        startActivity(new Intent(getApplicationContext(), NotificatieRegelActivity.class));
//        TODO: Fixen dat ie m wel finished maar dat je weer terug kunt vanuit het volgende scherm
//        this.finish();
    }

    public void onClickDiscountsButton(View view){
        // Go to the activity with all discounts
        Log.d(tag, "voorrrrrr");
        startActivity(new Intent(getApplicationContext(), AlleAanbiedingenActivity.class));
        Log.d(tag, "NAAAAAA");
//        TODO: Fixen dat ie m wel finished maar dat je weer terug kunt vanuit het volgende scherm
//        this.finish();
    }

    private void setUpdateAndNotifyAlarm(){
        AlarmManager alarmManager;
        PendingIntent alarmPendingIntent;

        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NightUpdate.class);
        alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

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
        int interval = 60 * 1000;

        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, alarmPendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
