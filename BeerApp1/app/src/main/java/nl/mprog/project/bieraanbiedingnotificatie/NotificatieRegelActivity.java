package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.AsyncTask;
//import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Arrays;
import java.util.List;

public class NotificatieRegelActivity extends AppCompatActivity implements NotifyFragment.OnFragmentInteractionListener {

    private static final String tag = "C_NotifDisc";
    private List<DiscountObject> discountArray = new ArrayList<>();
    private List<DiscountObject> discountsNearbyArray = new ArrayList<>();
    private HtmlParser htmlParser;
    private SuperMarketFinder superMarketFinder;
    private List<SuperMarket> superMarkets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificatie_regel);

        // Start up the settings fragment
        initializeFragment();

        // parse HTML and create an object of the class that can find near supermarkets
        superMarketFinder = new SuperMarketFinder();
        htmlParser = new HtmlParser();
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(52.3202303, 4.870456 , 1000, 10.3);
        // Uilenstede:
        //            String latitude = "52.3202303";
        //            String longitude = "4.870456";
        customAsyncTask.execute();

//        // Create an object of the class that can find near supermarkets
//        superMarketFinder = new SuperMarketFinder();
//        // Run the supermarket finding as a background thread
//        HttpTestAsyncTask testTask = new HttpTestAsyncTask();
//        testTask.execute();
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
        // Fill the listview list
        ArrayAdapter<DiscountObject> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.notifcationsItemsList);
        list.setAdapter(adapter);
    }


    // This custom adapter class is made to fill the listview with discount information
    private class MyListAdapter extends ArrayAdapter<DiscountObject> {

        // constructor
        public MyListAdapter() {
            super(NotificatieRegelActivity.this, R.layout.listview, discountsNearbyArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.listview, parent, false);
            }

            // Get the discount object
            DiscountObject discountObject = discountsNearbyArray.get(position);

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


    // TODO: this class is currently run from the oncreate function.
    // It should be run every night or something, to update the database
    private class CustomAsyncTask extends AsyncTask<Void, Void, Void> {

        private Double lat;
        private Double lng;
        private int radius;
        private Double maxPrice;

        public CustomAsyncTask(Double lat, Double lng, int radius, Double maxPrice){
            this.lat = lat;
            this.lng = lng;
            this.radius = radius;
            this.maxPrice = maxPrice;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            // get the discount data from parsing html
            discountArray = htmlParser.getDiscountsArray();

            // get the supermarkets nearby information
            // Uilenstede:
//            String latitude = "52.3202303";
//            String longitude = "4.870456";

//            // Abbekerk
//            String latitude = "52.7299972";
//            String longitude = "5.0129737";

//            // Science park:
//            String latitude = "52.3545072";
//            String longitude = "4.9491274";

//            int radius = 1000;
            superMarkets = superMarketFinder.getResults(radius, lat.toString(), lng.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(tag, "Nu zit ik in de onPostExecute functie");

            // Create a list with only the bare supermarket names, like: deen,albertheijn,coop
            List<String> superMarketsBare = new ArrayList<>();
            for (int i = 0; i < superMarkets.size(); i++) {
                if (!superMarketsBare.contains(superMarkets.get(i).chainName)) {
                    superMarketsBare.add(superMarkets.get(i).chainName);
                }
            }
            String testArray = "";
            for (String testChain : superMarketsBare) {
                testArray += (testChain + " , ");
            }
            Log.d(tag, testArray);

            // Filter the discounts on nearby supermarkets
            // First empty the discountsNearbyArray for a fresh start
            discountsNearbyArray.clear();

            for (DiscountObject discountObject : discountArray) {
                superMarketsBare.contains(discountObject.superMarkt);
                if (superMarketsBare.contains(discountObject.superMarkt)) {
                    discountsNearbyArray.add(discountObject);
                }
            }

            populateListView();
        }
    }

    // This method receives settings information from the fragment:
    // And updates the discount information

    @Override
    public void onFragmentInteraction(Double lat, Double lng, int radius, Double maxPrice) {
        TextView tvINT = (TextView)findViewById(R.id.introNotificationsTV);
        tvINT.setText("" + lat + " " + lng + " " + radius + " " + maxPrice );

        // Get the supermarkets
        CustomAsyncTask customAsyncTask = new CustomAsyncTask(lat, lng , radius, maxPrice);
        customAsyncTask.execute();

        // Filter the discounts

        // Update the listview
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