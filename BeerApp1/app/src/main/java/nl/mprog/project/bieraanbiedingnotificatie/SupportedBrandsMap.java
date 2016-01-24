package nl.mprog.project.bieraanbiedingnotificatie;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 12-1-2016.
 *
 * This class holds a map with as keys: possible ways to spell a part of a brand name
 * and as values the way this brand is always named inside the code of this app
 * This is used to filter out the supermarkets of witch the discount information is unknown
 */

public class SupportedBrandsMap extends HashMap {

    public static String[] brandsList = {
            "Alfa",
            "Amstel",
            "Bavaria",
            "Brand",
            "Grolsch",
            "Grolsch Beugel",
            "Grolsch Radler",
            "Gulpener",
            "Hertog Jan",
            "Heineken",
            "Jupiler",
            "Keizerskroon",
            "Palm",
            "Schutters",
            "Warsteiner",
    };

    public SupportedBrandsMap(){

        this.put("Alfa","alfa");
        this.put("Amstel","amstel");
        this.put("Bavaria","bavaria");
        this.put("Brand","brand");
        this.put("Grolsch","grolsch");
        this.put("Grolsch Beugel", "grolschbeugel");
        this.put("Grolsch Radler","grolschradler");
        this.put("Gulpener","gulpener");
        this.put("Hertog Jan","hertogjan");
        this.put("Heineken","heineken");
        this.put("Jupiler","jupiler");
        this.put("Keizerskroon","keizerskroon");
        this.put("Palm","palm");
        this.put("Schutters","schutters");
        this.put("Warsteiner","warsteiner");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");
    }

    // Get the list of supported brands in the lowerCase form without spaces
    public List<String> getBrands() {
        List<String> brands = new ArrayList<>();
        for (String name : brandsList) {
            brands.add((String) this.get(name));
        }
        return brands;
    }
}

