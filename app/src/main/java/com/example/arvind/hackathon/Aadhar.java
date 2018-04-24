package com.example.arvind.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Aadhar extends AppCompatActivity implements View.OnClickListener {


    Button buttonScan,button;
    TextView textViewName;
    TextView bal, result;
    int i,lowBal, bal1;
    String str,str1;
    int seat,cal = 0;

    String fromText,toText,busNum,board,destination;

    double userLat,userLng;
    double distance;
    double venueLat ,venueLng;
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;


    int c, reduceBal, addbal;
    private IntentIntegrator qrScan;

    String lati1, longi1,lati2,longi2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhar);


        buttonScan = (Button) findViewById(R.id.buttonScan);
        button = (Button) findViewById(R.id.button13);

        textViewName = (TextView) findViewById(R.id.textView13);
        bal = (TextView) findViewById(R.id.textView15);
        result = (TextView) findViewById(R.id.textView16);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fromText = prefs.getString("from1Key", null);
        busNum = prefs.getString("busKey", null);
        toText = prefs.getString("to1Key", null);

        button.setEnabled(false);

        DatabaseReference databaseReference;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        try {


            databaseReference = FirebaseDatabase.getInstance().getReference().child("Bank");

            databaseReference.child("balance").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String val = dataSnapshot.getValue().toString();
                    addbal = Integer.parseInt(val);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile");

            databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    seat = Integer.parseInt(dataSnapshot.child("seatAvail").getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Aadhar.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }

        qrScan = new IntentIntegrator(this);

        buttonScan.setOnClickListener(this);
        button.setOnClickListener(this);

    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                button.setEnabled(false);
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());


                    //setting values to textviews


                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                    button.setEnabled(true);

                    str = "";
                    String s = result.getContents();
                    Integer h = s.indexOf("name");
                    Integer g = h + 6;
                    for (i = g; s.charAt(i) != '"'; i++)
                        str = str + s.charAt(i);
                    textViewName.setText("Name:"+str);
                 /*   str1 = "";
                    h = s.indexOf("uid");
                    g = h + 5;
                    for (i = g; s.charAt(i) != '"'; i++)
                        str1 = str1 + s.charAt(i); */
                    //Toast.makeText(this,str1,Toast.LENGTH_LONG).show();

                    try{

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String string = dataSnapshot.child("balance").getValue().toString();
                                String string1 = dataSnapshot.child("count").getValue().toString();
                                String string2 = dataSnapshot.child("lowBal").getValue().toString();
                                c = Integer.parseInt(string1);
                                lowBal = Integer.parseInt(string2);
                                bal1 = Integer.parseInt(string);
                                bal.setText("Balance:"+string);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    catch (Exception e1)
                    {
                        Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
        else {

            super.onActivityResult(requestCode, resultCode, data);

        }



    }
    @Override
    public void onClick(View view) {

        if(view == buttonScan)
        {

            result.setText("");
            qrScan.initiateScan();

        }
        if(view == button)
        {

            button.setEnabled(false);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if(c == 0)
            {

                if(cal == 0 )
                {

                    try{

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("locationBus");

                        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String lati11 = dataSnapshot.child("lat").getValue().toString();

                                String longi11 = dataSnapshot.child("lon").getValue().toString();

                                lati1 = lati11;

                                longi1 = longi11;

                                Toast.makeText(Aadhar.this, String.valueOf(lati1)+","+String.valueOf(longi1), Toast.LENGTH_LONG).show();

                                if(bal1 < 500)
                                {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1).child("lowBal");
                                    databaseReference.setValue("1");
                                }
                                else if(bal1 >= 500)
                                {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1).child("lowBal");
                                    databaseReference.setValue("0");
                                }

                                Toast.makeText(Aadhar.this, "lat:"+lati1+"   lon:"+longi1,Toast.LENGTH_LONG).show();

                                if(cal == 0)
                                {

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1).child("count");
                                    databaseReference.setValue("1");


                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1).child("lat");
                                    databaseReference.setValue(lati1);

                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1).child("lon");
                                    databaseReference.setValue(longi1);

                                    seat = seat-1;

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("seatAvail");
                                    databaseReference.setValue(String.valueOf(seat));

                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromText).child(toText).child("buses").child(busNum).child("seatAvail");
                                    databaseReference.setValue(String.valueOf(seat));


                                }

                                cal++;

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                    catch (Exception e)
                    {

                    }


                }

            }
            else if(c == 1)
            {

                if(cal == 0)
                {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1).child("count");
                    databaseReference.setValue("0");

                    if(cal == 0)
                    {

                        seat = seat+1;

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("seatAvail");
                        databaseReference.setValue(String.valueOf(seat));

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromText).child(toText).child("buses").child(busNum).child("seatAvail");
                        databaseReference.setValue(String.valueOf(seat));


                    }

                    try {

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1);

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                lati2 = dataSnapshot.child("lat").getValue().toString();
                                longi2 = dataSnapshot.child("lon").getValue().toString();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("locationBus");

                                databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String lati11 = dataSnapshot.child("lat").getValue().toString();

                                        String longi11 = dataSnapshot.child("lon").getValue().toString();

                                        lati1 = lati11;

                                        longi1 = longi11;

                                        Toast.makeText(Aadhar.this, "lat:" + lati1 + "   lon:" + longi1, Toast.LENGTH_LONG).show();

                                        userLat = Double.parseDouble(lati1);
                                        userLng = Double.parseDouble(longi1);
                                        venueLat = Double.parseDouble(lati2);
                                        venueLng = Double.parseDouble(longi2);

                                        double latDistance = Math.toRadians(userLat - venueLat);
                                        double lngDistance = Math.toRadians(userLng - venueLng);
                                        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                                                (Math.cos(Math.toRadians(userLat))) *
                                                        (Math.cos(Math.toRadians(venueLat))) *
                                                        (Math.sin(lngDistance / 2)) *
                                                        (Math.sin(lngDistance / 2));
                                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                                        distance = Math.round(AVERAGE_RADIUS_OF_EARTH * c);
                                        reduceBal = (int) distance;

                                        if(lowBal == 1)
                                        {
                                            result.setText("You Have To Pay Rs."+String.valueOf(distance)+"+ Fine Rs.50");
                                            result.setTextColor(Color.parseColor("#ff0000"));
                                        }
                                        else if(lowBal == 0)
                                        {

                                            //if(cal == 1)
                                            //{
                                            //finish();
                                            //startActivity(new Intent(Aadhar.this,NavigationBar2.class));
                                            //}

                                            if(cal == 0)
                                            {

                                                result.setText("You Have To Pay Rs."+String.valueOf(distance));
                                                result.setTextColor(Color.parseColor("#1315b4"));
                                                bal1 = bal1 - reduceBal;
                                                addbal = addbal + reduceBal;

                                                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Aadhar").child(str1).child("balance");
                                                databaseReference1.setValue(String.valueOf(bal1));

                                                databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Bank").child("balance");
                                                databaseReference1.setValue(String.valueOf(addbal));


                                                try
                                                {

                                                    List<Address> addressList = null ;

                                                    {
                                                        Geocoder geocoder = new Geocoder(Aadhar.this);
                                                        try {
                                                            addressList = geocoder.getFromLocation(Double.parseDouble(lati2), Double.parseDouble(longi2), 1);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        board = addressList.get(0).getLocality();


                                                    }

                                                    addressList = null ;

                                                    {
                                                        Geocoder geocoder = new Geocoder(Aadhar.this);
                                                        try {
                                                            addressList = geocoder.getFromLocation(Double.parseDouble(lati1), Double.parseDouble(longi1), 1);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        destination = addressList.get(0).getLocality();


                                                    }

                                                }
                                                catch (Exception e)
                                                {

                                                }


                                                databaseReference1 = FirebaseDatabase.getInstance().getReference().child("blog1").child(str1).child(busNum);

                                                databaseReference1.child("boarding").setValue("Boarding Point: "+board);
                                                databaseReference1.child("destination").setValue("Destination: "+destination);
                                                databaseReference1.child("bill").setValue("Bill Fare: "+String.valueOf(distance));
                                                databaseReference1.child("balance").setValue("Current Balance: "+String.valueOf(bal1));


                                            }

                                        }


                                        cal++;

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(Aadhar.this, "Check Your Internet Connection",
                                Toast.LENGTH_SHORT).show();
                    }


                }

            }
        }

    }
}
