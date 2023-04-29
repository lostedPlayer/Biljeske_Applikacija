package com.gamadevlopment.biljeske.Fragment_classes;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gamadevlopment.biljeske.MainActivity;
import com.gamadevlopment.biljeske.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class Log_in_fragment extends Fragment {


    private FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        //check if user is log in allready
        FirebaseUser curentUser = mAuth.getCurrentUser();
        if (curentUser != null) {
            //TODO: UNCOMENT WHEN RELESING

            Snackbar.make(getActivity().findViewById(R.id.fragment_container) , "Korisnik Prijavljen" , Snackbar.LENGTH_SHORT).show();
            //  Intent startMainActivity = new Intent(Log_in_fragment.this.getContext(), MainActivity.class);
            //  startActivity(startMainActivity);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.log_in_fragment_layout, container, false);

        EditText userEmail = view.findViewById(R.id.log_in__acc_EX_userEmail);
        EditText userPassword = view.findViewById(R.id.Log_in_acc_EX_userPassword);
        ImageView backButton = view.findViewById(R.id.Log_in_back_button);

        //back button pressed
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_account_fragment forgeted_password_fragment = new Create_account_fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, forgeted_password_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        //if user has forgeted his password
        TextView forgetedPasswordButton = view.findViewById(R.id.Log_in_acc_BT_fogeted_password);
        forgetedPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and configure new fragment
                Forgeted_password_fragment forgeted_password_fragment = new Forgeted_password_fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, forgeted_password_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



        //log in user
        Button submitButton = view.findViewById(R.id.Log_in_acc_BT_Subbmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();


                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() > 8) {
                    //create user profile
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TEST", "createUserWithEmail:success");
                                        Snackbar.make(getActivity().findViewById(R.id.fragment_container) , "Korisnik uspjesno Prijavljen" , Snackbar.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent startMainActivity = new Intent(Log_in_fragment.this.getContext(), MainActivity.class);
                                        startActivity(startMainActivity);
                                        // updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TEST", "Log in user :failure", task.getException());
                                        Snackbar.make(getActivity().findViewById(R.id.fragment_container) , "Greska tokom prijave Korisnika" , Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Validate email
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        userEmail.setError("Email Adresa nije ispravna");
                        userEmail.requestFocus();
                    }

                    // Validate password
                    if (password.length() < 7) {
                        userPassword.setError("Lozinka mora biti duza od 8 znakova");
                        userPassword.requestFocus();
                    }
                }

            }
        });

        return view;
    }

}
