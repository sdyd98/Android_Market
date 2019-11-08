package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.kakao.auth.Session;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static android.R.*;

public class Main_Activity extends AppCompatActivity implements RewardedVideoAdListener {

    private ResponseCallback<KakaoLinkResponse> callback;
    private Map<String, String> serverCallbackArgs = getServerCallbackArgs();

    private GpsTracker gpsTracker;

    Thread Weather_Thread;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    // json 저장
    String json;

    //네이버 검색 api
    BufferedReader br;

    StringBuilder searchResult;

    // 구글 에드몹
    private AdView mAdView;

    private RewardedVideoAd mRewardedVideoAd;

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 유저 정보 어레이 객체 생성
    ArrayList<User_DB> User_Db_ArrayList = new ArrayList<>();

    // 유저 판매 아이템 어레이 객체 생성
    ArrayList<Item_DB> User_Sell_Item_ArrayList = new ArrayList<>();

    // 채팅방 전체 데이터
    ArrayList<Chat_List> Chat_List_Array = new ArrayList<>();

    // 유저의 채팅방 데이터
    ArrayList<Chat_List> Chat_User_List = new ArrayList<>();

    // 검색어 저장 어레이 생성
    ArrayList<Search_Text_DB> Search_Text_Save_Array = new ArrayList<>();

    // 검색어 랭킹 어레이 생성
    ArrayList<String> Rank_Search_Text = new ArrayList<>();

    // 뒤로가기 2번 종료 클래스
    BackPressCloseHandler backPressCloseHandler;

    // 채널 아이디
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    // 로그인한 유저 아이디
    private String Login_User_Id;

    // 로그인한 유저 포지션
    private int User_position;

    // 로그 아웃 체크
    public static Activity Main_activity;

    // 실시간 검색어 포지션 초기값
    int test = -1;

    // 날씨 정보
    String Weather_Img;

    // 뷰 플리퍼
    ViewFlipper viewFlipper;

    //위치 확인 변수
    //static int position_number;

    // 리사이클러뷰 구성 선언
    private RecyclerView recyclerView, Category_recycle, Rank_Recycle;
    private RecyclerView.Adapter mAdapter,cAdapter,rAdapter;
    private RecyclerView.LayoutManager layoutManager, LayoutManager_Category_recycle, Rank_Layout_Manager;

    // 넘겨줄 데이터 셋팅
//    static ArrayList<Item_Profile> test1 = new ArrayList<>();
    ArrayList<Category_Profile> Category_ArrayList = new ArrayList<>();
    // 카테고리 기본 값 셋팅
    Category_Profile CPU = new Category_Profile("CPU",getURLForResource(R.drawable.cpu));
    Category_Profile GPU = new Category_Profile("GPU",getURLForResource(R.drawable.gpu));
    Category_Profile RAM = new Category_Profile("RAM",getURLForResource(R.drawable.ram));
    Category_Profile MB = new Category_Profile("MB",getURLForResource(R.drawable.mb));
    Category_Profile SSD = new Category_Profile("SSD",getURLForResource(R.drawable.ssd));
    Category_Profile HDD = new Category_Profile("HDD",getURLForResource(R.drawable.hdd));
    Category_Profile POWER = new Category_Profile("POWER",getURLForResource(R.drawable.power));
    Category_Profile COOLER = new Category_Profile("COOLER",getURLForResource(R.drawable.cooler));

    // requestcode
    // static final int REQUEST_TEST = 100;
//    static final int REQUEST_TEST2 = 50;

    // 뷰 선언
    LinearLayout Main_Icon_Search_Btn;
    ConstraintLayout Main_User_Sell_List;
    ImageView Icon_Cpu, Main_Icon_Sell,Main_Icon_Chat,Main_My_Menu, Main_news;
    ImageView Main_Allim_btn, Main_ad, Main_app, Main_My_Select, Main_Real_Time_Menu, Main_Real_Time;
    TextView Main_User_Name, Main_Real_Time_Number, Main_Real_Time_Text, Real_Time_Text_2, test_mp3, temperture;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 로그인 아이디 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

