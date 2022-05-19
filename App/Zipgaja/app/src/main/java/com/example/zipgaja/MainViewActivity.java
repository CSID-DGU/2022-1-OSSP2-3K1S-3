package com.example.zipgaja;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;

import java.io.IOException;
import java.util.List;


public class MainViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    boolean alarm = false;
    public NotificationHelper notificationHelper;

    private GoogleMap mGoogleMap = null;

    String[] permission_list = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        checkPermission();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), "MAPS_API_KEY");

        // 현재 위치의 위도/경도 불러오기
        ImageButton currentBtn = findViewById(R.id.currentBtn);
        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startLocationService
                startLocationService();
            }
        });

        // 현재 위치, 목적지 입력하기
        EditText inputCurrent = findViewById(R.id.inputCurrent);
        EditText inputDestination = findViewById(R.id.inputDestination);

        // 엔터 입력 시 키보드 내리기
        inputCurrent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputCurrent.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        inputDestination.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputDestination.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        final Geocoder geocoder = new Geocoder(this);
        // SearchListActivity 로 Activity 전환
        ImageButton routeSearchBtn = (ImageButton) findViewById(R.id.routeSearchBtn);
        routeSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchListActivity.class);
                // 다음 Activity 에 출발지 목적지 전달
                String currentAdd = inputCurrent.getText().toString();
                String destinationAdd = inputDestination.getText().toString();
                // 출발지 위도/경도 추출
                List<Address> currentList = null;
                try {
                    currentList = geocoder.getFromLocationName(currentAdd, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (currentList != null) {
                    if (currentList.size() == 0) {
                        System.out.println("올바른 경로가 아닙니다.");
                    }
                    else {
                        Address address = currentList.get(0);
                        double currentLat = address.getLatitude();
                        double currentLon = address.getLongitude();
                        System.out.print("currentLat: " + currentLat);
                        System.out.println("\t\tcurrentLon: " + currentLon);
                    }
                }
                // 목적지 위도/경도 추출
                List<Address> destinationList = null;
                try {
                    destinationList = geocoder.getFromLocationName(destinationAdd, 10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (destinationList != null) {
                    if (destinationList.size() == 0) {
                        System.out.println("올바른 경로가 아닙니다");
                    }
                    else {
                        Address address = destinationList.get(0);
                        double destinationLat = address.getLatitude();
                        double destinationLon = address.getLongitude();
                        System.out.print("destinationLat: " + destinationLat);
                        System.out.println("\tdestinationLon: " + destinationLon);
                    }
                }

                // 다음 Activity 에 text 전달
                intent.putExtra("currentLocation", currentAdd);
                intent.putExtra("destinationLocation", destinationAdd);
                startActivity(intent);
                finish();
            }
        });

        // Alarm Setting
        ImageButton alarmSetting = (ImageButton) findViewById(R.id.alarmBtn);
        ImageView alarmOff = findViewById(R.id.alarmOff);

        notificationHelper = new NotificationHelper(this);
        alarmSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alarm) {   // alarm off 일 때
                    alarmOff.setImageResource(R.drawable.alarm_1);
                    alarm = true;
                    String title = "title";
                    String text = "text";
                    sendOnChannel1(title, text);

                } else {
                    alarmOff.setImageResource(R.drawable.alarm_0);
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
                    Toast.makeText(getApplicationContext(), "앱 권한 설정 허용", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "앱 권한 설정 거부\n해당 앱 이용에 제한이 생길 수 있습니다.", Toast.LENGTH_LONG).show();
                    // finish();
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
        long minTime = 100000;
        float minDistance = 0;

        // minTime 마다 location update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);
        Toast.makeText(getApplicationContext(), "Location Service started.\nyou can test using DDMS.", Toast.LENGTH_LONG).show();
    }


    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            // location 이 null 일 경우
            if (location == null) {
                return;
            }

            // capture location data sent by current provider
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
            Log.i("GPSLocationService", msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

            onMapCurrent(mGoogleMap, latitude, longitude);
        }

        public void onProviderDisabled(String provider) { }
        public void onProviderEnabled(String provider) { }
        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 13));
    }

    public void onMapCurrent(@NonNull final GoogleMap googleMap, double latitude, double longitude) {
        mGoogleMap = googleMap;

        LatLng Current = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Current);
        markerOptions.title("현재 위치");
        markerOptions.snippet("Current Location");
        mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current, 13));
    }

    private void sendOnChannel1(String title, String text) {
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title, text);
        notificationHelper.getManager().notify(1, nb.build());
    }


}
