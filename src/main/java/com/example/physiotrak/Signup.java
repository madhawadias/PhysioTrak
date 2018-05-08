package com.example.physiotrak;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private EditText email, password, name;
    private Button register;
    private DatabaseReference myRef;
    private String child;
    private FirebaseAuth auth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //Authentication databse reference
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //Edit Text Views for email, password, name respectively
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pass);
        name = (EditText) findViewById(R.id.name);

        register = (Button) findViewById(R.id.registerbtn);

        //Register button function
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String child = name.getText().toString();
                String aemail = email.getText().toString();
                String apass = password.getText().toString();
                //database reference to send values from the app to the database
                myRef = database.getReference("Patient").child(child);


                if (TextUtils.isEmpty(aemail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(apass)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(Signup.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);


                myRef.child("Username").setValue(name.getText().toString());
                myRef.child("email").setValue(email.getText().toString());
                myRef.child("password").setValue(password.getText().toString());


                //Method to authenticate the users
                auth.createUserWithEmailAndPassword(aemail, apass)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Signup.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();


                                if (!task.isSuccessful()) {
                                    Toast.makeText(Signup.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Signup.this, "Successfuly Registered ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // progressBar.setVisibility(View.GONE);
    }
}

/******************* HCI ISSUES *******************/
// HCI issues addressed :
//========================
// 1) User needs to enter his/her personal details to the system
//    SOLUTION : Provides a simple UI to enter the user details
//
/******************* SECURITY ISSUES *******************/
// Security issues addressed :
//========================
// 1) Protecting user details
//    SOLUTION : Personal details are stored in the cloud, not locally which makes is secure.
//
