package com.example.physiotrak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DoctorLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
    }
    public void patients(View view){
        Intent intent = new Intent(this, DoctorPatientView.class);
        startActivity(intent);
    }
}
/******************* HCI ISSUES *******************/
//
// HCI issues not addressed :
//===========================
// 1) Option for the doctor to communicate with the patient and address his/her issues
//
/******************* SECURITY ISSUES *******************/
// Security issues addressed :
//========================
// 1) EExercise details and doctor details are stored locally
//

