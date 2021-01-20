package com.example.smartattendance;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewStudentFragment extends Fragment {

    ArrayList<StudentDataModel> dataHolder1;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    String clsName, AdminName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_view_student, container, false);

        dataHolder1 = new ArrayList<>();
        new ClassPopup();
        String clsName= ClassPopup.clsnm;
        this.AdminName = Login.sharedpreferences.getString("adminKey", "");
        db = FirebaseFirestore.getInstance();

        recyclerView=(RecyclerView)root.findViewById(R.id.displayStudRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        db.collection(AdminName).document("Classes").collection("ClassInfo").document(clsName).
                collection("StudentList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (dataHolder1.size() > 0) {
                    dataHolder1.clear();
                }
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot querySnapshot : queryDocumentSnapshots.getDocuments()) {
                        String nm= querySnapshot.getString("FullName");
                        String rl=querySnapshot.getString("Student ID");
//                        Toast.makeText(getActivity(), nm+" Name "+rl+"" ,Toast.LENGTH_SHORT).show();
                        dataHolder1.add(new StudentDataModel(nm+"",rl+""));
                    }
                }
                recyclerView.setAdapter(new StudentAdapter(dataHolder1));
            }
            });
//        Toast.makeText(getActivity(),String.valueOf(dataHolder1.size()),Toast.LENGTH_SHORT).show();


        return root;
    }
}