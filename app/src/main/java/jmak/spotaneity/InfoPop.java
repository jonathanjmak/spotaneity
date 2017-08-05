package jmak.spotaneity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Ryanluu2017 on 8/2/2017.
 */

public class InfoPop extends Activity {

    //Pop up manager variables
    DisplayMetrics dm;
    int width;
    int height;

    //Content view variables
    TextView titleView;
    HashMap<String,HashMap<String,String>> yelpInfoList=new LinkedHashMap<String,HashMap<String,String>>();
    String selectedName=new String();
    String selectedUrl=new String();
    String selectedPhone=new String();
    String selectedDescription=new String();
    String selectedRating=new String();
    String selectedLocation=new String();
    ArrayList<String> selectedInfoList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sets the view to the map page
        setContentView(R.layout.info_pop);

        //Receives the intent and extra string and hash map
        Intent yelpIntent=getIntent();
        yelpInfoList=(HashMap<String, HashMap<String,String>>) yelpIntent.getSerializableExtra("yelp_data");
        selectedName=(String) yelpIntent.getStringExtra("selected");

        //Pre-processes data
        prepData();

        //Sets up all the data in the pop up
        setData();

        //Calls method that handles pop-up set up
        setPopUp();
    }

    protected void prepData(){

        //Extracts business data from the selected item
        selectedDescription=(String) yelpInfoList.get(selectedName).get("description");
        selectedPhone=(String) yelpInfoList.get(selectedName).get("phone");
        selectedUrl=(String) yelpInfoList.get(selectedName).get("website");
        selectedRating=(String) yelpInfoList.get(selectedName).get("rating");
        selectedLocation=(String) yelpInfoList.get(selectedName).get("location");

        //Adds business data to an ArrayList
        selectedInfoList.add("Phone: "+selectedPhone);
        selectedInfoList.add("Website: "+selectedUrl);
        selectedInfoList.add("Address: "+selectedLocation);
        selectedInfoList.add("Review: "+selectedDescription);
        selectedInfoList.add("Rating: "+selectedRating);


    }

    protected void setData(){

        //Sets the title of the text view to the selected restaurant name
        titleView=(TextView) findViewById(R.id.busTitle);
        titleView.setText(selectedName);

        //Sets the list view adapter for restaurant data
        ListView businessInfoLV=(ListView) findViewById(R.id.busInfoLV);
        ArrayAdapter businessInfoAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,selectedInfoList);
        businessInfoLV.setAdapter(businessInfoAdapter);

        //Calls a method that sets items clickable

        setClickableInfo(businessInfoLV);

    }

    protected void setClickableInfo(ListView infoListView){


        infoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Extracts text from the letters
                TextView clickedView=(TextView) view;
                String clickedText=((TextView) view).getText().toString();


                switch (clickedText.substring(0,4)){

                    case "Phon":
                        makeCall(selectedPhone);
                        break;

                    case "Webs":
                        openUrl(selectedUrl);
                        break;

                    case "Addr":
                        openMaps(selectedLocation);
                        break;

                }

            }
        });}

    //TODO!!!!!! On long click, make it call directly
    //Makes phone calls by starting intent to phone
        protected void makeCall(String number){

            Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",number,null));
            startActivity(callIntent);

    }

    //Redirects to web page by starting intent to webpage
    protected void openUrl(String website){

        Intent webIntent=new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(website));
        startActivity(webIntent);

    }

    //TODO!!!!!! On long click, make it nav directly (switch up the order of this intent)
    //Opens google maps to location
    protected void openMaps(String address){

        Intent mapIntent=new Intent(Intent.ACTION_VIEW,Uri.parse("google.navigation:q="+address));
        startActivity(mapIntent);

    }


    //Method that handles the set up of the info pop-up
    protected void setPopUp(){

        //Creates new Display Metrics object
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //Sets width and height from display manager
        width=dm.widthPixels;
        height=dm.heightPixels;

        //Sets the popup layout in the context of the screen
        getWindow().setLayout((int)(width*0.9),(int)(height*0.6));

    }

}
