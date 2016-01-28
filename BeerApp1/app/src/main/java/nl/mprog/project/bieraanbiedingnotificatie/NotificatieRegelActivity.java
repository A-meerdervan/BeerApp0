package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 20-1-2016.
 *
 * This Class holds a fragment that allows the user to set the settings that determine when a
 * discoutn qualifies for a notification.
 */

public class NotificatieRegelActivity extends AppCompatActivity implements NotifyFragment.OnFragmentInteractionListener {

    private static final String tag = "*C_NotifDisc";
    private List<DiscountObject> discountArray = new ArrayList<>();
    private List<DiscountObject> discountsNotifyArray = new ArrayList<>();
    private SuperMarketFinder superMarketFinder;
    private List<SuperMarket> superMarkets = new ArrayList<>();
    private List<String> bareSuperMarkets = new ArrayList<>();
    private FilterAndSorter filterAndSorter = new FilterAndSorter();
    private DataBaseHandler dataBaseHandler;
    private AdapterView.OnItemClickListener onListClickListener;
    private AdapterView.OnItemClickListener onListClickHideFragment;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificatie_regel);
        superMarketFinder = new SuperMarketFinder(getApplicationContext());
        dataBaseHandler = new DataBaseHandler(this);

        // Start up the settings fragment
        initializeFragment();

        // Hide the loading spinner
        findViewById(R.id.loadSpinnerNotify).setVisibility(View.GONE);

        // if there are already discounts flagged for notifying, display them
        discountsNotifyArray = dataBaseHandler.getNotifyFlaggedDiscounts();
        setTopMessageToUser();
        populateListView();

    }

    private void populateListView() {
        // Sort the entry's on price
        discountsNotifyArray = filterAndSorter.sortOnPrice(discountsNotifyArray);
        // Fill the listview list
        ArrayAdapter<DiscountObject> adapter = new MyListAdapter(discountsNotifyArray, this);
        list = (ListView) findViewById(R.id.notifcationsItemsList);
        list.setAdapter(adapter);
        // When an item is clicked, open an activity that shows the location of the nearest superMarket
        onListClickListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DiscountObject discountObject = (DiscountObject) parent.getItemAtPosition(position);
                // Go to the activity with information on the closest store
                Intent intent = new Intent(getApplicationContext(), ClosestSuperMarketActivity.class);
                intent.putExtra("chainName", discountObject.superMarkt);
                startActivity(intent);
            }
        };
        list.setOnItemClickListener(onListClickListener);
        // When the fragment is shown and the user clicks outside of it on the list, the fragment
        // should hide
        onListClickHideFragment = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                toggleFragment();
            }
        };
    }

    // This class gets the nearby supermarket and location information.
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
                if ( bareSuperMarkets.contains(discountArray.get(i).superMarkt)
                    && (discountArray.get(i).price < maxPrice )
                    && (favoriteBeers.contains(discountArray.get(i).brandPrint)) ){

                    discountsNotifyArray.add(discountArray.get(i));
                    // Set the notify flag of the disount objects
                    discountArray.get(i).notificationFlag = 1;
                }
            }
            // Udate de dataBase with the newly flagged discounts
            dataBaseHandler.storeDiscounts(discountArray);

            // Stop the loading spinner, fill the listview with results and update the top message
            findViewById(R.id.loadSpinnerNotify).setVisibility(View.GONE);
            populateListView();
            setTopMessageToUser();
        }
    }

    // There are multiple scenerio's where different messages are appropriate
    private void setTopMessageToUser(){
        SharedPreferences prefs = getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        Boolean previousSettingsDetected = prefs.getBoolean("previousSettingsDetected", false);
        TextView introTV = (TextView)findViewById(R.id.introNotificationsTV);
        introTV.setVisibility(View.VISIBLE);

        if ( ! previousSettingsDetected){
            introTV.setBackground(getResources().getDrawable(R.drawable.notify_top_positive));
            introTV.setText("Als u op VOORKEUREN klikt kunt u instellen voor welke aanbieding u een notificatie wilt ontvangen.");
            return;
        }
        if (discountsNotifyArray.size() == 0){
            introTV.setBackground(getResources().getDrawable(R.drawable.notify_top_negative));
            introTV.setText("Op dit moment hebben de supermarkten niet wat u wilt,\n" +
                    "U kijgt een notificatie zodra dit wel zo is!");
            return;
        }
        introTV.setBackground(getResources().getDrawable(R.drawable.notify_top_positive));
        introTV.setText("Er zijn supermarkten die hebben wat u wilt!");
    }

    // This method receives settings information from the fragment:
    // And updates the discount information
    @Override
    public void onFragmentInteraction(String zipCode, int radius, Double maxPrice, List<String> favoriteBeers) {
        // Show the loading spinner
        findViewById(R.id.loadSpinnerNotify).setVisibility(View.VISIBLE);
        // hide the top status textview
        findViewById(R.id.introNotificationsTV).setVisibility(View.GONE);

        // TODO: Make the fragment swipe away and in
        // Hide the fragment to show loading spinner
        toggleFragment();

        // Get the supermarkets and matching discounts on a new thread
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(zipCode, radius, maxPrice, favoriteBeers);
        customAsyncTask.execute();

    }

// fragment related functions:
    // setup the fragment
    private void initializeFragment() {
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
    private void toggleFragment() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment = fragmentManager.findFragmentByTag("notifySettings");
        // TODO: For some reason the fragment does not fade in or out, while it uses the same
        // code as the other FilterFragment class.
        if (fragment.isVisible()) {
            transaction
                    .hide(fragment)
                    .commit();
            getSupportFragmentManager().popBackStack();
            // When the fragment is gone the list can be clickable again
            list.setOnItemClickListener(onListClickListener);
        } else {
            transaction
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .show(fragment)
                    .addToBackStack("notifySettings")
                    .commit();
            // When the user clicks outside the fragment, the user clicks on the list, this listener
            // then hides the fragment
            list.setOnItemClickListener(onListClickHideFragment);
        }
    }

    // AUTO GENERATED
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

    // Open ore close the fragment by a click on a menu bar field
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