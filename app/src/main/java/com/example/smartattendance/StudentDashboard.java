package com.example.smartattendance;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        BottomNavigationView bottomnav=findViewById(R.id.bottom_navigation_student);
        bottomnav.setOnNavigationItemSelectedListener(navlistner);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment =null;
            switch(item.getItemId()){
                case R.id.nav_profile:
                    selectedFragment=new StudentProfileFragment();
                    break;
                case R.id.nav_attendance:
                    selectedFragment=new StudentAttendanceFragment();
                    break;
                case R.id.nav_report:
                    selectedFragment=new StudentReportFragment();
                    break;
                case R.id.nav_noticeboard:
                    selectedFragment=new StudentNoticeBoardFragment();
                    break;
                case R.id.nav_smartId:
                    selectedFragment= new StudentSmartIdFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,selectedFragment).commit();
            return true;
        };
    };
}