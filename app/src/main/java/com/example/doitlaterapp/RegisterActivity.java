package com.example.prarthana.doitlaterapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout disp;
    private TextInputLayout email;
    private TextInputLayout pass;
    private Button submitbtn;
    private FirebaseAuth mAuth;
    String TAG="";

    private Toolbar mtoolbar;
    private DatabaseReference mdatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mRegProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mRegProgressDialog=new ProgressDialog(this);

        mtoolbar=(Toolbar)findViewById(R.id.register_tool_bar) ;
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        disp = (TextInputLayout) findViewById(R.id.reg_disp_layout);
        email = (TextInputLayout) findViewById(R.id.reg_email_layout);
        pass = (TextInputLayout) findViewById(R.id.reg_pass_layout);
        submitbtn = (Button) findViewById(R.id.b1);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = disp.getEditText().getText().toString();
                String email_name = email.getEditText().getText().toString();
                String pass_name = pass.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email_name)||!TextUtils.isEmpty(pass_name))
                {
                    mRegProgressDialog.setTitle("RegisteringUser");
                    mRegProgressDialog.setMessage("Please wait few minutes");
                    mRegProgressDialog.setCanceledOnTouchOutside(false);
                    mRegProgressDialog.show();

                    register_user(display_name, email_name, pass_name);
                }
            }
        });

    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void register_user(final String display_name, String email_name, String pass_name) {
        mAuth.createUserWithEmailAndPassword(email_name, pass_name)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            mRegProgressDialog.hide();
                            Toast.makeText(RegisterActivity.this, "Sign in failed",
                                    Toast.LENGTH_SHORT).show();



                        }
                        else
                        {
                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();

                            String uid=current_user.getUid();
                            mdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String,String> userMap=new HashMap();
                            userMap.put("name",display_name);

                            userMap.put("image","default");
                            userMap.put("thumb_image","default");

                            mdatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mRegProgressDialog.dismiss();
                                        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                        /*mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
                                        String current_uid=mCurrentUser.getUid();
                                        mUserDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
                                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Toast.makeText(RegisterActivity.this,dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });*/
                                    }
                                }
                            });



                        }

                        // ...
                    }
                });
    }
}



