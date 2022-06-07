package com.example.zipgaja;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainViewActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean alarm;
    private AlarmManager alarmManager;
    private GregorianCalendar mCalendar;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private GoogleMap mGoogleMap = null;

    String[] permission_list = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    Handler handler;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        checkPermission();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

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

        // GeocoderLoading 으로 Activity 전환
        ImageButton routeSearchBtn = findViewById(R.id.routeSearchBtn);
        routeSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RouteLoadingActivity.class);
                // 다음 Activity 에 출발지 목적지 전달
                String currentAdd = inputCurrent.getText().toString();
                if (currentAdd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "출발지가 입력되지 않았습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                String destinationAdd = inputDestination.getText().toString();
                if (destinationAdd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "목적지가 입력되지 않았습니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                // 다음 Activity 에 text 전달
                intent.putExtra("currentLocation", currentAdd);
                intent.putExtra("destinationLocation", destinationAdd);
                intent.putExtra("sortCriterion", "lessTime");
                startActivity(intent);
                finish();
            }
        });

        // Alarm Setting

        // 1. Shared Preference 초기화
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        // 2. 저장해둔 값 불러오기 ("식별값", 초기값)
        alarm = pref.getBoolean("Alarm_setting", false);

        // 3. 레이아웃 변수 초기화
        ImageButton alarmSetting = (ImageButton) findViewById(R.id.alarmBtn);
        ImageView alarmOff = findViewById(R.id.alarmOff);

        // 4. 앱을 새로 켜면 이전에 저장해둔 값이 표시됨
        if (!alarm) {
            alarmOff.setImageResource(R.drawable.alarm_0);
        } else {
            alarmOff.setImageResource(R.drawable.alarm_1);
        }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalendar = new GregorianCalendar();
        Log.v("HelloAlarmActivity", mCalendar.getTime().toString());

        // 5. 각 버튼 클릭 시 새로운 값 저장
        alarmSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alarm) {   // alarm off 일 때
                    alarmOff.setImageResource(R.drawable.alarm_1);
                    alarm = true;
                    editor.putBoolean("Alarm_setting", true);
                    editor.apply();
                    setAlarm();

                } else {
                    alarmOff.setImageResource(R.drawable.alarm_0);
                    alarm = false;
                    editor.putBoolean("Alarm_setting", false);
                    editor.apply();
                    notificationManager.cancelAll();
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

            // 지울거지울거지울거지울거
            latitude = 37.514543;
            longitude = 127.106597;
            // 지울거지울거지울거지울거

            onMapCurrent(mGoogleMap, latitude, longitude);
        }

        public void onProviderDisabled(String provider) { }
        public void onProviderEnabled(String provider) { }
        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mGoogleMap = googleMap;

        float lat = 37.56f;
        float lon = 126.97f;

        LatLng SEOUL = new LatLng(lat, lon);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        mGoogleMap.addMarker(markerOptions);

        mContext = getApplicationContext();
        StationThread stationThread = new StationThread(handler, mContext, lat, lon);
        stationThread.run(mGoogleMap);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 16));
    }

    public void onMapCurrent(@NonNull final GoogleMap googleMap, double latitude, double longitude) {
        mGoogleMap = googleMap;

        float lat = (float) latitude;
        float lon = (float) longitude;

        LatLng Current = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(Current);
        markerOptions.title("현재 위치");
        markerOptions.snippet("Current Location");
        mGoogleMap.addMarker(markerOptions);

        mContext = getApplicationContext();
        StationThread stationThread = new StationThread(handler, mContext, lat, lon);
        stationThread.run(mGoogleMap);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current, 16));
    }

    private void setAlarm() {
        // AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(MainViewActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainViewActivity.this, 0, receiverIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 0, 0);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }


}
