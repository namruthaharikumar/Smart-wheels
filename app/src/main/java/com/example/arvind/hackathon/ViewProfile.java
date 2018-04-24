package com.example.arvind.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfile extends AppCompatActivity implements View.OnClickListener {

    EditText edit_text1, edit_text2, edit_text3;
    Button button_1,button_2;
    TextView textView;
    String category;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        textView = (TextView) findViewById(R.id.textView10);
        edit_text1 = (EditText) findViewById(R.id.editText3);
        edit_text2 = (EditText) findViewById(R.id.editText4);
        edit_text3 = (EditText) findViewById(R.id.editText5);
        button_1 = (Button) findViewById(R.id.button5);
        button_2 = (Button) findViewById(R.id.button6);


        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        try{

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue().toString();
                    edit_text1.setText(name);
                    edit_text1.setEnabled(false);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("phone");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String phone = dataSnapshot.getValue().toString();
                    edit_text2.setText(phone);
                    edit_text2.setEnabled(false);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(ViewProfile.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


        try
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("category");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String category = dataSnapshot.getValue().toString();
                    if(category.equals("public"))
                    {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("aadhar");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String phone = dataSnapshot.getValue().toString();
                                edit_text3.setText(phone);
                                edit_text3.setEnabled(false);
                                textView.setText("Aadhar Number");

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else if(category.equals("government"))
                    {

                        textView.setText("");
                        edit_text3.setVisibility(View.INVISIBLE);
                        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("govtId");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String phone = dataSnapshot.getValue().toString();
                                edit_text3.setText(phone);
                                edit_text3.setEnabled(false);
                                textView.setText("Government Id");

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(ViewProfile.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == button_1)
        {
            startActivity(new Intent(ViewProfile.this,EditProfile.class));
        }
        else if(v == button_2)
        {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            try
            {
                databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("category");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String category = dataSnapshot.getValue().toString();
                        if(category.equals("public"))
                        {
                            finish();
                            startActivity(new Intent(ViewProfile.this,NavigationBar.class));
                        }
                        else if(category.equals("government"))
                        {
                            finish();
                            startActivity(new Intent(ViewProfile.this,NavigationBar2.class));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            catch (Exception e)
            {
                Toast.makeText(ViewProfile.this, "Check Your Internet Connection",
                        Toast.LENGTH_SHORT).show();
            }


        }
    }

}

