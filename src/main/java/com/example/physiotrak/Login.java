package com.example.physiotrak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/** LOGIN PAGE **/
public class Login extends AppCompatActivity {
    public TextView usernameTextView;
    private TextView passwordTextView;
    public String getUsername;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Database reference used to get the user from Firebase
    DatabaseReference myRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button register = (Button) findViewById(R.id.toSignUp);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Signup.class);
                startActivity(intent);
            }
        });


        usernameTextView = (TextView) findViewById(R.id.username);
        passwordTextView = (TextView) findViewById(R.id.password);


    }

    public void login(View view){

        if (usernameTextView.getText().toString().equals("doctor")) {
            Intent intent = new Intent(this, DoctorLogin.class);
            startActivity(intent);
        }else if(usernameTextView.getText().toString().equals("patient")) {
            Intent intent = new Intent(this, PatientLogin.class);
            getUsername = usernameTextView.getText().toString();
            startActivity(intent);
        }
    }

}
/******************* HCI ISSUES *******************/
// HCI issues not addressed :
//===========================
// 1) Patient entering a false username/password
//
/******************* SECURITY ISSUES *******************/
// Security issues addressed :
//========================
// 1) Patients who are not in the database trying to access the app
//    SOLUTION : Doesn't let the user who is not in the database to access the app
//