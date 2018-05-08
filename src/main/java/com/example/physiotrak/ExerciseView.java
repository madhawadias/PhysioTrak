package com.example.physiotrak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExerciseView extends AppCompatActivity {

    /** Shows real time status of the exercise **/
    //Text Views
    private TextView resultWindow;

    //Calibration data which is used to calculate the angle and the movement of the arm in activity_exercise_view.xml
    private long angleAngle;
    private float aXaX;
    private float aYaY;
    private float aZaZ;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Database reference used to send data to the doctor of the status of the exercise of the patient
    DatabaseReference patientResult = database.getReference("Result");
    //Realtime database references of the axises and angles
    DatabaseReference angle = database.getReference("Angle");
    DatabaseReference aX = database.getReference("Ax");
    DatabaseReference aY = database.getReference("Ay");
    DatabaseReference aZ = database.getReference("Az");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_view);
        resultWindow = (TextView) findViewById(R.id.resultWindow);

        //ValeEventListener to get the realtime angle data from the Flex sensor connected to the node MCU
        angle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long angleValue = dataSnapshot.getValue(long.class);
                angleAngle = angleValue;
                Log.d("VALUE", "ANGLE is: " + angleAngle);

                ImageView imv = (ImageView) findViewById(R.id.armImage); //Image of the arm in the exercise activity page
                imv.setRotation(imv.getRotation() - (angleValue-11)); //Rotation of the arm in realtime


                //Checking for the correct exercise. Comparing the values of the patient's exercise with the values given by the doctor
                if(angleAngle==18){
                    if(aXaX>0.6 && aXaX<0.7 && aYaY>0.0 && aYaY<0.1 && aZaZ>0.7 && aZaZ<0.8){
                        resultWindow.setText("CORRECT");
                        patientResult.setValue("Correct " + "X-axis" +aXaX +"\n"
                                                        + "Y-xis" + aYaY + "\n"
                                                        + "Z-axis" + aZaZ +"\n");
                    }else{
                        resultWindow.setText("Wrong");
                        patientResult.setValue("Wrong");
                    }

                }if(angleAngle==54){
                    if(aXaX>0.5 && aXaX<0.6 && aYaY>-0.5 && aYaY<-0.6 && aZaZ>0.5 && aZaZ<0.6){
                        resultWindow.setText("CORRECT");
                        patientResult.setValue("54• \n" +
                                "\n" +
                                "Ax : 0.5 to 0.6\n" +
                                "\n" +
                                "Ay : -0.5 to -0.6\n" +
                                "\n" +
                                "Az : 0.5 to 0.6");
                    }else{
                        resultWindow.setText("Wrong");
                    }

                }if(angleAngle==98){
                    if(aXaX>0.0 && aXaX<0.1 && aYaY>-0.9 && aYaY<-1.1 && aZaZ>-0.1 && aZaZ<-0.2){
                        resultWindow.setText("CORRECT");
                        patientResult.setValue("98• \n" +
                                "\n" +
                                "Ax : 0.0 to 0.1\n" +
                                "\n" +
                                "Ay : -0.9 to -1.1\n" +
                                "\n" +
                                "Az : -0.1 to -0.2");
                    }else{
                        resultWindow.setText("Wrong");
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("VALUE", "Failed to read value.", error.toException());
            }
        });

        //ValeEventListener to get the realtime X-axis data from the MPU6050 sensor connected to the node MCU
        aX.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Float aXvalue = dataSnapshot.getValue(float.class);
                aXaX = aXvalue;
                Log.d("VALUE", "Ax is: " + aXaX);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("VALUE", "Failed to read value.", error.toException());
            }
        });

        //ValeEventListener to get the realtime Y-axis data from the MPU6050 sensor connected to the node MCU
        aY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Float aYvalue = dataSnapshot.getValue(float.class);
                aYaY = aYvalue;
                Log.d("VALUE", "Ay is: " + aYaY);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("VALUE", "Failed to read value.", error.toException());
            }
        });

        //ValeEventListener to get the realtime Z-axis data from the MPU6050 sensor connected to the node MCU
        aZ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Float aZvalue = dataSnapshot.getValue(float.class);
                aZaZ = aZvalue;
                Log.d("VALUE", "Az is: " + aZaZ);

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
// HCI issues addressed :
//========================
// 1) Patient being unable to detect the correct movement of the arm
//    SOLUTION : The real time movement of the arm is shown in the app with an image
//
// 2) Patient being unable to sync with the correct movement of the arm
//    SOLUTION : App denotes whether the movement is correct or wrong
//
// 3) Doctor being unable to monitor the patient remotely
//    SOLUTION : Status of the exercise of the patient is sent to the doctor realtime via Firebase
//
// HCI issues not addressed :
//===========================
// 1) Patient not being able to monitor in realtime with an animation, the deviation of his/her exercise from the exercise assigned by the doctor
//
/******************* SECURITY ISSUES *******************/
// Security issues addressed :
//========================
// 1) Exercise details of one patient can be viewed by another patient
//    SOLUTION : Only the authenticated user can see his/her data
//

