package com.example.zipgaja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchListActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Intent intent = getIntent();
        String currentLocation = intent.getStringExtra("currentLocation");
        String destinationLocation = intent.getStringExtra("destinationLocation");

        TextView current = findViewById(R.id.currentLocation);
        TextView destination = findViewById(R.id.destinationLocation);
        current.setText(currentLocation);
        destination.setText(destinationLocation);

        ImageButton recommendBtn1 = (ImageButton) findViewById(R.id.recommendDetailBtn1);
        ImageButton recommendBtn2 = (ImageButton) findViewById(R.id.recommendDetailBtn2);
        ImageButton recommendBtn3 = (ImageButton) findViewById(R.id.recommendDetailBtn3);
        ImageButton recommendBtn4 = (ImageButton) findViewById(R.id.recommendDetailBtn4);

        recommendBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecommendDetailActivity.class);
                startActivity(i);
                finish();
            }
        });
        recommendBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecommendDetailActivity.class);
                startActivity(i);
                finish();
            }
        });
        recommendBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecommendDetailActivity.class);
                startActivity(i);
                finish();
            }
        });
        recommendBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RecommendDetailActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
