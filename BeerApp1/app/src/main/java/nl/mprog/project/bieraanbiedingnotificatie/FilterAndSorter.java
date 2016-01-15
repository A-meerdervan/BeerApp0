package nl.mprog.project.bieraanbiedingnotificatie;

import java.util.ArrayList;
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

    public FilterAndSorter(){

    }

    // This function sorts the the discounts on price, the cheapest come first
    public List<DiscountObject> sortOnPrice(List<DiscountObject> discountArray){
        int arraySize = discountArray.size();
        DiscountObject discountObject;
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

}
