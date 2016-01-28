package nl.mprog.project.bieraanbiedingnotificatie;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alex on 15-1-2016.
 *
 * This class is responsible for sorting and filtering
 * It is possible to sort discounts on: price, distance
 *
 * It is possible to filter on Supermarket, brand, price and distance
 */
public class FilterAndSorter {

    private static final String tag = "*C_FltrAndSrtr";

    public FilterAndSorter(){

    }

    public List<DiscountObject> filterAndSort(String sortOption, Double maxPrice, List<String> checkedBeerOptions, List<String> checkedSuperMarkets, List<DiscountObject> discountArray) {

        // Filter on the given parameters
        List<DiscountObject> resultingList = filter(maxPrice, checkedBeerOptions, checkedSuperMarkets, discountArray);

        Log.d(tag, "Lengte resultList " + resultingList.size() + " na filteren");

        // Now sort the array
        if (sortOption.equals("Prijs")){
            Log.d(tag, "Sort on price..");
            resultingList = sortOnPrice(resultingList);
        }
        else if(sortOption.equals("Supermarkt keten") ){
            Log.d(tag, "Sort on supermarktet alfabetically..");
            resultingList = sortOnSuperMarket(resultingList);
        }
        else {
            Log.d(tag, "Sort on beer brand alfabetically..");
            resultingList = sortOnBeerBrand(resultingList);
        }

        Log.d(tag, "Lengte resultinglist " + resultingList.size()+ " Na sorteren");
        return resultingList;
    }

    // This method filters on price, on beer brands, and on supermarkets
    // It still works when the price is not set (null) or when the user did not check any beer brands
    // (then the list length == 0)
    public List<DiscountObject> filter(Double maxPrice, List<String> beerOptions, List<String> superMarkets, List<DiscountObject> discountArray ){
        List<DiscountObject> filteredList = new ArrayList<>();
        Boolean priceCheck;
        Boolean beerCheck;
        Boolean superMarketCheck;

        for (int i = 0; i < discountArray.size(); i++) {
            // If the user filled in a price, check the disocunt against it
//            Log.d(tag, "Maxprice: " + maxPrice + " beerlen " + beerOptions.size() +  " superlen " + superMarkets.size());
            if (maxPrice!= null){
                priceCheck = discountArray.get(i).price < maxPrice;
            }
            else { priceCheck = true; }
            // If the user checked some beer Brands, check for them
            if (beerOptions.size() > 0){
                beerCheck = beerOptions.contains(discountArray.get(i).brand);
//                Log.d(tag, "Check voor " + discountArray.get(i).brand)
            }
            else{ beerCheck = true; }
            // If the user checked some superMarkets, check for them
            if (superMarkets.size() > 0) {
                superMarketCheck = superMarkets.contains(discountArray.get(i).superMarkt);
            }
            else { superMarketCheck = true; }

            // If the item passes the filter settings, add it to the output list
            if ( priceCheck && beerCheck && superMarketCheck ){
                filteredList.add(discountArray.get(i));
            }
        }
        return filteredList;
    }

    // This function sorts the the discounts on price, the cheapest come first
    public List<DiscountObject> sortOnPrice(List<DiscountObject> discountArray){
        int arraySize = discountArray.size();
        DiscountObject tempDiscount;

        for( int i = 0; i < arraySize; i++){
            for( int j = 1; j < (arraySize - i); j++){
                if(discountArray.get(j - 1).price > discountArray.get(j).price){
                    //swap the elements
                    tempDiscount = discountArray.get(j - 1);
                    discountArray.set(j - 1, discountArray.get(j));
                    discountArray.set( j , tempDiscount);
                }
            }
        }
        return discountArray;
    }

    // Sort on the names of the superMarkets alfabetticaly.
    // Code inspired by: http://stackoverflow.com/questions/19471005/sorting-an-arraylist-of-objects-alphabetically
    public List<DiscountObject> sortOnSuperMarket(List<DiscountObject> discountArray){
        Collections.sort(discountArray, new Comparator<DiscountObject>() {
            public int compare(DiscountObject d1, DiscountObject d2) {
                return d1.superMarkt.compareTo(d2.superMarkt);
            }
        });
        return discountArray;
    }

    // Sort on the names of the beer brands alfabetticaly.
    // Code inspired by: http://stackoverflow.com/questions/19471005/sorting-an-arraylist-of-objects-alphabetically
    public List<DiscountObject> sortOnBeerBrand(List<DiscountObject> discountArray){
        Collections.sort(discountArray, new Comparator<DiscountObject>() {
            public int compare(DiscountObject d1, DiscountObject d2) {
                return d1.brand.compareTo(d2.brand);
            }
        });
        return discountArray;
    }
}
