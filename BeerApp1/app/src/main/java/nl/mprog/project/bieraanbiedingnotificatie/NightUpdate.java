package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Alex on 20-1-2016.
 *
 * This class will update the discount information and send the user a notification when there is a new discount.
 * It updates every night at 4:30. When the update failes it retries every 20 minutes.
 * code inspired by: http://www.sitepoint.com/scheduling-background-tasks-android/
 */
public class NightUpdate extends BroadcastReceiver{

    private DataBaseHandler dataBaseHandler;
    private Context appContext;
    private String tag = "*C_NightUpdt";
    private static final Integer RETRY_ID = 1;
    // The retry interval is every 20 minutes
    private static final Integer RETRY_INTERVAL = 20 * 60 * 1000;
    private List<DiscountObject> oldDiscountArray;
    private List<DiscountObject> newDiscountArray;



    @Override
    public void onReceive(Context appContext, Intent arg1) {
        this.appContext = appContext;
        dataBaseHandler = new DataBaseHandler(appContext);
        // Get the discounts on a new thread
        CustomAsyncTask customAsyncTask = new CustomAsyncTask();
        customAsyncTask.execute();
    }

    // Http requests have to be run on a sepparate thread. This is used to update the discount
    // information by parsing html. It can then send a notification when there are updates
    private class CustomAsyncTask extends AsyncTask<Void, Void, Void> {
        private HtmlParser htmlParser = new HtmlParser(appContext);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(appContext, "I'm updating", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            // save the previous discountArray to determine wich discount is new
            oldDiscountArray = dataBaseHandler.getAllDiscounts();
            // TODO Add random time jitter to prevent Denial of service from the website
            // Try to get the current discounts online by parsing html
            newDiscountArray = htmlParser.getDiscountsArray();

            // htmlParser failed to get the discount information (maybe no internet connection)
            // So set an alarm to try again every 20 min
            if (newDiscountArray == null) {
                setAlarmToRetry(true);
                return null;
            }
            // The information was retreived succesful so cancel the repeating alarm to try every
            // 20 min if it was set.
            setAlarmToRetry(false);
            dataBaseHandler.storeDiscounts(newDiscountArray);

            // Get the settings of the user
            SharedPreferences prefs = appContext.getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
            Boolean previousSettingsDetected = prefs.getBoolean("previousSettingsDetected", false);
            Log.d(tag, previousSettingsDetected + " Previous settings? Dit is in nightupdate");
            // Only set if the user has saved his settings before go and get relevant notifications
            if (previousSettingsDetected) {
                Double maxPrice = Double.valueOf(prefs.getString("maxPrice", "-1"));
                List<String> favoriteBeers = new ArrayList<>(prefs.getStringSet("favoBeersList", new HashSet<String>()));

                // Get the specific Nearby superMarkets from the database
                SuperMarketFinder superMarketFinder= new SuperMarketFinder(appContext);
                // Transform it to a list of only the chainname's of the supermarkets
                List<String> bareSuperMarkets = superMarketFinder.getBareSupermarkets(dataBaseHandler.getNearbySuperMarkets());

                // Get the discounts that qualify for notification and have not already been notified
                // in the past
                List<DiscountObject> discountsNotifyArray = getDiscountsToNotifyAndUpdateDB(bareSuperMarkets, maxPrice, favoriteBeers);

                // Send a notification to the user about the discounts.
                sendNotification(discountsNotifyArray);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
        }
    }

    // This function sets the alarm to retry every 20 minutes, or it cancels that alarm
    private void setAlarmToRetry(Boolean setAlarm){

        // Get the alarm system service and create a pending intent to open this class
        AlarmManager alarmManager;
        PendingIntent retryPendingIntent;

        alarmManager = (AlarmManager)appContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(appContext, NightUpdate.class);
        retryPendingIntent = PendingIntent.getBroadcast(appContext, RETRY_ID, intent, 0);

        if (setAlarm){
            // set the alarm
            alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + RETRY_INTERVAL, RETRY_INTERVAL, retryPendingIntent);
            Log.d(tag, "Update failed, Retry alarm was set!");
        }
        else {
            // cancel the alarm
            Log.d(tag, "Update was succesful, Retry alarm was canceld.");
            alarmManager.cancel(retryPendingIntent);
        }
    }

