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

        // ?????? ????????? ??????/?????? ????????????
        ImageButton currentBtn = findViewById(R.id.currentBtn);
        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startLocationService
                startLocationService();
            }
        });

        // ?????? ??????, ????????? ????????????
        EditText inputCurrent = findViewById(R.id.inputCurrent);
        EditText inputDestination = findViewById(R.id.inputDestination);

        // ?????? ?????? ??? ????????? ?????????
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

        // GeocoderLoading ?????? Activity ??????
        ImageButton routeSearchBtn = findViewById(R.id.routeSearchBtn);
        routeSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RouteLoadingActivity.class);
                // ?????? Activity ??? ????????? ????????? ??????
                String currentAdd = inputCurrent.getText().toString();
                if (currentAdd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "???????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String destinationAdd = inputDestination.getText().toString();
                if (destinationAdd.length() == 0) {
                    Toast.makeText(getApplicationContext(), "???????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ?????? Activity ??? text ??????
                intent.putExtra("currentLocation", currentAdd);
                intent.putExtra("destinationLocation", destinationAdd);
                intent.putExtra("sortCriterion", "lessTime");
                startActivity(intent);
                finish();
            }
        });

        // Alarm Setting

        // 1. Shared Preference ?????????
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        // 2. ???????????? ??? ???????????? ("?????????", ?????????)
        alarm = pref.getBoolean("Alarm_setting", false);

        // 3. ???????????? ?????? ?????????
        ImageButton alarmSetting = (ImageButton) findViewById(R.id.alarmBtn);
        ImageView alarmOff = findViewById(R.id.alarmOff);

        // 4. ?????? ?????? ?????? ????????? ???????????? ?????? ?????????
        if (!alarm) {
            alarmOff.setImageResource(R.drawable.alarm_0);
        } else {
            alarmOff.setImageResource(R.drawable.alarm_1);
        }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mCalendar = new GregorianCalendar();
        Log.v("HelloAlarmActivity", mCalendar.getTime().toString());

        // 5. ??? ?????? ?????? ??? ????????? ??? ??????
        alarmSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alarm) {   // alarm off ??? ???
                    alarmOff.setImageResource(R.drawable.alarm_1);
                    Toast.makeText(getApplicationContext(), "?????? 23??? ?????? ON", Toast.LENGTH_SHORT).show();
                    alarm = true;
                    editor.putBoolean("Alarm_setting", true);
                    editor.apply();
                    setAlarm();

                } else {
                    alarmOff.setImageResource(R.drawable.alarm_0);
                    Toast.makeText(getApplicationContext(), "?????? OFF", Toast.LENGTH_SHORT).show();
                    alarm = false;
                    editor.putBoolean("Alarm_setting", false);
                    editor.apply();
                    notificationManager.cancelAll();
                }
            }
        });

    }


    private void checkPermission() {
        // ?????? ??????????????? ????????? 6.0 ???????????? ???????????? ????????????.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for (String permission : permission_list) {
            // ?????? ?????? ????????? ????????????.
            int chk = checkCallingOrSelfPermission(permission);

            if (chk == PackageManager.PERMISSION_DENIED) {
                // ?????? ?????? ????????? ???????????? ?????? ?????????.
                requestPermissions(permission_list, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0;i < grantResults.length; i++) {
                // ???????????????
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "??? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "??? ?????? ?????? ??????\n?????? ??? ????????? ????????? ?????? ??? ????????????.", Toast.LENGTH_SHORT).show();
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

        // minTime ?????? location update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime,
                minDistance,
                gpsListener);
        Toast.makeText(getApplicationContext(), "?????? ????????? ?????? ????????????...", Toast.LENGTH_LONG).show();
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            // location ??? null ??? ??????
            if (location == null) {
                Toast.makeText(getApplicationContext(), "????????? ??? ??? ????????????.", Toast.LENGTH_SHORT).show();
                return;
            }

            // capture location data sent by current provider
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

//            // ?????????
//            latitude = 37.491208259143;
//            longitude = 127.14636050729;

            String msg = "Latitude : " + latitude + "\nLongitude : " + longitude;
//            String msg = "?????? ????????? ???????????????";
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
        markerOptions.title("??????");
        markerOptions.snippet("????????? ??????");
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
        markerOptions.title("?????? ??????");
        markerOptions.snippet("Current Location");
        mGoogleMap.addMarker(markerOptions);

        mContext = getApplicationContext();
        StationThread stationThread = new StationThread(handler, mContext, lat, lon);
        stationThread.run(mGoogleMap);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current, 16));
    }

    private void setAlarm() {
        // AlarmReceiver??? ??? ??????
        Intent receiverIntent = new Intent(MainViewActivity.this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainViewActivity.this, 0, receiverIntent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainViewActivity.this, 0, receiverIntent, PendingIntent.FLAG_MUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 0, 0);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onBackPressed() {
        /// ?????? ???????????? ????????? ????????? ???????????? ???????????? ?????? ??????
        // super.onBackPressed();

        // ?????? ?????? ?????? ????????? ??? ??????
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;
        if(0 <= gapTime && 2000 >= gapTime) {
            ActivityCompat.finishAffinity(this);    // ??????????????? ????????????
            System.exit(0); // ??????????????? ??????
        } else {
            backBtnTime = curTime;
            Toast.makeText(this,"??? ??? ??? ????????? ???????????????.",Toast.LENGTH_SHORT).show();
        }

    }

}
