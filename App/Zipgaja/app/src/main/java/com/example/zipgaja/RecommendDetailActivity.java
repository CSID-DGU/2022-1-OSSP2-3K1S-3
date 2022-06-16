package com.example.zipgaja;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class RecommendDetailActivity extends AppCompatActivity {
    final static String TAG = "RecommendDetail";
    Handler handler;
    Context mContext;

    String cLoc = "";
    String dLoc = "";
    String sort = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail);

        TextView good1 = findViewById(R.id.good1);
        TextView good2 = findViewById(R.id.good2);
        TextView good1cnt = findViewById(R.id.good1cnt);
        TextView good2cnt = findViewById(R.id.good2cnt);
        TextView bad1 = findViewById(R.id.bad1);
        TextView bad2 = findViewById(R.id.bad2);
        TextView bad1cnt = findViewById(R.id.bad1cnt);
        TextView bad2cnt = findViewById(R.id.bad2cnt);
        TextView etc = findViewById(R.id.etcText);

        Intent intent = getIntent();

        String good1S = intent.getStringExtra("good1");
        String good2S = intent.getStringExtra("good2");
        String good1cntS = intent.getStringExtra("good1cnt");
        String good2cntS = intent.getStringExtra("good2cnt");
        String bad1S = intent.getStringExtra("bad1");
        String bad2S = intent.getStringExtra("bad2");
        String bad1cntS = intent.getStringExtra("bad1cnt");
        String bad2cntS = intent.getStringExtra("bad2cnt");
        String good3S = intent.getStringExtra("good3") + "\n";
        String good4S = intent.getStringExtra("good4") + "\n";
        String bad3S = intent.getStringExtra("bad3") + "\n";
        String bad4S = intent.getStringExtra("bad4");
        String cLoc = intent.getStringExtra("currentLoc");
        String dLoc = intent.getStringExtra("destinationLoc");
        String sort = intent.getStringExtra("sort");
        this.cLoc = cLoc;
        this.dLoc = dLoc;
        this.sort = sort;


        good1.setText(good1S);
        good2.setText(good2S);
        good1cnt.setText(good1cntS + "회");
        good2cnt.setText(good2cntS + "회");
        bad1.setText(bad1S);
        bad2.setText(bad2S);
        bad1cnt.setText(bad1cntS + "회");
        bad2cnt.setText(bad2cntS + "회");
        etc.setText(good3S + good4S + bad3S + bad4S);

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), RouteLoadingActivity.class);

        intent.putExtra("currentLocation", this.cLoc);
        intent.putExtra("destinationLocation", this.dLoc);
        intent.putExtra("sortCriterion", this.sort);
        startActivity(intent);
        finish();
    }
}
