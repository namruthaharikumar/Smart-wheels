package com.example.arvind.hackathon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class GovtProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    Button button;

    String fromCity,toCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_profile);

        Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
        Spinner areaSpinner2 = (Spinner) findViewById(R.id.spinner2);
        Spinner areaSpinner3 = (Spinner) findViewById(R.id.spinner3);
        Spinner areaSpinner4 = (Spinner) findViewById(R.id.spinner4);
        button = (Button) findViewById(R.id.button4);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        try {

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
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(GovtProfile.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areasAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child("bussPass").child("number");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String areaName = areaSnapshot.getValue().toString();
                        areas.add(areaName);
                    }

                    Spinner areaSpinner = (Spinner) findViewById(R.id.spinner3);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(GovtProfile.this, android.R.layout.simple_spinner_item, areas);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areasAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child("bussPass").child("seat");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final List<String> areas = new ArrayList<String>();

                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String areaName = areaSnapshot.getKey();
                        areas.add(areaName);
                    }

                    Spinner areaSpinner = (Spinner) findViewById(R.id.spinner4);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(GovtProfile.this, android.R.layout.simple_spinner_item, areas);
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
            Toast.makeText(GovtProfile.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();

        }



        areaSpinner.setOnItemSelectedListener(this);
        areaSpinner2.setOnItemSelectedListener(this);

        button.setOnClickListener(this);

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
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(GovtProfile.this, android.R.layout.simple_spinner_item, areas);
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
            Toast.makeText(GovtProfile.this, "Check Your Internet Connection",
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
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void entry(){

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Spinner spinner_from = (Spinner) findViewById(R.id.spinner);
        String spinner_from_data = spinner_from.getSelectedItem().toString();

        Spinner spinner_to = (Spinner) findViewById(R.id.spinner2);
        String spinner_to_data = spinner_to.getSelectedItem().toString();

        Spinner busNumber = (Spinner) findViewById(R.id.spinner3);
        String busNumber_data = busNumber.getSelectedItem().toString();

        Spinner seatAvail = (Spinner) findViewById(R.id.spinner4);
        String seatAvail_data = seatAvail.getSelectedItem().toString();

        GovtInformation2 govtInformation2 = new GovtInformation2(spinner_from_data, spinner_to_data, busNumber_data, seatAvail_data);

        databaseReference.child("GovtProfile").child(user.getUid()).setValue(govtInformation2);

        Toast.makeText(GovtProfile.this, "Saved Successfully",
                Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

        if(v == button)
        {

            entry();
            finish();
            startActivity(new Intent(this, NavigationBar2.class));

        }

    }
}
