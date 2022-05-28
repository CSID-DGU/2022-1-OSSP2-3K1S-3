package com.example.zipgaja;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.zipgaja.module.api.ClientInterface;
import com.example.zipgaja.module.api.ClientModule;
import com.example.zipgaja.module.api.DetailResponse;
import com.example.zipgaja.module.api.LessMoneyResponse;
import com.example.zipgaja.module.api.LessMoneyResponseData;
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

public class LessActivity extends AppCompatActivity implements OnMapReadyCallback  {
    RecyclerView recyclerView = null;
    RecyclerviewItemAdapter recyclerviewItemAdapter = null;
    ImageButton controlBtn;
    String state = "start";
    private GoogleMap mGoogleMap = null;
    SupportMapFragment mapFragment;
    ArrayList<LessMoneyResponseData> data;

    String good1Str, good2Str, good3Str, good4Str, bad1Str, bad2Str, bad3Str, bad4Str;
    Dialog suggestDialog, recommendDialog, notRecommendDialog;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_less_money);
        recyclerView = findViewById(R.id.recyclerview);
        controlBtn = findViewById(R.id.con_btn);
        controlBtn.setOnClickListener(view -> {
            if(state.equals("start")){

                ConstraintLayout constraintLayout = findViewById(R.id.parent_layout);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.map,ConstraintSet.TOP,R.id.recommendDetail_title,ConstraintSet.BOTTOM,0);
                constraintSet.connect(R.id.map,ConstraintSet.BOTTOM,R.id.recyclerview,ConstraintSet.TOP,0);
                constraintSet.connect(R.id.recyclerview,ConstraintSet.TOP,R.id.map,ConstraintSet.BOTTOM,0);
                constraintSet.connect(R.id.recyclerview,ConstraintSet.BOTTOM,R.id.con_btn,ConstraintSet.TOP,0);
                constraintSet.applyTo(constraintLayout);
                controlBtn.setImageResource(R.drawable.btn_arrive);
                state = "arrive";
                ClientInterface service = ClientModule.getRetrofit("http://ec2-107-23-186-215.compute-1.amazonaws.com:5000").create(ClientInterface.class);
                Call<DetailResponse> call = service.getDetail(id);// todo 서버 세팅되면 바꿔주세요
                call.enqueue(new Callback<DetailResponse>() {
                    @Override
                    public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                        if(response!=null){
                            DetailResponse result = response.body();
                            good1Str = result.getGood1();
                            good2Str = result.getGood2();
                            good3Str = result.getGood3();
                            good4Str = result.getGood4();

                            bad1Str = result.getBad1();
                            bad2Str = result.getBad2();
                            bad3Str = result.getBad3();
                            bad4Str = result.getBad4();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailResponse> call, Throwable t) {

                    }
                });
            } else {


                ImageButton recomBtn, notRecomBtn;
                TextView cancleBtn;
                recomBtn = suggestDialog.findViewById(R.id.dialog_recom_btn);
                notRecomBtn = suggestDialog.findViewById(R.id.dialog_not_recom_btn);
                cancleBtn = suggestDialog.findViewById(R.id.dialog_cancle_btn);

                recomBtn.setOnClickListener(view1 -> {
                    suggestDialog.dismiss();
                    ImageView recommendBtn, recCancleBtn;
                    TextView good1,good2,good3,good4;
                    CheckBox checkBox1,checkBox2,checkBox3,checkBox4;
                    EditText good5;
                    recommendBtn = recommendDialog.findViewById(R.id.dialog_recom_btn);
                    recCancleBtn = recommendDialog.findViewById(R.id.dialog_not_recom_btn);

                    good1= recommendDialog.findViewById(R.id.dialog_text1);
                    good2= recommendDialog.findViewById(R.id.dialog_text2);
                    good3= recommendDialog.findViewById(R.id.dialog_text3);
                    good4= recommendDialog.findViewById(R.id.dialog_text4);
                    good5= recommendDialog.findViewById(R.id.dialog_edit);

                    checkBox1 = recommendDialog.findViewById(R.id.dialog_checkbox1);
                    checkBox2 = recommendDialog.findViewById(R.id.dialog_checkbox2);
                    checkBox3 = recommendDialog.findViewById(R.id.dialog_checkbox3);
                    checkBox4 = recommendDialog.findViewById(R.id.dialog_checkbox4);


                    good1.setText(good1Str);
                    good2.setText(good2Str);
                    good3.setText(good3Str);
                    good4.setText(good4Str);


                    recommendBtn.setOnClickListener(view2 -> {
                        recommendDialog.dismiss();
                    });
                    recCancleBtn.setOnClickListener(view2 -> {
                        recommendDialog.dismiss();
                    });
                    recommendDialog.show();

                });
                notRecomBtn.setOnClickListener(view1 -> {
                    suggestDialog.dismiss();
                    ImageView notRecmmendBtn, notCancleBtn;
                    TextView bad1,bad2,bad3,bad4;

                    notRecmmendBtn = notRecommendDialog.findViewById(R.id.dialog_recom_btn);
                    notCancleBtn = notRecommendDialog.findViewById(R.id.dialog_not_recom_btn);
                    bad1= notRecommendDialog.findViewById(R.id.dialog_not_text1);
                    bad2= notRecommendDialog.findViewById(R.id.dialog_not_text2);
                    bad3= notRecommendDialog.findViewById(R.id.dialog_not_text3);
                    bad4= notRecommendDialog.findViewById(R.id.dialog_not_text4);

                    bad1.setText(bad1Str);
                    bad2.setText(bad2Str);
                    bad3.setText(bad3Str);
                    bad4.setText(bad4Str);

                    notRecmmendBtn.setOnClickListener(view2 -> {
                        notRecommendDialog.dismiss();
                    });
                    notCancleBtn.setOnClickListener(view2 -> {
                        notRecommendDialog.dismiss();
                    });
                    notRecommendDialog.show();
                });

                cancleBtn.setOnClickListener(view1 ->{
                    suggestDialog.dismiss();
                });

                suggestDialog.show();
//                ConstraintLayout constraintLayout2 = findViewById(R.id.parent_layout);
//                ConstraintSet constraintSet2 = new ConstraintSet();
//                constraintSet2.clone(constraintLayout2);
//                constraintSet2.connect(R.id.recyclerview,constraintSet2.TOP,R.id.recommendDetail_title,constraintSet2.BOTTOM,0);
//                constraintSet2.connect(R.id.recyclerview,constraintSet2.BOTTOM,R.id.recyclerview,constraintSet2.TOP,0);
//                constraintSet2.connect(R.id.map,constraintSet2.TOP,R.id.map,constraintSet2.BOTTOM,0);
//                constraintSet2.connect(R.id.map,constraintSet2.BOTTOM,R.id.con_btn,constraintSet2.TOP,0);
//                constraintSet2.applyTo(constraintLayout2);
//                state = "start";
            }
        });

        suggestDialog = new Dialog(this);
        suggestDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        suggestDialog.setContentView(R.layout.dialog_suggest);
        recommendDialog = new Dialog(this);
        recommendDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recommendDialog.setContentView(R.layout.dialog_recommend);
        notRecommendDialog = new Dialog(this);
        notRecommendDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        notRecommendDialog.setContentView(R.layout.dialog_not_recommend);

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        recyclerviewItemAdapter = new RecyclerviewItemAdapter(null);
        recyclerView.setAdapter(recyclerviewItemAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }



    @Override
    public void onMapReady(@NonNull final GoogleMap googleMap) {
        mGoogleMap = googleMap;

//        ClientInterface service = ClientModule.getRetrofit("http://ec2-107-23-186-215.compute-1.amazonaws.com:5000").create(ClientInterface.class);
//        Call<LessMoneyResponse> call = service.getLessMoney()


        LessMoneyResponse res = new LessMoneyResponse();
        res.setStatus(200);
        data = new ArrayList<LessMoneyResponseData>();
        LessMoneyResponseData data1 = new LessMoneyResponseData();
        data1.setRoute("axfdFwicfWw@He@\\e@`CiAbATTCb@J|@Oz@}GPmFs_@cAkGWB`A`Gv@nG??`A~G^?{E}[UYCQ??sCuSOmBaDo@??oEsAc@hE{Cw@kDqBx@qEz@h@??bGdDDSeCiAwMiI{GZmDhBOb@}AjA??oMtL??aCnBcDhDy@fBe@zCClA??AbFuGQ}BD??iQ\\??yGZyHjA??aFd@}@i@m@NsEuDmA{BoAuD{BsIu@aBe@cCmBoFyAz@oBoGr@g@BH??pB~F^MgDwJqHwQk@{@yAwGs@}H[cAoANmAcCs@G}@gBuDkCxAqBlCvC??tBzCz@jBTL?]o@wAoB_DkPcR_Bs@k@GKLsBi@cFW??yDG{F[eEo@kA[]WYg@uDaI??qDqFeKoM_B_B??wJqIu@qAcBZaIOmOn@??ySf@oOjE??e_@`L??uf@pN??oJxCsMt@mAa@c@U_CgC??kJiK??qPyQIN??HOoKkLwCcCyFwC??cUaL??{b@iT??iMkGeDkBqFmCEL??DMg@WuHuDiC_Aq\\iDaEw@Eh@lCR??x@Ey@YgC]oYqC?h@zFl@??b@DMnCsGy@CtBeIm@{BW?{BLkCb@F??tKdA?i@{IaAqn@yFE^bQpB??ng@rE?i@YEcmAcLqKBiHb@?j@bLs@vFH??bAFj@g@mGQsEJ}YbC@`@fE_@??hJu@?k@sP~A@`@nBQNFJRPpBE^eBN`BcEr@G");
        data.add(data1);
        data1 = new LessMoneyResponseData();
        data1.setName("출발지");
        data1.setTimev(21.2f);
        data1.setCost(0);
        data1.setType("walk");
        data.add(data1);

        data1 = new LessMoneyResponseData();
        data1.setName("퇴계로5가");
        data1.setTimev(46.5f);
        data1.setCost(2250);
        data1.setType("bus");
        data.add(data1);

        data1 = new LessMoneyResponseData();
        data1.setName("목적지");
        data1.setTimev(0.4f);
        data1.setCost(0);
        data1.setType("walk");
        data.add(data1);
        res.setData(data);

        List<LatLng> entire_path = PolyUtil.decode(res.getData().get(0).getRoute());

        BitmapDrawable bitmapdraw1=(BitmapDrawable)getResources().getDrawable(R.drawable.marker_start);
        Bitmap b1=bitmapdraw1.getBitmap();
        Bitmap startMarker = Bitmap.createScaledBitmap(b1, 100, 100, false);
        BitmapDrawable bitmapdraw2 = (BitmapDrawable)getResources().getDrawable(R.drawable.marker_arive);
        Bitmap b2=bitmapdraw2.getBitmap();
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
}
