package com.viral.locationdemo;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;


public class GPS_Service extends Service implements LocationListener{

    private Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled=false;
    boolean canGetLocation=false;

    Location location;//Location
    double latitude;//Latitude
    double longitude;//Longitude

    // The minimum time between updates in milliseconds
    static int time;
    private static final long MIN_TIME_BW_UPDATES = 1000 * time;

    // Declaring a Location Manager
    protected LocationManager mlocationManager;

    public GPS_Service(){
    }
    public GPS_Service(Context mContext, String time){
        this.mContext=mContext;
        this.time= Integer.parseInt(time);
        getLocation();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getLocation();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        getLocation();
        Log.d("Working","Service Started");
    }

    public Location getLocation(){
        try{
            mlocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //noinspection MissingPermission
                    mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, 0, this);
                    Log.d("Network", "Network");
                    if (mlocationManager != null) {
                        //noinspection MissingPermission
                        location = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        //noinspection MissingPermission
                        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 0, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mlocationManager != null) {
                            //noinspection MissingPermission
                            location = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return location;
    }
    public void stopUsingGPS(){
        if(mlocationManager != null){
            mlocationManager.removeUpdates(GPS_Service.this);
        }
    }
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }


        return latitude;
    }
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
