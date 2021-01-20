package com.example.smartattendance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity implements View.OnClickListener {

    ToggleButton tg1;
    EditText mLoginPassword, mLoginEmailAddress, adminName;
    TextView mUserType, mForgetPassword;
    Button mLoginButton, mResetButton;
    ImageButton mAddNewAccount;
    SwitchCompat mSwitchAdmin;
    FirebaseAuth fAuth1;
    FirebaseFirestore fStore1;
    ProgressBar pb1;
    Dialog myDialog;
    boolean resSize=false;
    public static  String AdminName, LoginEmailAddress, AName;



    public static final String MyPREFERENCES = "MyPrefs";
    public static final String pEmail = "emailKey";
    public static final String pPass = "passKey";
    public static final String pUser = "userKey";
    public static final String pAdminName = "adminKey";

    public static SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDialog = new Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert);

        mLoginEmailAddress = findViewById(R.id.loginEmailAddress);
        mLoginPassword = findViewById(R.id.loginPassword);
        tg1 = findViewById(R.id.UserTypeImg);
        //cl1=(ConstraintLayout) findViewById(R.id.cl1);
        adminName=findViewById(R.id.admid);
        mUserType = findViewById(R.id.userType);
        mAddNewAccount = findViewById(R.id.addNewAccount);
        mLoginButton = findViewById(R.id.loginButton);
        mForgetPassword = findViewById(R.id.forgetPassword);
        mResetButton = findViewById(R.id.resetButton);
        mSwitchAdmin = findViewById(R.id.switch2);
        pb1 = findViewById(R.id.progressBar2);
        fAuth1 = FirebaseAuth.getInstance();
        fStore1 = FirebaseFirestore.getInstance();

        mLoginButton.setOnClickListener(this);

        mAddNewAccount.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this::ShowPopup);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

