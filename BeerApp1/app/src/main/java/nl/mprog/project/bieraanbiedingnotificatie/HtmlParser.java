package nl.mprog.project.bieraanbiedingnotificatie;

/**
 * Created by Alex on 5-1-2016.
 * This class gets the dicount information from a site
 * and is able to return a list of DiscountObject's
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// Jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HtmlParser {

    private static final String tag = "*C_htmlParse";
    private Context appContext;
    private Document doc;
    private String htmlURL = "http://www.bierindeaanbieding.nl/krattenindeaanbieding.html";
    public List<DiscountObject> discountArray = new ArrayList<>();
    private SuperMarketFinder superMarketFinder = new SuperMarketFinder(appContext);
    private static final HashMap supportedBrandsMap = new SupportedBrandsMap();

    // constructor
    public HtmlParser(Context context){
        this.appContext = context;
    }

    public List<DiscountObject> getDiscountsArray() {
        try {
            // First clear the array
            discountArray.clear();
            //Get Document object after parsing the html from given url.
            doc = Jsoup.connect(htmlURL).get();
            String type = "Krate";
            //Loop the discount elements.
            for (Element element : doc.getElementsByClass("data")) {

                // Get the supermarket
                Element div = element.getElementsByClass("data3").first();
                String superMarket = div.child(0).attr("alt");
                String[] supportInfo = superMarketFinder.checkStore(superMarket);
                // if the suppermarket is not supported it is not shown as a result (for exaple hanos)
                if (supportInfo[0].equals("false")) {
                    Log.d(tag, "Deze is niet supported: " + superMarket);
                    continue;
                }
                superMarket = supportInfo[1];
                // Get table rows filled with info
                div = element.getElementsByClass("data2").first();
                Elements tableRows = div.getElementsByTag("tbody").first().children();

                // Get the brand
                String brandPrint = tableRows.first().child(0).child(0).html();
                String brand = getBrand(brandPrint);

                // Get the format
                String format = tableRows.get(1).child(0).html();

                // Grolsch beugel krates have as brand on the page only "Grolsch", they can be
                // recognized to be a beugel krate by the format "16 bottles"
                if ( (brand.equals("grolsch")) && (format.contains("16")) ){
                    brandPrint = "Grolsch Beugel";
                    brand = getBrand(brandPrint);
                }

                // Get the price
                double price = 0;
                Elements priceH2 = tableRows.get(3).child(1).child(0).children();

                if (priceH2.size() == 2) {
                    String priceString = priceH2.get(1).html();
                    String[] priceParts = priceString.split(",");
                    priceString = priceParts[0] + "." + priceParts[1];
                    price = Double.parseDouble(priceString);
                } else {
                    String priceString = priceH2.get(0).html().substring(2);
                    String[] priceParts = priceString.split(",");
                    priceString = priceParts[0] + "." + priceParts[1];
                    price = Double.parseDouble(priceString);
                }
                Integer possibleOfset = 0;
                if (!tableRows.get(5).child(0).html().startsWith("Geldig")) {
                    possibleOfset = 1;
                }
                // get the discount period
                String discountPeriod = tableRows.get(5 + possibleOfset).child(0).html();
                // get the price per liter
                // TODO: FIX THE ERROR HERE
                String pricePerLiterString = tableRows.get(6 + possibleOfset).child(1).html().substring(2);
                String[] priceParts = pricePerLiterString.split(",");
                pricePerLiterString = priceParts[0] + "." + priceParts[1];
                double pricePerLiter = Double.parseDouble(pricePerLiterString);

                DiscountObject currentDiscount = new DiscountObject(brandPrint, brand, format, price, pricePerLiter, superMarket, discountPeriod, type);
                discountArray.add(currentDiscount);
            }
            return discountArray;

        } catch (IOException e) {
            // The connection probably failed, to indicate this, return null
            e.printStackTrace();
            return null;
        } catch (IndexOutOfBoundsException e) {
            // The site that is being parsed changed when this happens, Notify the user to send in a
            // bug report, because the app will not function anymore before I the developer fix this
            e.printStackTrace();
            sendNotificationBugReport();
            return null;
        }
    }


    // Convert a raw brand from the site (with spaces and such) into the format that is used in the app
    private String getBrand(String brandRaw) {
        if (supportedBrandsMap.containsKey(brandRaw)){
            return (String)supportedBrandsMap.get(brandRaw);
        }
        // Default:
        return "default";
    }

    // The site that is being parsed changed when this happens, Notify the user to send in a
    // bug report, because the app will not function anymore before I the developer fix this
    private void sendNotificationBugReport(){
        // Give notification
        // Code inspired by: http://developer.android.com/guide/topics/ui/notifiers/notifications.html#Removing
        // Add the specific discount info to the notification title
        String title = appContext.getString(R.string.app_name);
        String body = appContext.getString(R.string.NotificationWhenSiteChanged);

        NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // Creates an explicit intent for an Activity in your app
        // TODO: send an intent to the activity where the user is asked to go to the google play
        // TODO: page and send a bugreport
//            Intent resultIntent = new Intent(appContext, NotificatieRegelActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(appContext);
        // Adds the back stack for the Intent (but not the Intent itself)
//            stackBuilder.addParentStack(NotificatieRegelActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
//            stackBuilder.addNextIntent(resultIntent);
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(
//                            0,
//                            PendingIntent.FLAG_UPDATE_CURRENT
//                    );

        Notification notification = new Notification.Builder(appContext)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setShowWhen(false)
//                    .setContentIntent(resultPendingIntent)
                .setOngoing(false)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }
}
