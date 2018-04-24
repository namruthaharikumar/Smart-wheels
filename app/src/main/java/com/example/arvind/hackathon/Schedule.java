package com.example.arvind.hackathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    DatabaseReference databaseReference,mdatabase;
    FirebaseAuth firebaseAuth;
    private RecyclerView nbloglist;

    RecyclerView.LayoutManager layoutManager;

    String fromCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Spinner areaSpinner = (Spinner) findViewById(R.id.spinner7);

        from();

        areaSpinner.setOnItemSelectedListener(this);

    }

    private void from(){

        try{

            databaseReference = FirebaseDatabase.getInstance().getReference().child("City1");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String areaName = areaSnapshot.getKey();
                        areas.add(areaName);
                    }

                    Spinner areaSpinner = (Spinner) findViewById(R.id.spinner7);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Schedule.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areasAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Schedule.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void to()
    {

       try{

           nbloglist = (RecyclerView) findViewById(R.id.blog_list2);
           nbloglist.setHasFixedSize(true);

           layoutManager = new LinearLayoutManager(Schedule.this);
           //layoutManager.setStackFromEnd(true);
           //layoutManager.setReverseLayout(true);

           nbloglist.setLayoutManager(layoutManager);

           mdatabase = FirebaseDatabase.getInstance().getReference().child("City1").child(fromCity);
           mdatabase.keepSynced(true);

           FirebaseRecyclerAdapter<Blog2,Schedule.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog2, Schedule.BlogViewHolder>(
                   Blog2.class,
                   R.layout.blog_row2,
                   Schedule.BlogViewHolder.class,
                   mdatabase

           )


           {
               @Override
               protected void populateViewHolder(Schedule.BlogViewHolder viewHolder, Blog2 model, int position) {

                   //final String post_key = getRef(position).getKey();

                   viewHolder.setFrom(model.getFrom());
                   viewHolder.setTo(model.getTo());
                   viewHolder.setAtime(model.getAtime());
                   viewHolder.setDtime(model.getDtime());


               }

           };

           nbloglist.setAdapter(firebaseRecyclerAdapter);
           nbloglist.setLayoutManager(layoutManager);


       }
       catch (Exception e)
        {

        }

     }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        TextView textView1 = (TextView) view;
        String text = textView1.getText().toString();

        switch (parent.getId()) {
            case R.id.spinner7:
                fromCity = text;
                to();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public BlogViewHolder(View itemView) {

            super(itemView);
            mView=itemView;

        }

        public void setFrom(String from)
        {
            TextView post_from = (TextView) mView.findViewById(R.id.textView1) ;
            post_from.setText(from);

        }


        public void setTo(String to)
        {
            TextView post_to = (TextView) mView.findViewById(R.id.textView2);
            post_to.setText(to);
        }


        public void setAtime(String atime)
        {
            TextView post_Atime = (TextView) mView.findViewById(R.id.textView3);
            post_Atime.setText(atime);
        }

        public void setDtime(String Dtime)
        {
            TextView post_Dtime = (TextView) mView.findViewById(R.id.textView4);
            post_Dtime.setText(Dtime);
        }


    }

}
