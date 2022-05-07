package com.example.zipgaja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    boolean alarm = false;

    String[] permission_list = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        checkPermission();

        ImageButton currentBtn = findViewById(R.id.currentBtn);
        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startLocationService
                startLocationService();
            }
        });


        ImageButton routeSearchBtn = (ImageButton) findViewById(R.id.routeSearchBtn);

        routeSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SearchListActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Alarm Setting
        ImageButton alarmSetting = (ImageButton) findViewById(R.id.alarmBtn);
        ImageView alarmOn = findViewById(R.id.alarmOn);

        alarmSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alarm) {   // alarm off 일 때
                    alarmOn.setImageResource(R.drawable.alarm_1);
                    alarm = true;
                } else {
                    alarmOn.setImageResource(R.drawable.alarm_0);
                    alarm = false;
                }
            }
        });

    }

    private void checkPermission() {
        // 현재 안드로이드 버전이 6.0 미만이면 메소드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for (String permission : permission_list) {
            // 권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if (chk == PackageManager.PERMISSION_DENIED) {
                // 권한 허용 여부를 확인하는 창을 띄운다.
                requestPermissions(permission_list, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0;i < grantResults.length; i++) {
                // 허용됐다면
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(), "앱 권한 설정 하세요", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationService() {
        // get manager instance
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // set listener
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);
        Toast.makeText(getApplicationContext(), "Location Service started.\nyou can test using DDMS.", Toast.LENGTH_LONG).show();
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            // capture location data sent by current provider
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            String msg = "\nLatitude : " + latitude + "\nLongitude : " + longitude;
            Log.i("GPSLocationService", msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String provider) { }
        public void onProviderEnabled(String provider) { }
        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

}


//    Location lastKnownLocation = null;
//    LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(Location location) {
//            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//
//            // Get the last location
//            lastKnownLocation = location;
//
//            // Stop the updates as soon as get the location
//            // locationManager.removeUpdates(locationListener);
//
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//        }
//        @Override
//        public void onProviderDisabled(String provider) { }
//        @Override
//        public void onProviderEnabled(String provider) { }
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) { }
//    };