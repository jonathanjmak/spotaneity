package jmak.spotaneity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ryanluu2017 on 7/23/2017.
 */


//Remember to make an inner class that handles all of the web requests (Async Task should be extended)
public class FoursquareResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foursquare_results);

        //Receives the intent from home
        Intent homeReceiveIntent=getIntent();

        //Sets the text of the text view equal to a custom text
        TextView stuff=(TextView) findViewById(R.id.fourSquareText);
        stuff.setText("Stingy boy");

    }

    public void onMapClick(View v){

        //Starts an intent to the map page
        Intent mapIntent=new Intent(FoursquareResults.this, MapPage.class);
        startActivity(mapIntent);

    }

    public void onHomeClick(View v){

        //Starts an intent to home page
        Intent backHomeIntent= new Intent(FoursquareResults.this,HomePage.class);
        startActivity(backHomeIntent);

    }

}
