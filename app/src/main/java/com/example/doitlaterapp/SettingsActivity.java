package com.example.prarthana.doitlaterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private CircleImageView mImage;
    private TextView mdisplayName;
    private Button img_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mdisplayName=(TextView)findViewById(R.id.settings_disp) ;
        mImage=(CircleImageView)findViewById(R.id.settings_image);
        img_btn=(Button)findViewById(R.id.settings_image_btn) ;



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
            Intent LogIntent=new Intent(SettingsActivity.this,LoginActivity.class);
            startActivity(LogIntent);
        }
        // ...




    }
}


