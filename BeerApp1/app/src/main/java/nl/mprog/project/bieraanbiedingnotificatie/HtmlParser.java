package nl.mprog.project.bieraanbiedingnotificatie;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Alex on 5-1-2016.
 */
public class HtmlParser {

    private static final String tag = "htmlParse";
    private Document htmlDocument;
    private String htmlPageUrl = "http://inducesmile.com/";

    // constructor
    public HtmlParser(){
    }

    public String getString(){
        String tempString = "";
        try {
            htmlDocument = Jsoup.connect(htmlPageUrl).get();
            Log.d(tag, "oi");

            tempString = htmlDocument.title();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempString;
    }
}
