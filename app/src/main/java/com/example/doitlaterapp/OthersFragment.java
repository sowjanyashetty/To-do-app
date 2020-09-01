package com.example.prarthana.doitlaterapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import static com.example.prarthana.doitlaterapp.R.id.b1;


/**
 * A simple {@link Fragment} subclass.
 */
public class OthersFragment extends Fragment {
   private  FloatingActionButton fab;
    RecycleAdapter adapter;
    private ArrayList<Todo> todoList;
    private DatabaseReference newref;
   private ArrayList<String>keys=new ArrayList<String>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public OthersFragment() {
        // Required empty public constructor

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_others, container, false);



        fab= (FloatingActionButton) view.findViewById(R.id.o_tak_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new OTodoActivityFragment());
                fr.commit();
                fr.addToBackStack(null);
            }
        });
        todoList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.o_disp_view);
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

        DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Others");
        mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                todoList.clear();
                Log.w("TodoApp", "getUser:onCancelled " + dataSnapshot.toString());
                Log.w("TodoApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.w("TodoApp", " values " +String.valueOf(data.getValue()) );
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
            OSimpleItemViewHolder pvh=new OSimpleItemViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            OSimpleItemViewHolder viewHolder = (OSimpleItemViewHolder) holder;
            viewHolder.position = position;
            Todo todo = todoList.get(position);
            Log.w("TodoApp","Todolist="+todo.getName());

            ((OSimpleItemViewHolder) holder).title.setText(todo.getName());
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent newIntent=new Intent(getActivity(),ToDoActivity.class);
                    newIntent.putExtra("todo",todoList.get(position));
                    newIntent.putExtra("todoname","Others");
                    newIntent.putExtra("key",keys.get(position));

                    startActivity(newIntent);8*/
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
                                    newref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Others").child(newkey);

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



                    return false;
                }
            });



        }

        public final class OSimpleItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

            TextView title;
            public int position;
            public OSimpleItemViewHolder(View itemView)
            {
                super(itemView);
                itemView.setOnClickListener(this);
                title=(TextView)itemView.findViewById(R.id.myTextView);
            }

            @Override
            public void onClick(View v) {
                Intent newIntent=new Intent(getActivity(),ToDoActivity.class);
                newIntent.putExtra("todo",todoList.get(position));
                newIntent.putExtra("todoname","Others");
                newIntent.putExtra("key",keys.get(position));
                startActivity(newIntent);

            }
        }
    }

}
