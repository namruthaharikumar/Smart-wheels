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

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    EditText edit_text1, edit_text2;
    TextView text1, text2;
    Button button_1;
    int count;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edit_text1 = (EditText) findViewById(R.id.editText1);
        edit_text2 = (EditText) findViewById(R.id.editText2);
        text1 = (TextView) findViewById(R.id.textView);
        text2 = (TextView) findViewById(R.id.textView5);
        button_1 = (Button) findViewById(R.id.button2);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

       // databaseReference = FirebaseDatabase.getInstance().getReference();

     /*   if(firebaseAuth.getCurrentUser() != null){

            FirebaseUser user = firebaseAuth.getCurrentUser();

            databaseReference = FirebaseDatabase.getInstance().getReference();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.getValue().toString();
                    if(name.equals(""))
                    {
                        finish();
                        startActivity(new Intent(LogIn.this,Profile.class));
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
                        startActivity(new Intent(LogIn.this,NavigationBar.class));
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
                                    startActivity(new Intent(LogIn.this, GovtProfile.class));
                                }
                                else
                                {
                                    finish();
                                    startActivity(new Intent(LogIn.this, NavigationBar2.class));
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
        */

        button_1.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
    }
    private void UserLogin() {

        String email = edit_text1.getText().toString().trim();
        String password = edit_text2.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edit_text1.setError("This field is empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edit_text2.setError("This field is empty");
            return;
        }

        progressDialog.setMessage("Logging In..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.hide();

                        if (task.isSuccessful()) {

                            count = 1;
                            button_1.performClick();

                        }
                        else
                        {
                            count = 0;
                            Toast.makeText(LogIn.this, "LogIn Failed Or Please Check The Internet Connection",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        if(count == 1)
        {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // Actions to do after 10 seconds
                }
            }, 5000);

            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {

                boolean check = user.isEmailVerified();

                if (check == true) {

                    Toast.makeText(LogIn.this, "Logged In Successfully",
                            Toast.LENGTH_LONG).show();



                 /*   if(firebaseAuth.getCurrentUser() != null){

                        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String name = dataSnapshot.getValue().toString();
                                if(name.equals(""))
                                {
                                    finish();
                                    startActivity(new Intent(LogIn.this,Profile.class));
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
                                    startActivity(new Intent(LogIn.this,NavigationBar.class));
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
                                                startActivity(new Intent(LogIn.this, GovtProfile.class));
                                            }
                                            else
                                            {
                                                finish();
                                                startActivity(new Intent(LogIn.this, NavigationBar2.class));
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
*/

                }
                else
                {

                    databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid());
                    databaseReference.removeValue();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(LogIn.this, "Account Not Found or please verify the email",
                                                Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(LogIn.this,MainActivity.class));

                                    }
                                }
                            });

                }

            }

        }

    }

    @Override
    public void onClick(View v) {

        if(v == button_1)
        {
            UserLogin();
        }
        if(v == text1)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        if(v == text2)
        {
            String email = edit_text1.getText().toString();

            if (TextUtils.isEmpty(email)) {
                edit_text1.setError("Enter the Email Id Alone And 'Click Forgot Password'");
                return;
            }

            firebaseAuth = FirebaseAuth.getInstance();

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(LogIn.this, "Click The Link Sent To The Given Email Id",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

        }

    }
}
