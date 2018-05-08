package com.example.physiotrak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorPatientView extends AppCompatActivity {

    private TextView result;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Database reference used to retrieve data of the patient exercise status
    DatabaseReference myRef = database.getReference("Result");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_patient_view);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                result = (TextView) findViewById(R.id.resultView);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                result.setText(value);
                Log.d("VALUE", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("VALUE", "Failed to read value.", error.toException());
            }
        });
    }
}
/******************* HCI ISSUES *******************/
//
// HCI issues addressed :
//===========================
// 1) Option for the doctor to view the status of the patient exercise
//    SOLUTION : Provided a user friendly GUI to display a detailed report of the patient exercise
//
/******************* SECURITY ISSUES *******************/
// Security issues addressed :
//========================
// 1) Exercise details and patient details are stored in the Firebase database
//
