package com.example.arvind.hackathon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GovtDetails extends AppCompatActivity implements View.OnClickListener {

    CheckBox checkBox1,checkBox2,checkBox3;
    TextView textView,textView3,textView5;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    Button button;

    Spinner spinner;

    String fromText,toText,busNum;
    int seat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_details);

        checkBox1 = (CheckBox)findViewById(R.id.checkBox);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkBox3);
        textView = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView5 = (TextView) findViewById(R.id.textView5);
        Spinner areaSpinner = (Spinner) findViewById(R.id.spinner5);
        button = (Button) findViewById(R.id.button3);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        fromText = prefs.getString("from1Key", null);
        busNum = prefs.getString("busKey", null);
        seat = prefs.getInt("seatKey", -1);
        toText = prefs.getString("to1Key", null);
        textView.setText("Bus Number: "+busNum);
        textView3.setText("From City: "+fromText);
        textView5.setText("To City: "+toText);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        try {

                final List<String> areas = new ArrayList<String>();

                areas.add(String.valueOf(seat));

                Spinner areaSpinner2 = (Spinner) findViewById(R.id.spinner5);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(GovtDetails.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner2.setAdapter(areasAdapter);

        }
        catch (Exception e)
        {
            Toast.makeText(GovtDetails.this, "Check Your Internet Connection",
                    Toast.LENGTH_LONG).show();
        }

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == button)
        {
            update();
        }

    }

    private void update() {

        user = firebaseAuth.getCurrentUser();

        if(checkBox1.isChecked())
        {
            Spinner spinner_seat = (Spinner) findViewById(R.id.spinner5);
            String spinner_seat_data = spinner_seat.getSelectedItem().toString();

            try{

                databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromText).child(toText).child("buses").child(busNum);
                GovtInformation3 govtInformation3 = new GovtInformation3(user.getUid(), spinner_seat_data);
                databaseReference.setValue(govtInformation3);

            }
            catch (Exception e)
            {
                Toast.makeText(GovtDetails.this, "Check Your Internet Connection",
                        Toast.LENGTH_LONG).show();
            }



        }

        if(checkBox2.isChecked() || checkBox3.isChecked())
        {

            try{

                databaseReference = FirebaseDatabase.getInstance().getReference().child("City").child(fromText).child(toText).child("buses").child(busNum);

                databaseReference.removeValue();

            }
            catch (Exception e)
            {
                Toast.makeText(GovtDetails.this, "Check Your Internet Connection",
                        Toast.LENGTH_LONG).show();
            }



        }

    }
}
