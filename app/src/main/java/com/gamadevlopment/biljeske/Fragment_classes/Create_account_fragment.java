package com.gamadevlopment.biljeske.Fragment_classes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gamadevlopment.biljeske.Log_in_activity;
import com.gamadevlopment.biljeske.MainActivity;
import com.gamadevlopment.biljeske.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Create_account_fragment extends Fragment {

    private FirebaseAuth mAuth;

    public static String INTENT_TAG_USER_DATA = "INTENT_TAG_USER_DATA";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_acount_fragment_layout, container, false);


        //check if user is log in allready
        FirebaseUser curentUser = mAuth.getCurrentUser();
        if (curentUser != null) {
            Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Korisnik Prijavljen", Snackbar.LENGTH_SHORT).show();
            Intent startMainActivity = new Intent(Create_account_fragment.this.getContext(), MainActivity.class);
            startMainActivity.putExtra(INTENT_TAG_USER_DATA, curentUser);
            startActivity(startMainActivity);
        }


        EditText userEmail = view.findViewById(R.id.create_acc_EX_userEmail);
        EditText userPassword = view.findViewById(R.id.create_acc_EX_userPassword);

        Button submitButton = view.findViewById(R.id.create_acc_BT_Subbmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();


                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length() > 8) {
                    //create user profile
                    mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TEST", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent startMainActivity = new Intent(Create_account_fragment.this.getContext(), MainActivity.class);
                                        startMainActivity.putExtra(INTENT_TAG_USER_DATA, curentUser);
                                        startActivity(startMainActivity);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TEST", "createUserWithEmail:failure", task.getException());
                                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Greska prilikom prijave", Snackbar.LENGTH_LONG).show();
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


        //have account button
        TextView haveAccountButton = view.findViewById(R.id.create_acc_BT_have_account);
        haveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create and configure new fragment
                Log_in_fragment newFragment = new Log_in_fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;
    }

}
