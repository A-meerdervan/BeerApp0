package nl.mprog.project.bieraanbiedingnotificatie;


import java.util.HashMap;

/**
 * Created by Alex on 12-1-2016.
 *
 * This class holds a map with as keys possible ways to spell a part of a supermarket name
 * and as values the way this supermarket chain is always named inside the code of this app
 * This is used to filter out the supermarkets of witch the discount information is unknown
 */

public class SupportedSupermarketsMap extends HashMap {

    public SupportedSupermarketsMap() {
        this.put("albert","albertheijn");
        this.put("AH","albertheijn");
        this.put("Albert","albertheijn");
        this.put("ALBERT","albertheijn");

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

        this.put("Super de boer","superdeboer");
        this.put("super de Boer","superdeboer");

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

        this.put("Attent","attent");
        this.put("attent","attent");

        this.put("agrimarkt","agrimarkt");
        this.put("Agrimarkt","agrimarkt");

        this.put("poiesz","poiesz");
        this.put("Poiesz","poiesz");

//        this.put("","");
//        this.put("","");


    }
}

