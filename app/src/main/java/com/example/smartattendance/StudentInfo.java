package com.example.smartattendance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentInfo extends AppCompatActivity {
    //public String className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

//        className =getIntent().getStringExtra("ClassName");
        BottomNavigationView bottomnav=findViewById(R.id.studBottomview);
        bottomnav.setOnNavigationItemSelectedListener(navlistner);
        getSupportFragmentManager().beginTransaction().replace(R.id.studFramelayout ,new ViewStudentFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navlistner= item -> {
        Fragment selectedFragment1 = new ViewStudentFragment();
        switch(item.getItemId()){
            case R.id.View:
                selectedFragment1=new ViewStudentFragment();
                break;
            case R.id.Add:
                selectedFragment1=new AddStudentFragment();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.studFramelayout,selectedFragment1).commit();
        return true;
    };
}