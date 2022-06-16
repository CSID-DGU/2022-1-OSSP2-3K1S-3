package com.example.zipgaja;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zipgaja.api.ClientInterface;
import com.example.zipgaja.api.ClientModule;
import com.example.zipgaja.api.DetailResponse;
import com.example.zipgaja.api.LessMoneyResponse;
import com.example.zipgaja.api.LessMoneyResponseData;
import com.example.zipgaja.api.StatusResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static String BASE_URL = "http://ec2-3-39-232-107.ap-northeast-2.compute.amazonaws.com:3000";

    NestedScrollView nestedScrollView = null;
    RecyclerView recyclerView = null;
    RecyclerviewItemAdapter recyclerviewItemAdapter = null;
    TextView title, controlBtn;
    String state = "start";
    private GoogleMap mGoogleMap = null;
    SupportMapFragment mapFragment;
    ArrayList<LessMoneyResponseData> data;

    String good1Str, good2Str, good3Str, good4Str, bad1Str, bad2Str, bad3Str, bad4Str;
    Dialog suggestDialog, recommendDialog, notRecommendDialog;
    ConstraintLayout loadingView;

    String id = "";
    String start = "";
    String end = "";

    String cLoc = "";
    String dLoc = "";
    String sort = "";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_less_money);

        // #5 start (팀원 작업물 연결 작업, 리스트에서 LessActivity 호출시 데이터 전달
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");
        String sort = intent.getStringExtra("sort");
        // #5 end

        this.cLoc = start;
        this.dLoc = end;
        this.sort = sort;

        loadingView = findViewById(R.id.loadingView);
        title = findViewById(R.id.tvTitle);
        nestedScrollView = findViewById(R.id.svLess);
        recyclerView = findViewById(R.id.recyclerview);
        controlBtn = findViewById(R.id.tvBottom);
        controlBtn.setOnClickListener(view -> {
            if (state.equals("start")) {
                title.setText("경로 안내");
                controlBtn.setText("도착");
                controlBtn.setBackground(getDrawable(R.drawable.rect_round_ad0000));
                state = "arrive";

                good1Str = "돈이 예상보다 적게 들어요";
                good2Str = "예상 도착 시간과 비슷해요";
                good3Str = "도보 구간이 적어요";
                good4Str = "정류장 대기시간이 짧아요";

                bad1Str = "돈이 예상보다 많이 들어요";
                bad2Str = "예상 도착 시간보다 늦어요";
                bad3Str = "도보 구간이 많아요";
                bad4Str = "정류장 대기시간이 길어요";
            } else {


                TextView recomBtn, notRecomBtn, cancleBtn;
                recomBtn = suggestDialog.findViewById(R.id.tvRecommend);
                notRecomBtn = suggestDialog.findViewById(R.id.tvNotRecommend);
                cancleBtn = suggestDialog.findViewById(R.id.tvCancel);

                recomBtn.setOnClickListener(view1 -> {
                    suggestDialog.dismiss();
                    TextView good1, good2, good3, good4, recommendBtn, recCancleBtn;
                    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
                    EditText good5;
                    recommendBtn = recommendDialog.findViewById(R.id.dialog_recom_btn);
                    recCancleBtn = recommendDialog.findViewById(R.id.dialog_not_recom_btn);

                    good1 = recommendDialog.findViewById(R.id.dialog_text1);
                    good2 = recommendDialog.findViewById(R.id.dialog_text2);
                    good3 = recommendDialog.findViewById(R.id.dialog_text3);
                    good4 = recommendDialog.findViewById(R.id.dialog_text4);
                    good5 = recommendDialog.findViewById(R.id.dialog_edit);

                    checkBox1 = recommendDialog.findViewById(R.id.dialog_checkbox1);
                    checkBox2 = recommendDialog.findViewById(R.id.dialog_checkbox2);
                    checkBox3 = recommendDialog.findViewById(R.id.dialog_checkbox3);
                    checkBox4 = recommendDialog.findViewById(R.id.dialog_checkbox4);


                    good1.setText(good1Str);
                    good2.setText(good2Str);
                    good3.setText(good3Str);
                    good4.setText(good4Str);


                    recommendBtn.setOnClickListener(view2 -> {
                        showLoadingView();
                        ClientInterface service = ClientModule.getRetrofit(BASE_URL).create(ClientInterface.class);
                        Call<StatusResponse> call = service.postRecommnedGood(id,
                                checkBox1.isChecked(),
                                checkBox2.isChecked(),
                                checkBox3.isChecked(),
                                checkBox4.isChecked(),
                                good5.getText().toString());
                        call.enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                if (response != null) {
                                    StatusResponse result = response.body();
                                }
                                hideLoadingView();
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                hideLoadingView();
                            }
                        });
                        recommendDialog.dismiss();
                        Intent recInt = new Intent(getApplicationContext(), EndActivity.class);
                        startActivity(recInt);
                        finish();
                    });
                    recCancleBtn.setOnClickListener(view2 -> {
                        recommendDialog.dismiss();
                    });
                    recommendDialog.show();

                });
                notRecomBtn.setOnClickListener(view1 -> {
                    suggestDialog.dismiss();
                    TextView bad1, bad2, bad3, bad4, notRecmmendBtn, notCancleBtn;
                    EditText bad5;
                    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;

                    notRecmmendBtn = notRecommendDialog.findViewById(R.id.dialog_recom_btn);
                    notCancleBtn = notRecommendDialog.findViewById(R.id.dialog_not_recom_btn);
                    bad1 = notRecommendDialog.findViewById(R.id.dialog_not_text1);
                    bad2 = notRecommendDialog.findViewById(R.id.dialog_not_text2);
                    bad3 = notRecommendDialog.findViewById(R.id.dialog_not_text3);
                    bad4 = notRecommendDialog.findViewById(R.id.dialog_not_text4);
                    bad5 = notRecommendDialog.findViewById(R.id.dialog_edit);

                    checkBox1 = notRecommendDialog.findViewById(R.id.dialog_checkbox1);
                    checkBox2 = notRecommendDialog.findViewById(R.id.dialog_checkbox2);
                    checkBox3 = notRecommendDialog.findViewById(R.id.dialog_checkbox3);
                    checkBox4 = notRecommendDialog.findViewById(R.id.dialog_checkbox4);

                    bad1.setText(bad1Str);
                    bad2.setText(bad2Str);
                    bad3.setText(bad3Str);
                    bad4.setText(bad4Str);

                    notRecmmendBtn.setOnClickListener(view2 -> {
                        showLoadingView();
                        ClientInterface service = ClientModule.getRetrofit(BASE_URL).create(ClientInterface.class);
                        Call<StatusResponse> call = service.postRecommnedBad(id,
                                checkBox1.isChecked(),
                                checkBox2.isChecked(),
                                checkBox3.isChecked(),
                                checkBox4.isChecked(),
                                bad5.getText().toString());
                        call.enqueue(new Callback<StatusResponse>() {
                            @Override
                            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                if (response != null) {
                                    StatusResponse result = response.body();

                                }
                                hideLoadingView();
                            }

                            @Override
                            public void onFailure(Call<StatusResponse> call, Throwable t) {
                                hideLoadingView();
                            }
                        });

                        notRecommendDialog.dismiss();
                        Intent notRecInt = new Intent(getApplicationContext(), EndActivity.class);
                        startActivity(notRecInt);
                        finish();
                    });
                    notCancleBtn.setOnClickListener(view2 -> {
                        notRecommendDialog.dismiss();
                    });
                    notRecommendDialog.show();
                });

                cancleBtn.setOnClickListener(view1 -> {
                    suggestDialog.dismiss();
                    Intent cancelInt = new Intent(getApplicationContext(), EndActivity.class);
                    startActivity(cancelInt);
                    finish();
                });
                suggestDialog.show();
            }
        });

        suggestDialog = new Dialog(this, R.style.widthFullDialogStyle);
        suggestDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        suggestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        suggestDialog.setContentView(R.layout.dialog_suggest);
        recommendDialog = new Dialog(this, R.style.widthFullDialogStyle);
        recommendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        recommendDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recommendDialog.setContentView(R.layout.dialog_recommend);
        notRecommendDialog = new Dialog(this, R.style.widthFullDialogStyle);
        notRecommendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notRecommendDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        notRecommendDialog.setContentView(R.layout.dialog_not_recommend);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        recyclerviewItemAdapter = new RecyclerviewItemAdapter(null);
        recyclerView.setAdapter(recyclerviewItemAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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

    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mGoogleMap = googleMap;

        // 스크롤뷰 안에 지도 뷰의 움직임 제어
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

        showLoadingView();
        ClientInterface service = ClientModule.getRetrofit(BASE_URL).create(ClientInterface.class);
        Call<LessMoneyResponse> call = service.getLessMoney(id, start, end);
        call.enqueue(new Callback<LessMoneyResponse>() {
            @Override
            public void onResponse(Call<LessMoneyResponse> call, Response<LessMoneyResponse> response) {
                if (response != null) {
                    LessMoneyResponse result = response.body();
                    data = result.getData();
                    setMapPolyline(result);
                }

                hideLoadingView();
            }

            @Override
            public void onFailure(Call<LessMoneyResponse> call, Throwable t) {
                hideLoadingView();
            }
        });
    }

    public void setMapPolyline(LessMoneyResponse res) {
        List<LatLng> entire_path = PolyUtil.decode(res.getData().get(0).getRoute());

        BitmapDrawable bitmapdraw1 = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_start);
        Bitmap b1 = bitmapdraw1.getBitmap();
        Bitmap startMarker = Bitmap.createScaledBitmap(b1, 100, 100, false);
        BitmapDrawable bitmapdraw2 = (BitmapDrawable) getResources().getDrawable(R.drawable.marker_arive);
        Bitmap b2 = bitmapdraw2.getBitmap();
        Bitmap arriveMarker = Bitmap.createScaledBitmap(b2, 100, 100, false);

        for (int i = 0; i < entire_path.size(); i++) {
            if (i == 0) {
                mGoogleMap.addMarker(new MarkerOptions().position(entire_path.get(i)).title("출발").icon(BitmapDescriptorFactory.fromBitmap(startMarker)));
            } else if (i >= entire_path.size() - 1) {
                mGoogleMap.addMarker(new MarkerOptions().position(entire_path.get(i)).title("도착").icon(BitmapDescriptorFactory.fromBitmap(arriveMarker)));
//                    End_location = entire_path.get(i);
            }
        }

        Polyline line = null;

        if (line == null) {
            line = mGoogleMap.addPolyline(new PolylineOptions()
                    .color(Color.rgb(173, 0, 0))
                    .geodesic(true)
                    .addAll(entire_path));
        } else {
            line.remove();
            line = mGoogleMap.addPolyline(new PolylineOptions()
                    .color(Color.rgb(173, 0, 0))
                    .geodesic(true)
                    .addAll(entire_path));
        }

        LatLng arrival_path = entire_path.get(entire_path.size() - 1);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(arrival_path.latitude, arrival_path.longitude)));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        recyclerviewItemAdapter.setList(data);
        recyclerviewItemAdapter.notifyDataSetChanged();
    }

    private void showLoadingView() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingView() {
        loadingView.setVisibility(View.GONE);
    }
}
