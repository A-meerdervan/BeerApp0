package nl.mprog.project.bieraanbiedingnotificatie;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 12-1-2016.
 *
 * This class holds a map with as keys possible ways to spell a part of a supermarket name
 * and as values the way this supermarket chain is always named inside the code of this app
 * This is used to filter out the supermarkets of witch the discount information is unknown
 */

public class SupportedSupermarketsMap extends HashMap {

    public static String[] superMarketsList = {
            "Albert Heijn",
            "Aldi",
            "Agrimarkt",
            "Attent",
            "Coop",
            "C1000",
            "Deen",
            "DekaMarkt",
            "Dirk van den Broek",
            "Edeka",
            "EMTÉ",
            "Hoogvliet",
            "Jan Linders",
            "Jumbo",
            "Lidl",
            "MCD",
            "Penny",
            "Plus",
            "Poiesz",
            "Spar",
            "Super de Boer",
            "Vomar"
    };
    public HashMap<String,String> backwardsSuperMarketMap;

    // The keys are possible names that come up in a google places API search
    // and the values are the code name thats used in this app
    public SupportedSupermarketsMap() {
        this.put("albert","albertheijn");
        this.put("AH","albertheijn");
        this.put("Albert","albertheijn");
        this.put("ALBERT","albertheijn");
        this.put("Albert Heijn","albertheijn");

        this.put("DEEN","deen");
        this.put("Deen","deen");
        this.put("deen","deen");

        this.put("Aldi","aldi");
        this.put("aldi","aldi");
        this.put("ALDI","aldi");

        this.put("Vomar","vomar");
        this.put("vomar","vomar");
        this.put("VOMAR","vomar");

        this.put("Jumbo","jumbo");
        this.put("jumbo","jumbo");
        this.put("JUMBO","jumbo");

        this.put("Coop","coop");
        this.put("COOP","coop");
        this.put("coop","coop");

        this.put("Linders","janlinders");
        this.put("linders","janlinders");
        this.put("LINDERS","janlinders");
        this.put("Jan Linders","janlinders");

        this.put("MCD","mcd");
        this.put("mcd","mcd");

        this.put("Emte","emte");
        this.put("emte","emte");
        this.put("EMTÉ","emte");
        this.put("EMTE","emte");
        

        this.put("Spar","spar");
        this.put("spar","spar");
        this.put("SPAR","spar");

        this.put("Dirk","dirk");
        this.put("dirk","dirk");
        this.put("DIRK","dirk");
        this.put("Dirk van den Broek","dirk");

        this.put("Super de boer","superdeboer");
        this.put("super de Boer","superdeboer");
        this.put("Super de Boer","superdeboer");

        this.put("Edeka","edeka");
        this.put("edeka","edeka");
        this.put("EDEKA","edeka");

        this.put("Penny","penny");
        this.put("penny","penny");
        this.put("PENNY","penny");

        this.put("Lidl","lidl");
        this.put("LIDL","lidl");
        this.put("lidl","lidl");

        this.put("Plus","plus");
        this.put("plus","plus");
        this.put("PLUS","plus");


        this.put("C1000","c1000");
        this.put("c1000","c1000");

        this.put("Hoogvliet","hoogvliet");
        this.put("hoogvliet","hoogvliet");
        this.put("HOOGVLIET","hoogvliet");

        this.put("Deka","deka");
        this.put("deka","deka");
        this.put("DekaMarkt","deka");

        this.put("Attent","attent");
        this.put("attent","attent");

        this.put("agrimarkt","agrimarkt");
        this.put("Agrimarkt","agrimarkt");

        this.put("poiesz","poiesz");
        this.put("Poiesz","poiesz");

        // now create a backwards mapping
        backwardsSuperMarketMap = new HashMap<>();
        backwardsSuperMarketMap.put("albertheijn","Albert Heijn");
        backwardsSuperMarketMap.put("aldi","ALDI supermarkten");
        backwardsSuperMarketMap.put("agrimarkt","Agrimarkt");
        backwardsSuperMarketMap.put("attent","Attent");
        backwardsSuperMarketMap.put("coop","Coop supermarkten");
        backwardsSuperMarketMap.put("c1000","C1000");
        backwardsSuperMarketMap.put("deen","DEEN supermarkten");
        backwardsSuperMarketMap.put("deka","DekaMarkt");
        backwardsSuperMarketMap.put("dirk","Dirk van den Broek");
        backwardsSuperMarketMap.put("edeka","Edeka");
        backwardsSuperMarketMap.put("emte","EMTÉ supermarkt");
        backwardsSuperMarketMap.put("hoogvliet","Hoogvliet");
        backwardsSuperMarketMap.put("janlinders","Jan Linders");
        backwardsSuperMarketMap.put("jumbo","Jumbo");
        backwardsSuperMarketMap.put("lidl","Lidl");
        backwardsSuperMarketMap.put("mcd","MCD supermarkt");
        backwardsSuperMarketMap.put("penny","PENNY-Markt Discounter");
        backwardsSuperMarketMap.put("plus","Plus");
        backwardsSuperMarketMap.put("poiesz","Poiesz Supermarkten");
        backwardsSuperMarketMap.put("spar", "Spar");
        backwardsSuperMarketMap.put("superdeboer", "Super de Boer");
        backwardsSuperMarketMap.put("vomar", "Vomar Voordeelmarkt");
    }

    // Get the list of supported chainnames
    public List<String> getChainNames(){
        List<String> chainNames = new ArrayList<>();
        for (String name : superMarketsList){
            chainNames.add((String)this.get(name));
        }
        return chainNames;
    }
}

