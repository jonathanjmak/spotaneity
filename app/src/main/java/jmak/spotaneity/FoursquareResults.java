package jmak.spotaneity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenueGroup;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ryanluu2017 on 7/23/2017.
 */


//Remember to make an inner class that handles all of the web requests (Async Task should be extended)
public class FoursquareResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foursquare_results);

        Retrofit retrofit= new Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build();


        //Receives the intent from home
        Intent homeReceiveIntent=getIntent();

        //Sets the text of the text view equal to a custom text
        TextView fourSquarePage=(TextView) findViewById(R.id.fourSquareText);
        fourSquarePage.setText("spot events");

       new getFourSquareAPI().execute();


    }


    protected class getFourSquareAPI extends AsyncTask<Void, Void, ArrayList<String>> {

        protected ArrayList<String> placeNames=new ArrayList<String>();
        protected ArrayList<String> gah=new ArrayList<String>();



        @Override
        protected ArrayList<String> doInBackground(Void...params){
            //Initializes FoursquareAPI instance
            //Gathers data about a venue and then adds stuff to a list
            gah.add("bah"); //Take out later
            //Attempts to connect to the FourSquare API and extract data from the api
            try {
                FoursquareApi apiCall = new FoursquareApi("CLIENT_ID", "CLIENT_SECRET","https://spotaneity.com");



                    placeNames.add(apiCall.toString());

                Result<VenuesSearchResult> result = apiCall.venuesSearch("40.7,-70.4", null, null, null, null, null, null, null, null, null, null);

                // if query was ok we can finally we do something with the data
                   for (CompactVenue venue : result.getResult().getVenues()) {
                        // TODO: Do something with the data
                        placeNames.add(venue.getName());
                    }

            }




            catch(FoursquareApiException e){
                e.printStackTrace();
                Intent myIntent=new Intent(FoursquareResults.this,HomePage.class);
                startActivity(myIntent);

            }



            return placeNames;
        }

        @Override
        protected void onPostExecute(ArrayList<String> venues){

            TextView randView=(TextView) findViewById(R.id.fourSquareText);
            randView.setText(venues.get(0));

            ListView spotViews=(ListView) findViewById(R.id.spotView);
            ArrayAdapter venueAdapter=new ArrayAdapter(FoursquareResults.this,android.R.layout.simple_list_item_1,venues);
            spotViews.setAdapter(venueAdapter);


        }




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
