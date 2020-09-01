package com.example.prarthana.doitlaterapp;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class OTodoActivityFragment extends Fragment {
    private EditText messageEditText;
    private EditText nameEditText;
    private DatabaseReference mdatabase;
    private FirebaseAuth mAuth;
    String TAG="";

    public OTodoActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_otodo_activity, container, false);
        String name="Others";
        nameEditText = (EditText)view.findViewById(R.id.oto_name);
        messageEditText = (EditText)view.findViewById(R.id.oto_msg);
        final DatePicker datePicker = (DatePicker)view.findViewById(R.id.oto_date);
        mAuth = FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.oto_upload_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar= Calendar.getInstance();

                Date date = new Date();
                date.setMonth(datePicker.getMonth());
                date.setYear(datePicker.getYear());
                date.setDate(datePicker.getDayOfMonth());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");

                String dateString = format.format(date);
                Todo todo = new Todo();
                todo.setName(nameEditText.getText().toString());
                todo.setMessage(messageEditText.getText().toString());
                todo.setDate(dateString);
                FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();

                String uid=current_user.getUid();
                mdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Others");
                String key=mdatabase.push().getKey();
                Map<String,Object> userMap=new HashMap<>();
                userMap.put(key,todo.toFirebaseObject());
                mdatabase.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getActivity(),"Successfully Added The Task",Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

        return view;
    }

}
