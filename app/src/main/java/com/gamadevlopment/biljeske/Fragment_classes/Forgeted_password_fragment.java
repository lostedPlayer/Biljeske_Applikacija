package com.gamadevlopment.biljeske.Fragment_classes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gamadevlopment.biljeske.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class Forgeted_password_fragment extends Fragment {

    private FirebaseAuth mAuth;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forgeted_password_fragment_layout, container, false);
        ImageView backButton = view.findViewById(R.id.forgeted_password_back_button);
        EditText emailEditText = view.findViewById(R.id.forgeted_password_acc_EX_userEmail);
        Button submitButton = view.findViewById(R.id.forgeted_password_acc_BT_submit);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and configure new fragment
                Log_in_fragment log_in_fragment = new Log_in_fragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, log_in_fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.length() > 5) {
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Password reset email sent successfully
                                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Email je poslan", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        // Error sending password reset email
                                        Log.d("TEST", "Error when sending forgeted pasword email : " + task.getException());
                                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Greska prilikom slanja Emaila", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {
                    Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Email je nespravan", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
