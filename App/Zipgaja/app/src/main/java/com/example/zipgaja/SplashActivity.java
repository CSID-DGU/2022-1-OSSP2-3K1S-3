package com.example.zipgaja;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hand = new Handler();   // Handler 생성
        // 3초 후 쓰레드를 생성하는 postDelayed 메소드
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent 생성
                Intent i = new Intent(getApplicationContext(), MainViewActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);       // 3초 후 화면 전환

    }
}
