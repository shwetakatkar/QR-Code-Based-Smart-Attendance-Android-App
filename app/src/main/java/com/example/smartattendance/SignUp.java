package com.example.smartattendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    Bitmap bitmap;
    EditText mFullName, mMobileNumber, mEmailAddress, mBirthdate, adminName, mRegisterPassword, mConfirmPassword;
    TextView mUserType;
    Button mRegisterButton;
    SwitchCompat mSwitchAdmin;
    ToggleButton tg1;
    ProgressBar pb1;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatePickerDialog dialog;
    int studSize, resSize=0;
    String studid;
    String st;
    FirebaseStorage storage;
    public static final int QRCodeWidth = 500;

    private DatePickerDialog.OnDateSetListener mDatesetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFullName = findViewById(R.id.fullName);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mEmailAddress = findViewById(R.id.emailAddress);
        mBirthdate = findViewById(R.id.birthdate);
        adminName = findViewById(R.id.adminName);
        mRegisterPassword = findViewById(R.id.registerPassword);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mUserType = findViewById(R.id.userType);
        mRegisterButton = findViewById(R.id.registerButton);
        mSwitchAdmin = findViewById(R.id.switch1);
        tg1 = findViewById(R.id.UserTypeImg);
        pb1 = findViewById(R.id.progressBar);

        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        mBirthdate.setOnClickListener(this);



        //onToggleClick
        tg1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mUserType.setText(R.string.teacher);
                mRegisterButton.setText("REGISTER AS TEACHER");
                mSwitchAdmin.setVisibility(View.VISIBLE);
                mSwitchAdmin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mRegisterButton.setText("REGISTER AS ADMIN");
                            adminName.setVisibility(View.GONE);
                        } else {
                            adminName.setVisibility(View.VISIBLE);
                            mRegisterButton.setText("REGISTER AS TEACHER");
                        }
                    }
                });
            } else {
                // The toggle is disabled
                mUserType.setText(R.string.student);
                mRegisterButton.setText("REGISTER AS STUDENT");
                mSwitchAdmin.setVisibility(View.GONE);
            }
        });
        mRegisterButton.setOnClickListener(this);


    }

    //Hide Status Bar
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // <Insert code here to re-hide status bar>
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerButton:
                registerUser();
                break;

            case R.id.birthdate:
                selectDate();
                break;

        }

    }

    private void selectDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dialog = new DatePickerDialog(SignUp.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDatesetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();

        mDatesetListener = (view, year1, month1, dayOfMonth) -> {
            month1 += 1;
            String bdate = month1 + "/" + day + "/" + year1;
            mBirthdate.setText(bdate);
        };
    }

    private void registerUser() {

        String FullName = mFullName.getText().toString().trim();
        String MobileNumber = mMobileNumber.getText().toString().trim();
        String EmailAddress = mEmailAddress.getText().toString().trim();
        String ConfirmPassword = mConfirmPassword.getText().toString().trim();
        String RegisterPassword = mRegisterPassword.getText().toString().trim();
        String BirthDate = mBirthdate.getText().toString().trim();
        String AdminName = adminName.getText().toString();
        boolean SwitchAdmin = mSwitchAdmin.isChecked();
        String UserType = mUserType.getText().toString();

        if (FullName.isEmpty()) {
            mFullName.setError("Full Name Is Required");
            mFullName.requestFocus();
            return;
        }
        if (MobileNumber.isEmpty()) {
            mMobileNumber.setError("Mobile Number Is Required");
            mMobileNumber.requestFocus();
            return;
        }
        if (MobileNumber.length() != 10) {
            mMobileNumber.setError("Please provide valid Mobile Number");
            mMobileNumber.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(MobileNumber).matches()) {
            mMobileNumber.setError("Please provide valid Mobile number");
            mMobileNumber.requestFocus();
            return;
        }
        if (EmailAddress.isEmpty()) {
            mEmailAddress.setError("Email Address Is Required");
            mEmailAddress.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(EmailAddress).matches()) {
            mEmailAddress.setError("Please provide valid email");
            mEmailAddress.requestFocus();
            return;
        }
        if (BirthDate.isEmpty()) {
            mBirthdate.setError("Birthdate Is Required");
            mBirthdate.requestFocus();
            return;
        }else {
            mBirthdate.setError(null);
        }


        if (RegisterPassword.isEmpty()) {
            mRegisterPassword.setError("Password Is Required");
            mRegisterPassword.requestFocus();
            return;
        }
        if (RegisterPassword.length() < 6) {
            mRegisterPassword.setError("Min password length should be 6 characters");
            return;
        }
        if (ConfirmPassword.isEmpty()) {
            mConfirmPassword.setError("Confirm your password");
            mConfirmPassword.requestFocus();
            return;
        }

        if (!ConfirmPassword.equals(RegisterPassword)) {
            mConfirmPassword.setError("Password doesn't matches");
            mConfirmPassword.requestFocus();
            return;
        }
        if (UserType.equals("Student")) {
            if (AdminName.isEmpty()) {
                adminName.setError("Admin Name Is Required");
                adminName.requestFocus();
                return;
            }
        }

        //later add for teacher also;

        if (UserType.equals("Student")) {
            fStore.collection("AdminList").get().addOnCompleteListener(task -> {
                for (DocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
                        if (document.getId().equals(AdminName)) {
                            resSize =100;
                        }
                    }

                }
                resSize=0;
            });

            if (resSize==0) {
                //Toast.makeText(SignUp.this, "Admin Doesn/'t Exist ", Toast.LENGTH_SHORT).show();
                adminName.setError("Admin Doesn/'t Exist");
                adminName.requestFocus();
                return;
            }else{
                adminName.setError(null);
            }
        }

        //           check student's entry by admin;
        if (UserType.equals("Student")) {
            fStore.collection(AdminName).document("Classes").collection("ClassInfo").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            //Toast.makeText(getApplicationContext(),document.getId()+"",Toast.LENGTH_SHORT).show();
                            fStore.collection(AdminName).document("Classes").collection("ClassInfo").
                                    document(document.getId()+"").collection("StudentList").whereEqualTo("FullName", FullName)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        studSize += task.getResult().size();
                                    }
                                }
                            });
                        }
                    }
                }
            });


            if (studSize == 0 ) {
//              Toast.makeText(SignUp.this, "Student Record Doesn/'t Exist. Contact Your Admin", Toast.LENGTH_SHORT).show();
                mFullName.setError("Student Record Doesn/'t Exist. Contact Your Admin");
                mFullName.requestFocus();
                return;
            }
            //else
