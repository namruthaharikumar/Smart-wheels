package com.example.arvind.hackathon;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Location1 extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    Button button;

    String location = "";

    List<String> areas2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location1);

        button = (Button) findViewById(R.id.button7);

        firebaseAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v == button)
        {

            FirebaseUser user = firebaseAuth.getCurrentUser();

            try{

                databaseReference = FirebaseDatabase.getInstance().getReference().child("busStops").child(user.getUid()).child("list");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final List<String> areas = new ArrayList<String>();

                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                            String areaName = areaSnapshot.getValue().toString();
                            areas.add(areaName);
                        }

                        areas2 = areas;

                        location = areas2.get(1);


                        if(location.equals(""))
                        {
                            Toast.makeText(Location1.this, "BusStops Not Found, Try Again Later",
                                    Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(Location1.this,NavigationBar.class));
                        }
                        else
                        {

                            List<Address> addressList = null ;

                            if(location != null || !location.equals("")) {
                                Geocoder geocoder = new Geocoder(Location1.this);
                                try {
                                    addressList = geocoder.getFromLocationName(location, 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                android.location.Address address = addressList.get(0);

                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?daddr="+String.valueOf(address.getLatitude())+","+String.valueOf(address.getLongitude())));
                                startActivity(intent);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
            catch (Exception e)
            {
                Toast.makeText(Location1.this, "BusStops Not Found, Try Again Later",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}
