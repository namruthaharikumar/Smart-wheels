package com.example.arvind.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavigationBar2 extends AppCompatActivity {

    DrawerLayout drawerLayout;

    ActionBarDrawerToggle actionBarDrawerToggle;

    Toolbar toolbar;

    NavigationView navigation;

    private FirebaseAuth firebaseAuth;

    private RecyclerView nbloglist;
    private DatabaseReference mdatabase,databaseReference;

    LinearLayoutManager layoutManager;

    String busNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar2);
         try{
              FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

             databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("busNumber");
             databaseReference.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {
                     busNumber = dataSnapshot.getValue().toString();
                     //Toast.makeText(NavigationBar2.this,busNumber,Toast.LENGTH_LONG).show();

                     nbloglist = (RecyclerView) findViewById(R.id.blog_list);
                     nbloglist.setHasFixedSize(true);

                     layoutManager = new LinearLayoutManager(NavigationBar2.this);
                     //layoutManager.setStackFromEnd(true);
                     //layoutManager.setReverseLayout(true);

                     nbloglist.setLayoutManager(layoutManager);

                     mdatabase = FirebaseDatabase.getInstance().getReference().child("blog").child(busNumber);
                     mdatabase.keepSynced(true);

                     FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                             Blog.class,
                             R.layout.blog_row,
                             BlogViewHolder.class,
                             mdatabase

                     )


                     {
                         @Override
                         protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                             final String post_key = getRef(position).getKey();

                             viewHolder.setName(model.getName());
                             viewHolder.setBusStop(model.getBusStop());
                             viewHolder.setNOP(model.getNumberOfPassengers());

                             viewHolder.btnButton1.setOnClickListener(new View.OnClickListener(){
                                 @Override
                                 public void onClick(View v) {

                                     databaseReference = FirebaseDatabase.getInstance().getReference().child("blog").child(busNumber).child(post_key);

                                     databaseReference.removeValue();

                                 }
                             });

                         }

                     };

                     nbloglist.setAdapter(firebaseRecyclerAdapter);
                     nbloglist.setLayoutManager(layoutManager);


                 }

                 @Override
                 public void onCancelled(DatabaseError databaseError) {
                 }
             });

         }
         catch (Exception e)
         {

         }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        firebaseAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.nav_actionbar);
        setSupportActionBar(toolbar);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        navigation = (NavigationView) findViewById(R.id.nav_view2);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    switch (id) {

                        case R.id.nav_account:
                             startActivity(new Intent(NavigationBar2.this,ViewProfile.class));
                             break;
                        case R.id.nav_bus:
                             startActivity(new Intent(NavigationBar2.this,Government.class));
                             break;
                        case R.id.nav_aadhar:
                             startActivity(new Intent(NavigationBar2.this,Aadhar.class));
                             break;
                        case R.id.nav_edit:
                             startActivity(new Intent(NavigationBar2.this,EditBus.class));
                             break;
                        case R.id.nav_logout:
                             stopService(new Intent(getApplicationContext(), ServiceLocation.class));
                             firebaseAuth.signOut();
                             Toast.makeText(NavigationBar2.this, "Logged Out Successfully",
                                     Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(getApplicationContext(), LogIn.class);
                             //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             startActivity(intent);
                             break;
                    }
                    return false;
                }
        });

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        Button btnButton1;

        public BlogViewHolder(View itemView) {

            super(itemView);
            mView=itemView;

            this.btnButton1= (Button) mView.findViewById(R.id.button);

        }

        public void setName(String name)
        {
            TextView post_name = (TextView) mView.findViewById(R.id.textView1) ;
            post_name.setText(name);

        }


        public void setBusStop(String busStop)
        {
            TextView post_bus = (TextView) mView.findViewById(R.id.textView2);
            post_bus.setText(busStop);
        }


        public void setNOP(String numberOfPassengers)
        {
            TextView post_NOP = (TextView) mView.findViewById(R.id.textView3);
            post_NOP.setText(numberOfPassengers);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

