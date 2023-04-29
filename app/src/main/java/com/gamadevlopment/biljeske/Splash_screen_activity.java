package com.gamadevlopment.biljeske;

import static com.gamadevlopment.biljeske.Fragment_classes.Create_account_fragment.INTENT_TAG_USER_DATA;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash_screen_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();


        // Display the splash screen for a few seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Launch the main activity
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser curentUser = mAuth.getCurrentUser();
                if (curentUser != null) {
                    Intent startMainActivity = new Intent(Splash_screen_activity.this, MainActivity.class);
                    startMainActivity.putExtra(INTENT_TAG_USER_DATA, curentUser);
                    startActivity(startMainActivity);
                    finish();
                } else {
                    Intent intent = new Intent(Splash_screen_activity.this, Log_in_activity.class);
                    startActivity(intent);

                    // Finish the splash screen activity
                    finish();
                }


            }
        }, 2000); // Delay for 2 seconds
    }
}