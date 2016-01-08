package nl.mprog.project.bieraanbiedingnotificatie;

/**
 * Created by Alex on 5-1-2016.
 * This class gets the dicount information from a site
 * and is able to return a list of DiscountObject's
 */

import android.util.Log;

// Jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    private static final String tag = "htmlParse";
    private Document doc;
    private String htmlURL = "http://www.bierindeaanbieding.nl/krattenindeaanbieding.html";
    public List<DiscountObject> discountArray = new ArrayList<>();

    // constructor
    public HtmlParser(){
    }

    public List<DiscountObject> getDiscountsArray(){
        try {
            //Get Document object after parsing the html from given url.
            doc = Jsoup.connect(htmlURL).get();
            String type = "Krate";
            //Loop the discount elements.
            for (Element element : doc.getElementsByClass("data")){

                // Get table rows filled with info
                Element div = element.getElementsByClass("data2").first();
                Elements tableRows = div.getElementsByTag("tbody").first().children();
                // Get the brand
                String brand = tableRows.first().child(0).child(0).html();
                // Get the format
                String format = tableRows.get(1).child(0).html();
                // Get the price
                double price = 0;
                Elements priceH2 = tableRows.get(3).child(1).child(0).children();

                if (priceH2.size() == 2){
                    String priceString = priceH2.get(1).html();
                    String[] priceParts = priceString.split(",");
                    priceString = priceParts[0] + "." + priceParts[1];
                    price = Double.parseDouble(priceString);
                }
                else{
                    String priceString = priceH2.get(0).html().substring(2);
                    String[] priceParts = priceString.split(",");
                    priceString = priceParts[0] + "." + priceParts[1];
                    price = Double.parseDouble(priceString);
                }
                // get the discount period
                String discountPeriod = tableRows.get(5).child(0).html();
                // get the price per liter
                String pricePerLiterString = tableRows.get(6).child(1).html().substring(2);
                String[] priceParts = pricePerLiterString.split(",");
                pricePerLiterString = priceParts[0] + "." + priceParts[1];
                double pricePerLiter = Double.parseDouble(pricePerLiterString);


                // Get the supermarket
                div = element.getElementsByClass("data3").first();
                String superMarket = div.child(0).attr("alt");

                DiscountObject currentDiscount = new DiscountObject(brand,format,price,pricePerLiter,superMarket,discountPeriod,type);
                discountArray.add(currentDiscount);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return discountArray;
    }
}
