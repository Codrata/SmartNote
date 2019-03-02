package com.codrata.notefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookRepo extends AppCompatActivity implements NotesListAdapter.ItemClickListener {
    NotesListAdapter adapter;

    //the listview
    GridView listView;

    //database reference to get uploads data
    DatabaseReference mDatabaseReference;

    //list to store uploads data
    List<Upload> uploadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_repo);

        final ArrayList<String[]> bookNames = new ArrayList<>();

        uploadList = new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesListAdapter(this, R.layout.list_model, uploadList);
        adapter.setClickListener(this);



        //getting the database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Const.DATABASE_PATH_UPLOADS);

        //retrieving upload data from firebase database 
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list
                bookNames.add(uploads);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        Upload upload = uploadList.get(position);

        //Opening the upload file in browser using the upload url
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upload.getUrl()));
        startActivity(intent);

    }
}