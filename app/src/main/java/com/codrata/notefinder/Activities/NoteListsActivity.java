package com.codrata.notefinder.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.GridView;
import android.widget.SearchView;

import com.codrata.notefinder.Const;
import com.codrata.notefinder.NoteConst.NotesListAdapter;
import com.codrata.notefinder.R;
import com.codrata.notefinder.NoteConst.Upload;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoteListsActivity extends AppCompatActivity implements NotesListAdapter.ItemClickListener {
    NotesListAdapter adapter;

    //the listview
    GridView listView;

    //database reference to get uploads data
    DatabaseReference mDatabaseReference;

    //list to store uploads data
    List<Upload> uploadList;
    private BottomNavigationView buttomNavBarRepo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_repo);

        final ArrayList<String[]> bookNames = new ArrayList<>();

        uploadList = new ArrayList<>();

        final RecyclerView recyclerView = findViewById(R.id.repo_recycler_view);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new NotesListAdapter(this, R.layout.list_model, uploadList);
        adapter.setClickListener(this);
        buttomNavBarRepo = findViewById(R.id.navigationRepo);

        buttomNavBarRepo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_download_repo:

                        return true;
                    case R.id.nav_upload_repo:
                        Intent downloadAc = new Intent(getApplicationContext(), NoteUploadActivity.class);
                        startActivity(downloadAc);
                        return true;
                }
                return false;
            }
        });


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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.getSuggestionsAdapter();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    ArrayList<String[]> bookNames = new ArrayList<>();
                    RecyclerView recyclerView = findViewById(R.id.repo_recycler_view);
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
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.getSuggestionsAdapter();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        Upload upload = uploadList.get(position);
        Bundle extras = new Bundle();
        extras.putString("READER", String.valueOf(Uri.parse(upload.getUrl())));
        //Opening the upload file in browser using the upload url
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upload.getUrl()));
        intent.putExtras(extras);
        startActivity(intent);

    }
}