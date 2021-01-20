package com.example.smartattendance;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminHomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<DataModel> dataHolder;
    ImageButton newClass;
    Dialog dialog;
    EditText classname;
    Button create;
    FirebaseFirestore db;
    String AdminName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_admin_home, container, false);

        dialog = new Dialog(root.getContext(), R.style.Theme_AppCompat_Light_Dialog_MinWidth);
        recyclerView = root.findViewById(R.id.recyclerview_home);
        newClass = root.findViewById(R.id.add_Button);
        db = FirebaseFirestore.getInstance();
        this.AdminName= Login.sharedpreferences.getString("adminKey","");


        newClass.setOnClickListener(v -> {
            dialog.setContentView(R.layout.activity_add_class);
            create = dialog.findViewById(R.id.create_class);
            classname = dialog.findViewById(R.id.editTextCreateClass);

            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String clsname = classname.getText().toString();
                    DocumentReference df= db.collection(AdminName).document("Classes").collection("ClassInfo").document(clsname);
                    Map<String, Object> clsInfo = new HashMap<>();
                    clsInfo.put("ClassName", clsname);
//                    clsInfo.put("StudentsCount", 0);
//                    clsInfo.put("TeachersCount", 0);
//                    clsInfo.put("SubjectsCount", 0);
                    df.set(clsInfo);
                    dialog.dismiss();
                    Toast.makeText(getActivity(),"New Class Added Successfully",Toast.LENGTH_SHORT).show();
                    CollectionReference crf= db.collection(AdminName).document("Classes").collection("ClassInfo");
                    crf.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (dataHolder.size() > 0) {
                                dataHolder.clear();
                            }
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                dataHolder.add(new DataModel(R.drawable.class_image,
                                        querySnapshot.getString("ClassName")));

                            }
                            recyclerView.setAdapter(new RecyclerviewAdapter(dataHolder));
//                            "Total Students : " + querySnapshot.getLong("StudentsCount"),
//                                    "Total Teachers :" + querySnapshot.getLong("TeachersCount"),
//                                    "Total Subjects :" + querySnapshot.getLong("SubjectsCount")
                        }
                    });
                }
            });
            dialog.show();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataHolder = new ArrayList<>();

            db.collection(AdminName).document("Classes").collection("ClassInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (dataHolder.size() > 0) {
                        dataHolder.clear();
                    }
                    for (DocumentSnapshot querySnapshot : task.getResult()) {
                        dataHolder.add(new DataModel(R.drawable.class_image,
                                querySnapshot.getString("ClassName")
                               ));

                    }
                    recyclerView.setAdapter(new RecyclerviewAdapter(dataHolder));
                }

            });
//        "Total Students : " + querySnapshot.getLong("StudentsCount"),
//                "Total Teachers : " + querySnapshot.getLong("TeachersCount"),
//                "Total Subjects : " + querySnapshot.getLong("SubjectsCount")

//        DataModel obj=new DataModel(R.drawable.class_image,"FyCS","Total Students : 55","Total Teachers : 4","Total Subjects : 6");
//        dataHolder.add(obj);
        return root;
    }

}