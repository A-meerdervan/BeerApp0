package nl.mprog.project.bieraanbiedingnotificatie;

/**
 * Created by Alex van der Meer
 * Student number: 10400958
 * on 6-1-2016.
 *
 * This class holds the information on a discount.
 * It holds all relevant details.
 */

public class DiscountObject {

    public int id = 0;
    public String brandPrint = "defaultBrandPrint";
    public String brand = "defaultBrand";
    public String format = "defaultFormat";
    public Double price = 0.;
    public Double pricePerLiter = 0.;
    public String superMarkt = "defaultSuperMarkt";
    public String discountPeriod = "defaultPeriod";
    public String type = "defaultType";
    public Integer notificationFlag = 0;

    // Constructor
    public DiscountObject(String brandPrint, String brand , String format, Double price, Double pricePerLiter, String superMarkt, String discountPeriod, String type){
        this.brandPrint = brandPrint;
        this.brand = brand;
        this.format = format;
        this.price = price;
        this.pricePerLiter = pricePerLiter;
        this.superMarkt = superMarkt;
        this.discountPeriod = discountPeriod;
        this.type = type;
    }

    // Empty constructor for use in the database
    public DiscountObject(){

    }


}