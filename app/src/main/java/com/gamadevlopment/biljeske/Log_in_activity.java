package com.gamadevlopment.biljeske;


import static com.gamadevlopment.biljeske.Fragment_classes.Create_account_fragment.INTENT_TAG_USER_DATA;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gamadevlopment.biljeske.Fragment_classes.Create_account_fragment;
import com.gamadevlopment.biljeske.Fragment_classes.Log_in_fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Log_in_activity extends AppCompatActivity {

    Create_account_fragment create_account_fragment;
    private FirebaseAuth mAuth;

    Log_in_fragment log_in_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();

        //check if user is log in allready
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser curentUser = mAuth.getCurrentUser();
        if (curentUser != null) {
            Intent startMainActivity = new Intent(this, MainActivity.class);
            startMainActivity.putExtra(INTENT_TAG_USER_DATA, curentUser);
            startActivity(startMainActivity);
            finish();
        }

        create_account_fragment = new Create_account_fragment();
        log_in_fragment = new Log_in_fragment();

        //create acc
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, create_account_fragment).commit();


    }

}