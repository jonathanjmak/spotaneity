package jmak.spotaneity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Ryanluu2017 on 7/27/2017.
 */

//Must implement serializable if you want to pass along ArrayLists in intents
public class GooglePlacePicker extends AppCompatActivity implements Serializable {

    //API variables
    YelpData callYelp;

    //Widget variables
    private ListView placeView;

    //Place picker variables
    private int PLACE_PICKER_REQUEST=1; //1 for release //2 for debug

    //Organizing info variables
    private ArrayList<String> placeList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        //Sets the view to the map page
            setContentView(R.layout.spot_page);

        }


        //Button event that loads the google place picker functionality
        public void onGoogPickClick(View v){
            //Sets up the builder for the intent to Google Place Picker
            PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
            Intent placeIntent;

            try {
                //Creates an intent to the GooglePlacePicker class
                placeIntent = builder.build(GooglePlacePicker.this);
                startActivityForResult(placeIntent,PLACE_PICKER_REQUEST);
            }
            catch(Exception e){
                //Alerts user of limited functionality of app
                Toast.makeText(getApplicationContext(),"Sorry, Google place picker is currently unavailable",Toast.LENGTH_SHORT).show();

            }

        }

        //Yields the result of the activity and utilizes data extracted from Google Place Picker
        protected void onActivityResult(int requestCode,int resultCode,Intent data){
            /*Make sure to add in a hash map that contains data in an easy to understand format.
            Also, add an else statement to return user in the case that the request goes awry.
             */

            //Authenticates the request and result codes
            if (requestCode==PLACE_PICKER_REQUEST){

                if (resultCode==RESULT_OK){

                    //Gets the user's pick of the place from the PlacePicker UI
                    Place place=PlacePicker.getPlace(getApplicationContext(),data);

                    //Gets locational information from Google Place Picker such as the name and coordinates
                    Double lat= place.getLatLng().latitude;
                    Double lon=place.getLatLng().longitude;
                    String name=place.getName().toString();

                    //Calls the yelp API to extract detailed info about a location
                    callYelp=new YelpData(GooglePlacePicker.this,this,name,lat,lon);
                    callYelp.startYelp();


                }

            }

        }



        //Button click event that performs a straight search of locations based on keywords and a location
        public void onSearchClick(View v){

            callYelp=new YelpData(GooglePlacePicker.this,this);
            callYelp.startYelp();

        }

}

