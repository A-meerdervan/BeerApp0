package nl.mprog.project.bieraanbiedingnotificatie;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Alex on 22-1-2016.
 *
 * This is the screen that shows the details of 1 supermarket. The supermarket of this chain that is
 * closest to the user. The screens shows the adres and distance to the supermarket, and shows a
 * it's location on a map. This is done using the Google maps API. The relevant discounts that this
 * supermarket has are shown in a listview.
 * can use to filter the discounts.
 */

public class ClosestSuperMarketActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DataBaseHandler dataBaseHandler;
    private SuperMarket superMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closest_super_market);
        dataBaseHandler = new DataBaseHandler(getApplicationContext());

        // Get the superMarket where this activity is about
        Bundle extras = getIntent().getExtras();
        String chainName = extras.getString("chainName");

        // Get the superMarket Object
        superMarket = dataBaseHandler.getClosestStore(chainName);
        // Fill the layout with details about this store
        fillNearestStoreInfo(chainName);
        // Set up the google maps view
        setUpMapIfNeeded();
        // Get the discounts that this store currently has.
        List<DiscountObject> discountsFromStore = dataBaseHandler.getDiscountsByStore(chainName);
        populateListView(discountsFromStore);
    }

    // Fill the listview list  with discounts
    private void populateListView(List<DiscountObject> discountArray) {
        ArrayAdapter<DiscountObject> adapter = new MyListAdapter(discountArray, this);
        ListView list = (ListView) findViewById(R.id.nearestStoreDiscountList);
        list.setAdapter(adapter);
    }

    // This function fills the layout with details of the supermarket
    private void fillNearestStoreInfo(String chainName){

        TextView storeNameTV = (TextView)findViewById(R.id.storeNameTV);
        // Get the name with caps and spaces from the bare chainName using a map
        SupportedSupermarketsMap supportedSupermarketsMap = new SupportedSupermarketsMap();
        storeNameTV.setText(supportedSupermarketsMap.backwardsSuperMarketMap.get(chainName));

        TextView distanceTV = (TextView)findViewById(R.id.distanceToStoreTV);

        distanceTV.setText(String.format("%.2f", superMarket.distance) + " km");

        TextView addressTV = (TextView)findViewById(R.id.storeAddressTV);
        addressTV.setText(superMarket.adres);

        ImageView storeIMG = (ImageView)findViewById(R.id.storeInfoIMG);
        storeIMG.setImageResource(getSuperImageResource(superMarket.chainName));

    }

    // this returns the resource integer id of a supermaket image
    private int getSuperImageResource(String superMarket) {
        int resId = getApplicationContext().getResources().getIdentifier(superMarket, "drawable", "nl.mprog.project.bieraanbiedingnotificatie");
        if (resId != 0) {
            return resId;
        }
        // If image is not found:
        return R.drawable.imagenotfound;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    // set up the google maps fragment
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMapsFragment))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    // This function sets the settings for the maps fragment.
    // The focus is set on the supermarket and a market for it, and the users location are created.
    private void setUpMap() {
        // Add a marker on the spot of the supermarket
        LatLng storeLocation = new LatLng(superMarket.latitude, superMarket.longitude);
        mMap.addMarker(new MarkerOptions().position(storeLocation).title("Locatie Winkel"));
        mMap.addMarker(new MarkerOptions()
                .position(storeLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 16));

        SharedPreferences prefs = getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        Double userLatitude = Double.valueOf(prefs.getString("userLatitude", "default"));
        Double userLonitude = Double.valueOf(prefs.getString("userLongitude", "default"));
        LatLng userLocation = new LatLng(userLatitude, userLonitude);
        mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
}
