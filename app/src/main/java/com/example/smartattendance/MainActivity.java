package com.example.smartattendance;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN=5000;

    Animation topAnim, bottomAnim;
    ImageView imgAttendance, imgStudentattendance, imgTeacherattendance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        imgAttendance=findViewById(R.id.imageAttendance);
        imgStudentattendance=findViewById(R.id.imageStudentattend);
       // imgTeacherattendance=findViewById(R.id.imageTeacherattend);

        imgStudentattendance.setAnimation(topAnim);
       // imgTeacherattendance.setAnimation(bottomAnim);
        imgAttendance.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_SCREEN);

    }
}

