package com.example.arvind.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    EditText edit_text1, edit_text2, edit_text3;
    TextView textView;
    Button button_1,button_2;
    int count = 0;

    final List<String> areas = new ArrayList<String>();

    String category;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        textView = (TextView) findViewById(R.id.textView10);
        edit_text1 = (EditText) findViewById(R.id.editText3);
        edit_text2 = (EditText) findViewById(R.id.editText4);
        edit_text3 = (EditText) findViewById(R.id.editText5);
        button_1 = (Button) findViewById(R.id.button5);
        button_2 = (Button) findViewById(R.id.button6);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        try {

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue().toString();
                    edit_text1.setText(name);

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

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("category");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    category = dataSnapshot.getValue().toString();

                    if(category.equals("public"))
                    {

                        int maxLength = 12;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                        textView.setText(String.valueOf("Aadhar Number"));
                        edit_text3.setFilters(fArray);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("aadhar");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String phone = dataSnapshot.getValue().toString();
                                edit_text3.setText(phone);

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
                        /*int maxLength = 6;
                        InputFilter[] fArray = new InputFilter[1];
                        fArray[0] = new InputFilter.LengthFilter(maxLength);
                        textView.setText(String.valueOf("Government ID"));
                        edit_text3.setFilters(fArray);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("govtId");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String phone = dataSnapshot.getValue().toString();
                                edit_text3.setText(phone);

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("govtId");

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                                            String areaName = areaSnapshot.getValue().toString();
                                            areas.add(areaName);
                                            Toast.makeText(EditProfile.this, areaName,Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


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
            Toast.makeText(EditProfile.this, "Check Your Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }


        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);

    }

    private void UserProfile(){

        String name = edit_text1.getText().toString().trim();
        String phone = edit_text2.getText().toString().trim();
        final String id = edit_text3.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edit_text1.setError("This field is empty");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            edit_text2.setError("This field is empty");
            return;
        }
        if(phone.length() < 10)
        {
            edit_text2.setError("Invalid Number");
            return;
        }

        Boolean check;

        check = isPhoneValid(phone);

        if(check == false)
        {
            edit_text2.setError("Invalid Number");
            return;
        }

        if(category.equals("public"))
        {
            if (TextUtils.isEmpty(id)) {
                edit_text3.setError("This field is empty");
                return;
            }

            if(id.length() < 12)
            {
                edit_text3.setError("Invalid Entry");
                return;
            }

            Boolean check1;

            check1 = isAadharValid(id);

            if(check1 == false)
            {
                edit_text3.setError("Invalid Number");
                return;
            }

        }
        /*else if(category.equals("government"))
        {
            if(id.length() < 6)
            {
                edit_text3.setError("Invalid Entry");
                return;
            }

            Boolean check1;

            check1 = isIdValid(id);

            if(check1 == false)
            {
                edit_text3.setError("Invalid Number");
                return;
            }

        }*/




        try{

            if(category.equals("public"))
            {

                UserInformation2 userInformation2 = new UserInformation2(name,phone,category,id);

                FirebaseUser user = firebaseAuth.getCurrentUser();

                databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child(user.getUid()).setValue(userInformation2);

            }
            else if(category.equals("government"))
            {

                UserInformation userInformation = new UserInformation(name,phone,category);

                FirebaseUser user = firebaseAuth.getCurrentUser();

                databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference.child(user.getUid()).setValue(userInformation);

            }

        }
        catch (Exception e)
        {
            Toast.makeText(EditProfile.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        /*else if(category.equals("government"))
        {

            for (int i = 0;i < areas.size();i++)
            {
                String placeName = areas.get(i);

                if(placeName.equals(id))
                {
                    count = 1;
                    Toast.makeText(EditProfile.this, String.valueOf(count),Toast.LENGTH_SHORT).show();
                }

            }


        }

        Toast.makeText(EditProfile.this, String.valueOf(count),Toast.LENGTH_SHORT).show();

        if(count != 1)
        {

            Toast.makeText(EditProfile.this, "2",Toast.LENGTH_SHORT).show();
            edit_text3.setError("Invalid Entry");
            return;

        }
        else if(count == 1)
        {

            Toast.makeText(EditProfile.this, "Its 1",Toast.LENGTH_SHORT).show();
            //databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("govtId");
            //databaseReference.setValue(id);

        }*/

        Toast.makeText(EditProfile.this, "Saved Successfully",
                Toast.LENGTH_SHORT).show();

        if(category.equals("public"))
        {
            finish();
            startActivity(new Intent(this,NavigationBar.class));
        }
        else if(category.equals("government"))
        {
            finish();
            startActivity(new Intent(this,NavigationBar2.class));
        }

    }

    @Override
    public void onClick(View v) {

        if(v == button_1)
        {
            UserProfile();
        }
        else if(v == button_2)
        {
            if(category.equals("public"))
            {
                finish();
                startActivity(new Intent(this,NavigationBar.class));
            }
            else if(category.equals("government"))
            {
                finish();
                startActivity(new Intent(this,NavigationBar2.class));
            }
        }

    }

    public static boolean isPhoneValid(String phone) {
        boolean retval = false;
        String phoneNumberPattern = "^[789]\\d{9}$";
        retval = phone.matches(phoneNumberPattern);
        return retval;
    }

    public static boolean isAadharValid(String phone) {
        boolean retval = false;
        String phoneNumberPattern = "^[1234567890]\\d{11}$";
        retval = phone.matches(phoneNumberPattern);
        return retval;
    }

    /*public static boolean isIdValid(String phone) {
        boolean retval = false;
        String phoneNumberPattern = "^[1234567890]\\d{5}$";
        retval = phone.matches(phoneNumberPattern);
        return retval;
    }*/

}
