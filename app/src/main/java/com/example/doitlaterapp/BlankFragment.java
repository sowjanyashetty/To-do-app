package com.example.prarthana.doitlaterapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    private DatabaseReference mUserDatabase;
    private CircleImageView mImage;
    private TextView mdisplayName;
    private Button img_btn;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_blank, container, false);
        mdisplayName= (TextView) view.findViewById(R.id.sett_disp);
        mImage= (CircleImageView) view.findViewById(R.id.sett_image);
        img_btn= (Button) view.findViewById(R.id.sett_image_btn);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String name=dataSnapshot.child("name").getValue().toString();
                    String image=dataSnapshot.child("image").getValue().toString();
                    String thumb_image=dataSnapshot.child("thumb_image").getValue().toString();
                    mdisplayName.setText(name);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else {

        }

        return view;
    }

}