    // This function checks what discounts are new and have not been notified to the user yet.
    // It then sends a notification about these.
    private List<DiscountObject> getDiscountsToNotifyAndUpdateDB(List<String> bareSuperMarkets, Double maxPrice, List<String> favoriteBeers){

        List<DiscountObject> discountsNotifyArray = new ArrayList<>();
        // Set all notify flags to 0 in the database
        dataBaseHandler.resetNotifyFlags();
        newDiscountArray = dataBaseHandler.getAllDiscounts();

        // Filter the discounts on nearby supermarkets
        // Loop the discountArray and set the notify flags to 1 for all discounts that match
        // the user's criteria
        for (int i = 0; i< newDiscountArray.size(); i++) {
            DiscountObject newDiscount = newDiscountArray.get(i);

            if (    bareSuperMarkets.contains(newDiscount.superMarkt)
                    && (newDiscount.price < maxPrice )
                    && (favoriteBeers.contains(newDiscount.brandPrint)) ){
                // Set the notify flag of the disount objects
                newDiscount.notificationFlag = 1;
                // If the discount is new; add to notify array to inform user
                if (discountIsNew(newDiscount)){
                    discountsNotifyArray.add(newDiscount);
                    Log.d(tag, "New Discount: " + newDiscount.brandPrint + " " + newDiscount.superMarkt);
                }
                else{
                    Log.d(tag, "Not new:   " + newDiscount.brandPrint + " " + newDiscount.superMarkt);
                }
            }
        }
        // Udate de dataBase with the newly flagged discounts
        dataBaseHandler.storeDiscounts(newDiscountArray);
        return discountsNotifyArray;
    }

    // This function is used to see whether a discount is new compared to the previous loading of
    // Discounts, if it is new than a notifaction can be sent about it. If it is not new,
    // a notification has already been sent in the past.
    private boolean discountIsNew(DiscountObject newDiscount){
        Boolean discountIsNew = true;
        for (DiscountObject oldDiscount : oldDiscountArray) {
            if ( oldDiscount.brand.equals(newDiscount.brand) && oldDiscount.superMarkt.equals(newDiscount.superMarkt)
                    && oldDiscount.discountPeriod.equals(newDiscount.discountPeriod)){
                discountIsNew = false;
                break;
            }
        }
        return discountIsNew;
    }

    // This notification takes an array of discounts, and then sends a notification to the user
    private void sendNotification(List<DiscountObject> discountsNotifyArray){

        // Only if the array has elements there is new information for the user
        if ( ! (discountsNotifyArray.size() == 0) ) {
            // Give notification
            // Code inspired by: http://developer.android.com/guide/topics/ui/notifiers/notifications.html#Removing
            // Add the specific discount info to the notification title
            String title = "";
            for ( int i = 0; i < discountsNotifyArray.size(); i++) {
                if (i == 0) { title += discountsNotifyArray.get(i).brandPrint + " krat €" + discountsNotifyArray.get(i).price;}
                else { title += ", " + discountsNotifyArray.get(i).brandPrint + " krat €" + discountsNotifyArray.get(i).price;}
            }
            Log.d(tag, "!!!!!!!!! " + title);
            String body = "Nieuwe aanbieding(en) gevonden";

            NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(appContext, NotificatieRegelActivity.class);
            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(NotificatieRegelActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            Notification notification = new Notification.Builder(appContext)
                    .setContentTitle(title)
                    .setContentText(body)
                            .setSmallIcon(R.drawable.notifyicon)
                    .setShowWhen(false)
                    .setContentIntent(resultPendingIntent)
                    .setOngoing(false)
                    .setAutoCancel(true)
                    .build();
            notificationManager.notify(0, notification);
        }

    }
}
