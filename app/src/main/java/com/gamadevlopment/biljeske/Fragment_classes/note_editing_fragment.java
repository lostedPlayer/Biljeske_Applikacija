package com.gamadevlopment.biljeske.Fragment_classes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gamadevlopment.biljeske.Adapters.Note;
import com.gamadevlopment.biljeske.Adapters.firebase_editing;
import com.gamadevlopment.biljeske.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class note_editing_fragment extends Fragment {

    EditText ED_title, ED_text;
    TextView TX_title;
    ImageButton BT_back;
    Button BT_submit;

    String userID;
    Note note;

    private firebase_editing listener;

    public note_editing_fragment(firebase_editing listener, Note note) {
        this.listener = listener;
        this.note = note;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_editing_fragment_layout, container, false);

        //firebase stup
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser curentUser = mAuth.getCurrentUser();
        userID = curentUser.getUid();


        ED_title = view.findViewById(R.id.Note_edit_layout_ET_title);
        ED_text = view.findViewById(R.id.Note_edit_layout_ET_text);
        BT_submit = view.findViewById(R.id.Note_edit_layout_BT_submit);
        BT_back = view.findViewById(R.id.Note_edit_layout_BT_back);
        TX_title = view.findViewById(R.id.Note_edit_layout_title);

        //back Button click listener
        BT_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        if (note == null) {
            //creating new note
            TX_title.setText(getResources().getString(R.string.Add_Note_screen_title_adding_note));
            BT_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = ED_title.getText().toString();
                    String text = ED_text.getText().toString();
                    listener.writeDataToFireBase(userID, title, text);
                    getParentFragmentManager().popBackStack();
                }
            });
        } else {
            //Editing existing note
            ED_title.setText(note.getTitle());
            ED_text.setText(note.getText());

            BT_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //update data inside firebase
                    String title = ED_title.getText().toString();
                    String text = ED_text.getText().toString();
                    listener.updateDataToFireBase(title, text, note.getId(), note.getDocumentId());
                    getParentFragmentManager().popBackStack();
                }
            });
        }

        return view;
    }


}
