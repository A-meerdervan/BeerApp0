package nl.mprog.project.googleplacesapitest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

// Imports for the Google places API
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String placesSearchURL = "";
    private static final String tag = "C_MainAct";
    private String JSONreturned = "";
    private SuperMarktFinder superMarktFinder;
    private List<SuperMarket> superMarkets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create an object of the class that can find near supermarkets
        superMarktFinder = new SuperMarktFinder();
        // Run the supermarket finding as a background thread
        HttpTestAsyncTask testTask = new HttpTestAsyncTask();
        testTask.execute();
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
            superMarkets = superMarktFinder.getResults(radius, latitude, longitude);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Print supermarket info to textview
            Log.d(tag, "Nu zit ik in de onPostExecute functie");
            TextView outputTV = (TextView)findViewById(R.id.outputTV);
            String output = "";
            // Create a list with only the bare supermarket names, like: deen,albertheijn,coop
            List <String> superMarketsBare = new ArrayList<>();
            for(int i = 0; i < superMarkets.size(); i++){
                // add to all supermarktes info output string
                output += superMarkets.get(i).toString();
                // only add supermarket if it has not already been added.
                if (! superMarketsBare.contains(superMarkets.get(i).chainName)){
                    superMarketsBare.add(superMarkets.get(i).chainName);
                }
            }
            outputTV.setText("Supermarkten: \n" + output);
            String testArray = "";
            for (String testChain : superMarketsBare){
                testArray += (testChain + " , ") ;
            }
            Log.d(tag, testArray);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
