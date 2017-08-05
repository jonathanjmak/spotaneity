package jmak.spotaneity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ryanluu2017 on 7/24/2017.
 */

public class MapPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_page);

        //Receives the intent from home
        Intent resultsReceiveIntent=getIntent();


    }


}
