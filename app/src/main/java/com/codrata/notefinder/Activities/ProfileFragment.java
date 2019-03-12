package com.codrata.notefinder.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codrata.notefinder.LoginActivity;
import com.codrata.notefinder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    View view;
    private Button btnLogout;
    private String userId, name, faculty, email, phone;
    private TextView mFaculty, mName, mEmail, mPhone;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;


    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_profile_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();

        btnLogout = view.findViewById(R.id.button_logout);
        mFaculty = view.findViewById(R.id.faculty_view);
        mName = view.findViewById(R.id.profile_name);
        mEmail = view.findViewById(R.id.email_view);
        mPhone = view.findViewById(R.id.phone_view);


        userId = mAuth.getCurrentUser().getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logOut = new Intent(getContext(), LoginActivity.class);
                logOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logOut);
            }
        });
        getUserInfo();
        return view;
    }

    public void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    if (dataSnapshot.child("name").getValue() != null)
                        name = dataSnapshot.child("name").getValue().toString();
                    if (dataSnapshot.child("Faculty").getValue() != null)
                        faculty = dataSnapshot.child("Faculty").getValue().toString();
                    if (dataSnapshot.child("email").getValue() != null)
                        email = dataSnapshot.child("email").getValue().toString();
                    if (dataSnapshot.child("phone").getValue() != null)
                        phone = dataSnapshot.child("phone").getValue().toString();

                    mFaculty.setText(faculty);
                    mName.setText(name);
                    mPhone.setText(phone);
                    mEmail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
