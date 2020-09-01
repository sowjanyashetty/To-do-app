package com.example.prarthana.doitlaterapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.prarthana.doitlaterapp.R.id.b1;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    String TAG = "authentication";
    Bundle b1;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Toolbar mtoolbar;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.INTERNET}, 1);
        mAuth = FirebaseAuth.getInstance();

        mtoolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("TO DO LIST");

        mdrawerlayout=(DrawerLayout)findViewById(R.id.main_drawer);
        NavigationView mnavigation= (NavigationView) findViewById(R.id.main_navi);
        mnavigation.setNavigationItemSelectedListener(this);

        mtoggle=new ActionBarDrawerToggle(this,mdrawerlayout,mtoolbar,R.string.open,R.string.close);
        mdrawerlayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState==null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PersonalFragment()).commit();
        mnavigation.setCheckedItem(R.id.d_personal);}
    }

    @Override
    public void onBackPressed() {
        if (mdrawerlayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void onStart()
    {

        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null)
        {
            sendToStart();
        }


    }

    private void sendToStart()
    {
        Intent startIntent=new Intent(this,StartActivity.class);
        startActivity(startIntent);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.d_personal:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PersonalFragment()).commit();
                break;
            case R.id.d_others:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new OthersFragment()).commit();
                break;
            case R.id.d_accounts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new BlankFragment()).commit();
                break;
            case R.id.d_logout:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(mtoggle.onOptionsItemSelected(item))
        {
            return true;
        }
        if(item.getItemId()== R.id.main_logout_button)
        {
            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        if(item.getItemId()==R.id.main_setting_button)
        {
            Intent settingintent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingintent);
        }


        return true;
    }
}



