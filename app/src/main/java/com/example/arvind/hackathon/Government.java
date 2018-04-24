package com.example.arvind.hackathon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class Government extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView textView;

    Button button;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String start = "from1Key";
    public static final String Stop = "to1Key";

    SharedPreferences sharedpreferences;

    String fromCity,toCity,fromCity1,fromCity2,toCity1,toCity2;

    int seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government);

        textView = (TextView) findViewById(R.id.textView4);
        Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
        Spinner areaSpinner2 = (Spinner) findViewById(R.id.spinner2);
        Spinner areaSpinner3 = (Spinner) findViewById(R.id.spinner3);
        button = (Button) findViewById(R.id.button4);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        if(!runtime_permissions())
        {
            enableService();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue().toString();
                    textView.setText("WelCome "+name);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("busNumber");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String busNumber = dataSnapshot.getValue().toString();

                    final List<String> areas = new ArrayList<String>();
                    areas.add(busNumber);

                    Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Government.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areasAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("seatAvail");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    seat = Integer.parseInt(dataSnapshot.getValue().toString());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        catch (Exception e)
        {
            Toast.makeText(Government.this, "Check Your Internet Connection",
                    Toast.LENGTH_LONG).show();
        }

        areaSpinner.setOnItemSelectedListener(this);
        areaSpinner2.setOnItemSelectedListener(this);
        areaSpinner3.setOnItemSelectedListener(this);

        button.setOnClickListener(this);


    }

    private void enableService() {

        startService(new Intent(getApplicationContext(), ServiceLocation.class));

    }

    private Boolean runtime_permissions() {

        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100)
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

    private void from(){

        FirebaseUser user = firebaseAuth.getCurrentUser();

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("city1");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String from = dataSnapshot.getValue().toString();
                    fromCity1 = from;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("city2");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String from = dataSnapshot.getValue().toString();
                    fromCity2 = from;

                    final List<String> areas = new ArrayList<String>();
                    areas.add(fromCity1);
                    areas.add(fromCity2);

                    Spinner areaSpinner = (Spinner) findViewById(R.id.spinner2);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Government.this, android.R.layout.simple_spinner_item, areas);
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
            Toast.makeText(Government.this, "Check Your Internet Connection",
                    Toast.LENGTH_LONG).show();
        }



    }

    private void to(){

        //count = 1;

        FirebaseUser user = firebaseAuth.getCurrentUser();

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("city1");;

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    toCity1 = dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("city2");;

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    toCity2 = dataSnapshot.getValue().toString();
                    final List<String> areas = new ArrayList<String>();

                    if(toCity2 != fromCity )
                        areas.add(toCity2);
                    else if(toCity1 != fromCity )
                        areas.add(toCity1);

                    Spinner areaSpinner2 = (Spinner) findViewById(R.id.spinner3);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Government.this, android.R.layout.simple_spinner_item, areas);
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
            Toast.makeText(Government.this, "Check Your Internet Connection",
                    Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        TextView textView1 = (TextView) view;
        String text = textView1.getText().toString();

        switch (parent.getId()) {
            case R.id.spinner:
                from();
                break;
            case R.id.spinner2:
                fromCity = text;
                to();
                break;
            case R.id.spinner3:
                toCity = text;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void entry(){

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Spinner spinner_from = (Spinner) findViewById(R.id.spinner2);
        String spinner_from_data = spinner_from.getSelectedItem().toString();

        Spinner spinner_to = (Spinner) findViewById(R.id.spinner3);
        String spinner_to_data = spinner_to.getSelectedItem().toString();

        Spinner busNumber = (Spinner) findViewById(R.id.spinner);
        String busNumber_data = busNumber.getSelectedItem().toString();

        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(start, fromCity);
        editor.putString(Stop, toCity);
        editor.putString("busKey", busNumber_data);
        editor.putInt("seatKey", seat);
        editor.commit();


        GovtInformation govtInformation = new GovtInformation(spinner_from_data, spinner_to_data, busNumber_data);

        databaseReference.child("GovtData").child(user.getUid()).setValue(govtInformation);

    }

    @Override
    public void onClick(View v) {

        if(v == button)
        {

            entry();
            finish();
            startActivity(new Intent(this, GovtDetails.class));

        }

    }
}