        try{
            if(getIntent().getStringExtra("Link_User_ID").equals("")){

            }
            else{
                Login_User_Id = getIntent().getStringExtra("Link_User_ID");
                Intent intent = new Intent(getApplicationContext(), Buy_Activity.class);
                intent.putExtra("User_ID", getIntent().getStringExtra("Link_User_ID"));
                intent.putExtra("Item_Number",getIntent().getIntExtra("Link_Item_Number",0));
                startActivity(intent);
            }
        }catch (Exception e){

        }


        //GPS 권한 체크
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MobileAds.initialize(this, "ca-app-pub-5157110510509826/1499970073");
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        // 파이어베이스 토큰
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            return;
                        }
                        String token = task.getResult().getToken();
                    }

                });

        Count count = new Count();

        count.start();

        // 유저 정보 가져오기
        User_getShared();

        // 아이템 쉐어드 불러옴
        Item_getShared();

        // 검색결과 쉐어드 불러옴
        getSearch_Text_Save();

        // 검색 쉐어드 가지고 랭킹 매기기
        Rank_Search_Text();

        Log.e("True_Check", Rank_Search_Text.toString());

        // 뷰 매칭
        Main_User_Name = findViewById(R.id.Main_User_Name);
        Main_app = findViewById(R.id.Main_app);
        Main_ad = findViewById(R.id.Main_ad);
        //Main_My_Item = findViewById(R.id.Main_My_Item);
        Main_Icon_Search_Btn = findViewById(R.id.Main_Icon_Search_Btn);
        Icon_Cpu = findViewById(R.id.Icon_Cpu);
        Main_Icon_Sell = findViewById(R.id.Main_Icon_Sell);
        Main_Icon_Chat = findViewById(R.id.Main_Icon_Chat);
        Main_My_Menu = findViewById(R.id.Main_My_Menu);
        Main_Allim_btn = findViewById(R.id.Main_Allim_btn);
        Main_My_Select = findViewById(R.id.Main_My_Select);
        Main_User_Sell_List = findViewById(R.id.Main_User_Sell_List);
        Main_Real_Time_Menu = findViewById(R.id.Main_Real_Time_Menu);
        Main_Real_Time = findViewById(R.id.Main_Real_Time);
        Main_Real_Time_Number = findViewById(R.id.Main_Real_Time_Number);
        Main_Real_Time_Text = findViewById(R.id.Main_Real_Time_Text);
        Real_Time_Text_2 = findViewById(R.id.Real_Time_Text_2);
        viewFlipper = findViewById(R.id.Main_My_Item);
        test_mp3 = findViewById(R.id.test_mp3);
        temperture = findViewById(R.id.temperture);
        LottieAnimationView Lottie_News = findViewById(R.id.Lottie_News);
        LottieAnimationView Lottie_Search = findViewById(R.id.Search_Lottie);
        LottieAnimationView Lottie_Weather = findViewById(R.id.Lottie_Weather);

        // 리사이클러뷰 매칭
        recyclerView =  findViewById(R.id.My_TestRe);
        Category_recycle = findViewById(R.id.Category_Recycle);
        Rank_Recycle = findViewById(R.id.Rank_Recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // ??
        recyclerView.setHasFixedSize(true);
        Category_recycle.setHasFixedSize(true);
        Rank_Recycle.setHasFixedSize(true);

        // 레이아웃 매니저 출력방식 설정
        // use a linear layout manager
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        LayoutManager_Category_recycle = new LinearLayoutManager(Main_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        Rank_Layout_Manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        Category_recycle.setLayoutManager(LayoutManager_Category_recycle);
        recyclerView.setLayoutManager(layoutManager);
        Rank_Recycle.setLayoutManager(Rank_Layout_Manager);

        System.out.println("로그인 아이디"+Login_User_Id);

        Start_Lottie(Lottie_News, "news.json");
        Start_Lottie(Lottie_Search, "search.json");

        // 로그인한 유저 포지션 확인
        User_Position_Check();

        // 게시물 기본 정보 셋팅
        Main_User_Name.setText(User_Db_ArrayList.get(User_position).getUser_name());
        Allim_Check();

        //Drawable alpha = ((ImageView)findViewById(R.id.Main_My_Item)).getDrawable();
        //alpha.setAlpha(50);

        // 판매 어레이 set
        setUser_Item_Sell_List();

        // 아이템 랜덤 set
        Item_Sell_Check();

        // 채팅 리스트 불러오기
        getChat_List();

        // 유저 채팅방 데이터 set
        setUser_Chat_List();

        // 뷰플리퍼 이미지 셋팅
        setImage_ViewFlipper();

        gpsTracker = new GpsTracker(getApplicationContext());

        double lon = gpsTracker.getLongitude();//경도
        double lat = gpsTracker.getLatitude();//위도

        //날씨 api
        WeatherAPI(lon,lat);

        Weather_Thread.start();

        try {
            Weather_Thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Weather_Img);

        temperture.setSelected(true);

        // 로티 시작
        if(Weather_Img.equals("Rain")){
            Start_Lottie(Lottie_Weather, "rain.json");
        }
        else if(Weather_Img.equals("Clouds")){
            Start_Lottie(Lottie_Weather, "cloud.json");
        }
        else{
            Start_Lottie(Lottie_Weather, "weather.json");
        }

        // 알림창 띄우기
        if(User_Db_ArrayList.get(User_position).getAllim_db().size() > 0){
            NotificationSomethings();
        }


        // 랭킹 어뎁터 생성
        rAdapter = new Rank_Adapter(Rank_Search_Text, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 어뎁터 셋팅
        Rank_Recycle.setAdapter(rAdapter);


        // 모든 게시글 어뎁터 생성
        mAdapter = new MyAdapter(item_db_array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 포지션 값 가져오기
                Object obj = v.getTag();

                // 예외처리
                if(obj != null) {

                    // 포지션 값 저장
                    int position = (int)obj;

                    // 인텐트 생성
                    Intent intent = new Intent(Main_Activity.this, Buy_Activity.class);

                    // 게시물 고유 넘버 인텐트
                    intent.putExtra("Item_Number", ((MyAdapter)mAdapter).getData(position).getItem_number());

                    // 현재 접속 유저 아이디 인텐트
                    intent.putExtra("User_ID", Login_User_Id);

                    // 인텐트 전송
                    startActivity(intent);

                    //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                    // 하나씩 넘길수도 있고
                    // 한번에 넘길수도 있다 (Serializable)

//                    Item_Profile My_Menu_Item_Profile = new Item_Profile(((MyAdapter)mAdapter).getData(position).getItem_Name(), ((MyAdapter)mAdapter).getData(position).getItem_Price(), ((MyAdapter)mAdapter).getData(position).getItem_Img(), ((MyAdapter)mAdapter).getData(position).getItem_Detail(), ((MyAdapter)mAdapter).getData(position).getCategori_Name());
//                    for(int i = 0; i < Mymenu_Activity.My_Menu_Array.size(); i++){
//                        if(Mymenu_Activity.My_Menu_Array.get(i).getItem_Img().equals(My_Menu_Item_Profile.getItem_Img())){
//                            Mymenu_Activity.My_Menu_Array.remove(i);
//                        }
//                    }
//                    Mymenu_Activity.My_Menu_Array.add(0, My_Menu_Item_Profile);
                }
            }
        });

        // 게시글 어뎁터 set
        recyclerView.setAdapter(mAdapter);

        // 카테고리 더미값 추가
        if(Category_ArrayList.size() < 3) {
            Category_ArrayList.add(CPU);
            Category_ArrayList.add(GPU);
            Category_ArrayList.add(RAM);
            Category_ArrayList.add(MB);
            Category_ArrayList.add(SSD);
            Category_ArrayList.add(HDD);
            Category_ArrayList.add(POWER);
            Category_ArrayList.add(COOLER);
        }

        // 카테고리 목록 어뎁터 생성
        cAdapter = new Category_MyAdapter(Category_ArrayList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){

                    // 포지션값 저장
                    int position = (int)obj;

                    // 인텐트 생성
                    Intent intent = new Intent(Main_Activity.this, Category_Cpu_Activity.class);

                    // 카테고리 이름 인텐트
                    intent.putExtra("Category_Name", ((Category_MyAdapter)cAdapter).getData(position).getItem_Name());

                    // 유저 아이디 인텐트
                    intent.putExtra("User_ID", Login_User_Id);

                    // 인텐트 전송
                    startActivity(intent);

                }
            }
        });

        // 카테고리 어뎁터 set
        Category_recycle.setAdapter(cAdapter);

        // 메인 액티비티 넘겨주기 위함
        Main_activity = Main_Activity.this;

        // 뒤로가기 하기 위한 핸들러 생성
        backPressCloseHandler = new BackPressCloseHandler(this);

        //찜 목록 버튼
        Main_My_Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Select_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);
            }
        });

        // 다나와 암시적 인텐트
        Main_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 앱이 있으면 다나와 연결
                if(getPackageList()) {
                    ComponentName compName = new ComponentName("com.danawa.estimate", "com.danawa.estimate.activity.IntroActivity");
                    Intent intent23 = new Intent(Intent.ACTION_MAIN);
                    intent23.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent23.setComponent(compName);
                    startActivity(intent23);
                }

                // 앱이 없으면 구글 스토어에서 설치
                else{
                    String url = "market://details?id=" + "com.danawa.estimate";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        // 퀘이사존 웹페이지 암시적 인텐트
        Main_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action 인텐트 생성
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://quasarzone.co.kr/"));

                // 인텐트 전송
                startActivity(intent);
            }
        });

        // 내상품
        Main_User_Sell_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 인텐트 생성
                Intent intent = new Intent(Main_Activity.this, My_Item_Activity.class);

                // 유저아이디 인텐트
                intent.putExtra("User_ID", Login_User_Id);

                // 인텐트 전송
                startActivity(intent);
            }
        });

        // 검색
        Main_Icon_Search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 인텐트 생성
                Intent intent = new Intent(Main_Activity.this, Search_Activity.class);

                // 유저 아이디 인텐트
                intent.putExtra("User_ID", Login_User_Id);

                // 인텐트 전송
                startActivity(intent);
            }
        });

        // 메뉴 (프래그먼트 사용해야함)
        Main_My_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //인텐트  생성
                Intent intent = new Intent(Main_Activity.this, Mymenu_Activity.class);

                // 유저 아이디 인텐트
                intent.putExtra("User_ID", Login_User_Id);

                // 인텐트 전송
                startActivity(intent);
            }
        });

        // 채팅
        Main_Icon_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 인텐트 생성
                Intent intent = new Intent(Main_Activity.this, Chat_Activity.class);

                // 유저 아이디 인텐트
                intent.putExtra("User_ID", Login_User_Id);

                // 인텐트 전송
                startActivity(intent);

            }
        });

        // 판매
        Main_Icon_Sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 인텐트 생성
                Intent intent = new Intent(Main_Activity.this, Sell_Activity.class);

                // 유저 아이디 인텐트
                intent.putExtra("User_ID", Login_User_Id);

                // 인텐트 전송
                startActivity(intent);
            }
        });

        //알림 버튼
        Main_Allim_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 인텐트 생성
                Intent intent = new Intent(Main_Activity.this, allim_Activity.class);

                // 유저 아이디 인텐트
                intent.putExtra("User_ID", Login_User_Id);

                // 인텐트 전송
                startActivity(intent);
            }
        });

        // 실시간 검색어 메뉴
        Main_Real_Time_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Rank_Recycle.getVisibility() == View.VISIBLE){
                    Rank_Recycle.setVisibility(View.GONE);
                    Main_Real_Time_Number.setVisibility(View.VISIBLE);
                    Main_Real_Time_Text.setVisibility(View.VISIBLE);
                    Main_Real_Time.setVisibility(View.VISIBLE);
                    Real_Time_Text_2.setVisibility(View.INVISIBLE);
                    Drawable drawable = getResources().getDrawable(R.drawable.main_menu);
                    Main_Real_Time_Menu.setImageDrawable(drawable);
                }
                else{
                    Drawable drawable = getResources().getDrawable(R.drawable.main_menu_up);
                    Main_Real_Time_Menu.setImageDrawable(drawable);
                    Real_Time_Text_2.setVisibility(View.VISIBLE);
                    Main_Real_Time_Number.setVisibility(View.INVISIBLE);
                    Main_Real_Time_Text.setVisibility(View.INVISIBLE);
                    Main_Real_Time.setVisibility(View.INVISIBLE);
                    Rank_Recycle.setVisibility(View.VISIBLE);
                }
            }
        });

        // 테스트
        test_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRewardedVideoAd();
            }
        });

        Lottie_News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), it_news_Activity.class);
                intent.putExtra("User_ID", Login_User_Id);
                startActivity(intent);

            }
        });




    }



