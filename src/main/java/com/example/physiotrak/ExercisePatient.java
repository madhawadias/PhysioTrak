package com.example.physiotrak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ExercisePatient extends AppCompatActivity {

    /** Displays a page for the patient to select a exercise from the exercises
     *  assigned to him/her by the doctor **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_patient);
    }

    public void exercise(View view) {

        Intent intent = new Intent(this, ExerciseView.class);

        // Exercise is selected
        switch(view.getId()){
            case R.id.exerciseOneBut :
                //Exercise 1 - Bicep Curl
                intent.putExtra("type", 1);
                break;
            case R.id.exerciseTwoBut :
                //Exercise 2
                intent.putExtra("type", 2);
                break;
            case R.id.exerciseThreeBut :
                //Exercise 3
                intent.putExtra("type", 3);
                break;
            case R.id.exerciseFourBut :
                //Exercise 4
                intent.putExtra("type", 4);
                break;
            default:
                break;

        }
        finish();
        startActivity(intent);
    }

}

/******************* HCI ISSUES *******************/
// HCI issues not addressed :
//===========================
// 1) Patient not being able to see a preview of the exercise before selecting
//
/******************* SECURITY ISSUES *******************/
// Security issues not addressed :
//========================
// 1) Exercise details of one patient can be edited by another doctor
//

