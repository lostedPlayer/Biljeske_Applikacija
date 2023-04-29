package com.gamadevlopment.biljeske;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gamadevlopment.biljeske.Adapters.Note;
import com.gamadevlopment.biljeske.Adapters.Note_list_adapter;
import com.gamadevlopment.biljeske.Adapters.firebase_editing;
import com.gamadevlopment.biljeske.Fragment_classes.note_editing_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.ionbit.ionalert.IonAlert;

public class MainActivity extends AppCompatActivity implements firebase_editing {

    RecyclerView recyclerView_notes_list;
    ImageButton BT_log_out, addNoteButton;
    LinearLayout no_notes_layout;
    static Note_list_adapter adapter;

    FirebaseFirestore db;
    String userID;


    private ArrayList<Note> mNotesList = new ArrayList<>();

    //TODO: add message to apeear on top
    //TODO: show error when no internet


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        no_notes_layout = findViewById(R.id.no_notes_layout);
        BT_log_out = findViewById(R.id.Note_list_activity_menuButton);
        recyclerView_notes_list = findViewById(R.id.Notes_activity_recyclerView);


        //Log Out user on Button clicked
        BT_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show dialog to confirm user log out
                new IonAlert(MainActivity.this, IonAlert.WARNING_TYPE)
                        .setContentText(getString(R.string.log_out_dialog_message))
                        .setCancelText(getString(R.string.log_out_dialog_cancle_button))
                        .setConfirmText(getString(R.string.log_out_dialog_log_out_button))
                        .setConfirmClickListener(new IonAlert.ClickListener() {
                            @Override
                            public void onClick(IonAlert ionAlert) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                mAuth.signOut();

                                Intent beckToLoginIntent = new Intent(MainActivity.this, Log_in_activity.class);
                                startActivity(beckToLoginIntent);
                                finish();
                            }
                        })
                        .setCancelClickListener(null)
                        .show();


            }
        });

        //Firestore firebase
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);

        //Firebase user authentication
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser curentUser = mAuth.getCurrentUser();
        userID = curentUser.getUid();


        //recyclerview adapter and manager initialisation

        adapter = new Note_list_adapter(mNotesList, getApplicationContext(), this, this);
        recyclerView_notes_list.setAdapter(adapter);

        recyclerView_notes_list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //gett data from firebase
        retriveDataFromFireBase(userID);

        //add data button
        addNoteButton = findViewById(R.id.Notes_list_activity_BT_add_newNote);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //lunch add new note fragment
                note_editing_fragment newFragment = new note_editing_fragment(MainActivity.this, null);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.Note_list_activity_parent_view, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

    }


    //writing Note  to Firebase method
    @Override
    public void writeDataToFireBase(String userID, String title, String text) {
        mNotesList.clear();
        Map<String, String> user = new HashMap<>();
        user.put("title", title);
        user.put("text", text);
        user.put("id", userID);
        db.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                //refresh data and adapter
                retriveDataFromFireBase(userID);
                adapter.notifyDataSetChanged();

            }
        });


    }

    //read data from Firebase
    @Override
    public void retriveDataFromFireBase(String userID) {

        mNotesList.clear();
        db.collection("Users").whereEqualTo("id", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    task.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.d("TEST", "onSuccess: " + queryDocumentSnapshots.size());
                            for (QueryDocumentSnapshot noteDocument : queryDocumentSnapshots) {
                                // Retrieve the "title" and "text" fields for each Note object
                                String title = noteDocument.getString("title");
                                String text = noteDocument.getString("text");
                                String id = noteDocument.getString("id");
                                String documentId = noteDocument.getId();

                                // Create a new Note object and add it to the ArrayList
                                Note note = new Note(title, text, id, documentId);
                                mNotesList.add(note);
                                Log.d("TEST", title);
                            }

                            if (mNotesList.size() != 0) {
                                no_notes_layout.setVisibility(View.INVISIBLE);
                            } else {
                                no_notes_layout.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                            // Do something with the ArrayList of Note objects
                            // For example, update a RecyclerView or ListView adapter
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TEST", "Error getting notes", e);
                        }
                    });
                } else {
                    Log.d("TEST", "No such document" + userID);
                }
            }
        });


    }


    //Delete Note from Firebase
    @Override
    public void deleteDataFromFireBase(String title, String text, String id, String documentID) {

        IonAlert ionAlert = new IonAlert(MainActivity.this, IonAlert.WARNING_TYPE);
        ionAlert.setTitleText(getString(R.string.delete_note_dialog_title));
        ionAlert.setContentText(getString(R.string.delete_note_dialog_message) + title + " ?");
        ionAlert.setCancelText(getString(R.string.delete_note_dialog_negative_button));
        ionAlert.setConfirmText(getString(R.string.delete_note_dialog_positive_button));
        ionAlert.showCancelButton(true);
        ionAlert.setCancelClickListener(null);
        ionAlert.setConfirmClickListener(new IonAlert.ClickListener() {
            @Override
            public void onClick(IonAlert ionAlert) {
                db.collection("Users").document(documentID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ionAlert.setTitleText(getString(R.string.delete_note_dialog_title_deleted));
                        ionAlert.setContentText(getString(R.string.delete_note_dialog_message_deleted));
                        ionAlert.setConfirmText(getString(R.string.delete_note_dialog_deleted_button));
                        mNotesList.clear();
                        retriveDataFromFireBase(userID);
                        ionAlert.showCancelButton(false);
                        ionAlert.setCancelClickListener(null);
                        ionAlert.setConfirmClickListener(null);
                        ionAlert.changeAlertType(IonAlert.SUCCESS_TYPE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TEST", "onFailure: failed to delete document" + e.getMessage());
                        new IonAlert(MainActivity.this, IonAlert.ERROR_TYPE)
                                .setTitleText("Greska")
                                .setContentText("Desila se greska prilikom brisanja")
                                .show();
                    }
                });
            }
        });
        ionAlert.setCancelClickListener(null)
                .show();


    }


    //update data to firebase
    @Override
    public void updateDataToFireBase(String title, String text, String id, String documentId) {
        mNotesList.clear();
        Map<String, String> user = new HashMap<>();
        user.put("title", title);
        user.put("text", text);
        user.put("id", id);
        db.collection("Users").document(documentId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //refresh data and adapter
                retriveDataFromFireBase(userID);
                adapter.notifyDataSetChanged();
            }
        });

    }


    //lunching edit data from note adapter
    @Override
    public void runEditFragmentHelper(Note note) {
        //lunch add new note fragment
        note_editing_fragment newFragment = new note_editing_fragment(this, note);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Note_list_activity_parent_view, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}




