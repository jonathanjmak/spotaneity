package jmak.spotaneity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;


/**
 * Created by Ryanluu2017 on 7/27/2017.
 */

public class GooglePlacePicker extends AppCompatActivity {

    //Widget variables
    private ListView placeView;
    private Button placeButton;

    //Place picker variables
    private int PLACE_PICKER_REQUEST=1; //1 for release //2 for debug

    //Organizing info variables
    private ArrayList<String> placeList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.map_page);



        //Code for setting the place picker
            placeButton=(Button) findViewById(R.id.placeButton);
            placeButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();

                    Intent placeIntent;

                    try {


                        placeIntent = builder.build(GooglePlacePicker.this);
                        startActivityForResult(placeIntent,PLACE_PICKER_REQUEST);
                    }
                    catch(Exception e){


                    }

                    }

            });



        };

        protected void onActivityResult(int requestCode,int resultCode,Intent data){

            if (requestCode==PLACE_PICKER_REQUEST){

                if (resultCode==RESULT_OK){

                    //Gets the user's pick of the place from the PlacePicker UI
                    Place place=PlacePicker.getPlace(getApplicationContext(),data);

                    //Gets attributes from the place
                    String name=String.format("Name: %s",place.getName().toString());
                    String address=String.format("Place: %s",place.getAddress().toString());

                    placeList.add(name);
                    placeList.add(address);
                    if (place.getWebsiteUri()!=null) {
                        String site = String.format("Website Link: %s", place.getWebsiteUri().toString());
                        placeList.add(site);
                    }


                    //Calls a function that sets the list adapter for these items
                    setListAdapter();




                }

            }

        }

        protected void setListAdapter(){

            //Creates an adapter for the info data form the PlacePicker before
            ArrayAdapter infoAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,placeList);

            //Sets up the list view and then sets the adapter to the one created above
            placeView=(ListView) findViewById(R.id.placesListView);
            placeView.setAdapter(infoAdapter);

        }



        public void onSearchClick(View v){

            //Code for calling the Yelp Data
            YelpData callYelp=new YelpData(GooglePlacePicker.this,this);
            callYelp.startYelp();

        }

}

