package jmak.spotaneity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Ryanluu2017 on 7/23/2017.
 */

public class FoursquareResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foursquare_results);

        TextView stuff=(TextView) findViewById(R.id.fourSquareText);
        stuff.setText("Jasdkl;fjas");

    }

}
