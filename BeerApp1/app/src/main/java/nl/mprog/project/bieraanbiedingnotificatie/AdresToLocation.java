package nl.mprog.project.bieraanbiedingnotificatie;

import android.content.Context;
import android.content.SharedPreferences;
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

/**
 * Created by Alex on 14-1-2016.
 *
 * This class transforms a Zipcode to a location in latitude and longitude using the
 * google geocoding API
 */

public class AdresToLocation {

    private static final String tag = "*C_AdresToL";
    private Context appContext;

    // Constructor
    public AdresToLocation(Context context){
        this.appContext = context;
    }

    // This function returns the location in lat, lng with a zipcode as input.
    // It does this using a HTTPS request to the google GeoCoding API
    public String[] getLocationFromAdres( String zipCode ){

        String locationSearchURL = createLocationAPIsearchURL( zipCode );

        StringBuilder locationJSONBuilder = new StringBuilder();

        URL url;
        HttpURLConnection urlConnection = null;

        // Try to astablish a connection with google and request the location data
        try {
            url = new URL(locationSearchURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == 200) {
                //we have an OK response
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                InputStreamReader locationInput = new InputStreamReader(inputStream);
                BufferedReader locationReader = new BufferedReader(locationInput);
                String lineIn;
                while ((lineIn = locationReader.readLine()) != null) {
                    locationJSONBuilder.append(lineIn);
//                    Log.d(tag, lineIn);
                }
            }
            else {
                //TODO: Do something when the google places API is not working
                Log.d(tag, "De places API connectie had niet de Ok code van 200 dus is er geen data binnengehaald");
                Log.d(tag, urlConnection.getResponseCode() + "");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(tag, "The url is wrongly formatted");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(tag, "The connection to the google Geolocation API would not open");
            // TODO: Show the user something to let them now there is an internet problem
        } catch (Exception e){
            Log.d(tag, "Something went wrong when reading the input");
            Log.d(tag, e.toString());
        }
        finally {
            urlConnection.disconnect();
        }
        String JSONreturned = locationJSONBuilder.toString();

        // Return the parsed JSON input
        return parseJSONlocationInfo(JSONreturned);
    }

    private String createLocationAPIsearchURL(String zipCode){
        String APIdoorOpener = "AIzaSyDN4NL_3RZ7JFWhjW707eJ3I12omMxDt2Y";
        String languageCode = "nl";
        String countryCode = "NL";

//        1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY

        return "https://maps.googleapis.com/maps/api/geocode/" +
                "json?address=" + zipCode +
                "&components=" + "country:" + countryCode +
                "&key=" + APIdoorOpener +
                "&language=" + languageCode
                ;
    }

    // This function gets the Double latitude and longitude information from the JSON that google has sent.
    private String[] parseJSONlocationInfo(String JSONreturned){
        String[] location = new String[2];
        location[0] = "default";
        location[1] = "default2";
        JSONObject obj;
        try {
            obj = new JSONObject(JSONreturned);
            JSONArray subArray = obj.getJSONArray("results");
            location[0] = Double.toString(subArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            location[1] = Double.toString(subArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

        } catch (JSONException e) {
            Log.d(tag, "Json parsen is mislukt");
            e.printStackTrace();
        }
        // Save the users location to the shared prefferences. (it will be used later to show on
        // a map
        SharedPreferences prefs = appContext.getSharedPreferences("NotifySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userLatitude",location[0]);
        editor.putString("userLongitude",location[1]);
        editor.apply();
        return location;
    }

}
