package jmak.spotaneity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Receives an intent from anything that calls home
        Intent anyIntent=getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }


    public void onSpotItClick(View v){

        Intent fourSquareIntent=new Intent(HomePage.this,FoursquareResults.class);
        startActivity(fourSquareIntent);


    }

}
