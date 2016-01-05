package nl.mprog.project.bieraanbiedingnotificatie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // TODO dit gaat meteen naar naar het volgende scherm
        TextView nonsens = (TextView)findViewById(R.id.html_content);
        onClickDiscountsButton(nonsens);
    }

    public void onClickNotificationButton(View view){
        // Go to the Notification managing Activity
        startActivity(new Intent(getApplicationContext(), NotificatieRegelActivity.class));
        this.finish();
    }

    public void onClickDiscountsButton(View view){
        // Go to the activity with all discounts
        startActivity(new Intent(getApplicationContext(), AlleAanbiedingenActivity.class));
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
