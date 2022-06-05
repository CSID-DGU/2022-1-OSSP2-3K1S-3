package com.example.zipgaja;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = getIntent();
        String id = "";
        String start = "";
        String end = "";

        id = intent.getStringExtra("id");
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");

        TextView id_tv = findViewById(R.id.id);
        TextView start_tv = findViewById(R.id.start);
        TextView end_tv = findViewById(R.id.end);

        id_tv.setText("id: " + id);
        start_tv.setText("start: " + start);
        end_tv.setText("end: " + end);

    }
}
