package com.example.study_application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {
    public static final String EXTRA_NUMBER = "package com.example.study_application";
    private static final String TAG = "RecyclerViewAdapter";

    //arrays
    private final ArrayList<String> NAMES;
    private final ArrayList<String> IDS;
    private final Context CONTEXT;

    public RecyclerViewAdapter(Context context, ArrayList<String> Names, ArrayList<String> Ids) {
        //used get the information from the homeScreen so i don't have to
        // make the method of reading and writing a file inside the recyclerview
        this.NAMES = Names;
        this.IDS = Ids;
        this.CONTEXT = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // is used to set the amount of items allowed on the screen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contents, parent, false);
        return new viewHolder(view);
    }

    // this is what updates the recycler view
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.CAROUSEL_TEXT.setText(NAMES.get(position).replace("_", " "));
        holder.LAYOUT.setOnClickListener(v -> {
            Log.d(TAG, "onClick: On an a button: " + NAMES.get(position));
            Log.d(TAG, "onClick: On an a button: " + IDS.get(position));
            Intent toContentPopupScreen = new Intent(CONTEXT, ContentPopupScreen.class);
            toContentPopupScreen.putExtra(EXTRA_NUMBER, IDS.get(position));

            // this code is used for the transition between the cardView to another layout
            Pair layout = Pair.create(holder.LAYOUT, "shared_container");
            Pair textName = Pair.create(holder.CAROUSEL_BUTTON, "transition_button");
            Pair textBody = Pair.create(holder.CAROUSEL_TEXT, "transition_text");
            ActivityOptionsCompat transitionToNextScreen = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) CONTEXT, layout, textName, textBody);

            //goes to the next screen
            CONTEXT.startActivity(toContentPopupScreen, transitionToNextScreen.toBundle());
        });

        //checks if the button inside the layout is clicked instead
        holder.CAROUSEL_BUTTON.setOnClickListener(v -> {
            Intent toTaskScreen = new Intent(CONTEXT, TaskScreen.class);
            toTaskScreen.putExtra(EXTRA_NUMBER, IDS.get(position));
            //sends user straight to the task start screen
            CONTEXT.startActivity(toTaskScreen);
        });
    }

    //finds the amount of items for the recyclerview
    @Override
    public int getItemCount() {
        return NAMES.size();
    }

    //this code makes it so that i can call these names to
    // change there object attached in places like on viewBindHolder
    public static class viewHolder extends RecyclerView.ViewHolder {
        final TextView CAROUSEL_TEXT;
        final Button CAROUSEL_BUTTON;
        final CardView LAYOUT;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            CAROUSEL_TEXT = itemView.findViewById(R.id.carouselText);
            CAROUSEL_BUTTON = itemView.findViewById(R.id.carouselButton);
            CAROUSEL_BUTTON.bringToFront();
            LAYOUT = itemView.findViewById(R.id.layout);
        }
    }
}
