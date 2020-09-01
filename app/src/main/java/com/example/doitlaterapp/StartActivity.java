package com.example.prarthana.doitlaterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    Button reg;
    Button start_log_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        reg=(Button)findViewById(R.id.src_reg_btn);
        start_log_btn=(Button)findViewById(R.id.src_exist_btn);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regInt=new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(regInt);
                finish();
            }
        });
        start_log_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regInt=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(regInt);
                finish();
            }
        });
    }
}


