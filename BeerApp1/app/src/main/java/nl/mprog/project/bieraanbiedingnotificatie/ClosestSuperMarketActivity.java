package nl.mprog.project.bieraanbiedingnotificatie;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ClosestSuperMarketActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private DataBaseHandler dataBaseHandler;
    SuperMarket superMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closest_super_market);
        dataBaseHandler = new DataBaseHandler(getApplicationContext());

        // Get the superMarket where this activity is about
        Bundle extras = getIntent().getExtras();
        String chainName = extras.getString("chainName");

        // Get the superMarket which is the subject of this activity
        superMarket = dataBaseHandler.getClosestStore(chainName);
        // Fill the layout with details about this store
        fillNearestStoreInfo(chainName);
        // Set up the google maps view
        setUpMapIfNeeded();
        // Get the discounts that this store currently has.
        List<DiscountObject> discountsFromStore = dataBaseHandler.getDiscountsByStore(chainName);
        populateListView(discountsFromStore);
    }

    private void populateListView(List<DiscountObject> discountArray) {
        // Fill the listview list
        ArrayAdapter<DiscountObject> adapter = new MyListAdapter(discountArray, this);
        ListView list = (ListView) findViewById(R.id.nearestStoreDiscountList);
        list.setAdapter(adapter);
    }

    public void fillNearestStoreInfo(String chainName){

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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        // Add a marker on the spot of the supermarket
        LatLng storeLocation = new LatLng(superMarket.latitude, superMarket.longitude);
        mMap.addMarker(new MarkerOptions().position(storeLocation).title("Locatie Winkel"));
        mMap.addMarker(new MarkerOptions()
                .position(storeLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.setMyLocationEnabled(true);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(storeLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 16));
//        mMap.moveCamera(CameraUpdateFactory.zoomBy(19));

        SharedPreferences prefs = getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        Double userLatitude = Double.valueOf(prefs.getString("userLatitude", "default"));
        Double userLonitude = Double.valueOf(prefs.getString("userLongitude", "default"));
        LatLng userLocation = new LatLng(userLatitude, userLonitude);
        Marker userMarker = mMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
}