//        fStore1.collection("Admins").get().addOnSuccessListener(queryDocumentSnapshots -> {
//            if(queryDocumentSnapshots.isEmpty()){
//
//            }else{
//
//                for(DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()){
//                    String aeids=documentChange.getDocument().getData().get("EmailAddress").toString();
//                    adminsEmailList.add(aeids);
//                }
//            }
//        });


        //onToggleClick
        tg1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mUserType.setText(R.string.teacher);
                mLoginButton.setText(R.string.login_as_teacher);
                mSwitchAdmin.setVisibility(View.VISIBLE);
                adminName.setVisibility(View.GONE);
            } else {
                // The toggle is disabled
                mUserType.setText(R.string.student);
                mLoginButton.setText(R.string.login_as_student);
                mSwitchAdmin.setVisibility(View.GONE);
                adminName.setVisibility(View.VISIBLE);
            }
        });

        if (fAuth1.getCurrentUser() != null) {
            //handle already login user
            String utype = sharedpreferences.getString("userKey", "");
            if (utype.equals("Teacher")) {
                startActivity(new Intent(getApplicationContext(), TeacherDashboard.class));
//                finish();
            }
            if (utype.equals("Admin")) {
                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
//                finish();
            }
            if (utype.equals("Student")) {
                startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
//                finish();
            }

        }

    }

    //  Hide Status bar
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // <Insert code here to re-hide status bar>
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fAuth1.getCurrentUser() != null) {
            //handle already login user
            String utype = sharedpreferences.getString("userKey", "");
            if (utype.equals("Teacher")) {
                startActivity(new Intent(getApplicationContext(), TeacherDashboard.class));
 //               finish();
            }
            if (utype.equals("Admin")) {
                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
 //               finish();
            }
            if (utype.equals("Student")) {
                startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
 //               finish();
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            userLogin();
        }
            if (v.getId() == R.id.addNewAccount) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
            //       if(v.getId()==R.id.forgetPassword){
//             //ShowPopup();
//        }

        }




    public void ShowPopup(View v) {
        TextView textClose;
        EditText mResetEmailAddress;
        Button mResetPassword;
        myDialog.setContentView(R.layout.reset_password_prompt);
        textClose = myDialog.findViewById(R.id.textClose);
        mResetPassword = myDialog.findViewById(R.id.resetButton);
        mResetEmailAddress = myDialog.findViewById(R.id.resetEmailAddress);

        textClose.setOnClickListener(v1 -> myDialog.dismiss());
        mResetPassword.setOnClickListener(v12 -> {
            String Email = mResetEmailAddress.getText().toString().trim();
            if (Email.isEmpty()) {
                mResetEmailAddress.setError("Email Address is required");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                mResetEmailAddress.setError("Please provide valid email");
                return;
            }
            fAuth1.sendPasswordResetEmail(Email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(myDialog.getContext(), "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(myDialog.getContext(), "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        myDialog.show();
    }




    private void userLogin() {
            LoginEmailAddress = mLoginEmailAddress.getText().toString().trim();
            String LoginPassword = mLoginPassword.getText().toString().trim();
            String UserType = mUserType.getText().toString();
            AName = adminName.getText().toString();
            boolean SwitchAdmin = mSwitchAdmin.isChecked();

            if (LoginEmailAddress.isEmpty()) {
                mLoginEmailAddress.setError("Email Address Is Required");
                mLoginEmailAddress.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(LoginEmailAddress).matches()) {
                mLoginEmailAddress.setError("Please provide valid email");
                mLoginEmailAddress.requestFocus();
                return;
            }
            if (LoginPassword.isEmpty()) {
                mLoginPassword.setError("Password Is Required");
                mLoginPassword.requestFocus();
                return;
            }
            if (LoginPassword.length() < 6) {
                mLoginPassword.setError("Min password length should be 6 characters");
                return;
            }
            if(UserType.equals("Student")){
                if (AName.isEmpty()) {
                    adminName.setError("AdminName Is Required");
                    adminName.requestFocus();
                    return;
                }

            }
            if (UserType.equals("Student")) {
                FirebaseFirestore.getInstance().collection("AdminList").get().addOnCompleteListener(task -> {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            if (document.getId().equals(AName)) {
                                resSize = true;
                            }
                        }
                    }
                });

                if (!resSize) {
                    Toast.makeText(Login.this, "Admin Doesn/'t Exist ", Toast.LENGTH_SHORT).show();
//              adminName.setError("Admin Doesn/'t Exist");
                    adminName.requestFocus();
                    return;
                }
            }


            pb1.setVisibility(View.VISIBLE);


//            fStore1.collection("Students").addSnapshotListener((value, error) -> {
//            studentsEmailList.clear();
//            for(DocumentSnapshot snapshot : value){
//                studentsEmailList.add(snapshot.getString("EmailAddress"));
//            }
//        });
//
            fAuth1.signInWithEmailAndPassword(LoginEmailAddress, LoginPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    pb1.setVisibility(View.INVISIBLE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(pEmail, LoginEmailAddress);
                    editor.putString(pPass, LoginPassword);

                    if (UserType.equals("Teacher") && SwitchAdmin) {
                        fStore1.collection("AdminList").whereEqualTo("EmailAddress", LoginEmailAddress).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                        AdminName = document.getId().toString();
                                        editor.putString(pAdminName, AdminName);
                                    }

                                    Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                                    editor.putString(pUser, "Admin");
                                    editor.apply();
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Please select valid user type", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (UserType.equals("Teacher") && !SwitchAdmin) {
                        fStore1.collection("Teachers").whereEqualTo("EmailAddress", LoginEmailAddress).get().
                                addOnSuccessListener(queryDocumentSnapshots -> {
                                    if(!queryDocumentSnapshots.isEmpty()){
                                        Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), TeacherDashboard.class));
                                        editor.putString(pUser, "Teacher");
                                        editor.commit();
                                        finish();
                                    }else{
                                        Toast.makeText(Login.this, "Please select valid user type", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else if (UserType.equals("Student")) {
                        fStore1.collection(AName).document("Students").collection("StudentInfo").whereEqualTo("EmailAddress", LoginEmailAddress).get().
                                addOnSuccessListener(queryDocumentSnapshots -> {
                                    if(!queryDocumentSnapshots.isEmpty()){
                                        Toast.makeText(Login.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
                                        editor.putString(pUser, "Student");
                                        editor.putString(pAdminName, AName);
                                        editor.commit();
                                        finish();
                                    }else{
                                        Toast.makeText(Login.this, "Please select valid user type", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                } else {
                    pb1.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        }
