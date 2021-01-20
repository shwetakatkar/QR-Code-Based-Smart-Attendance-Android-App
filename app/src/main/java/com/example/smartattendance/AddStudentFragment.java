package com.example.smartattendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AddStudentFragment extends Fragment {

    Button add;
    EditText nm, email, mob, sid;
    String AdminName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_add_student, container, false);
        add=root.findViewById(R.id.stud_add_button);
        nm=root.findViewById(R.id.studName);
        email=root.findViewById(R.id.studEmail);
        mob=root.findViewById(R.id.studMob);
        sid=root.findViewById(R.id.studID);
        String clsName= new ClassPopup().clsnm;
        this.AdminName=Login.sharedpreferences.getString("adminKey","");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> studInfo = new HashMap<>();
                studInfo.put("FullName", nm.getText().toString());
                studInfo.put("Student Email", email.getText().toString());
                studInfo.put("Student Mobile No", mob.getText().toString());
                studInfo.put("Student ID", sid.getText().toString());
                FirebaseFirestore.getInstance().collection(AdminName).document("Classes").collection("ClassInfo").
                        document(clsName).collection("StudentList").
                        document(nm.getText().toString()).set(studInfo);
                Toast.makeText(v.getContext(),"Student Record Added Successfully",Toast.LENGTH_SHORT).show();
                nm.setText("");
                email.setText("");
                mob.setText("");
                sid.setText("");
            }
        });

        return root;
    }
}