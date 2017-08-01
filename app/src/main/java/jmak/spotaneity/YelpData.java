package jmak.spotaneity;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ryanluu2017 on 7/31/2017.
 */

public class YelpData {

    //Take these out later when I finally set up gradle properties
    String consumerKey="your key";
    String consumerSecret="your secret";
    String token="your token";
    String tokenSecret="your token secret";

    Context context;
    Activity activity;

    Map<String,String> params=new HashMap<>();
    String searchTerm;
    String limit;
    String location;

    YelpAPI yelpAPI;

    //Constructor that takes in the context of the activity and the activity
    public YelpData(Context context, Activity activity){

        this.context=context;
        this.activity=activity;

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

        //Extracts text from text fields for the term and location
        String searchTerm=((TextView) activity.findViewById(R.id.searchEnter)).getText().toString();
        String locationTerm=((TextView) activity.findViewById(R.id.locationEnter)).getText().toString();

        //Configures the parameters into a hashmap object
        params.put("term",searchTerm);
        params.put("limit","10");

        //Creates the actual call with the search queries
        Call<SearchResponse> call=yelpAPI.search(locationTerm,params);
        getAsyncResponse(call);

    }

    protected void getAsyncResponse(Call<SearchResponse> call){


        //Creates a callback that is passed to the enqueue method
        Callback<SearchResponse> yelpCallback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                String busName=searchResponse.businesses().get(0).categories().toString();

                //Calls a local method that will set the info based on the extracted business info
                setInfo(busName);

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

    protected void setInfo(String text){

        TextView mapTitle=(TextView) activity.findViewById(R.id.mapTitle);
        mapTitle.setText(text);

    }

}
