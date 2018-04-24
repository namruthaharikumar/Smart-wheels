package com.example.arvind.hackathon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.List;

public class Public extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    TextView textView;
    Button button;

    int count = 0, count1 = 0;
    int check = 0;

    String fromCity;
    String toCity;
    String busStops;
    String peopleCount;
    String uname,stopName = "";

    public String busNumber1 = "", busNumber2 = "", seat1 = "", seat2 = "", uid1 = "", uid2 = "", lat1 = "", lat2 = "", lon1 = "", lon2 = "";
    public String busNumber11 = "", busNumber22 = "", seat11 = "", seat22 = "", uid11 = "", uid22 = "", lat11 = "", lat22 = "", lon11 = "", lon22 = "";

    List<String> areas1 = new ArrayList<String>();
    List<String> areas2 = new ArrayList<String>();

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Place = "placeKey";
    public static final String Stop = "stopKey";
    public static final String city1 = "city1Key";
    public static final String city2 = "city2Key";


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);

        textView = (TextView) findViewById(R.id.textView4);
        Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
        Spinner areaSpinner2 = (Spinner) findViewById(R.id.spinner2);
        Spinner areaSpinner3 = (Spinner) findViewById(R.id.spinner3);
        Spinner areaSpinner4 = (Spinner) findViewById(R.id.spinner4);
        button = (Button) findViewById(R.id.button4);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        try{

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue().toString();
                    uname = name;
                    textView.setText("Welcome "+name);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

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

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Public.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


        from();

        limit();

        areaSpinner.setOnItemSelectedListener(this);
        areaSpinner2.setOnItemSelectedListener(this);
        areaSpinner3.setOnItemSelectedListener(this);
        areaSpinner4.setOnItemSelectedListener(this);

        button.setOnClickListener(this);

    }

    private void from(){

        try{

            databaseReference = FirebaseDatabase.getInstance().getReference().child("City");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String areaName = areaSnapshot.getKey();
                        areas.add(areaName);
                    }

                    Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Public.this, android.R.layout.simple_spinner_item, areas);
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
            Toast.makeText(Public.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void to(){

        //count = 1;

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromCity);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String areaName = areaSnapshot.getKey();
                        areas.add(areaName);
                    }

                    Spinner areaSpinner2 = (Spinner) findViewById(R.id.spinner2);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Public.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner2.setAdapter(areasAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Public.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void busStop(String text){

        //count = 2;

        try{

            databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromCity).child(text);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String areaName = areaSnapshot.getKey();
                        if(!areaName.equals("buses"))
                            areas.add(areaName);
                        if(areaName.equals("buses"))
                            count1 = 1;
                    }


                    areas1 = areas;
                    Spinner areaSpinner3 = (Spinner) findViewById(R.id.spinner3);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Public.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner3.setAdapter(areasAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Public.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void limit(){

        //count = 3;

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference().child("publicLimit");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String areaName = areaSnapshot.getKey();
                        areas.add(areaName);
                    }

                    Spinner areaSpinner4 = (Spinner) findViewById(R.id.spinner4);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Public.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner4.setAdapter(areasAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Public.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        TextView textView1 = (TextView) view;
        String text = textView1.getText().toString();

        switch (parent.getId()) {
            case R.id.spinner:
                fromCity = text;
                to();
                break;
            case R.id.spinner2:
                toCity = text;
                busStop(text);
                break;
            case R.id.spinner3:
                busStops = text;
                break;
            case R.id.spinner4:
                peopleCount = text;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void entry(){

        for (int i = 0;i < areas2.size();i++)
        {
            String placeName = areas2.get(i);
            if(areas1.contains(placeName))
            {
                stopName = placeName;
            }
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();

        PublicInformation publicInformation = new PublicInformation(uname,fromCity,toCity,busStops,peopleCount);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("bookings").child(user.getUid()).setValue(publicInformation);

        String match = "";

        if(busStops.equals("Choose NearBy BusStop"))
        {
            //editText.setText(stopText);
            try {

                if(stopName.equals(""))
                {

                    check = 1;

                }
                else
                {

                    match = stopName;

                }

            }
            catch (Exception e)
            {

            }

        }
        else
        {

            match = busStops;

        }


        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(Place, busStops);
        editor.putString(Stop, stopName);
        editor.putString(city1, fromCity);
        editor.putString(city2, toCity);
        editor.putString("bus1Key", "");
        editor.putString("bus2Key", "");
        editor.putString("seat1Key", "");
        editor.putString("seat2Key", "");
        editor.putString("uid1Key", "");
        editor.putString("uid2Key", "");
        editor.putString("uname", uname);
        editor.putString("busStop", match);
        editor.putString("numberCount", peopleCount);
        //editor.putString("lat1Key", "");
        //editor.putString("lat2Key", "");
        //editor.putString("lon1Key", "");
        //editor.putString("lon2Key", "");
        editor.commit();

        try {

            if(count1 == 1)
            {

                databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromCity).child(toCity).child("buses");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int c2 = 1;

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {

                            if(c2 == 1)
                            {
                                busNumber1 = areaSnapshot.getKey().toString().trim();
                                busNumber11 = busNumber1;
                                editor.putString("bus1Key", busNumber11);
                                editor.commit();
                                //textView1.setText(busNumber1);

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromCity).child(toCity).child("buses");

                                databaseReference.child(busNumber1).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        int v = 1;
                                        SharedPreferences.Editor editor = sharedpreferences.edit();

                                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {

                                            if(v == 1)
                                            {
                                                seat1 = areaSnapshot.getValue().toString();
                                                seat11 = seat1;
                                                editor.putString("seat1Key", seat11);
                                                editor.commit();
                                                //textView2.setText(seat1);
                                                v = 2;
                                            }

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromCity).child(toCity).child("buses").child(busNumber1);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        int v = 1;

                                        SharedPreferences.Editor editor = sharedpreferences.edit();

                                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                                            if (v == 2) {
                                                uid1 = areaSnapshot.getValue().toString();
                                                uid11 = uid1;
                                                editor.putString("uid1Key", uid11);
                                                editor.commit();
                                            }

                                            v = 2;

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            if(c2 == 2)
                            {
                                busNumber2 = areaSnapshot.getKey().toString().trim();
                                busNumber22 = busNumber2;
                                editor.putString("bus2Key", busNumber22);
                                editor.commit();
                                //textView3.setText(busNumber2);


                                databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromCity).child(toCity).child("buses").child(busNumber2);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        int v = 1;
                                        SharedPreferences.Editor editor = sharedpreferences.edit();

                                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                                            if (v == 1) {
                                                seat2 = areaSnapshot.getValue().toString();
                                                seat22 = seat2;
                                                editor.putString("seat2Key", seat22);
                                                editor.commit();
                                                //textView4.setText(seat1);
                                            }

                                            v = 2;

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromCity).child(toCity).child("buses").child(busNumber2);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        int v = 1;
                                        SharedPreferences.Editor editor = sharedpreferences.edit();

                                        for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {

                                            if (v == 2) {
                                                uid2 = areaSnapshot.getValue().toString();
                                                uid22 = uid2;
                                                editor.putString("uid2Key", uid22);
                                                editor.commit();
                                                //textView2.setText(seat1);
                                            }

                                            v = 2;

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                c2 = 3;
                            }

                            if(c2 == 1)
                            {
                                c2 = 2;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            else
            {
                editor.putString("bus1Key", busNumber11);
                editor.putString("bus2Key", busNumber22);
                editor.putString("seat1Key", seat11);
                editor.putString("seat2Key", seat22);
                editor.putString("uid1Key", uid11);
                editor.putString("uid2Key", uid22);
                //editor.putString("lat1Key", lat11);
                //editor.putString("lat2Key", lat22);
                //editor.putString("lon1Key", lon11);
                //editor.putString("lon2Key", lon22);
                editor.commit();
            }


        }
        catch (Exception e)
        {
            Toast.makeText(Public.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onClick(View v) {

        if(v == button)
        {

            entry();

            if(check == 1)
            {

                try {
                    AlertDialog alertDialog = new AlertDialog.Builder(Public.this).create();

                    alertDialog.setTitle("Alert!!!");
                    alertDialog.setMessage("BusStops Not Found, Try with specific Bus Stop");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                            startActivity(new Intent(Public.this,Public.class));

                        }
                    });

                    alertDialog.show();
                }
                catch(Exception e)
                {
                    Toast.makeText(Public.this, "Error",Toast.LENGTH_SHORT).show();
                }


            }
            else
            {

                finish();
                startActivity(new Intent(this,ChooseBus.class));


            }

        }

    }

}
