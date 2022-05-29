package com.example.zipgaja;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SearchListActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

//        Intent intent = getIntent();
//        String currentLocation = intent.getStringExtra("currentLocation");
//        String destinationLocation = intent.getStringExtra("destinationLocation");

        String currentLocation = "";
        String destinationLocation = "";

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            currentLocation = "error";
        } else {
            currentLocation = extras.getString("currentLocation");
            destinationLocation = extras.getString("destinationLocation");
        }

        TextView current = findViewById(R.id.currentLocation);
        TextView destination = findViewById(R.id.destinationLocation);
        current.setText(currentLocation);
        destination.setText(destinationLocation);

        ImageButton sortBtn = (ImageButton)findViewById(R.id.sortBtn);
        TextView sortCriterion = findViewById(R.id.sort);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[]{"최소시간순", "최소금액순", "추천순"};
                final int[] selectedIndex = {0};

                AlertDialog.Builder dialog = new AlertDialog.Builder(SearchListActivity.this);
                dialog.setTitle("정렬 기준을 선택하세요.")
                        .setSingleChoiceItems(
                                items,
                                0,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        selectedIndex[0] = i;
                                    }
                                }
                        )
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Toast.makeText(SearchListActivity.this, items[selectedIndex[0]], Toast.LENGTH_SHORT).show();
                                sortCriterion.setText(items[selectedIndex[0]]);
                            }
                        }).create().show();
            }
        });


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
