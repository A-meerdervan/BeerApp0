package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alex on 15-1-2016.
 *
 * This class holds the homescreen. It has two buttons to navigate to the rest of the app.
 * When it is fist started it sets a periodic alarm to update the app every night.
 * If the user has not internet the first time this is app is started, no further navigation is
 * possible and the user is asked to get an internet connection and then try again.
 */

public class HomeScreenActivity extends AppCompatActivity {

    private static final String tag = "*C_HomeAct";
    private DataBaseHandler dataBaseHandler;
    private HtmlParser htmlParser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        htmlParser = new HtmlParser(getApplicationContext());
        dataBaseHandler = new DataBaseHandler(getApplicationContext());

        // Hide the loading spinner
        findViewById(R.id.loadSpinnerHomeAct).setVisibility(View.GONE);

        // Find out whether this is the first boot of this app
        SharedPreferences prefs = this.getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Boolean firstAppBoot = prefs.getBoolean("firstAppBoot", true);
        Boolean noSuccesfulUpdateYet = prefs.getBoolean("noSuccesfulUpdateYet", true);
        // Only set update alarm if it has not been set before
        if (firstAppBoot){
            // prevent the user from going to the next activities.
            Log.d(tag, "make buttons NOT clickable");
            makeButtonsClickable(false);
            // Show loading spinner
            findViewById(R.id.loadSpinnerHomeAct).setVisibility(View.VISIBLE);
            // Get the discount data for the first time (on a separate thread)
            HtmlParseAsyncTask htmlParseAsyncTask = new HtmlParseAsyncTask();
            htmlParseAsyncTask.execute();
            // This sets the task to update discount info every night
            setUpdateAndNotifyAlarm();
            // let the app now that next boot will not be the first
            editor.putBoolean("firstAppBoot", false);
            editor.commit();
            return;
        }
        Log.d(tag, "NOT first boot, did not set periodic alarm");
        // If there has never been a succesful update the user must be shown only
        // A button to try and refresh, instead of being able to continue to other activities
        if (noSuccesfulUpdateYet){
            // The database is already filled, so set the noSuccesfulUpdateYet to false
            if (dataBaseHandler.getAllDiscounts().size() > 0 ){
                Log.d(tag, "database is filled, so set the bool to false" + " size is: " + dataBaseHandler.getAllDiscounts().size());
                editor.putBoolean("noSuccesfulUpdateYet", false);
                editor.commit();
                return;
            }
            // prevent the user from going to the next activities.
            Log.d(tag, "make buttons NOT clickable");
            makeButtonsClickable(false);
            Log.d(tag, "Nog geen succesvolle update hiervoor gehad");
            // Show loading spinner
            findViewById(R.id.loadSpinnerHomeAct).setVisibility(View.VISIBLE);
            // Get the discount data for the first time (on a separate thread)
            HtmlParseAsyncTask htmlParseAsyncTask = new HtmlParseAsyncTask();
            htmlParseAsyncTask.execute();
        }
    }

    public void onClickNotificationButton(View view){
        // Go to the Notification managing Activity
        startActivity(new Intent(getApplicationContext(), NotificatieRegelActivity.class));
    }

    public void onClickDiscountsButton(View view){
        // Go to the activity with all discounts
        startActivity(new Intent(getApplicationContext(), AlleAanbiedingenActivity.class));
    }

    // This function hides or shows a message that there is no internet, it also
    // enables or disables further navigation
    private void showUserNoConnectionMessage(boolean show){
        TextView TV = (TextView)findViewById(R.id.noConnectionTV);
        if (show){
            makeButtonsClickable(false);
            TV.setVisibility(View.VISIBLE);
        }
        else{
            makeButtonsClickable(true);
            TV.setVisibility(View.GONE);
        }
    }

    // This function prevents the user from being able to navigate further
    private void makeButtonsClickable(boolean clickable){
        Button button1 = (Button)findViewById(R.id.notificationScreenButton);
        Button button2 = (Button)findViewById(R.id.AllDiscountsButton);
        button1.setClickable(clickable);
        button2.setClickable(clickable);
    }

    // This function sets the folowing task every night; Update discount info and push notifications
    // if necessary.
    private void setUpdateAndNotifyAlarm(){

        AlarmManager alarmManager;
        PendingIntent alarmPendingIntent;

        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NightUpdate.class);
        alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        // Set the alarm to start at 4:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 30);

        // Set the alarm to fire comming night and then repeat after an interval of one day
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmPendingIntent);
        Log.d(tag, "FIRST BOOT, Periodic update alarm set!");
    }

    private void showDialog(){
        final AlertDialog.Builder winAlertBuilder = new AlertDialog.Builder(this);
        winAlertBuilder.setTitle("Oeps");
        winAlertBuilder.setMessage("Mogelijk heb je geen internet.\nStart de app opnieuw met internet aan");

        AlertDialog winAlertDialog = winAlertBuilder.create();
        winAlertDialog.show();
    }

    // This class is run only on the first day the application is started.
    // It updates the discount info by parsing html.
    private class HtmlParseAsyncTask extends AsyncTask<Void, Void, Void> {

        private Boolean updateFailed = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<DiscountObject> discountArray = htmlParser.getDiscountsArray();
            if (discountArray == null) {
                updateFailed = true;
            } else {
                dataBaseHandler.storeDiscounts(discountArray);
                Log.d(tag, "first ever Update was SUCCESFUL");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            // Hide the loading spinner
            findViewById(R.id.loadSpinnerHomeAct).setVisibility(View.GONE);
            // if the update failed, let the user know, else enable the user to navigate further
            if (updateFailed){
                // There was a problem, maybe no internet connection
                Log.d(tag, "NOT COOL: first Ever Update FAILED");
                showDialog();
                Log.d(tag, "Show user no connection message");
                showUserNoConnectionMessage(true);
            }
            else {
                Log.d(tag, "make buttons Clickable again");
                makeButtonsClickable(true);
            }
        }
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
