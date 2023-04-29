package com.gamadevlopment.biljeske.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.gamadevlopment.biljeske.Fragment_classes.note_editing_fragment;
import com.gamadevlopment.biljeske.MainActivity;
import com.gamadevlopment.biljeske.R;

import java.util.ArrayList;

public class Note_list_adapter extends RecyclerView.Adapter<Note_list_adapter.myViewHolder> {

    private ArrayList<Note> mData;
    private Context context;
    Activity activity = null;

    private firebase_editing listner;

    private firebase_editing listener;


    public Note_list_adapter(ArrayList<Note> mData, Context context, firebase_editing listner, Activity activity) {
        this.mData = mData;
        this.context = context;
        this.listner = listner;
        this.activity = activity;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_card_layout, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        Note note = mData.get(position);
        String title = note.getTitle();
        String text = note.getText();
        String id = note.getId();
        String documentID = note.getDocumentId();


        holder.title_tx.setText(title);
        holder.text_tx.setText(text);


        holder.delete_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listner.deleteDataFromFireBase(title, text, id, documentID);
            }
        });


        //lunch
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //edit data
                listner.runEditFragmentHelper(note);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView title_tx, text_tx;
        CardView cardView;

        RelativeLayout cardParentLayout;

        LinearLayout editMenuLayout;

        ImageButton delete_card_button;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            title_tx = itemView.findViewById(R.id.Note_card_layout_TX_title);
            text_tx = itemView.findViewById(R.id.Note_card_layout_TX_text);

            cardView = itemView.findViewById(R.id.Note_card_layout_cardView);

            cardParentLayout = itemView.findViewById(R.id.Note_card_parent_layout);

            delete_card_button = itemView.findViewById(R.id.Note_card_BT_delete);


        }


    }


}
