package com.example.arvind.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Profile extends AppCompatActivity implements View.OnClickListener {

    EditText edit_text1, edit_text2,edit_text4;
    TextInputLayout textInputLayout;
    Button button_1;
    RadioGroup radioGroup;
    int count = 0;
    RadioButton radioButton,radioButton2;

    final List<String> areas = new ArrayList<String>();

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edit_text1 = (EditText) findViewById(R.id.editText1);
        edit_text2 = (EditText) findViewById(R.id.editText);
        edit_text4 = (EditText) findViewById(R.id.editText4);
        button_1 = (Button) findViewById(R.id.button2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        textInputLayout = (TextInputLayout)findViewById(R.id.secNumWrapper);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioButton = (RadioButton) findViewById(checkedId);

                if(radioButton.getText().toString().equals("public"))
                {
                    int maxLength = 12;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);

                    textInputLayout.setHint("Aadhar Number");
                    edit_text4.setFilters(fArray);
                }

                if(radioButton.getText().toString().equals("government"))
                {
                    int maxLength = 6;
                    InputFilter[] fArray = new InputFilter[1];
                    fArray[0] = new InputFilter.LengthFilter(maxLength);
                    edit_text4.setFilters(fArray);

                    textInputLayout.setHint("Government ID");
                    edit_text4.setVisibility(View.VISIBLE);
                }

            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() == null){

            finish();
            startActivity(new Intent(Profile.this,LogIn.class));

        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("govtId");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.getValue().toString();
                    areas.add(areaName);
                    Toast.makeText(Profile.this, areaName,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        button_1.setOnClickListener(this);

    }

    private void UserProfile(){

        String name = edit_text1.getText().toString().trim();
        String phone = edit_text2.getText().toString().trim();
        final String id = edit_text4.getText().toString().trim();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        String category = "";
        category = radioButton.getText().toString();

        if (TextUtils.isEmpty(name)) {
            edit_text1.setError("This field is empty");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            edit_text2.setError("This field is empty");
            return;
        }
        if (TextUtils.isEmpty(id)) {
            edit_text4.setError("This field is empty");
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
            if(id.length() < 12)
            {
                edit_text4.setError("Invalid Entry");
                return;
            }

            Boolean check1;

            check1 = isAadharValid(id);

            if(check1 == false)
            {
                edit_text4.setError("Invalid Number");
                return;
            }

        }
        else if(category.equals("government"))
        {
            if(id.length() < 6)
            {
                edit_text4.setError("Invalid Entry");
                return;
            }

            Boolean check1;

            check1 = isIdValid(id);

            if(check1 == false)
            {
                edit_text4.setError("Invalid Number");
                return;
            }

        }


        UserInformation userInformation = new UserInformation(name,phone,category);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(user.getUid()).setValue(userInformation);

        if(category.equals("public"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("aadhar");
            databaseReference.setValue(id);
        }
        else if(category.equals("government"))
        {

            for (int i = 0;i < areas.size();i++)
            {
                String placeName = areas.get(i);

                if(placeName.equals(id))
                {
                    Toast.makeText(Profile.this, "1",Toast.LENGTH_SHORT).show();
                    count = 1;
                }

            }


        }

        if(count != 1)
        {

            Toast.makeText(Profile.this, "2",Toast.LENGTH_SHORT).show();
            edit_text4.setError("Invalid Entry");
            return;

        }
        else if(count == 1)
        {

            Toast.makeText(Profile.this, "Its 1",Toast.LENGTH_SHORT).show();
            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("govtId");
            databaseReference.setValue(id);

        }

        Toast.makeText(Profile.this, "Saved Successfully",
                Toast.LENGTH_SHORT).show();

        if(category.equals("public"))
        {
            finish();
            startActivity(new Intent(this,NavigationBar.class));
        }
        else if(category.equals("government"))
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("city1");
            databaseReference.setValue("");
            databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("city2");
            databaseReference.setValue("");
            finish();
            startActivity(new Intent(this,GovtProfile.class));
        }

    }

    @Override
    public void onClick(View v) {

        if(v == button_1)
        {
            UserProfile();
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

    public static boolean isIdValid(String phone) {
        boolean retval = false;
        String phoneNumberPattern = "^[1234567890]\\d{5}$";
        retval = phone.matches(phoneNumberPattern);
        return retval;
    }

}
