package nl.mprog.project.bieraanbiedingnotificatie;

/**
 * Created by Alex on 12-1-2016.
 *
 * This class has methods to obtain information on nearby supermarkets through the
 * Google places API
 *
 */


// Imports for the Google places API
import android.util.Log;

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
import java.util.HashMap;


public class SuperMarketFinder {

    private static final String tag = "C_SuperFndr";
    private static final HashMap supportedSupermarketsMap = new SupportedSupermarketsMap();

    // Constructor
    public void SuperMarketFinder(){

    }

    // Some HTTP request code was taken from http://developer.android.com/reference/java/net/HttpURLConnection.html
    public List<SuperMarket> getResults(int radius, String latitude, String longitude){

        String placesSearchURL = createAPIsearchURL(radius, latitude, longitude);

        StringBuilder placesBuilder = new StringBuilder();

        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(placesSearchURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == 200) {
                //we have an OK response
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                InputStreamReader placesInput = new InputStreamReader(inputStream);
                BufferedReader placesReader = new BufferedReader(placesInput);
                String lineIn;
                while ((lineIn = placesReader.readLine()) != null) {
                    placesBuilder.append(lineIn);
//                    Log.d(tag, lineIn);
                }
            }
            else {
                //TODO: Verzinnen wat er moet gebeuren als de connectie met places API niet werkt
                Log.d(tag, "De places API connectie had niet de Ok code van 200 dus is er geen data binnengehaald");
                Log.d(tag, urlConnection.getResponseCode() + "");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(tag, "De url klopt sws niet");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(tag, "connectie wou niet openen");
        } catch (Exception e){
            Log.d(tag, "Er ging iets mis in de input lezen");
            Log.d(tag, e.toString());
        }
        finally {
            urlConnection.disconnect();
        }
        String JSONreturned = placesBuilder.toString();

        // Return the parsed JSON input
        return parseJSONsupermarketInfo(JSONreturned);
    }

    // Some code was taken from
    // http://code.tutsplus.com/tutorials/android-sdk-working-with-google-maps-google-places-integration--mobile-16054
    public String createAPIsearchURL(int radius, String latitude, String longitude){

        boolean sensorBool = false;
//        String types = "grocery_or_supermarket";
        String APIdoorOpener = "AIzaSyDN4NL_3RZ7JFWhjW707eJ3I12omMxDt2Y";
        String languageCode = "nl";
        String keyWord = "supermarkt";
//        String supportedSupermarkets = "spar|albert|vomar";
//        String supportedSupermarkets = "aldi|deen|spar|coop|linders|agrimarkt|MCD|emte|Vomar|Jumbo|Broek|Boer|Edeka|penny|lidl|plus|C1000|hoogvliet|deka";

        String placesSearchURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location=" + latitude + "," + longitude +
                "&radius=" + radius +
                "&sensor=" + sensorBool +
//                "&types=" + types +
                "&key=" + APIdoorOpener +
                "&language=" + languageCode +
//                "&name=" + supportedSupermarkets +
                "&keyword=" + keyWord +
                "&rankby=prominence"
                ;
        return placesSearchURL;
    }

    public List<SuperMarket> parseJSONsupermarketInfo(String JSONreturned){
        JSONObject obj = null;
        List<SuperMarket> superMarkets = new ArrayList<>();
        try {
            obj = new JSONObject(JSONreturned);
            JSONArray superMarketArray = obj.getJSONArray("results");

            // Loop all supermarkt JSON objects and parse them to SuperMarkt class objects
            for (int i = 0; i < superMarketArray.length(); i++)
            {
                // Only store supported supermarkets
                String storeName = superMarketArray.getJSONObject(i).getString("name");
                String[] resultArray = checkStore(storeName);
                boolean isSupported = resultArray[0].equals("true");
                if ( isSupported ){
                    String chainName = resultArray[1];
                    String adres = superMarketArray.getJSONObject(i).getString("vicinity");
                    Double latitude = superMarketArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    Double longitude = superMarketArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    Log.d(tag, storeName);
                    SuperMarket superMarket = new SuperMarket(chainName, storeName, adres, latitude, longitude);
                    superMarkets.add(superMarket);
                }
                // else: ignore supermarkt
            }

        } catch (JSONException e) {
            Log.d(tag, "Json parsen is mislukt");
            e.printStackTrace();
        }

        return superMarkets;
    }

    // This function checks whether a store is supported.
    // It returnes an array with as first; the string true if supported
    // it returnes as second the chain of the store that is concerend.
    public String[] checkStore(String storeName){
        String[] resultArray = new String[2];

        for (Object key : supportedSupermarketsMap.keySet()) {
            Boolean isSupported = storeName.contains((String)key);
            if (isSupported) {
                resultArray[0] = "" + isSupported;
                resultArray[1] = (String)supportedSupermarketsMap.get(key);
                return resultArray;
            }
            // else: continue search
        }
        // nothing was found so return false:
        resultArray[0] = "false";
        resultArray[1] = "";
        return resultArray;
    }
}

