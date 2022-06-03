package com.example.zipgaja;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchListActivity extends AppCompatActivity {
    final static String TAG = "SearchList";
    Handler handler;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Intent intent = getIntent();
//        String currentLocation = intent.getStringExtra("currentLocation");
//        String destinationLocation = intent.getStringExtra("destinationLocation");

        String currentLocation = "";
        float cLat = 0;
        float cLon = 0;
        String destinationLocation = "";
        float dLat = 0;
        float dLon = 0;
        String sort = "";
        SearchList result = new SearchList();

        currentLocation = intent.getStringExtra("currentLocation");
        cLat = intent.getFloatExtra("currentLat", 0);
        cLon = intent.getFloatExtra("currentLon", 0);
        destinationLocation = intent.getStringExtra("destinationLocation");
        dLat = intent.getFloatExtra("destinationLat", 0);
        dLon = intent.getFloatExtra("destinationLon", 0);
        sort = intent.getStringExtra("sortCriterion");
        result = (SearchList) intent.getSerializableExtra("result");

//        Bundle extras = getIntent().getExtras();
//
//        if (extras == null) {
//            currentLocation = "error";
//        } else {
//            currentLocation = extras.getString("currentLocation");
//            destinationLocation = extras.getString("destinationLocation");
//            result = (SearchList) extras.getSerializableExtra("result");
//        }

        TextView current = findViewById(R.id.currentLocation);
        TextView destination = findViewById(R.id.destinationLocation);
        current.setText(currentLocation);
        destination.setText(destinationLocation);
//        resultList.setText(result);

//        JSONObject searchList_result = null;
//        try {
//            searchList_result = new JSONObject(result);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        Log.e(TAG, String.valueOf(result));

        TextView sortCriterion = findViewById(R.id.sort);
        if (sort.equals("lessTime")) {
            sortCriterion.setText("최소시간순");
        } else if (sort.equals("lessMoney")) {
            sortCriterion.setText("최소금액순");
        } else if (sort.equals("recommend")) {
            sortCriterion.setText("추천순");
        }

        ImageView vehicle1 = findViewById(R.id.vehicle1);
        ImageView vehicle2 = findViewById(R.id.vehicle2);
        ImageView vehicle3 = findViewById(R.id.vehicle3);
        ImageView vehicle4 = findViewById(R.id.vehicle4);
        ImageView vehicle5 = findViewById(R.id.vehicle5);
        TextView typeResult1 = findViewById(R.id.typeResult1);
        TextView typeResult2 = findViewById(R.id.typeResult2);
        TextView typeResult3 = findViewById(R.id.typeResult3);
        TextView typeResult4 = findViewById(R.id.typeResult4);
        TextView typeResult5 = findViewById(R.id.typeResult5);

        int i = 1;
        // 확인
        for (Route route : result.getData()[0].getRoute()) {
            Log.d(TAG, "route " + route.toString());
            String routeID = route.getRouteID();
            String type = route.getType();
            int time = (int) route.getTime();
            int cost = route.getCost();
            String[] routeDetail = route.getRouteDetail();

            Log.e(TAG, routeID + type + time + cost);

            if (i == 1) {
                typeResult1.setText(time + "분 / " + cost + "원");
                if (type.equals("bus")) {
                    vehicle1.setImageResource(R.drawable.ic_bus);
                } else if (type.equals("taxi")) {
                    vehicle1.setImageResource(R.drawable.ic_taxi);
                }
            } else if (i == 2) {
                typeResult2.setText(time + "분 / " + cost + "원");
                if (type.equals("bus")) {
                    vehicle2.setImageResource(R.drawable.ic_bus);
                } else if (type.equals("taxi")) {
                    vehicle2.setImageResource(R.drawable.ic_taxi);
                }
            } else if (i == 3) {
                typeResult3.setText(time + "분 / " + cost + "원");
                if (type.equals("bus")) {
                    vehicle3.setImageResource(R.drawable.ic_bus);
                } else if (type.equals("taxi")) {
                    vehicle3.setImageResource(R.drawable.ic_taxi);
                }
            } else if (i == 4) {
                typeResult4.setText(time + "분 / " + cost + "원");
                if (type.equals("bus")) {
                    vehicle4.setImageResource(R.drawable.ic_bus);
                } else if (type.equals("taxi")) {
                    vehicle4.setImageResource(R.drawable.ic_taxi);
                }
            } else if (i == 5) {
                typeResult5.setText(time + "분 / " + cost + "원");
                if (type.equals("bus")) {
                    vehicle5.setImageResource(R.drawable.ic_bus);
                } else if (type.equals("taxi")) {
                    vehicle5.setImageResource(R.drawable.ic_taxi);
                }
            }

            i++;
            if (i == 6) {
                break;
            }

        }


        ImageButton sortBtn = (ImageButton)findViewById(R.id.sortBtn);
        String finalCurrentLocation = currentLocation;
        float finalCLat = cLat;
        float finalCLon = cLon;
        String finalDestinationLocation = destinationLocation;
        float finalDLat = dLat;
        float finalDLon = dLon;
        String finalSort = sort;
        String finalCurrentLocation1 = currentLocation;
        String finalDestinationLocation1 = destinationLocation;
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
//                                sortCriterion.setText(items[selectedIndex[0]]);

                                Intent intent = new Intent(getApplicationContext(), RouteLoadingActivity.class);
                                intent.putExtra("currentLocation", finalCurrentLocation1);
                                intent.putExtra("destinationLocation", finalDestinationLocation1);
                                intent.putExtra("sortCriterion", items[selectedIndex[0]]);
                                startActivity(intent);
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
