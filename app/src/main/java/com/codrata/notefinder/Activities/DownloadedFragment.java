package com.codrata.notefinder.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.GridView;
import android.widget.SearchView;

import com.codrata.notefinder.Const;
import com.codrata.notefinder.NoteConst.Download;
import com.codrata.notefinder.NoteConst.NotesListAdapter;
import com.codrata.notefinder.NoteConst.SavedListAdapter;
import com.codrata.notefinder.NoteConst.Upload;
import com.codrata.notefinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DownloadedFragment extends Fragment implements SavedListAdapter.ItemClickListener {

    SavedListAdapter adapter;

    //the listview
    GridView listView;
    //database reference to get uploads data
    DatabaseReference mDatabaseReference, mDownloadedRef;
    FirebaseAuth mAuth;
    //list to store uploads data
    List<Download> downloadList;
    View view;
    private long downloadID;
    private BottomNavigationView buttomNavBarRepo;


    public DownloadedFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.activity_downloaded_fragment, container, false);
        final ArrayList<String[]> bookNames = new ArrayList<>();

        downloadList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        final RecyclerView recyclerView = view.findViewById(R.id.repo_recycler_view_downloaded);
        int numberOfColumns = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfColumns);
        gridLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SavedListAdapter(getContext(), R.layout.list_model_downloaded, downloadList);
        adapter.setClickListener(this);
        String userId = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child(Const.USERS_PATH_DOWNLOADS);
        //retrieving upload data from firebase database
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Download download = postSnapshot.getValue(Download.class);
                    downloadList.add(download);
                }

                String[] downloads = new String[downloadList.size()];

                for (int i = downloads.length - 1; i >= 0; i--) {
                    downloads[i] = downloadList.get(i).getName();
                }

                //displaying it to list
                bookNames.add(downloads);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.getSuggestionsAdapter();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    ArrayList<String[]> bookNames = new ArrayList<>();
                    RecyclerView recyclerView = view.findViewById(R.id.repo_recycler_view);

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Download download = postSnapshot.getValue(Download.class);
                            downloadList.add(download);
                        }

                        String[] downloads = new String[downloadList.size()];

                        for (int i = 0; i < downloads.length; i++) {
                            downloads[i] = downloadList.get(i).getName();
                        }

                        //displaying it to list
                        bookNames.add(downloads);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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

    }

    @Override
    public void onItemClick(View view, int position) {

        Download download = downloadList.get(position);
        String noteName = download.getName();
        String urL = download.getUrl();

        Bundle extras = new Bundle();
        extras.putString("READER", urL);
        //Opening the upload file in browser using the upload url
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urL));
        intent.putExtras(extras);
        startActivity(intent);

    }
}