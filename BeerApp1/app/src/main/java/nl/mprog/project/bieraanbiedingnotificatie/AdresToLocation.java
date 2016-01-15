package nl.mprog.project.bieraanbiedingnotificatie;

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
 * This class transforms a Zipcode to a location in latitude and longitude.
 */

public class AdresToLocation {

    private static final String tag = "*C_AdresToL";

    // Constructor
    public AdresToLocation(){

    }
    private String[] location;

    // This function returns the location in lat, lng with a zipcode as input.
    // It does this using a HTTPS request to the google GeoCoding API
    public String[] getLocationFromAdres( String zipCode ){

        String locationSearchURL = createLocationAPIsearchURL( zipCode );

        StringBuilder locationJSONBuilder = new StringBuilder();

        URL url = null;
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
            // TODO: regel een toast ofzo dat ze geen internet hebben miss?
        } catch (Exception e){
            Log.d(tag, "Er ging iets mis in de input lezen");
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

        String locationSearchURL = "https://maps.googleapis.com/maps/api/geocode/" +
                "json?address=" + zipCode +
                "&components=" + "country:" + countryCode +
                "&key=" + APIdoorOpener +
                "&language=" + languageCode
                ;
        return locationSearchURL;
    }

    // TODO: iets inbouwen voor wanneer google het geen goed adres vind, of de internet connectie niet werkt ofzo

    // This function gets the Double latitude and longitude information from the JSON that google has sent.
    public String[] parseJSONlocationInfo(String JSONreturned){
        String[] location = new String[2];
        location[0] = "default";
        location[1] = "default2";
        JSONObject obj = null;
        try {
            obj = new JSONObject(JSONreturned);
            JSONArray subArray = obj.getJSONArray("results");
            location[0] = Double.toString(subArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
            location[1] = Double.toString(subArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

        } catch (JSONException e) {
            Log.d(tag, "Json parsen is mislukt");
            e.printStackTrace();
        }

        return location;
    }

}
