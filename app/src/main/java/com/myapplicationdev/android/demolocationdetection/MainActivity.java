package com.myapplicationdev.android.demolocationdetection;

import android.Manifest;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient client;
    Button btnGetLastLocation, btnGetLocationUpdate, btnRemoveLocationUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetLastLocation = findViewById(R.id.btnGetLastLocation);
        btnGetLocationUpdate = findViewById(R.id.btnGetLocationUpdate);
        btnRemoveLocationUpdate = findViewById(R.id.btnRemoveLocationUpdate);

        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {


            btnGetLastLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkPermission() == true) {
                        Task<Location> task = client.getLastLocation();
                        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                if (location != null) {
                                    String msg = "Lat: " + location.getLatitude() +
                                            "Lng : " + location.getLongitude();
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    String msg = "No Last Knock Location found";
                                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "No Permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            final LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setSmallestDisplacement(100);

            final LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        Location data = locationResult.getLastLocation();
                        double lat = data.getLatitude();
                        double lng = data.getLongitude();
                        String msg = "Lat :" + lat +
                                " Lng : " + lng;
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }

                ;
            };

            btnGetLocationUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkPermission()) {
                        client.requestLocationUpdates(mLocationRequest,mLocationCallback,null);
                    }


                }
            });

            btnRemoveLocationUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkPermission()){
                    client.removeLocationUpdates(mLocationCallback);
                    Toast.makeText(MainActivity.this, "Location Update Removed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
