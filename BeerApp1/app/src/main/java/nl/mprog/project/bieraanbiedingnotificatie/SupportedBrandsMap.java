package nl.mprog.project.bieraanbiedingnotificatie;


import java.util.HashMap;

/**
 * Created by Alex on 12-1-2016.
 *
 * This class holds a map with as keys: possible ways to spell a part of a brand name
 * and as values the way this brand is always named inside the code of this app
 * This is used to filter out the supermarkets of witch the discount information is unknown
 */

public class SupportedBrandsMap extends HashMap {

    public SupportedBrandsMap(){

        this.put("Grolsch","grolsch");
        this.put("Grolsch Radler","grolschradler");
        this.put("Grolsch Beugel", "grolschbeugel");
        this.put("Amstel","amstel");
        this.put("Hertog Jan","hertogjan");
        this.put("Gulpener","gulpener");
        this.put("Alfa","alfa");
        this.put("Brand","brand");
        this.put("Warsteiner","warsteiner");
        this.put("Jupiler","jupiler");
        this.put("Heineken","heineken");
        this.put("Bavaria","bavaria");
        this.put("Schutters","schutters");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");
//        this.put("","");



    }
}

