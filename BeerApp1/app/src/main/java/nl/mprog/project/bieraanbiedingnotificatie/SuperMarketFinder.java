package nl.mprog.project.bieraanbiedingnotificatie;

/**
 * Created by Alex on 12-1-2016.
 *
 * This class has methods to obtain information on nearby supermarkets through the
 * Google places API
 *
 */


// Imports for the Google places API
import android.content.Context;
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

/**
 * Created by Alex on 12-1-2016.
 *
 * This class is responsible for finding nearby supermarkets using the google places API.
 */

public class SuperMarketFinder {

    private static final String tag = "*C_SuperFndr";
    private static final HashMap supportedSupermarketsMap = new SupportedSupermarketsMap();
    private AdresToLocation adresToLocation;
    private List<SuperMarket> superMarkets = new ArrayList<>();
    private String[] location;
    private DataBaseHandler dataBaseHandler;

    // Constructor
    public SuperMarketFinder(Context context){
        this.dataBaseHandler = new DataBaseHandler(context);
        this.adresToLocation = new AdresToLocation(context);
    }

    // This function takes a radius and a zipCode and returns all superMarkets that are
    // near the entered zipCode.
    // Some HTTP request code was taken from http://developer.android.com/reference/java/net/HttpURLConnection.html
    public List<SuperMarket> getResults(int radius, String zipCode){

        location = adresToLocation.getLocationFromAdres(zipCode);
        // Print the coordinates
        Log.d(tag, location[0]);
        Log.d(tag, location[1]);

        // Get the API url
        String placesSearchURL = createAPIsearchURL(radius, location[0], location[1]);

        // Create the string builder that will receive the supermarket data from the google places API
        // And the required HTTP connection.
        StringBuilder placesBuilder = new StringBuilder();
        URL url;
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
                }
            }
            else {
                //TODO: Take action when the connection with the google Places API does not work.
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

        // The JSON is converted to a list of supermarktet objects.
        superMarkets = parseJSONsupermarketInfo(JSONreturned);
        // Set the distance between the supermarkets and the adres of the user, then find out the
        // wich stores are nearest to the user and mark that in the database
        setDistancesToUser();
        markNearestStores();
        return superMarkets;
    }

    // Create a list with only the bare supermarket names, like: deen,albertheijn,coop
    public List<String> getBareSupermarkets(List<SuperMarket> superMarkets){
        List<String> bareSuperMarkets = new ArrayList<>();
        for (int i = 0; i < superMarkets.size(); i++) {
            // If it is not already stored, store it.
            if (!bareSuperMarkets.contains(superMarkets.get(i).chainName)) {
                bareSuperMarkets.add(superMarkets.get(i).chainName);
            }
        }
        String testArray = "";
        for (String testChain : bareSuperMarkets) {
            testArray += (testChain + ",");
        }
        Log.d(tag, testArray);
        return bareSuperMarkets;
    }

    // Some code was taken from
    // http://code.tutsplus.com/tutorials/android-sdk-working-with-google-maps-google-places-integration--mobile-16054
    private String createAPIsearchURL(int radius, String latitude, String longitude){

        boolean sensorBool = false;
        String APIdoorOpener = "AIzaSyDN4NL_3RZ7JFWhjW707eJ3I12omMxDt2Y";
        String languageCode = "nl";
        String keyWord = "supermarkt";

        return  "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                "json?location=" + latitude + "," + longitude +
                "&radius=" + radius +
                "&sensor=" + sensorBool +
                "&key=" + APIdoorOpener +
                "&language=" + languageCode +
                "&keyword=" + keyWord +
                "&rankby=prominence"
                ;
    }

    // This function transforms the JSON input intro a list of SuperMarket objects
    private List<SuperMarket> parseJSONsupermarketInfo(String JSONreturned){
        JSONObject obj;
        List<SuperMarket> superMarkets = new ArrayList<>();
        try {
            obj = new JSONObject(JSONreturned);
            JSONArray superMarketArray = obj.getJSONArray("results");

            // Loop all supermarkt JSON objects and parse them to SuperMarkt class objects
            for (int i = 0; i < superMarketArray.length(); i++)
            {
                // Get the storename from the Json and use the checkStore function to
                // get the name of the store chain like "albertheijn" and to see whether the store
                // is supported
                String storeName = superMarketArray.getJSONObject(i).getString("name");
                String[] resultArray = checkStore(storeName);
                boolean isSupported = resultArray[0].equals("true");
                // only store supported supermarkets
                if ( isSupported ){
                    String chainName = resultArray[1];
                    String adres = superMarketArray.getJSONObject(i).getString("vicinity");
                    Double latitude = superMarketArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    Double longitude = superMarketArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    Log.d(tag, storeName);
                    SuperMarket superMarket = new SuperMarket(chainName, storeName, adres, latitude, longitude);
                    superMarkets.add(superMarket);
                }
                else{
                    // ignore supermarkt
                    Log.d(tag, "Not supported: " + storeName);
                }

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

    // This function marks the supermarket, that per supermarket chain, is closest to the user, by
    // setting the closestFlag property to 1.
    private void markNearestStores(){
        // Loop the unique superMarket chain names (even if 2 AH are close, this list only contains
        // "albertheijn" once.
        List<String> bareSuperMarkets = getBareSupermarkets(superMarkets);
        for(int j = 0; j < bareSuperMarkets.size(); j++){
            // This is the circumference of the earth to ensure that it is further then any supermarket
            Double closestDistance = 40000.;
            int closestSuperMarketIndex = 1000; // this ensures an error when this is not rewritten
            // loop all supermarket objects to find the closest one of this chain
            for (int index = 0; index < superMarkets.size(); index++){
                if (bareSuperMarkets.get(j).equals(superMarkets.get(index).chainName)){
                    // if the current super market is closer save it.
                    if (superMarkets.get(index).distance < closestDistance){
                        closestDistance = superMarkets.get(index).distance;
                        closestSuperMarketIndex = index;
                    }
                }
            }
            superMarkets.get(closestSuperMarketIndex).closestFlag = 1;
        }
        // Store the changes to the database
        dataBaseHandler.storeSuperMarkets(superMarkets);
    }

    private void setDistancesToUser(){
        for (int i = 0; i < superMarkets.size(); i++){
            superMarkets.get(i).distance = calculateDistance(Double.valueOf(location[0]),
                    Double.valueOf(location[1]), superMarkets.get(i).latitude,
                    superMarkets.get(i).longitude);
        }
    }

    // This function aproximates the distance in km between to points on the earth
    // acurately for small distances. This is done using pythogoras.
    // The function was inspired by: http://www.movable-type.co.uk/scripts/latlong.html
    private static Double calculateDistance(Double latitudeA, Double longitudeA, Double latitudeB, Double longitudeB){

        Integer R = 6371000; // radius of the earth in metres
        Double latInRadA = latitudeA * Math.PI / 180;
        Double latInRadB = latitudeB * Math.PI / 180;
        Double lngInRadA = longitudeA * Math.PI / 180;
        Double lngInRadB = longitudeB * Math.PI / 180;

        Double x = (lngInRadB - lngInRadA) * Math.cos((latInRadA + latInRadB)/2);
        Double y = (latInRadB - latInRadA);
        Double distance = Math.sqrt(x*x + y*y) * R;

        // return the distance in km
        return distance/1000;
    }
}

