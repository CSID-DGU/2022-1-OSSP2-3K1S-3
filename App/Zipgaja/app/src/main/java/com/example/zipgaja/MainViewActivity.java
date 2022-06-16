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
import androidx.core.app.ActivityCompat;
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

    private long backBtnTime = 0;
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
                    Toast.makeText(getApplicationContext(), "출발지가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String destinationAdd = inputDestination.getText().toString();
                if (destinationAdd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "목적지가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "매일 23시 알람 ON", Toast.LENGTH_SHORT).show();
                    alarm = true;
                    editor.putBoolean("Alarm_setting", true);
                    editor.apply();
                    setAlarm();

                } else {
                    alarmOff.setImageResource(R.drawable.alarm_0);
                    Toast.makeText(getApplicationContext(), "알람 OFF", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "앱 권한 설정 허용", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "앱 권한 설정 거부\n해당 앱 이용에 제한이 생길 수 있습니다.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(), "현재 위치를 찾는 중입니다...", Toast.LENGTH_LONG).show();
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            // location 이 null 일 경우
            if (location == null) {
                Toast.makeText(getApplicationContext(), "위치를 알 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            // capture location data sent by current provider
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

//            // 확인용
//            latitude = 37.491208259143;
//            longitude = 127.14636050729;

            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
//            String msg = "현재 위치를 찾았습니다";
            Log.i("GPSLocationService", msg);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

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
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainViewActivity.this, 0, receiverIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainViewActivity.this, 0, receiverIntent, PendingIntent.FLAG_MUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 0, 0);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onBackPressed() {
        /// 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 뒤로 가기 두번 누르면 앱 종료
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;
        if(0 <= gapTime && 2000 >= gapTime) {
            ActivityCompat.finishAffinity(this);    // 액티비티를 종료하고
            System.exit(0); // 프로세스를 종료
        } else {
            backBtnTime = curTime;
            Toast.makeText(this,"한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }

    }

}
