package com.example.arvind.hackathon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
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

public class NavigationBar extends AppCompatActivity {

    DrawerLayout drawerLayout;

    ActionBarDrawerToggle actionBarDrawerToggle;

    Toolbar toolbar;

    NavigationView navigation;

    private FirebaseAuth firebaseAuth;

    private RecyclerView nbloglist;
    private DatabaseReference mdatabase,databaseReference;

    LinearLayoutManager layoutManager;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);

        if(!runtime_permissions())
        {
            enableService();
        }

        try{

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("aadhar");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userId = dataSnapshot.getValue().toString();
                    //Toast.makeText(NavigationBar.this,userId,Toast.LENGTH_LONG).show();

                    nbloglist = (RecyclerView) findViewById(R.id.blog_list1);
                    nbloglist.setHasFixedSize(true);

                    layoutManager = new LinearLayoutManager(NavigationBar.this);
                    //layoutManager.setStackFromEnd(true);
                    //layoutManager.setReverseLayout(true);

                    nbloglist.setLayoutManager(layoutManager);

                    mdatabase = FirebaseDatabase.getInstance().getReference().child("blog1").child(userId);
                    mdatabase.keepSynced(true);

                    FirebaseRecyclerAdapter<Blog1,NavigationBar.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog1, NavigationBar.BlogViewHolder>(
                            Blog1.class,
                            R.layout.blog_row1,
                            NavigationBar.BlogViewHolder.class,
                            mdatabase

                    )


                    {
                        @Override
                        protected void populateViewHolder(NavigationBar.BlogViewHolder viewHolder, Blog1 model, int position) {

                            //final String post_key = getRef(position).getKey();

                            viewHolder.setBoarding(model.getBoarding());
                            viewHolder.setDestination(model.getDestination());
                            viewHolder.setBill(model.getBill());
                            viewHolder.setBalance(model.getBalance());

                            viewHolder.btnButton1.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {

                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("blog1").child(userId);

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

        navigation = (NavigationView) findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_account:
                         startActivity(new Intent(NavigationBar.this,ViewProfile.class));
                         break;
                    case R.id.nav_bus:
                         startActivity(new Intent(NavigationBar.this,Public.class));
                         break;
                    case R.id.nav_schedule:
                         startActivity(new Intent(NavigationBar.this,Schedule.class));
                         break;
                    case R.id.nav_loc:
                         startActivity(new Intent(NavigationBar.this,Location1.class));
                         break;
                    case R.id.nav_logout:
                         stopService(new Intent(getApplicationContext(), ServiceNearByLocation.class));
                         firebaseAuth.signOut();
                         Toast.makeText(NavigationBar.this, "Logged Out Successfully",
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

    private void enableService() {

        startService(new Intent(getApplicationContext(), ServiceNearByLocation.class));

    }

    private Boolean runtime_permissions() {

        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},101);
            return true;
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                enableService();
            }
            else
            {
                runtime_permissions();
            }
        }

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        Button btnButton1;

        public BlogViewHolder(View itemView) {

            super(itemView);
            mView=itemView;

            this.btnButton1= (Button) mView.findViewById(R.id.button);

        }

        public void setBoarding(String boarding)
        {
            TextView post_boarding = (TextView) mView.findViewById(R.id.textView1) ;
            post_boarding.setText(boarding);

        }


        public void setDestination(String destination)
        {
            TextView post_destination = (TextView) mView.findViewById(R.id.textView2);
            post_destination.setText(destination);
        }


        public void setBill(String bill)
        {
            TextView post_bill = (TextView) mView.findViewById(R.id.textView3);
            post_bill.setText(bill);
        }

        public void setBalance(String balance)
        {
            TextView post_balance = (TextView) mView.findViewById(R.id.textView4);
            post_balance.setText(balance);
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
