package com.example.arvind.hackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_text1, edit_text2;
    TextView text1;
    Button button_1;
    int count = 0, count1 = 0, c = 1;
    String storeId = "";
    private Thread thread;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_text1 = (EditText) findViewById(R.id.editText1);
        edit_text2 = (EditText) findViewById(R.id.editText2);
        text1 = (TextView) findViewById(R.id.textView);
        button_1 = (Button) findViewById(R.id.button2);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() != null){

            FirebaseUser user = firebaseAuth.getCurrentUser();

            storeId = user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue().toString();
                    if(name.equals(""))
                    {
                        finish();
                        startActivity(new Intent(MainActivity.this,Profile.class));
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("category");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String category = dataSnapshot.getValue().toString();
                    if(category.equals("public"))
                    {
                        finish();
                        startActivity(new Intent(MainActivity.this,NavigationBar.class));
                    }
                    else if(category.equals("government"))
                    {

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("GovtProfile").child(user.getUid()).child("city1");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String fcity = dataSnapshot.getValue().toString();
                                if(fcity.equals(""))
                                {
                                    finish();
                                    startActivity(new Intent(MainActivity.this, GovtProfile.class));
                                }
                                else
                                {
                                    finish();
                                    startActivity(new Intent(MainActivity.this, NavigationBar2.class));
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        button_1.setOnClickListener(this);
        text1.setOnClickListener(this);
    }

    private void registerUser(){

        final String email = edit_text1.getText().toString().trim();
        String password = edit_text2.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edit_text1.setError("This field is empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edit_text2.setError("This field is empty");
            return;
        }
        if(password.length() < 6)
        {
            edit_text2.setError("Atleast 6 Characters Required");
            return;
        }

        if(count == 0)
        {
            count1 = 2;

            progressDialog.setMessage("Registering User..");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.hide();

                            if(task.isSuccessful())
                            {

                                count1 = 2;
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user != null) {

                                    String name = "";
                                    String phone = "";
                                    String category = "public";

                                    UserInformation userInformation = new UserInformation(name,phone,category);

                                    databaseReference = FirebaseDatabase.getInstance().getReference();

                                    databaseReference.child(user.getUid()).setValue(userInformation);

                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(MainActivity.this, "Click The Verification Link Sent To The Given Email Id And Then Click Verify",
                                                                Toast.LENGTH_LONG).show();

                                                        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                        count1 = 2;
                                                        button_1.setText("Verify");

                                                        firebaseAuth.signOut();

                                                    }
                                                    else
                                                    {

                                                        Toast.makeText(MainActivity.this, "Verification Failed",
                                                                Toast.LENGTH_SHORT).show();
                                                        count = 0;
                                                        button_1.setText("Register");

                                                    }
                                                }
                                            });


                                }


                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Already Registered Or Please Check You Internet Connection",
                                        Toast.LENGTH_LONG).show();
                                count1 = 0;
                                count = 0;
                            }
                        }
                    });


        }

        else if(count == 2)
        {
            progressDialog.setMessage("Under Verification..");
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.hide();

                            if (task.isSuccessful()) {

                                c = 1;
                                button_1.performClick();

                            }
                            else
                            {
                                c = 0;
                            }
                        }
                    });

            if(c == 1)
            {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // Actions to do after 10 seconds
                    }
                }, 5000);

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null)
                {

                    boolean check = user.isEmailVerified();

                    if(check == true)
                    {

                        Toast.makeText(MainActivity.this, "Registered Successfully",
                                Toast.LENGTH_SHORT).show();

                        String name = "";
                        String phone = "";
                        String category = "";

                        UserInformation userInformation = new UserInformation(name,phone,category);

                        databaseReference = FirebaseDatabase.getInstance().getReference();

                        databaseReference.child(user.getUid()).setValue(userInformation);

                        finish();
                        startActivity(new Intent(MainActivity.this,Profile.class));


                    }
                    else
                    {

                        button_1.setText("Register");
                        count = 0;
                        count1 = 0;


                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(MainActivity.this, "Registration Failed",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                    }

                }


            }
        }

        thread=  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        wait(5000);
                    }
                }
                catch(InterruptedException ex){
                }

                // TODO
            }
        };

        thread.start();

        if(count1 == 2)
        {
            count = 2;
        }


    }

    @Override
    public void onClick(View v) {

        if(v == button_1)
        {
            registerUser();
        }
        if(v == text1)
        {
            finish();
            startActivity(new Intent(this,LogIn.class));
        }

    }

}


