package jmak.spotaneity;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.EditText;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {
    // Declare Firebase Authentication, AuthStateListener, and TAG
    private static final String TAG = "HomePage";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Declare Foursquare API Information
    // TODO DO NOT PUSH COMMITS WITH API KEYS OR OAUTH TOKENS
    final String FourSquareClientID = "CLIENT_ID";
    final String FourSquareClientSecret = "CLIENT_SECRET";
    // UI References
    private EditText mEmail, mPassword;
    private Button btnSignIn, btnSignOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Receives an intent from anything that calls home
        Intent anyIntent=getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Declare Buttons and Edit Texts
        mEmail = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);
        btnSignIn = (Button)findViewById(R.id.email_sign_in_button);
        btnSignOut = (Button)findViewById(R.id.email_sign_out_button);



        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in as: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out!");
                }
                // ...
            }
        };

        // Signing In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if(!email.equals("") && !password.equals("")){
                    mAuth.signInWithEmailAndPassword(email,password);
                } else {
                    toastMessage("You didn't fill in all the fields!");
                }
            }

        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                toastMessage("Signed out.");
            }
        });
    }

    // Listens on Start
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // Removes Listener on Stop
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onSpotItClick(View v){

        Intent fourSquareIntent=new Intent(HomePage.this,FoursquareResults.class);
        startActivity(fourSquareIntent);


    }

}
