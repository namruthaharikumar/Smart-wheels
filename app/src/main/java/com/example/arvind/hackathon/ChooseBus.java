package com.example.arvind.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseBus extends AppCompatActivity implements View.OnClickListener {

    TextView textView1,textView2,textView3,textView4,textView5;

    Button button,button2,button3,button4;

    String fromCity,toCity,busNumber1,busNumber2,seat1,seat2,lat1,lat2,lon1,lon2,uid1,uid2,uname,busPlace,pcount;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bus);

        textView1 = (TextView) findViewById(R.id.textView8);
        textView2 = (TextView) findViewById(R.id.textView9);
        textView3 = (TextView) findViewById(R.id.textView11);
        textView4 = (TextView) findViewById(R.id.textView12);
        textView5 = (TextView) findViewById(R.id.textViewA);

        button = (Button) findViewById(R.id.button8);
        button2 = (Button) findViewById(R.id.button9);
        button3 = (Button) findViewById(R.id.button11);
        button4 = (Button) findViewById(R.id.button10);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fromCity = prefs.getString("city1Key", null);
        toCity = prefs.getString("city2Key", null);
        uname = prefs.getString("uname", null);
        busPlace = prefs.getString("busStop", null);
        pcount = prefs.getString("numberCount", null);

        busNumber1 = prefs.getString("bus1Key", null);
        busNumber2 = prefs.getString("bus2Key", null);
        seat1 = prefs.getString("seat1Key", null);
        seat2 = prefs.getString("seat2Key", null);
        uid1 = prefs.getString("uid1Key", null);
        uid2 = prefs.getString("uid2Key", null);
        //lat1 = prefs.getString("lat1Key", null);
        //lat2 = prefs.getString("lat2Key", null);
        //lon1 = prefs.getString("lon1Key", null);
        //lon2 = prefs.getString("lon2Key", null);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference().child("locationBus");

            databaseReference.child(uid1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int c5 = 1;

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {

                        if(c5 == 1)
                        {
                            lat1 = areaSnapshot.getValue().toString();
                        }
                        else  if(c5 == 2)
                        {
                            lon1 = areaSnapshot.getValue().toString();
                        }

                        c5 = 2;

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child("locationBus");

            databaseReference.child(uid2).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int c5 = 1;

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {

                        if(c5 == 1)
                        {
                            lat2 = areaSnapshot.getValue().toString();

                        }
                        else  if(c5 == 2)
                        {
                            lon2 = areaSnapshot.getValue().toString();
                        }

                        c5 = 2;

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(ChooseBus.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


        button.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);
        button4.setEnabled(true);

        if(!busNumber1.equals(""))
        {

            textView1.setText("Bus Number: "+busNumber1);
            textView2.setText("Tickets Availability: "+seat1);
            button.setEnabled(true);
            button3.setEnabled(true);

        }
        else
        {

            textView1.setText("Not Available");
            textView2.setText("Not Available");
            button.setEnabled(false);
            button3.setEnabled(false);
            textView3.setText("Not Available");
            textView4.setText("Not Available");
            button2.setEnabled(false);
            button4.setEnabled(false);

        }
        if(!busNumber2.equals(""))
        {

            textView3.setText("Bus Number: "+busNumber2);
            textView4.setText("Tickets Availability: "+seat2);
            button2.setEnabled(true);
            button4.setEnabled(true);

        }
        else
        {

            textView3.setText("Not Available");
            textView4.setText("Not Available");
            button2.setEnabled(false);
            button4.setEnabled(false);

        }

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        textView5.setText("Available Buses From "+fromCity+" to "+toCity);


    }

    @Override
    public void onClick(View v) {

        if(v == button)
        {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+lat1+","+lon1));
            startActivity(intent);

        }
        else if(v == button2)
        {

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr="+lat2+","+lon2));
            startActivity(intent);

        }
        else if(v == button3)
        {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("blog").child(busNumber1).child(user.getUid());

            databaseReference.child("name").setValue("Name: "+uname);
            databaseReference.child("busStop").setValue("Bus Stop: "+busPlace);
            databaseReference.child("numberOfPassengers").setValue("Number Of People: "+pcount);

            Toast.makeText(ChooseBus.this,"Requested Successfully",Toast.LENGTH_SHORT).show();

            finish();
            startActivity(new Intent(ChooseBus.this, FinalPublic.class));

        }
        else if(v == button4)
        {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("blog").child(busNumber2).child(user.getUid());

            databaseReference.child("name").setValue("Name: "+uname);
            databaseReference.child("busStop").setValue("Bus Stop: "+busPlace);
            databaseReference.child("numberOfPassengers").setValue("Number Of People: "+pcount);

            Toast.makeText(ChooseBus.this,"Requested Successfully",Toast.LENGTH_SHORT).show();

            finish();
            startActivity(new Intent(ChooseBus.this, FinalPublic.class));

        }

    }
}
