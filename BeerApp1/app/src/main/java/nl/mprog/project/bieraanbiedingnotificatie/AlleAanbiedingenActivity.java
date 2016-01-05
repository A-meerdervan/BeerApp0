package nl.mprog.project.bieraanbiedingnotificatie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;
// imports related to parsing HTML
import android.os.AsyncTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;



public class AlleAanbiedingenActivity extends AppCompatActivity {

    private List<String> testStrings = Arrays.asList("sup1", "sup2", "sup3");

    private Document htmlDocument;
    private String htmlPageUrl = "http://inducesmile.com/";
    private String htmlContentInStringFormat = "Failed to load data...";
    private TextView htmlTextView;
    private HtmlParser htmlParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alle_aanbiedingen);

        // parse HTML
        htmlParser = new HtmlParser();
        htmlTextView = (TextView)findViewById(R.id.html_content);
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();

        // Fill the listView
        populateListView();
    }

    private void populateListView() {
        // Fill the listview list
        ArrayAdapter<String> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.AllDiscountsList);
        list.setAdapter(adapter);
    }

    // This custom adapter class is made to fill the listview with discount information
    private class MyListAdapter extends ArrayAdapter<String> {
        private List<String> SortedArray;
        public MyListAdapter() {
            super(AlleAanbiedingenActivity.this, R.layout.listview, testStrings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with (may have been given null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.listview, parent, false);
            }

            // Find the player to work with
            String testString = testStrings.get(position);

            // Fill the view

            // Rank:
            TextView Number = (TextView) itemView.findViewById(R.id.RankNumber);
            Number.setText(testString);

            return itemView;
        }
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            htmlContentInStringFormat = htmlParser.getString();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            htmlTextView.setText(htmlContentInStringFormat);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alle_aanbiedingen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
