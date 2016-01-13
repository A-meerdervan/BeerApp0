package nl.mprog.project.bieraanbiedingnotificatie;

/**
 * Created by Alex on 12-1-2016.
 *
 * This model class represents the simple information that belongs to a supermarket
 */
public class SuperMarket {

    public String chainName = "Default Supermarket";
    public String individualName = "Default individual name";
    public String adres = "Default Adres";
    public Double latitude;
    public Double longitude;

    public SuperMarket(String chainName, String individualName, String adres, Double latitude, Double longitude){
        this.chainName = chainName;
        this.individualName = individualName;
        this.adres = adres;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        String output = "";
        output += ("\n" + chainName + "\n" + individualName + "\n" + adres + "\n"
        );
        return output;
    }
}
