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

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout mloginemail;
    private TextInputLayout mloginpass;
    private Button mlogin_btn;
    private Toolbar mtoolbar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mloginProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mloginProgressDialog=new ProgressDialog(this);
        mloginemail=(TextInputLayout)findViewById(R.id.log_email_layout) ;
        mloginpass=(TextInputLayout)findViewById(R.id.log_pass_layout);
        mlogin_btn=(Button)findViewById(R.id.log_login_btn);
        mtoolbar=(Toolbar)findViewById(R.id.login_tool_bar) ;
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mlogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mloginemail.getEditText().getText().toString();
                String password=mloginpass.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)|| !TextUtils.isEmpty(password))
                {    mloginProgressDialog.setTitle("Logging In");
                    mloginProgressDialog.setMessage("Wait for few minutes");
                    mloginProgressDialog.setCanceledOnTouchOutside(false);
                    mloginProgressDialog.show();
                    loginUser(email,password);
                }
            }
        });
    }
    private void loginUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            mloginProgressDialog.hide();
                            Log.w("", "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(),"Loggin in activity failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {  mloginProgressDialog.dismiss();
                            Intent logIntent=new Intent(LoginActivity.this,MainActivity.class);
                            logIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(logIntent);
                            finish();
                        }

                        // ...
                    }
                });

    }
}


