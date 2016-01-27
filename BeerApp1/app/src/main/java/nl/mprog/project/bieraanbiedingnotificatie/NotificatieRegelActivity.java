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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotificatieRegelActivity extends AppCompatActivity implements NotifyFragment.OnFragmentInteractionListener {

    private static final String tag = "*C_NotifDisc";
    private List<DiscountObject> discountArray = new ArrayList<>();
    private List<DiscountObject> discountsNotifyArray = new ArrayList<>();
    private SuperMarketFinder superMarketFinder;
    private List<SuperMarket> superMarkets = new ArrayList<>();
    private List<String> bareSuperMarkets = new ArrayList<>();
    private FilterAndSorter filterAndSorter = new FilterAndSorter();
    private DataBaseHandler dataBaseHandler;

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
        // TODO: dit geprint weghalen
        for (DiscountObject discountObject : discountsNotifyArray){
            Log.d(tag, discountObject.brandPrint + " " + discountObject.brand);
        }
        setTopMessageToUser();
        populateListView();
    }

    private void populateListView() {
        // Sort the entry's on price
        discountsNotifyArray = filterAndSorter.sortOnPrice(discountsNotifyArray);
        // Fill the listview list
        ArrayAdapter<DiscountObject> adapter = new MyListAdapter(discountsNotifyArray, this);
        ListView list = (ListView) findViewById(R.id.notifcationsItemsList);
        list.setAdapter(adapter);
        // When an item is clicked, open an activity that shows the location of the nearest superMarket
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                DiscountObject discountObject = (DiscountObject)parent.getItemAtPosition(position);
                // Go to the activity with information on the closest store
                Intent intent = new Intent(getApplicationContext(), ClosestSuperMarketActivity.class);
                intent.putExtra("chainName",discountObject.superMarkt);
                startActivity(intent);
            }
        });
    }

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

            // Stop the loading spinner, fill the listview with results and update the top message
            findViewById(R.id.loadSpinnerNotify).setVisibility(View.GONE);
            populateListView();
            setTopMessageToUser();
        }
    }

    private void setTopMessageToUser(){
        SharedPreferences prefs = getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        Boolean previousSettingsDetected = prefs.getBoolean("previousSettingsDetected", false);
        TextView introTV = (TextView)findViewById(R.id.introNotificationsTV);
        // TODO: Dit verwijderen als je het niet nodig vind.
//        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) introTV.getLayoutParams();
//        p.setMargins(0, 0, 100, 0);
//        introTV.requestLayout();
        if (previousSettingsDetected == false){
            introTV.setText("Als u op NOTIFY SETTINGS klikt kunt u uw bier voorkeuren instellen.");
//            TODO: kleur veranderen
//            introTV.setBackgroundColor(getResources().getColor(R.color.Gold));
            return;
        }
        if (discountsNotifyArray.size() == 0){
            introTV.setText("Op dit moment hebben de supermarkten niet wat u wilt,\n" +
                    "U kijgt een notificatie zodra dit wel zo is!");
//            introTV.setBackgroundColor(getResources().getColor(R.color.BlueButtonColor));
            return;
        }
        introTV.setText("Er zijn supermarkten die hebben wat u wilt!");
//        introTV.setBackgroundColor(getResources().getColor(R.color.Gold));
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
                    // TODO: checken of dit wel goed gaat, ik heb net van ani of anim -> animator gemaakt bij fade out.
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
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