package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
//import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotificatieRegelActivity extends AppCompatActivity implements NotifyFragment.OnFragmentInteractionListener {

    private static final String tag = "*C_NotifDisc";
    private List<DiscountObject> discountArray = new ArrayList<>();
    private List<DiscountObject> discountsNotifyArray = new ArrayList<>();
    private SuperMarketFinder superMarketFinder = new SuperMarketFinder();
    private List<SuperMarket> superMarkets = new ArrayList<>();
    private List<String> bareSuperMarkets = new ArrayList<>();
    private FilterAndSorter filterAndSorter = new FilterAndSorter();
    private DataBaseHandler dataBaseHandler = new DataBaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificatie_regel);

        // Start up the settings fragment
        initializeFragment();

        // Hide the loading spinner
        findViewById(R.id.loadSpinnerNotify).setVisibility(View.GONE);

        // Get the discount information from the database
        discountArray = dataBaseHandler.getAllDiscounts();

        // If there are no discounts in the database then the data was not downloaded yet, this
        // could be caused by a lack of internet connection for example. A message is shown to the
        // user
        TextView introTV = (TextView)findViewById(R.id.introNotificationsTV);
        if (discountArray.size() == 0) {
            introTV.setText("Er is geen aanbieding informatie gevonden, wellicht heeft u geen " +
                    "internet verbinding");
            return;
        }

        // if there are already discounts flagged for notifying, display them
        discountsNotifyArray = dataBaseHandler.getNotifyFlaggedDiscounts();
        if (discountsNotifyArray.size() == 0){
            introTV.setText("Er zijn op dit moment geen aanbiedingen die aan uw eisen voldoen");
            return;
        }
        introTV.setText("Deze aanbiedingen volgen uit uw voorkeuren");
        populateListView();
    }

    // this returns the resource integer id of a supermaket image
    // Code inspired by: http://stackoverflow.com/questions/5576156/dynamically-set-source-of-the-imageview
    private int getSuperImageResource(String superMarket) {
        int resId = getApplicationContext().getResources().getIdentifier(superMarket, "drawable", "nl.mprog.project.bieraanbiedingnotificatie");
        if (resId != 0) {
            return resId;
        }
        // If image is not found:
        return R.drawable.hertogjan;
    }

    // this returns the resource integer id of a brand image
    // Code inspired by: http://stackoverflow.com/questions/5576156/dynamically-set-source-of-the-imageview
    private int getBeerImageResource(String brand) {
        int resId = getApplicationContext().getResources().getIdentifier(brand, "drawable", "nl.mprog.project.bieraanbiedingnotificatie");
        if (resId != 0) {
            return resId;
        }
        // If image is not found:
        return R.drawable.imagenotfound;
    }

    private void populateListView() {
        // Sort the entry's on price
        discountsNotifyArray = filterAndSorter.sortOnPrice(discountsNotifyArray);

        // Fill the listview list
        ArrayAdapter<DiscountObject> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.notifcationsItemsList);
        list.setAdapter(adapter);
    }


    // This custom adapter class is made to fill the listview with discount information
    private class MyListAdapter extends ArrayAdapter<DiscountObject> {

        // constructor
        public MyListAdapter() {
            super(NotificatieRegelActivity.this, R.layout.listview, discountsNotifyArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.listview, parent, false);
            }

            // Get the discount object
            DiscountObject discountObject = discountsNotifyArray.get(position);

            // Fill the views

            // Fill the brand
            TextView brand = (TextView) itemView.findViewById(R.id.brand);
            brand.setText(discountObject.brandPrint);

            // Fill the format
            TextView format = (TextView) itemView.findViewById(R.id.format);
            format.setText(discountObject.format);

            // Fill the period
            TextView period = (TextView) itemView.findViewById(R.id.period);
            period.setText(discountObject.discountPeriod);

            // Fill the price
            TextView price = (TextView) itemView.findViewById(R.id.price);
            price.setText("€" + discountObject.price);

            // Fill the literPrice
            TextView literPrice = (TextView) itemView.findViewById(R.id.literPrice);
            literPrice.setText("€" + discountObject.pricePerLiter + " p/lr");

            // Set the supermarkt image
            ImageView superMarkt = (ImageView) itemView.findViewById(R.id.imgSuper);
            superMarkt.setImageResource(getSuperImageResource(discountObject.superMarkt));


            // Set the item image
            ImageView itemImg = (ImageView) itemView.findViewById(R.id.img);
            itemImg.setImageResource(getBeerImageResource(discountObject.brand));
            return itemView;
        }
    }


    // TODO: this class is currently run from the save settings function.
    // It should be run every night or something, to update the database
    private class CustomAsyncTask extends AsyncTask<Void, Void, Void> {

        private String zipCode;
        private int radius;
        private Double maxPrice;
        private List<String> favoriteBeers;

        public CustomAsyncTask(String zipCode, int radius, Double maxPrice, List <String> favoriteBeers) {
            this.zipCode = zipCode;
            this.radius = radius;
            this.maxPrice = maxPrice;
            this.favoriteBeers = favoriteBeers;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            // get the supermarkets nearby information
            superMarkets = superMarketFinder.getResults(radius, zipCode);
            // Save to the database
            dataBaseHandler.storeSuperMarkets(superMarkets);
            // get a list with only the bare supermarket names, like: deen,albertheijn,coop
            bareSuperMarkets = superMarketFinder.getBareSupermarkets(superMarkets);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(tag, "Nu zit ik in de onPostExecute functie");

            // Set all notify flags to 0 in the database
            dataBaseHandler.resetNotifyFlags();

            // Filter the discounts on nearby supermarkets
            // First empty the discountsNotifyArray for a fresh start
            discountsNotifyArray.clear();
            // Update the discountArray from the dataBase and set the notify flags to 1
            // for the newly found settings
            discountArray = dataBaseHandler.getAllDiscounts();
            for (int i = 0; i< discountArray.size(); i++) {
                Log.d(tag, discountArray.get(i).brandPrint + " Zit in favoBeers? " + favoriteBeers.contains(discountArray.get(i).brandPrint));
                if ( bareSuperMarkets.contains(discountArray.get(i).superMarkt)
                    && (discountArray.get(i).price < maxPrice )
                    && (favoriteBeers.contains(discountArray.get(i).brandPrint)) ){

                    discountsNotifyArray.add(discountArray.get(i));
                    // Set the notify flag of the disount objects
                    discountArray.get(i).notificationFlag = 1;
                }
            }
            // Udate de dataBase with the newly flagged discounts
            // TODO: per id een update doen in plaats van de hele DB te verwijderen en weer op te bouwen (dan kun je de discountarr = .getAllDIscounts regel hierboven ook weghalen)
            dataBaseHandler.storeDiscounts(discountArray);

            // Stop the loading spinner and fill the listview with results
            findViewById(R.id.loadSpinnerNotify).setVisibility(View.GONE);
            populateListView();
            // TODO: Dit hier niet doen (notify)
            // Send a notification to the user about the discounts.
            sendNotification(discountsNotifyArray);
        }
    }

    public void sendNotification(List<DiscountObject> discountsNotifyArrayy){
        // Give notification
        // TODO: zorgen dat discountNotifyArray niet global is zodat ik hier geen Araayy hoef te gebruiken
        // Code inspired by: http://developer.android.com/guide/topics/ui/notifiers/notifications.html#Removing
        // Add the specific discount info to the notification title
        String title = "";
        for ( int i = 0; i < discountsNotifyArrayy.size(); i++) {
            if (i == 0) { title += discountsNotifyArrayy.get(i).brandPrint + " krat €" + discountsNotifyArrayy.get(i).price;}
            else { title += ", " + discountsNotifyArrayy.get(i).brandPrint + " krat €" + discountsNotifyArrayy.get(i).price;}
        }
        String body = "Nieuwe aanbieding(en) gevonden";

        NotificationManager notificationManager = (NotificationManager)getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(getApplication(), NotificatieRegelActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificatieRegelActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setShowWhen(false)
                .setContentIntent(resultPendingIntent)
                .setOngoing(false)
                .setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setLargeIcon(aBitmap)
                .build();
        notificationManager.notify(0, notification);
    }

    // This method receives settings information from the fragment:
    // And updates the discount information
    @Override
    public void onFragmentInteraction(String zipCode, int radius, Double maxPrice, List<String> favoriteBeers) {
        // Show the loading spinner
        findViewById(R.id.loadSpinnerNotify).setVisibility(View.VISIBLE);

        // TODO: Zorgen dat de fragment nice opzij swiped
        // Hide the fragment to show loading spinner
        toggleFragment();

        // Get the supermarkets and matching discounts on a new thread
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(zipCode, radius, maxPrice, favoriteBeers);
        customAsyncTask.execute();

    }

// fragment related functions:
    // setup the fragment
    public void initializeFragment() {
        // Replace the empty holder in the layout file with the fragment, and hide it
        NotifyFragment notifyFragment = new NotifyFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.notifyFragment, notifyFragment, "notifySettings")
                .hide(notifyFragment)
                .commit();
        // This brings the fragment on top of everything
        FrameLayout container = (FrameLayout) findViewById(R.id.notifyFragment);
        container.bringToFront();
    }

    // This function toggle's the fragment with a fade in transition
    public void toggleFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment = fragmentManager.findFragmentByTag("notifySettings");
        if (fragment.isVisible()) {
            transaction
                    .hide(fragment)
                    .commit();
            getSupportFragmentManager().popBackStack();
        } else {
            transaction
                    .setCustomAnimations(android.R.animator.fade_in, android.R.anim.fade_out)
                    .show(fragment)
                    .addToBackStack("notifySettings")
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            NavUtils.navigateUpFromSameTask(this);
        } else {
            getFragmentManager().popBackStack();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notificatie_regel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.notifyOption) {
            toggleFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}