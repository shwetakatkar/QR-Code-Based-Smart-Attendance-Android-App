package com.example.smartattendance;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.common.collect.Table;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdminAttendanceFragment extends Fragment {
    
    CameraSource cameraSource;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    BarcodeDetector barcodeDetector;
    String QrText;
    Button save_attendance, next;
    ArrayList<String> arrayList;
    public static final int CAMERA_PERMISSION_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_admin_attendance, container, false);

        surfaceView = root.findViewById(R.id.camera);
        txtBarcodeValue = root.findViewById(R.id.qrtext);
        next=root.findViewById(R.id.next);
        save_attendance=root.findViewById(R.id.Save);
        save_attendance.setOnClickListener(this::onClick);
        arrayList = new ArrayList<>();

        return root;
    }

//    public void checkPermission(String permission, int requestcode) {
//        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestcode);
//        } else {
//            Toast.makeText(getActivity(),"Permission Always Granted", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//        @Override
//        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
//        @NonNull int[] grantResults){
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            if (requestCode == CAMERA_PERMISSION_CODE) {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }


    private void initialiseDetectorsAndSources() {

        Toast.makeText(getContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(35.0f)
                .setRequestedPreviewSize(960, 960)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new
                                String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
                            vibrator.vibrate(1);
                            QrText = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(QrText);
                            next.setOnClickListener(v -> arrayList.add(QrText));
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    public void onClick(View v) {
        Workbook wb = null;
        File file = new File(getActivity().getExternalFilesDir(null),String.valueOf("Smart Attendance")+".xls");
        if (file.exists()){
            try {
                FileInputStream inputstream = new FileInputStream(file);
                wb=new HSSFWorkbook(inputstream);
            } catch (Exception e) {
            }
        }
        else{
            wb=new HSSFWorkbook();
        }

        Table.Cell cell=null;
        CellStyle cellStyle=wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        //Date date=new Date();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date()).replace(':','-');
//        int num=wb.getNumberOfSheets();

        //Now we are creating sheet
        Sheet sheet=null;
        sheet = wb.createSheet(timeStamp);
        //Now column and row
        Row firstrow =sheet.createRow(0);

        Cell cell1 = firstrow.createCell(0);
        cell1.setCellValue("Name");
        cell1.setCellStyle(cellStyle);

        Cell cell2 = firstrow.createCell(1);
        cell2.setCellValue("Email ID");
        cell2.setCellStyle(cellStyle);

//        Set uniqueValues = new HashSet(arrayList);
//        List<String> list = new ArrayList<String>(uniqueValues);

        for (int i = 0; i < arrayList.size(); i++) {
            String val=arrayList.get(i);
            try {
                String[] arrOfStr = val.split("\\r?\\n");
                String Name = arrOfStr[0];
                String Email = arrOfStr[1];
                Row row= sheet.createRow(i);
                Cell cell3= row.createCell(0);
                cell3.setCellValue(Name);

                Cell cell4 =row.createCell(1);
                cell4.setCellValue(Email);
            }catch(Exception e){
            }

        }

//        sheet.setColumnWidth(0,200);
//        sheet.setColumnWidth(0,600);



        FileOutputStream outputStream =null;

        try {
            outputStream=new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(getContext(),"Saved",Toast.LENGTH_LONG).show();
            arrayList.clear();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Cancel",Toast.LENGTH_LONG).show();
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
