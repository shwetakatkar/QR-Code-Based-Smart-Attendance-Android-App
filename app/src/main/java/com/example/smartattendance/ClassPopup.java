package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ClassPopup extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    static String clsnm;
    ImageView stud, teach, sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_popup);

        textView=findViewById(R.id.display_class_nam);
        clsnm= getIntent().getStringExtra("ClassName");
        textView.setText(clsnm);
        stud=findViewById(R.id.studimg);
        stud.setOnClickListener(this);
        teach=findViewById(R.id.teachimg);
        teach.setOnClickListener(this);
        sub=findViewById(R.id.subimg);
        sub.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.studimg){
            Intent intent=new Intent(getApplicationContext(),StudentInfo.class);
            intent.putExtra("ClassName",clsnm);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (v.getId() == R.id.teachimg){
            Intent intent=new Intent(getApplicationContext(),TeacherInfo.class);
            intent.putExtra("ClassName",clsnm);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (v.getId() == R.id.subimg){
            Intent intent=new Intent(getApplicationContext(),SubjectInfo.class);
            intent.putExtra("ClassName",clsnm);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }
}