//                {
//                mFullName.setError(null);
//                fStore.collection(AdminName).document("Classes").collection("ClassInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult()) {
//                                fStore.collection(AdminName).document("Classes").collection("ClassInfo").
//                                        document(document.getId()).collection("StudentList").whereEqualTo("FullName", FullName)
//                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                                                studid = documentSnapshot.getString("Student Id");
//                                            }
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
//            }
        }
                pb1.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(EmailAddress, RegisterPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = fAuth.getCurrentUser();


                        Toast.makeText(SignUp.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();


                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("FullName", FullName);
                        userInfo.put("EmailAddress", EmailAddress);
                        userInfo.put("MobileNumber", MobileNumber);
                        userInfo.put("BirthDate", BirthDate);
                        userInfo.put("Password", RegisterPassword);
                        userInfo.put("UserType", UserType);
                        userInfo.put("isAdmin", SwitchAdmin);
                        if (UserType.equals("Student")) {
                            String qrText = FullName + "\n" + EmailAddress;
                            try {
                                bitmap = textToImageEncode(qrText);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                StorageReference uploader = storage.getReference().child(EmailAddress + " QR Code");
                                UploadTask uploadTask = uploader.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(SignUp.this, "Smart ID Error! " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(SignUp.this, "Smart ID Created Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                // userInfo.put("QRCode",bitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        }

                        if (UserType.equals("Student")) {
                            DocumentReference studsdb = fStore.collection(AdminName).document("Students").collection("StudentInfo").document(user.getUid());
                            studsdb.set(userInfo);
                        }
                        if (UserType.equals("Teacher") && SwitchAdmin) {
                            DocumentReference adminsdb = fStore.collection(FullName).document(user.getUid());
                            fStore.collection("AdminList").document(FullName).set(userInfo);
                            adminsdb.set(userInfo);
                        }
                        if (UserType.equals("Teacher") && !SwitchAdmin) {
                            DocumentReference teachersdb = fStore.collection(AdminName).document("Teachers").collection("StudentInfo").document(user.getUid());
                            teachersdb.set(userInfo);
                        }

                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    } else {
                        pb1.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignUp.this, "Error !" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

            }


            private Bitmap textToImageEncode (String Value) throws WriterException {
                BitMatrix bitMatrix;
                try {
                    bitMatrix = new MultiFormatWriter().encode(Value,
                            BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeWidth, null);

                } catch (IllegalArgumentException e) {
                    return null;
                }
                int bitMatrixWidth = bitMatrix.getWidth();
                int bitMatrixHeight = bitMatrix.getHeight();
                int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

                for (int y = 0; y < bitMatrixHeight; y++) {
                    int offset = y * bitMatrixWidth;
                    for (int x = 0; x < bitMatrixWidth; x++) {
                        pixels[offset + x] = bitMatrix.get(x, y) ?
                                getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
                    }
                }
                Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
                bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
                return bitmap;
            }
        }

