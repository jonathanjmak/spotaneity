package jmak.spotaneity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ryanluu2017 on 7/31/2017.
 */

public class YelpData{

    //Take these out later when I finally set up gradle properties
    String consumerKey="your stuff";
    String consumerSecret="your stuff";
    String token="your stuff";
    String tokenSecret="your stuff";

    //Declarations for contexts and activities
    Context context;
    Activity activity;

    //Creates Java data structures/types for instance data
    Map<String,String> params=new HashMap<>();
    int type; //1 for straight search, 2 for google places search
    String searchTerm;
    String locationTerm;
    String limit;
    String name;
    double lat;
    double lon;

    //Instantiates ArrayList for data (change to hash map later)
    ArrayList<String> placesList=new ArrayList<String>();
    LinkedHashMap<String,LinkedHashMap<String,String>> yelpPlaces=new LinkedHashMap<String,LinkedHashMap<String, String>>();


    //Instantiates yelpAPI object
    YelpAPI yelpAPI;

    /*Remember to switch back type everytime you declare a type of search */

    //Constructor that takes in the context of the activity and the activity
    public YelpData(Context context, Activity activity){

        this.context=context;
        this.activity=activity;
        this.type=1;
    }

    public YelpData(Context context, Activity activity, String name,double lat,double lon){

        this.type=2;
        this.context=context;
        this.activity=activity;

        //If there is data in placeInfo, extract latitude, longitude and name
        if (name !=null) {
            this.lat = lat;
            this.lon = lon;
            this.name = name;
        }

        //Pre-emptively handles the null pointer exception
        else{

            Toast.makeText(activity.getApplicationContext(),"Sorry, it appears that there is no relevant data",Toast.LENGTH_SHORT);

        }

    }

    //Starts the Yelp API call
    protected void startYelp(){

        //Configures API and makes it ready to be called
        YelpAPIFactory apiFactory=new YelpAPIFactory(consumerKey,consumerSecret,token,tokenSecret);
        yelpAPI=apiFactory.createAPI();
        prepSearch(yelpAPI);

    }

    //Prepares the search terms by extracting text from EditTexts
    protected void prepSearch(YelpAPI yelpAPI){

        //If straight search, extracts data from text fields
        if (this.type==1) {
            searchTerm = ((TextView) activity.findViewById(R.id.searchEnter)).getText().toString();
            locationTerm = ((TextView) activity.findViewById(R.id.locationEnter)).getText().toString();

            //Configures the parameters into a hashmap object
            params.put("term",searchTerm);
            params.put("limit","10");

            //Creates the actual call with the search queries
            Call<SearchResponse> call=yelpAPI.search(locationTerm,params);
            getAsyncResponse(call);
        }

        //If map search, extracts coordinate data and name
        else if (this.type==2){

            //Builds the coordinate search
            CoordinateOptions coordinate = CoordinateOptions.builder()
                    .latitude(lat)
                    .longitude(lon).build();

            searchTerm=this.name;

            //Configures the parameters into a hashmap object
            params.put("term",searchTerm);
            params.put("limit","10");

            //Creates the actual call with the search queries
            Call<SearchResponse> call=yelpAPI.search(coordinate,params);
            getAsyncResponse(call);

        }


    }

    protected void getAsyncResponse(Call<SearchResponse> call){


        //Creates a callback that is passed to the enqueue method
        Callback<SearchResponse> yelpCallback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();

                //Adds each name to a list view
                for (int i=0;i<searchResponse.businesses().size();i++) {

                    //Reinstantiates the detailedInfo so that we can have updated values
                    LinkedHashMap<String,String> detailedInfo=new LinkedHashMap<String,String>();

                    //Adds business data into a hash map that is associated with the business name
                    detailedInfo.put("phone",searchResponse.businesses().get(i).phone());
                    detailedInfo.put("website",searchResponse.businesses().get(i).url());
                    detailedInfo.put("description",searchResponse.businesses().get(i).snippetText());
                    detailedInfo.put("rating",searchResponse.businesses().get(i).rating().toString());
                    detailedInfo.put("latitude",searchResponse.businesses().get(i).location().coordinate().latitude().toString());
                    detailedInfo.put("longitude",searchResponse.businesses().get(i).location().coordinate().longitude().toString());
                    //Change to full address later on
                    detailedInfo.put("location",searchResponse.businesses().get(i).location().address().get(0));

                    //Takes care of the list view adapter items
                    placesList.add(searchResponse.businesses().get(i).name());

                    //Takes care of the HashMap that gets sent to the information page
                    yelpPlaces.put(searchResponse.businesses().get(i).name(),detailedInfo);



                }

                Log.i("check: ",yelpPlaces.toString());
                //Calls a local method that will set a list of names
                setListAdapter(placesList,yelpPlaces);

            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.

                //Advises user to check their query if failure happens
                Toast.makeText(activity.getApplicationContext(),"Please check your query", Toast.LENGTH_SHORT).show();

            }
        };

        //Starts the async procedure and then sets result as the part of the view elements
        call.enqueue(yelpCallback);

    }


    //Method that sets the list adapter and then makes items clickable
    protected void setListAdapter(final ArrayList<String> placesList,final LinkedHashMap<String,LinkedHashMap<String,String>> yelpPlaces){

        //Creates an adapter for the info data form the PlacePicker before
        ArrayAdapter infoAdapter=new ArrayAdapter(activity.getApplicationContext(),android.R.layout.simple_list_item_1,placesList);

        //Sets up the list view and then sets the adapter to the one created above
        ListView placeView=(ListView) activity.findViewById(R.id.placesListView);
        placeView.setAdapter(infoAdapter);

        //Makes list view elements clickable
        placeView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {

                //Extracts the text from the TextView
                TextView selectedView=(TextView) v;
                String selected=selectedView.getText().toString();

                //Creates an intent from the current activity to the info pop up
                Intent infoPopIntent = new Intent(activity,
                        InfoPop.class);

                //Adds the array list of strings and selected string to the intent
                infoPopIntent.putExtra("yelp_data", yelpPlaces);
                infoPopIntent.putExtra("selected",selected);

                //Begins the intent to the pop up page and sends restaurant data
                activity.startActivity(infoPopIntent);
            }
        });

    }

}
