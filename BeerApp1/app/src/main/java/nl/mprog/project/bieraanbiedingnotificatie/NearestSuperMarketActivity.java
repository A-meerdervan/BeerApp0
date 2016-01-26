package nl.mprog.project.bieraanbiedingnotificatie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class NearestSuperMarketActivity extends AppCompatActivity {

    private DataBaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearest_super_market);
        dataBaseHandler = new DataBaseHandler(getApplicationContext());

        // Get the superMarket where this activity is about
        Bundle extras = getIntent().getExtras();
        String chainName = extras.getString("chainName");

        // Fill the layout with details about this store
        fillNearestStoreInfo(chainName);

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

        SuperMarket superMarket = dataBaseHandler.getClosestStore(chainName);

        TextView storeNameTV = (TextView)findViewById(R.id.storeNameTV);
        storeNameTV.setText(superMarket.individualName);

        TextView distanceTV = (TextView)findViewById(R.id.distanceToStoreTV);

        distanceTV.setText(String.format("%.2f", superMarket.distance));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nearest_super_market, menu);
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
