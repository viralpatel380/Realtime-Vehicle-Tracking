package com.viral.locationdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    String timer[]={"Select time","5 sec","10 sec","15 sec","20 sec","30 sec"};
    String tim;
    Button mLocationBtn;
    TextView mText;
    GPS_Service gps;

    //Firebase Work
    DatabaseReference mDatabaseLocationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mText = (TextView) findViewById(R.id.location_tv);
        Spinner mSpinTime= (Spinner) findViewById(R.id.spinner_time);
        mLocationBtn= (Button) findViewById(R.id.location_btn);
        mDatabaseLocationDetails = FirebaseDatabase.getInstance().getReference("test/Trucks/t01/currentLocation/").child("2");

//      permission check
        if(!runtime_permission())
            enable_button();
        runtime_permission();


        mSpinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                tim= adapterView.getItemAtPosition(i).toString();
                if(tim.equals("Select time")){
                    Toast.makeText(MainActivity.this, "Please Select time!", Toast.LENGTH_SHORT).show();
                }
                if(tim=="5 sec"){
                    tim= String.valueOf(tim.charAt(0));
                    Toast.makeText(MainActivity.this, tim+"", Toast.LENGTH_SHORT).show();
                }
                if(tim=="10 sec"){
                    tim= tim.substring(0,2);
                    Toast.makeText(MainActivity.this, tim+"", Toast.LENGTH_SHORT).show();
                }if(tim=="15 sec"){
                    tim= tim.substring(0,2);
                    Toast.makeText(MainActivity.this, tim+"", Toast.LENGTH_SHORT).show();
                }if(tim=="20 sec"){
                    tim= tim.substring(0,2);
                    Toast.makeText(MainActivity.this, tim+"", Toast.LENGTH_SHORT).show();
                }if(tim=="30 sec"){
                    tim= tim.substring(0,2);
                    Toast.makeText(MainActivity.this, tim+"", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tim= String.valueOf(0);
            }
        });

        ArrayAdapter arrayAdapterCity = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,timer);
        arrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinTime.setAdapter(arrayAdapterCity);
    }

    private void enable_button() {

        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gps = new GPS_Service(MainActivity.this,tim);
                startService(new Intent(MainActivity.this,GPS_Service.class));

                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    storeInDatabase(latitude,longitude);
                    mText.setText(latitude+" ::: "+longitude);
                    Toast.makeText(MainActivity.this, latitude+" ::: "+ longitude, Toast.LENGTH_SHORT).show();
                }else{
                    gps.showSettingsAlert();
                }
            }
        });



    }

    private void storeInDatabase(double latitude, double longitude) {


        mDatabaseLocationDetails.child("Longitude").setValue(longitude);
        mDatabaseLocationDetails.child("Latitude").setValue(latitude);
    }

    private boolean runtime_permission() {
        if(Build.VERSION.SDK_INT>=23 && ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},123);
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==123){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    enable_button();
            }else{
                runtime_permission();
            }
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
//        if(broadcastReceiver==null){
//            broadcastReceiver  = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    String loc = intent.getExtras().get("coordinates").toString();
//                    mText.setText(loc);
////                    mText.append("\n" + intent.getExtras().get("coordinates"));
//                }
//            };
//        }
//    }

//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
//        if(broadcastReceiver==null){
//            broadcastReceiver  = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    String loc = intent.getExtras().get("coordinates").toString();
//                    mText.setText(loc);
////                    mText.append("\n" + intent.getExtras().get("coordinates"));
//                }
//            };
//        }
//    }
}
