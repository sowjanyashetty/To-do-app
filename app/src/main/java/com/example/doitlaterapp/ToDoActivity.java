package com.example.prarthana.doitlaterapp;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class ToDoActivity extends AppCompatActivity {
    Button fab;
    EditText nameEditText;
    EditText messageEditText;
    EditText dateEditText;
    DatePicker datePicker;
    String todonames;
    private String key;
    String Tdate;
    private DatabaseReference mdatabase;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        nameEditText = (EditText) findViewById(R.id.to_name);
        messageEditText = (EditText) findViewById(R.id.to_msg);
        dateEditText = (EditText) findViewById(R.id.dates);
        datePicker = (DatePicker) findViewById(R.id.to_date);
        mtoolbar=(Toolbar)findViewById(R.id.todo_page_toolbar) ;
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Content Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        fab = (Button) findViewById(R.id.fab);


        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Todo todo = (Todo) extras.get("todo");
            todonames = extras.getString("todoname");
            key = extras.getString("key");
            if (todo != null) {


                nameEditText.setText(todo.getName());
                messageEditText.setText(todo.getMessage());
                dateEditText.setText(todo.getDate());



                /*try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Date date = format.parse(todo.getDate());
                    Calendar calendar= Calendar.getInstance();
                    datePicker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH) );


                    datePicker.updateDate(date.getYea(), date.getMonth(), date.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Calendar calendar= Calendar.getInstance();

                Date date = new Date();
                date.setMonth(datePicker.getMonth());
                date.setYear(datePicker.getYear());
                date.setDate(datePicker.getDayOfMonth());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");

                String dateString = format.format(date);*/
                Todo todo = new Todo();
                todo.setName(nameEditText.getText().toString());
                todo.setMessage(messageEditText.getText().toString());
                todo.setDate(dateEditText.getText().toString());
                String date = todo.getDate().substring(0, 2);
                String month = todo.getDate().substring(3, 5);
                String year = todo.getDate().substring(6, 8);





                Tdate = dateEditText.getText().toString();
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

                String uid = current_user.getUid();
                mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(todonames);
                Map<String, Object> userMap = new HashMap<>();
                userMap.put(key, todo.toFirebaseObject());
                mdatabase.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getApplicationContext(), "Successfully updated The Task", Toast.LENGTH_LONG).show();
                            /*scheduleNotification(getNotification(nameEditText.getText().toString()),cal);*/
                        }
                    }
                });



                /*String key=mdatabase.push().getKey();
                Map<String,Object> userMap=new HashMap<>();
                userMap.put(key,todo.toFirebaseObject());
                mdatabase.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getApplicationContext(),"Successfully Added The Task",Toast.LENGTH_LONG).show();
                        }
                    }
                });*/


            }
        });

    }
}
    /*private void scheduleNotification(Notification notification,Calendar cal) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, key);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");





            long futureInMillis =cal.getTimeInMillis();
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);

    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Do it Later Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_settings_black_24dp);
        return builder.build();
    }
}
*/