//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // 이름 변경시 가져오기 (삭제 예정)
//        if( requestCode == REQUEST_TEST2){
//            if(My_Menu_Detail_Activity.User_Name != null){
//                Main_User_Name.setText(My_Menu_Detail_Activity.User_Name);
//            }
//        }
//         else {   // RESULT_CANCEL
//      }
//    }

    // 액티비티 재시작
    @Override
    protected void onRestart() {
        super.onRestart();

        // 기존 판매 항목 초기화
        User_Sell_Item_ArrayList.clear();

        // 기존 유저 정보 어레이 초기화
        User_Db_ArrayList.clear();

        // 기존 채팅방 리스트 초기화
        Chat_User_List.clear();

        // 기존 유저 채팅방 리스트 초기화
        Chat_List_Array.clear();

        // 유저 정보 가져오기
        User_getShared();

        // 로그인한 유저 포지션 파악
        User_Position_Check();

        // 유저 아이디 설정
        Main_User_Name.setText(User_Db_ArrayList.get(User_position).getUser_name());

        // 아이템 어레이 초기화
        item_db_array.clear();

        //검색어 쉐어드 초기화
        Search_Text_Save_Array.clear();

        //검색어 결과 랭킹 초기화
        Rank_Search_Text.clear();

        // 판매 상품 뷰플리퍼 이미지 초기화
        viewFlipper.removeAllViews();

        // 아이템 쉐어드 불러옴
        Item_getShared();

        Allim_Check();

        // 판매 어레이 set
        setUser_Item_Sell_List();

        // 아이템 랜덤 set
        Item_Sell_Check();

        // 채팅 리스트 불러오기
        getChat_List();

        // 유저 채팅방 데이터 set
        setUser_Chat_List();

        // 검색결과 쉐어드 불러옴
        getSearch_Text_Save();

        // 검색 쉐어드 가지고 랭킹 매기기
        Rank_Search_Text();

        // 븊플리퍼 이미지 셋팅
        setImage_ViewFlipper();

        // 채팅왔는지 체크
        //getChat_Check();

        // 어뎁터 새로고침
        mAdapter.notifyDataSetChanged();

        rAdapter.notifyDataSetChanged();

    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    // 앱있는지 없는지 판별
    public boolean getPackageList() {

        // 반환 값
        boolean isExist = false;


        PackageManager pkgMgr = getPackageManager();
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = pkgMgr.queryIntentActivities(mainIntent, 0);

        try {
            for (int i = 0; i < mApps.size(); i++) {
                if(mApps.get(i).activityInfo.packageName.startsWith("com.danawa.estimate")){
                    isExist = true;
                    break;
                }
            }
        }
        catch (Exception e) {
            isExist = false;
        }
        return isExist;
    }

    // 아이템 정보 쉐어드 get
    public void Item_getShared(){

        // 쉐어드 파일 이름 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item", 0);
        // 키값을 통해 데이터 가져옴
        String item_db_array_string = sharedPreferences.getString("data","");

        // 키값을 통해 가져온 데이터가 null 값이 아니라면
        if(item_db_array_string != null){
            try {

                // item_db_array_string (String) 데이터로 JSonArray 생성
                JSONArray jsonArray_item_db = new JSONArray(item_db_array_string);

                // JsonArray 길이 만큼 반복
                for(int i = 0; i < jsonArray_item_db.length(); i++){

                    // JsonArray에 있는 값을 data에 저장
                    String data = jsonArray_item_db.optString(i);

                    // gson 생성
                    Gson gson = new Gson();

                    // item_db에 gson을 이용해 data값 파싱하여 객체 생성
                    Item_DB item_db = gson.fromJson( data, Item_DB.class);

                    // 생성된 객체 어레이에 추가
                    item_db_array.add(item_db);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 유저 정보 쉐어드 get
    public void User_getShared() {

        // 쉐어드 이름과 모드 설정
        SharedPreferences sharedPreferences = getSharedPreferences("User", 0);
        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String user_db_array = sharedPreferences.getString("Data", "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if (user_db_array != null) {
            try {

                // Key="Data" 값을 이용해 JsonArray 생성
                JSONArray jsonArray_user_db = new JSONArray(user_db_array);

                // JsonArray 길이 만큼 반복
                for (int i = 0; i < jsonArray_user_db.length(); i++) {

                    // JsonArray 값을 data에 저장
                    String data = jsonArray_user_db.optString(i);

                    // Gson 생성
                    Gson gson = new Gson();

                    // data 값을 gson으로 파싱 하여 객체 생성
                    User_DB user_db = gson.fromJson(data, User_DB.class);

                    // 생성된 객체 추가
                    User_Db_ArrayList.add(user_db);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // drawable 파일 string 변환 메소드
    private String getURLForResource(int resId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resId).toString();
    }

    // 유저 포지션 확인
    public void User_Position_Check(){
        for(int i = 0;  i< User_Db_ArrayList.size(); i++){
            if(User_Db_ArrayList.get(i).getUser_id().equals(Login_User_Id)){
                User_position = i;
                break;
            }
        }
    }

    // 알림 왔는지 체크
    public void Allim_Check(){
        if(User_Db_ArrayList.get(User_position).getAllim_db().size() > 0){
            Drawable drawable = getResources().getDrawable(R.drawable.bell_call);
            Main_Allim_btn.setImageDrawable(drawable);
        }
        else{
            Drawable drawable = getResources().getDrawable(R.drawable.bell);
            Main_Allim_btn.setImageDrawable(drawable);
        }
    }

    // 판매 상품 있는지 체크
    public void Item_Sell_Check(){
        if(User_Sell_Item_ArrayList.size() > 0){
            Random random = new Random();
            Main_User_Sell_List.setVisibility(View.VISIBLE);
           // Main_My_Item.setImageURI(Uri.parse(User_Sell_Item_ArrayList.get(random.nextInt(User_Sell_Item_ArrayList.size())).getitem_img()));
        }
        else{
            Main_User_Sell_List.setVisibility(View.GONE);
        }
    }

    // 판매 상품 set
    public void setUser_Item_Sell_List(){
        // 아이템 전체 갯수 만큼 반복
        for(int i = 0; i < item_db_array.size(); i++){
            // 아이템 작성자 아이디와 로그인 유저 아이디가 같으면
            if(item_db_array.get(i).getItem_id().equals(Login_User_Id))
            {
                // 그아이템 객체 판매 어레이에 추가
                User_Sell_Item_ArrayList.add(item_db_array.get(i));

            }
        }
    }

    // 채팅방 목록 불러오기
    public void getChat_List(){
        SharedPreferences sharedPreferences = getSharedPreferences("Chat", 0);
        String Chat_List_Array = sharedPreferences.getString("Data", "");

        // JsonArray를 파싱하여 User_DB형 어레이리스트에 담는과정
        if (Chat_List_Array!= null) {
            try {

                // Key="Data" 값을 이용해 JsonArray 생성
                JSONArray jsonArray_user_db = new JSONArray(Chat_List_Array);

                // JsonArray 길이 만큼 반복
                for (int i = 0; i < jsonArray_user_db.length(); i++) {

                    // JsonArray 값을 data에 저장
                    String data = jsonArray_user_db.optString(i);

                    // Gson 생성
                    Gson gson = new Gson();

                    // data 값을 gson으로 파싱 하여 객체 생성
                    Chat_List chat_list = gson.fromJson(data, Chat_List.class);

                    // 생성된 객체 추가
                    this.Chat_List_Array.add(chat_list);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    // 로그인 유저에 채팅방 분류
    public void setUser_Chat_List(){

        Chat_User_List.clear();

        // 전체 채팅방 목록만큼 반복
        for(int i = 0; i < Chat_List_Array.size(); i++){
            // 만약 채팅방 아이디 목록에 유저의 아이디가 들어가있는 채팅방을 발견 한다면
            if(Chat_List_Array.get(i).getChat_Maker_User().equals(Login_User_Id)||Chat_List_Array.get(i).getChat_Receiver_User().equals(Login_User_Id)){
                // 그채팅방 추가
                Chat_User_List.add(Chat_List_Array.get(i));
            }
        }
    }

    // 검색어 저장 쉐어드 get
    public void getSearch_Text_Save(){

        // 쉐어드 파일이름과 모드 선언
        SharedPreferences sharedPreferences = getSharedPreferences("Item",0);

        // key를 통해 벨류값  (get ArrayList 전체 목록) 저장
        String Search_DB_Array = sharedPreferences.getString("Search_Text", "");

        // JsonArray를 파싱하여 Search_DB형 어레이리스트에 담는과정
        if(Search_DB_Array !=  null) {
            try {

                JSONArray jsonArray_user_db = new JSONArray(Search_DB_Array);

                for (int i = 0; i < jsonArray_user_db.length(); i++) {

                    String data = jsonArray_user_db.optString(i);

                    Gson gson = new Gson();

                    Search_Text_DB search_text_db = gson.fromJson(data, Search_Text_DB.class);

                    Search_Text_Save_Array.add(search_text_db);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 검색어 순위 매기기
    public void Rank_Search_Text(){

        // 예비 검색 정보 어레이 리스트 선언
        ArrayList<Search_Text_DB> Rank_Check_Array = new ArrayList<>();

        // 모든 검색 정보 복사
        Rank_Check_Array.addAll(Search_Text_Save_Array);


        // 검색어 순위 5개 만들때까지 반복
        while(true){

            String Search_Text = null;

            int MaxCount = 0;

            int Search_Text_Position = 0;

            // 총 검색어 정보 만큼 반복
            for (int i = 0; i < Rank_Check_Array.size(); i++) {

                // 만약 MaxCount보다 더 높다면
                if (Rank_Check_Array.get(i).getSearch_Count() > MaxCount) {

                    // MaxCount에 현재 값 저장
                    MaxCount = Rank_Check_Array.get(i).getSearch_Count();

                    // Search_Text 에도 그 검색어 저장
                    Search_Text = Rank_Check_Array.get(i).getSearch_Text();

                    // 포지션 값 저장
                    Search_Text_Position = i;
                }

            }

            // 가장 많이 검색된 검색어 포지션 삭제
            if(Rank_Check_Array.size() != 0) {
                Rank_Check_Array.remove(Search_Text_Position);
            }

            // 검색 가장 많이된 검색어 저장
            Rank_Search_Text.add(Search_Text);

            // 검색어 셋팅 2되면 반복 탈출
            if(Rank_Search_Text.size() > 4 || Rank_Check_Array.size() == 0){
                break;
            }
        }
    }

    // 뷰 플리퍼 판매 이미지 셋팅
    public void setImage_ViewFlipper(){
        for(int i = 0; i < User_Sell_Item_ArrayList.size(); i++){
            Image_Change(User_Sell_Item_ArrayList.get(i).getitem_img());
        }
    }

    // 이미지 슬라이더
    public void Image_Change(String Image){
        ImageView imageView = new ImageView(this);
        imageView.setImageURI(Uri.parse(Image));

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(1000);
        viewFlipper.setAutoStart(true);

        // animation
        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);


    }

    // 알림 발생 메서드
    public void NotificationSomethings() {

        boolean Check = true;

        //SystemService ??
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // 알림창 터치시 해당 게시글로 이동
        Intent notificationIntent = new Intent(this, Buy_Activity.class);

        // 전달할 인텐트
        notificationIntent.putExtra("User_ID", Login_User_Id);

        // 알림 타고 들어갔는지 판단
        notificationIntent.putExtra("Check_noti", Check);

        // 마직막 알림 게시글 번호 인텐트
        notificationIntent.putExtra("Item_Number", User_Db_ArrayList.get(User_position).getAllim_db().get(User_Db_ArrayList.get(User_position).getAllim_db().size()-1).getItem_Number());

        // ? setFlags ???
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;

        // 인텐트
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

        // 알림창 설정
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(loadBitmap(User_Db_ArrayList.get(User_position).getAllim_db().get(User_Db_ArrayList.get(User_position).getAllim_db().size()-1).getAllim_Item_Img())) //BitMap 이미지 요구
                .setContentTitle("작성한 게시글에 "+User_Db_ArrayList.get(User_position).getAllim_db().get(User_Db_ArrayList.get(User_position).getAllim_db().size()-1).getAllim_User_Name()+"님이")
                .setContentText(User_Db_ArrayList.get(User_position).getAllim_db().get(User_Db_ArrayList.get(User_position).getAllim_db().size()-1).getAllim_Ments())
                // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))

                // 우선순위
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                .setDefaults(Notification.DEFAULT_VIBRATE)

                .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정

                .setAutoCancel(true);

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.market); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴

    }

    //문자열 비트맵으로 변환
    public Bitmap loadBitmap(String url)
    {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try
        {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (bis != null)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

                // 핸들러 선언
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);

                        // 만약 날라온 메세지가 0 이라면
                        if(msg.what == 0){

                            // 순위
                            Main_Real_Time_Number.setText(String.valueOf(test+1));

                            // 검색어
                            Main_Real_Time_Text.setText(Rank_Search_Text.get(test));
                        }
                    }
                };

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {
        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }

    }

    @Override
    public void onRewardedVideoAdOpened() {
    }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }

    // 로티 시작 메소드
    public void Start_Lottie(LottieAnimationView lottieAnimationView, String json){

        // 파일 정하기
        lottieAnimationView.setAnimation(json);

        // 반복 횟수
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);

        // 시작
        lottieAnimationView.playAnimation();
    }

    // 날씨 api
    public void WeatherAPI(final double lon, final double lat) {
        // 네트워크 연결은 Thread 생성 필요
        Weather_Thread = new Thread() {
            @Override
            public void run() {
                try {

                    String urlstr = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=e309d9d54ddcbfb84195fca7f949b064";
                    URL url = new URL(urlstr);
                    // 위에서 생성한 url을 토대로 연결
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    // ?
                    con.setRequestMethod("GET");
                    // 연결
                    con.connect();
                    // 응답 코드 저장
                    int responseCode = con.getResponseCode();
                    if(responseCode==200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }
                    // 스트링 빌더
                    searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
                    }
                    // 버퍼 리더 닫기
                    br.close();
                    // http 연결 종료
                    con.disconnect();
                    // json 데이터
                    json = searchResult.toString();
                } catch (Exception e) {
                    Log.e("test", "run: "+ e );
                }


                try {
                    JSONObject jsonObject = new JSONObject(json);

                    String temp = jsonObject.getJSONObject("main").getString("temp");

                    double Temp_Change = Double.valueOf(temp);

                    int Temp_Cheage2 = (int)Temp_Change - 270;

                    String Real_Temp = String.valueOf(Temp_Cheage2);

                    Weather_Img = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

                    if(Weather_Img.equals("Rain")){
                        temperture.setText("현재 온도: "+Real_Temp+"°C"+"             현재 날씨: 비");
                    }
                    else if(Weather_Img.equals("Clouds")){
                        temperture.setText("현재 온도: "+Real_Temp+"°C"+"             현재 날씨: 구름");
                    }
                    else{
                        temperture.setText("현재 온도: "+Real_Temp+"°C"+"             현재 날씨: 맑음");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(Main_Activity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(Main_Activity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main_Activity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Main_Activity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Main_Activity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Main_Activity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private Map<String, String> getServerCallbackArgs() {
        Map<String, String> callbackParameters = new HashMap<>();
        callbackParameters.put("user_id", "1234");
        callbackParameters.put("title", "프로방스 자동차 여행 !@#$%");
        return callbackParameters;
    }

    class Count extends Thread{
                    @Override
                    public void run() {
                        super.run();

                        while(true){
                            test++;
                            handler.sendEmptyMessage(0);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if(test == Rank_Search_Text.size()-1){
                                test = -1;
                            }
                        }
                    }
                }
            }






