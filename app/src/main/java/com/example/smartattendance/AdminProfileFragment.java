package com.example.smartattendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProfileFragment extends Fragment implements View.OnClickListener {

    Button mLogout;
    TextView pName, pEmail, pMobileno, pAddress, pClass, pOrganisation, pStudentid, pBdate;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseFirestore fStore;
    SharedPreferences sharedpreferences;
    String AdminName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_admin_profile,container,false);

        mLogout = root.findViewById(R.id.alogoutButton);
        pName=root.findViewById(R.id.aName);
        pEmail=root.findViewById(R.id.aEmailAddress);
        pMobileno=root.findViewById(R.id.aMobileNumber);
        pClass=root.findViewById(R.id.pClass);
        pOrganisation=root.findViewById(R.id.aOrganizationName);
        pAddress=root.findViewById(R.id.aAddress);
        pStudentid=root.findViewById(R.id.StudentId);
        pBdate=root.findViewById(R.id.aBirthDate);
        mLogout.setOnClickListener(this);
        fAuth=FirebaseAuth.getInstance();
        fUser= fAuth.getCurrentUser();
        fStore= FirebaseFirestore.getInstance();
        this.AdminName=Login.sharedpreferences.getString("adminKey","");

        DocumentReference df=fStore.collection(AdminName).document(fUser.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String name= documentSnapshot.getString("FullName");
                    String email= documentSnapshot.getString("EmailAddress");
                    String mobileno= documentSnapshot.getString("MobileNumber");
                    String address= documentSnapshot.getString("Address");
                    String bdate= documentSnapshot.getString("BirthDate");
                    String orgname= documentSnapshot.getString("OrganisationName");

                    pName.setText(name);
                    pEmail.setText(email);
                    pMobileno.setText(mobileno);
                    pBdate.setText(bdate);
                    pOrganisation.setText(orgname);

                    pAddress.setText(address);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Please complete your profile",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private View.OnClickListener logout() {
        fAuth.signOut();
        Intent intent = new Intent(getActivity(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sharedpreferences =getActivity().getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        startActivity(intent);
        return null;
    }


    @Override
    public void onClick(View v) {
        logout();
    }
}
