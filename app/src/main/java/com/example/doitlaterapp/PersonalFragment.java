package com.example.prarthana.doitlaterapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {

    private FloatingActionButton fab;
    RecycleAdapter adapter;
   private ArrayList<Todo>todoList;
    private ArrayList<String>keys=new ArrayList<String>();
    private DatabaseReference newref;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_personal, container, false);


        fab= (FloatingActionButton) view.findViewById(R.id.p_task_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new ToDoFragment());
                fr.commit();
                fr.addToBackStack(null);
            }
        });
        todoList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.p_view);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());//this.getActivity()
        recyclerView.setLayoutManager(llm);
        adapter = new RecycleAdapter();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();

        String uid=current_user.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Personal");
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                todoList.clear();
                Log.w("TodoApp", "getUser:onCancelled " + dataSnapshot.toString());

                Log.w("TodoApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey()+"key");
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Log.w("TodoApp", " values " +String.valueOf(data.getKey()) );
                    keys.add(data.getRef().getRef().getKey().toString());
                    Todo todo = data.getValue(Todo.class);
                    todoList.add(todo);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
            }
        });
    }
    class RecycleAdapter extends RecyclerView.Adapter
    {
        @Override
        public int getItemCount() {
            Log.w("TodoApp","Todolist Count"+ String.valueOf(todoList.size()));
            return todoList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
            SimpleItemViewHolder pvh=new SimpleItemViewHolder(v);
            return pvh;

        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
            viewHolder.position = position;
            Todo todo = todoList.get(position);
            Log.w("TodoApp","Todolist="+todo.getName());

            ((SimpleItemViewHolder) holder).title.setText(todo.getName());
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent newIntent=new Intent(getActivity(),ToDoActivity.class);
                    newIntent.putExtra("todo",todoList.get(position));
                    newIntent.putExtra("todoname","Personal");
                    newIntent.putExtra("key",keys.get(position));

                    startActivity(newIntent);*/

                }
            });
            viewHolder.title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());

                    // set title
                    alertDialogBuilder.setTitle("Delete Task ??");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Click yes to Delete Task")
                            .setCancelable(false)
                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {


                                    String newkey=keys.remove(position);
                                    FirebaseUser c_uid=FirebaseAuth.getInstance().getCurrentUser();
                                    String uid=c_uid.getUid();
                                    newref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Personal").child(newkey);

                                    newref.removeValue();
                                    Toast.makeText(getActivity(),"successfully deleted the task refresh the page to see changes",Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();










                    return true;
                }
            });


        }


        public  final class SimpleItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

           TextView title;
            public int position;
            public SimpleItemViewHolder(View itemView)
            {
                super(itemView);
                itemView.setOnClickListener(this);
                title=(TextView)itemView.findViewById(R.id.myTextView);
            }

            @Override
            public void onClick(View v) {
                Intent newIntent=new Intent(getActivity(),ToDoActivity.class);
                newIntent.putExtra("todo",todoList.get(position));
                newIntent.putExtra("todoname","Personal");
                newIntent.putExtra("key",keys.get(position));

                startActivity(newIntent);



            }

        }

    }
}
