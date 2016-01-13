package nl.mprog.project.bieraanbiedingnotificatie;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// imports related to parsing HTML
import android.os.AsyncTask;

public class AlleAanbiedingenActivity extends AppCompatActivity implements FilterFragment.OnFragmentInteractionListener {

    private static final String tag = "C_AllDisc";
    private List<DiscountObject> discountArray = new ArrayList<>();
    private HtmlParser htmlParser;
    private SuperMarketFinder superMarketFinder;
    private List<SuperMarket> superMarkets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alle_aanbiedingen);

        // Start up the filter fragment
        initializeFragment();

        // parse HTML
        htmlParser = new HtmlParser();
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        // Create an object of the class that can find near supermarkets
        superMarketFinder = new SuperMarketFinder();
        // Run the supermarket finding as a background thread
        HttpTestAsyncTask testTask = new HttpTestAsyncTask();
        testTask.execute();
    }

    private void populateListView() {
        // Fill the listview list
        ArrayAdapter<DiscountObject> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.AllDiscountsList);
        list.setAdapter(adapter);
    }

    // this returns the resource integer id of a supermaket image
    private int getSuperImageResource(String superMarket) {
        int resId = getApplicationContext().getResources().getIdentifier(superMarket, "drawable", "nl.mprog.project.bieraanbiedingnotificatie");
        if(resId != 0){
            return resId;
        }
        // If image is not found:
        return R.drawable.hertogjan;
    }

    // this returns the resource integer id of a brand image
    private int getBeerImageResource(String brand) {
        int resId = getApplicationContext().getResources().getIdentifier(brand, "drawable", "nl.mprog.project.bieraanbiedingnotificatie");
        if(resId != 0){
            return resId;
        }
        // If image is not found:
        return R.drawable.imagenotfound;
    }

    // This custom adapter class is made to fill the listview with discount information
    private class MyListAdapter extends ArrayAdapter<DiscountObject> {
        private List<String> SortedArray;

        // constructor
        public MyListAdapter() {
            super(AlleAanbiedingenActivity.this, R.layout.listview, discountArray);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.listview, parent, false);
            }

            // Get the discount object
            DiscountObject discountObject = discountArray.get(position);

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
    private class HttpTestAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

//            // Uilenstede:
//            String lat = "52.3202303";
//            String lng = "4.870456";

            // Abbekerk
            String latitude = "52.7299972";
            String longitude = "5.0129737";

//            // Science park:
//            String lat = "52.3545072";
//            String lng = "4.9491274";

            int radius = 7000;
            superMarkets = superMarketFinder.getResults(radius, latitude, longitude);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Print supermarket info to textview
            Log.d(tag, "Nu zit ik in de onPostExecute functie");
//            TextView outputTV = (TextView)findViewById(R.id.outputTV);
//            String output = "";
            // Create a list with only the bare supermarket names, like: deen,albertheijn,coop
            List <String> superMarketsBare = new ArrayList<>();
            for(int i = 0; i < superMarkets.size(); i++){
                // add to all supermarktes info output string
//                output += superMarkets.get(i).toString();
                // only add supermarket if it has not already been added.
                if (! superMarketsBare.contains(superMarkets.get(i).chainName)){
                    superMarketsBare.add(superMarkets.get(i).chainName);
                }
            }
//            outputTV.setText("Supermarkten: \n" + output);
            String testArray = "";
            for (String testChain : superMarketsBare){
                testArray += (testChain + " , ") ;
            }
            Log.d(tag, testArray);
        }
    }


    // TODO: this class is currently run from the oncreate function.
    // It should be run every night or something, to update the database
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            discountArray = htmlParser.getDiscountsArray();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Fill the listView
            populateListView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alle_aanbiedingen, menu);
        return true;
    }

    // Handle the fragment
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.filter) {
            toggleFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void initializeFragment() {
        // Replace the empty holder in the layout file with the fragment, and hide it
        FilterFragment filterFragment = new FilterFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction
                .replace(R.id.filterFragment, filterFragment, "filter")
                .hide(filterFragment)
                .commit();
        // This brings the fragment on top of everything
        FrameLayout container = (FrameLayout)findViewById(R.id.filterFragment);
        container.bringToFront();
    }

    public void toggleFragment() {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("filter");
        if(fragment.isVisible()) {
            transaction
                    .hide(fragment)
                    .commit();
            getSupportFragmentManager().popBackStack();
        }
        else {
            transaction
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .show(fragment)
                    .addToBackStack("filter")
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
}


