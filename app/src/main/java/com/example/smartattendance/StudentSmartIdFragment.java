package com.example.smartattendance;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StudentSmartIdFragment extends Fragment {
    FirebaseStorage storage;
    FirebaseFirestore fstore;
    String AdminName, EmailAddress, BirthDate,FullName, MobileNo;
    TextView name, id, bdate, oname,cname,address,mob;
    ImageView qrimage;
    FirebaseUser fUser;
    FirebaseAuth fAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_student_smartid, container, false);
        fstore=FirebaseFirestore.getInstance();
        fAuth= FirebaseAuth.getInstance();
        fUser= fAuth.getCurrentUser();
        qrimage=root.findViewById(R.id.QrCodeImage);
        name=root.findViewById(R.id.idName);
        bdate=root.findViewById(R.id.idBirthdate);
        cname=root.findViewById(R.id.idClass);
        mob=root.findViewById(R.id.idMobileNumber);
        oname=root.findViewById(R.id.idOrganisationName);
        id=root.findViewById(R.id.StudentId);
        address=root.findViewById(R.id.IdAddress);
        this.EmailAddress=Login.sharedpreferences.getString("emailKey","");
        this.AdminName=Login.sharedpreferences.getString("adminKey","");

        DocumentReference df=fstore.collection(AdminName).document("Students").collection("StudentInfo").document(fUser.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    BirthDate= documentSnapshot.getString("BirthDate");
                    MobileNo= documentSnapshot.getString("MobileNumber");
                    FullName= documentSnapshot.getString("FullName");
                    name.setText(FullName);
                    bdate.setText(BirthDate);
                    mob.setText(MobileNo);
                }
            }
        });
        address.setText(EmailAddress);


        //fetch qrcode img from firestorage
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageReference= storage.getReferenceFromUrl("gs://smartattendance-f0eaf.appspot.com").child(EmailAddress+ " QR Code");
        try {
            final File file= File.createTempFile("image","jpeg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                    qrimage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),"Failed to Load QR Code",Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.smartid_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.share_id:
                shareid();
                break;
            case R.id.download_id:
                downlaod();
                break;
            case R.id.print_id:
                    printId();
        }

        return super.onOptionsItemSelected(item);

    }

    private void downlaod() {
        View screenView = getView().getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, "SmartID");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareid() {
        View screenView = getView().getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bm = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        Toast.makeText(getActivity(),dirPath,Toast.LENGTH_SHORT);
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, "SmartID");
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void printId() {
//// Get a PrintManager instance
//        PrintManager printManager = (PrintManager) getActivity()
//                .getSystemService(Context.PRINT_SERVICE);
//
//        // Set job name, which will be displayed in the print queue
//        String jobName = getActivity().getString(R.string.app_name) + " Document";
//
//        // Start a print job, passing in a PrintDocumentAdapter implementation
//        // to handle the generation of a print document
//        printManager.print(jobName, new MyPrintDocumentAdapter(getActivity()),
//                null);

    }
}
