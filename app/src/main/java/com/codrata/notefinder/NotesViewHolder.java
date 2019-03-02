package com.codrata.notefinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView noteName;
    public LinearLayout linearLayout;

    public NotesViewHolder (View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        noteName = itemView.findViewById(R.id.noteName);
        linearLayout = itemView.findViewById(R.id.layoutList);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(view.getContext(), NoteDescription.class);
        Bundle b = new Bundle();
        b.putString("matchId", mLayout.getTag().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);

    }
}
