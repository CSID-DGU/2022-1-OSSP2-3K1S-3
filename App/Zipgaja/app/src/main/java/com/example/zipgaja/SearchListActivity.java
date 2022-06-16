package com.example.zipgaja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SearchListActivity extends AppCompatActivity {
    final static String TAG = "SearchList";
    Handler handler;
    Context mContext;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Intent intent = getIntent();

        String currentLocation = "";
        String currentAddress = "";
        String destinationLocation = "";
        String destinationAddress = "";
        String sort = "";
        SearchList result = new SearchList();

        currentLocation = intent.getStringExtra("currentLocation");
        currentAddress = intent.getStringExtra("currentAddress");
        destinationLocation = intent.getStringExtra("destinationLocation");
        destinationAddress = intent.getStringExtra("destinationAddress");
        sort = intent.getStringExtra("sortCriterion");
        result = (SearchList) intent.getSerializableExtra("result");

        TextView current = findViewById(R.id.currentLocation);
        TextView currentAdd = findViewById(R.id.currentAddress);
        TextView destination = findViewById(R.id.destinationLocation);
        TextView destinationAdd = findViewById(R.id.destinationAddress);
        current.setText(currentLocation);
        currentAdd.setText(currentAddress);
        destination.setText(destinationLocation);
        destinationAdd.setText(destinationAddress);

        String finalCurrentLocation = currentLocation;
        String finalDestinationLocation = destinationLocation;
        String finalSort = sort;

        Log.e(TAG, String.valueOf(result));

        TextView sortCriterion = findViewById(R.id.sort);
        switch (sort) {
            case "lessTime":
                sortCriterion.setText("최소시간순");
                break;
            case "lessMoney":
                sortCriterion.setText("최소금액순");
                break;
            case "recommend":
                sortCriterion.setText("추천순");
                break;
        }

        // 탈것 이미지
        ImageView vehicle1 = findViewById(R.id.vehicle1);
        ImageView vehicle2 = findViewById(R.id.vehicle2);
        ImageView vehicle3 = findViewById(R.id.vehicle3);
        ImageView vehicle4 = findViewById(R.id.vehicle4);
        ImageView vehicle5 = findViewById(R.id.vehicle5);
        ImageView vehicle6 = findViewById(R.id.vehicle6);
        ImageView vehicle7 = findViewById(R.id.vehicle7);
        ImageView vehicle8 = findViewById(R.id.vehicle8);
        ImageView vehicle9 = findViewById(R.id.vehicle9);
        ImageView vehicle10 = findViewById(R.id.vehicle10);
        ImageView vehicle11 = findViewById(R.id.vehicle11);
        ImageView vehicle12 = findViewById(R.id.vehicle12);
        ImageView vehicle13 = findViewById(R.id.vehicle13);
        ImageView vehicle14 = findViewById(R.id.vehicle14);
        ImageView vehicle15 = findViewById(R.id.vehicle15);
        ImageView vehicle16 = findViewById(R.id.vehicle16);
        ImageView vehicle17 = findViewById(R.id.vehicle17);
        ImageView vehicle18 = findViewById(R.id.vehicle18);
        ImageView vehicle19 = findViewById(R.id.vehicle19);
        ImageView vehicle20 = findViewById(R.id.vehicle20);
        ImageView vehicle21 = findViewById(R.id.vehicle21);
        ImageView vehicle22 = findViewById(R.id.vehicle22);
        ImageView vehicle23 = findViewById(R.id.vehicle23);
        ImageView vehicle24 = findViewById(R.id.vehicle24);
        ImageView vehicle25 = findViewById(R.id.vehicle25);
        ImageView vehicle26 = findViewById(R.id.vehicle26);
        ImageView vehicle27 = findViewById(R.id.vehicle27);
        ImageView vehicle28 = findViewById(R.id.vehicle28);
        ImageView vehicle29 = findViewById(R.id.vehicle29);
        ImageView vehicle30 = findViewById(R.id.vehicle30);
        // ~~분 / ~~원 텍스트
        TextView typeResult1 = findViewById(R.id.typeResult1);
        TextView typeResult2 = findViewById(R.id.typeResult2);
        TextView typeResult3 = findViewById(R.id.typeResult3);
        TextView typeResult4 = findViewById(R.id.typeResult4);
        TextView typeResult5 = findViewById(R.id.typeResult5);
        TextView typeResult6 = findViewById(R.id.typeResult6);
        TextView typeResult7 = findViewById(R.id.typeResult7);
        TextView typeResult8 = findViewById(R.id.typeResult8);
        TextView typeResult9 = findViewById(R.id.typeResult9);
        TextView typeResult10 = findViewById(R.id.typeResult10);
        TextView typeResult11 = findViewById(R.id.typeResult11);
        TextView typeResult12 = findViewById(R.id.typeResult12);
        TextView typeResult13 = findViewById(R.id.typeResult13);
        TextView typeResult14 = findViewById(R.id.typeResult14);
        TextView typeResult15 = findViewById(R.id.typeResult15);
        TextView typeResult16 = findViewById(R.id.typeResult16);
        TextView typeResult17 = findViewById(R.id.typeResult17);
        TextView typeResult18 = findViewById(R.id.typeResult18);
        TextView typeResult19 = findViewById(R.id.typeResult19);
        TextView typeResult20 = findViewById(R.id.typeResult20);
        TextView typeResult21 = findViewById(R.id.typeResult21);
        TextView typeResult22 = findViewById(R.id.typeResult22);
        TextView typeResult23 = findViewById(R.id.typeResult23);
        TextView typeResult24 = findViewById(R.id.typeResult24);
        TextView typeResult25 = findViewById(R.id.typeResult25);
        TextView typeResult26 = findViewById(R.id.typeResult26);
        TextView typeResult27 = findViewById(R.id.typeResult27);
        TextView typeResult28 = findViewById(R.id.typeResult28);
        TextView typeResult29 = findViewById(R.id.typeResult29);
        TextView typeResult30 = findViewById(R.id.typeResult30);
        // 시작 위치 -> ... -> 도착 위치 텍스트
        TextView routeDetail1 = findViewById(R.id.routeDetail1);
        TextView routeDetail2 = findViewById(R.id.routeDetail2);
        TextView routeDetail3 = findViewById(R.id.routeDetail3);
        TextView routeDetail4 = findViewById(R.id.routeDetail4);
        TextView routeDetail5 = findViewById(R.id.routeDetail5);
        TextView routeDetail6 = findViewById(R.id.routeDetail6);
        TextView routeDetail7 = findViewById(R.id.routeDetail7);
        TextView routeDetail8 = findViewById(R.id.routeDetail8);
        TextView routeDetail9 = findViewById(R.id.routeDetail9);
        TextView routeDetail10 = findViewById(R.id.routeDetail10);
        TextView routeDetail11 = findViewById(R.id.routeDetail11);
        TextView routeDetail12 = findViewById(R.id.routeDetail12);
        TextView routeDetail13 = findViewById(R.id.routeDetail13);
        TextView routeDetail14 = findViewById(R.id.routeDetail14);
        TextView routeDetail15 = findViewById(R.id.routeDetail15);
        TextView routeDetail16 = findViewById(R.id.routeDetail16);
        TextView routeDetail17 = findViewById(R.id.routeDetail17);
        TextView routeDetail18 = findViewById(R.id.routeDetail18);
        TextView routeDetail19 = findViewById(R.id.routeDetail19);
        TextView routeDetail20 = findViewById(R.id.routeDetail20);
        TextView routeDetail21 = findViewById(R.id.routeDetail21);
        TextView routeDetail22 = findViewById(R.id.routeDetail22);
        TextView routeDetail23 = findViewById(R.id.routeDetail23);
        TextView routeDetail24 = findViewById(R.id.routeDetail24);
        TextView routeDetail25 = findViewById(R.id.routeDetail25);
        TextView routeDetail26 = findViewById(R.id.routeDetail26);
        TextView routeDetail27 = findViewById(R.id.routeDetail27);
        TextView routeDetail28 = findViewById(R.id.routeDetail28);
        TextView routeDetail29 = findViewById(R.id.routeDetail29);
        TextView routeDetail30 = findViewById(R.id.routeDetail30);
        // 하얀 박스 (클릭 시 상세 경로 이동)
        ImageButton resultBox1 = findViewById(R.id.resultBox1);
        ImageButton resultBox2 = findViewById(R.id.resultBox2);
        ImageButton resultBox3 = findViewById(R.id.resultBox3);
        ImageButton resultBox4 = findViewById(R.id.resultBox4);
        ImageButton resultBox5 = findViewById(R.id.resultBox5);
        ImageButton resultBox6 = findViewById(R.id.resultBox6);
        ImageButton resultBox7 = findViewById(R.id.resultBox7);
        ImageButton resultBox8 = findViewById(R.id.resultBox8);
        ImageButton resultBox9 = findViewById(R.id.resultBox9);
        ImageButton resultBox10 = findViewById(R.id.resultBox10);
        ImageButton resultBox11 = findViewById(R.id.resultBox11);
        ImageButton resultBox12 = findViewById(R.id.resultBox12);
        ImageButton resultBox13 = findViewById(R.id.resultBox13);
        ImageButton resultBox14 = findViewById(R.id.resultBox14);
        ImageButton resultBox15 = findViewById(R.id.resultBox15);
        ImageButton resultBox16 = findViewById(R.id.resultBox16);
        ImageButton resultBox17 = findViewById(R.id.resultBox17);
        ImageButton resultBox18 = findViewById(R.id.resultBox18);
        ImageButton resultBox19 = findViewById(R.id.resultBox19);
        ImageButton resultBox20 = findViewById(R.id.resultBox20);
        ImageButton resultBox21 = findViewById(R.id.resultBox21);
        ImageButton resultBox22 = findViewById(R.id.resultBox22);
        ImageButton resultBox23 = findViewById(R.id.resultBox23);
        ImageButton resultBox24 = findViewById(R.id.resultBox24);
        ImageButton resultBox25 = findViewById(R.id.resultBox25);
        ImageButton resultBox26 = findViewById(R.id.resultBox26);
        ImageButton resultBox27 = findViewById(R.id.resultBox27);
        ImageButton resultBox28 = findViewById(R.id.resultBox28);
        ImageButton resultBox29 = findViewById(R.id.resultBox29);
        ImageButton resultBox30 = findViewById(R.id.resultBox30);
        // 추천수 버튼 (클릭 시 추천 상세보기 이동)
        ImageButton recommendBtn1 = findViewById(R.id.recommendDetailBtn1);
        ImageButton recommendBtn2 = findViewById(R.id.recommendDetailBtn2);
        ImageButton recommendBtn3 = findViewById(R.id.recommendDetailBtn3);
        ImageButton recommendBtn4 = findViewById(R.id.recommendDetailBtn4);
        ImageButton recommendBtn5 = findViewById(R.id.recommendDetailBtn5);
        ImageButton recommendBtn6 = findViewById(R.id.recommendDetailBtn6);
        ImageButton recommendBtn7 = findViewById(R.id.recommendDetailBtn7);
        ImageButton recommendBtn8 = findViewById(R.id.recommendDetailBtn8);
        ImageButton recommendBtn9 = findViewById(R.id.recommendDetailBtn9);
        ImageButton recommendBtn10 = findViewById(R.id.recommendDetailBtn10);
        ImageButton recommendBtn11 = findViewById(R.id.recommendDetailBtn11);
        ImageButton recommendBtn12 = findViewById(R.id.recommendDetailBtn12);
        ImageButton recommendBtn13 = findViewById(R.id.recommendDetailBtn13);
        ImageButton recommendBtn14 = findViewById(R.id.recommendDetailBtn14);
        ImageButton recommendBtn15 = findViewById(R.id.recommendDetailBtn15);
        ImageButton recommendBtn16 = findViewById(R.id.recommendDetailBtn16);
        ImageButton recommendBtn17 = findViewById(R.id.recommendDetailBtn17);
        ImageButton recommendBtn18 = findViewById(R.id.recommendDetailBtn18);
        ImageButton recommendBtn19 = findViewById(R.id.recommendDetailBtn19);
        ImageButton recommendBtn20 = findViewById(R.id.recommendDetailBtn20);
        ImageButton recommendBtn21 = findViewById(R.id.recommendDetailBtn21);
        ImageButton recommendBtn22 = findViewById(R.id.recommendDetailBtn22);
        ImageButton recommendBtn23 = findViewById(R.id.recommendDetailBtn23);
        ImageButton recommendBtn24 = findViewById(R.id.recommendDetailBtn24);
        ImageButton recommendBtn25 = findViewById(R.id.recommendDetailBtn25);
        ImageButton recommendBtn26 = findViewById(R.id.recommendDetailBtn26);
        ImageButton recommendBtn27 = findViewById(R.id.recommendDetailBtn27);
        ImageButton recommendBtn28 = findViewById(R.id.recommendDetailBtn28);
        ImageButton recommendBtn29 = findViewById(R.id.recommendDetailBtn29);
        ImageButton recommendBtn30 = findViewById(R.id.recommendDetailBtn30);
        // 추천수 텍스트
        TextView reNum1 = findViewById(R.id.reNum1);
        TextView reNum2 = findViewById(R.id.reNum2);
        TextView reNum3 = findViewById(R.id.reNum3);
        TextView reNum4 = findViewById(R.id.reNum4);
        TextView reNum5 = findViewById(R.id.reNum5);
        TextView reNum6 = findViewById(R.id.reNum6);
        TextView reNum7 = findViewById(R.id.reNum7);
        TextView reNum8 = findViewById(R.id.reNum8);
        TextView reNum9 = findViewById(R.id.reNum9);
        TextView reNum10 = findViewById(R.id.reNum10);
        TextView reNum11 = findViewById(R.id.reNum11);
        TextView reNum12 = findViewById(R.id.reNum12);
        TextView reNum13 = findViewById(R.id.reNum13);
        TextView reNum14 = findViewById(R.id.reNum14);
        TextView reNum15 = findViewById(R.id.reNum15);
        TextView reNum16 = findViewById(R.id.reNum16);
        TextView reNum17 = findViewById(R.id.reNum17);
        TextView reNum18 = findViewById(R.id.reNum18);
        TextView reNum19 = findViewById(R.id.reNum19);
        TextView reNum20 = findViewById(R.id.reNum20);
        TextView reNum21 = findViewById(R.id.reNum21);
        TextView reNum22 = findViewById(R.id.reNum22);
        TextView reNum23 = findViewById(R.id.reNum23);
        TextView reNum24 = findViewById(R.id.reNum24);
        TextView reNum25 = findViewById(R.id.reNum25);
        TextView reNum26 = findViewById(R.id.reNum26);
        TextView reNum27 = findViewById(R.id.reNum27);
        TextView reNum28 = findViewById(R.id.reNum28);
        TextView reNum29 = findViewById(R.id.reNum29);
        TextView reNum30 = findViewById(R.id.reNum30);
        // layout (크기 조절)
        ConstraintLayout CL1 = findViewById(R.id.ConstraintLayout1);
        ConstraintLayout CL2 = findViewById(R.id.ConstraintLayout2);
        ConstraintLayout CL3 = findViewById(R.id.ConstraintLayout3);
        ConstraintLayout CL4 = findViewById(R.id.ConstraintLayout4);
        ConstraintLayout CL5 = findViewById(R.id.ConstraintLayout5);
        ConstraintLayout CL6 = findViewById(R.id.ConstraintLayout6);
        ConstraintLayout CL7 = findViewById(R.id.ConstraintLayout7);
        ConstraintLayout CL8 = findViewById(R.id.ConstraintLayout8);
        ConstraintLayout CL9 = findViewById(R.id.ConstraintLayout9);
        ConstraintLayout CL10 = findViewById(R.id.ConstraintLayout10);
        ConstraintLayout CL11 = findViewById(R.id.ConstraintLayout11);
        ConstraintLayout CL12 = findViewById(R.id.ConstraintLayout12);
        ConstraintLayout CL13 = findViewById(R.id.ConstraintLayout13);
        ConstraintLayout CL14 = findViewById(R.id.ConstraintLayout14);
        ConstraintLayout CL15 = findViewById(R.id.ConstraintLayout15);
        ConstraintLayout CL16 = findViewById(R.id.ConstraintLayout16);
        ConstraintLayout CL17 = findViewById(R.id.ConstraintLayout17);
        ConstraintLayout CL18 = findViewById(R.id.ConstraintLayout18);
        ConstraintLayout CL19 = findViewById(R.id.ConstraintLayout19);
        ConstraintLayout CL20 = findViewById(R.id.ConstraintLayout20);
        ConstraintLayout CL21 = findViewById(R.id.ConstraintLayout21);
        ConstraintLayout CL22 = findViewById(R.id.ConstraintLayout22);
        ConstraintLayout CL23 = findViewById(R.id.ConstraintLayout23);
        ConstraintLayout CL24 = findViewById(R.id.ConstraintLayout24);
        ConstraintLayout CL25 = findViewById(R.id.ConstraintLayout25);
        ConstraintLayout CL26 = findViewById(R.id.ConstraintLayout26);
        ConstraintLayout CL27 = findViewById(R.id.ConstraintLayout27);
        ConstraintLayout CL28 = findViewById(R.id.ConstraintLayout28);
        ConstraintLayout CL29 = findViewById(R.id.ConstraintLayout29);
        ConstraintLayout CL30 = findViewById(R.id.ConstraintLayout30);


        int i = 1;
        // 확인
        for (Route route : result.getData()[0].getRoute()) {
            Log.d(TAG, "route " + route.toString());
            String routeID = route.getRouteID();
            String type = route.getType();
            int time = (int) route.getTime();
            int cost = route.getCost();
            String busNum = route.getBusNum();
            int recommend = route.getRecommend();
            String[] routeDetail = route.getRouteDetail();
            String routeDetail_String = "";
            if (routeDetail == null) {
                Log.e(TAG, "routeDetail이 NULL");
            } else {
                routeDetail_String = "출발";
                for (String s : routeDetail) {
                    routeDetail_String += " → " + s;
                }
                routeDetail_String += "(도착)";
            }

            if (i == 1) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL1.setLayoutParams(params);
                typeResult1.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle1.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle1.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle1.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle1.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail1.setText(routeDetail_String);
                reNum1.setText("추천수: " + recommend);

                resultBox1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 2) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL2.setLayoutParams(params);
                typeResult2.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle2.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle2.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle2.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle2.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail2.setText(routeDetail_String);
                reNum2.setText("추천수: " + recommend);

                resultBox2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 3) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL3.setLayoutParams(params);
                typeResult3.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle3.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle3.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle3.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle3.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail3.setText(routeDetail_String);
                reNum3.setText("추천수: " + recommend);

                resultBox3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 4) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL4.setLayoutParams(params);
                typeResult4.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle4.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle4.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle4.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle4.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail4.setText(routeDetail_String);
                reNum4.setText("추천수: " + recommend);

                resultBox4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 5) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL5.setLayoutParams(params);
                typeResult5.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle5.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle5.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle5.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle5.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail5.setText(routeDetail_String);
                reNum5.setText("추천수: " + recommend);

                resultBox5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 6) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL6.setLayoutParams(params);
                typeResult6.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle6.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle6.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle6.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle6.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail6.setText(routeDetail_String);
                reNum6.setText("추천수: " + recommend);

                resultBox6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 7) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL7.setLayoutParams(params);
                typeResult7.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle7.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle7.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle7.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle7.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail7.setText(routeDetail_String);
                reNum7.setText("추천수: " + recommend);

                resultBox7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 8) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL8.setLayoutParams(params);
                typeResult8.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle8.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle8.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle8.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle8.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail8.setText(routeDetail_String);
                reNum8.setText("추천수: " + recommend);

                resultBox8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 9) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL9.setLayoutParams(params);
                typeResult9.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle9.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle9.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle9.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle9.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail9.setText(routeDetail_String);
                reNum9.setText("추천수: " + recommend);

                resultBox9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 10) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL10.setLayoutParams(params);
                typeResult10.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle10.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle10.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle10.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle10.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail10.setText(routeDetail_String);
                reNum10.setText("추천수: " + recommend);

                resultBox10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 11) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL11.setLayoutParams(params);
                typeResult11.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle11.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle11.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle11.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle11.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail11.setText(routeDetail_String);
                reNum11.setText("추천수: " + recommend);

                resultBox11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 12) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL12.setLayoutParams(params);
                typeResult12.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle12.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle12.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle12.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle12.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail12.setText(routeDetail_String);
                reNum12.setText("추천수: " + recommend);

                resultBox12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 13) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL13.setLayoutParams(params);
                typeResult13.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle13.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle13.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle13.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle13.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail13.setText(routeDetail_String);
                reNum13.setText("추천수: " + recommend);

                resultBox13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 14) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL14.setLayoutParams(params);
                typeResult14.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle14.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle14.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle14.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle14.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail14.setText(routeDetail_String);
                reNum14.setText("추천수: " + recommend);

                resultBox14.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn14.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 15) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL15.setLayoutParams(params);
                typeResult15.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle15.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle15.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle15.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle15.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail15.setText(routeDetail_String);
                reNum15.setText("추천수: " + recommend);

                resultBox15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn15.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 16) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL16.setLayoutParams(params);
                typeResult16.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle16.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle16.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle16.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle16.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail16.setText(routeDetail_String);
                reNum16.setText("추천수: " + recommend);

                resultBox16.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn16.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 17) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL17.setLayoutParams(params);
                typeResult17.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle17.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle17.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle17.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle17.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail17.setText(routeDetail_String);
                reNum17.setText("추천수: " + recommend);

                resultBox17.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn17.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 18) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL18.setLayoutParams(params);
                typeResult18.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle18.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle18.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle18.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle18.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail18.setText(routeDetail_String);
                reNum18.setText("추천수: " + recommend);

                resultBox18.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn18.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 19) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL19.setLayoutParams(params);
                typeResult19.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle19.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle19.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle19.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle19.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail19.setText(routeDetail_String);
                reNum19.setText("추천수: " + recommend);

                resultBox19.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn19.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 20) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL20.setLayoutParams(params);
                typeResult20.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle20.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle20.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle20.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle20.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail20.setText(routeDetail_String);
                reNum20.setText("추천수: " + recommend);

                resultBox20.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn20.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 21) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL21.setLayoutParams(params);
                typeResult21.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle21.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle21.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle21.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle21.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail21.setText(routeDetail_String);
                reNum21.setText("추천수: " + recommend);

                resultBox21.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn21.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 22) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL22.setLayoutParams(params);
                typeResult22.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle22.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle22.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle22.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle22.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail22.setText(routeDetail_String);
                reNum22.setText("추천수: " + recommend);

                resultBox22.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn22.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 23) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL23.setLayoutParams(params);
                typeResult23.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle23.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle23.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle23.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle23.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail23.setText(routeDetail_String);
                reNum23.setText("추천수: " + recommend);

                resultBox23.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn23.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 24) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL24.setLayoutParams(params);
                typeResult24.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle24.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle24.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle24.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle24.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail24.setText(routeDetail_String);
                reNum24.setText("추천수: " + recommend);

                resultBox24.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn24.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 25) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL25.setLayoutParams(params);
                typeResult25.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle25.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle25.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle25.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle25.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail25.setText(routeDetail_String);
                reNum25.setText("추천수: " + recommend);

                resultBox25.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn25.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 26) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL26.setLayoutParams(params);
                typeResult26.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle26.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle26.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle26.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle26.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail26.setText(routeDetail_String);
                reNum26.setText("추천수: " + recommend);

                resultBox26.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn26.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 27) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL27.setLayoutParams(params);
                typeResult27.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle27.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle27.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle27.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle27.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail27.setText(routeDetail_String);
                reNum27.setText("추천수: " + recommend);

                resultBox27.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn27.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 28) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL28.setLayoutParams(params);
                typeResult28.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle28.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle28.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle28.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle28.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail28.setText(routeDetail_String);
                reNum28.setText("추천수: " + recommend);

                resultBox28.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn28.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 29) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL29.setLayoutParams(params);
                typeResult29.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle29.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle29.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle29.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle29.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail29.setText(routeDetail_String);
                reNum29.setText("추천수: " + recommend);

                resultBox29.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn29.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            } else if (i == 30) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                CL30.setLayoutParams(params);
                typeResult30.setText(time + "분 / " + cost + "원");
                switch (type) {
                    case "bus":
                        vehicle30.setImageResource(R.drawable.ic_bus);
                        break;
                    case "taxi":
                        vehicle30.setImageResource(R.drawable.ic_taxi);
                        break;
                    case "bike":
                        vehicle30.setImageResource(R.drawable.ic_bike);
                        break;
                    case "walk":
                        vehicle30.setImageResource(R.drawable.ic_walk);
                        break;
                }
                routeDetail30.setText(routeDetail_String);
                reNum30.setText("추천수: " + recommend);

                resultBox30.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), LessActivity.class);
                        intent.putExtra("id", routeID);
                        intent.putExtra("start", finalCurrentLocation);
                        intent.putExtra("end", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });

                recommendBtn30.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailLoadingActivity.class);
                        intent.putExtra("busNum", busNum);
                        intent.putExtra("currentLoc", finalCurrentLocation);
                        intent.putExtra("destinationLoc", finalDestinationLocation);
                        intent.putExtra("sort", finalSort);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            i++;
        }


        ImageButton sortBtn = (ImageButton)findViewById(R.id.sortBtn);
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
                                intent.putExtra("currentLocation", finalCurrentLocation);
                                intent.putExtra("destinationLocation", finalDestinationLocation);
                                intent.putExtra("sortCriterion", items[selectedIndex[0]]);
                                startActivity(intent);
                            }
                        }).create().show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
