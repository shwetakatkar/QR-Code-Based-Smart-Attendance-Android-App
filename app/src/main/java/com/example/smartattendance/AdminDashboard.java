

package com.example.smartattendance;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboard extends AppCompatActivity {

    public static final int PERMISSION_CODE= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        checkPermission(Manifest.permission.CAMERA, PERMISSION_CODE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_CODE);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_CODE);

        BottomNavigationView bottomnavadmin=findViewById(R.id.bottom_navigation_admin);
        bottomnavadmin.setOnNavigationItemSelectedListener(adminnavlistner);
        getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer ,new AdminHomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener adminnavlistner= item -> {
        Fragment selectedFragment1 = new AdminHomeFragment();
        switch(item.getItemId()){
            case R.id.nav_profile_admin:
                selectedFragment1=new AdminProfileFragment();
                break;
            case R.id.nav_attendance_admin:
                selectedFragment1=new AdminAttendanceFragment();
                break;
            case R.id.nav_home_admin:
                selectedFragment1=new AdminHomeFragment();
                break;
            case R.id.nav_noticeboard_admin:
                selectedFragment1=new AdminNoticeboardFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.adminFragmentContainer ,selectedFragment1).commit();
        return true;
    };
//    public class CsvToJsonTest {
//        public static void main(String args[]) throws Exception {
//            File input = new File("input.csv");
//            try {
//                CsvSchema csv = CsvSchema.emptySchema().withHeader();
//                CsvMapper csvMapper = new CsvMapper();
//                MappingIterator<Map<?, ?>> mappingIterator =  csvMapper.reader().forType(Map.class).with(csv).readValues(input);
//                List<Map<?, ?>> list = mappingIterator.readAll();
//                System.out.println(list);
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }

//    var menu =[
//    {
//        "id":1,
//            "name":"Focaccia al rosmarino",
//            "description":"Wood fired rosemary and garlic focaccia",
//            "price":8.50,
//            "type":"Appetizers"
//    },
//    {
//        "id":2,
//            "name":"Burratta con speck",
//            "description":"Burratta cheese, imported smoked prok belly prosciutto, pached balsamic pear",
//            "price":13.50,
//            "type":"Appetizers"
//    }
// ]
//
//         menu.forEach(function(obj) {
//        db.collection("menu").add({
//                id: obj.id,
//                name: obj.name,
//                description: obj.description,
//                price: obj.price,
//                type: obj.type
//    }).then(function(docRef) {
//            console.log("Document written with ID: ", docRef.id);
//        })
//    .catch(function(error) {
//            console.error("Error adding document: ", error);
//        });
//    });

    public void checkPermission(String permission, int requestcode) {
        if (ContextCompat.checkSelfPermission(AdminDashboard.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AdminDashboard.this, new String[]{permission}, requestcode);
        }
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AdminDashboard.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminDashboard.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}