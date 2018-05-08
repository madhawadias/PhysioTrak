package com.example.physiotrak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PatientLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
    }

    public void exercises(View view){
        Intent intent = new Intent(this, ExercisePatient.class);
        startActivity(intent);
    }
}

/******************* HCI ISSUES *******************/
// HCI issues addressed :
//========================
// 1) Patient selecting the exercises provided by the doctor
//    SOLUTION : provided a simple UI which displays the image of the patient and a UI to select the exercises
//
// HCI issues not addressed :
//===========================
// 1) Option for the patient to contact the doctor regarding issues he/she face
//
/******************* SECURITY ISSUES *******************/
// Security issues not addressed :
//========================
// 1) Exercise details and patient details are stored locally
//

