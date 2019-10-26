package com.example.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.R.*;

public class Main_Activity extends AppCompatActivity {

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

    // 로그인한 유저 아이디
    private String Login_User_Id;

    // 로그인한 유저 포지션
    private int User_position;

    // 로그 아웃 체크
    public static Activity Main_activity;

    // 실시간 검색어 포지션 초기값
    int test = -1;

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
    ImageView Icon_Cpu, Main_Icon_Sell,Main_Icon_Chat,Main_My_Menu, Main_My_Item;
    ImageView Main_Allim_btn, Main_ad, Main_app, Main_My_Select, Main_Real_Time_Menu, Main_Real_Time;
    TextView Main_User_Name, Main_Real_Time_Number, Main_Real_Time_Text, Real_Time_Text_2;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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

        // 로그인 아이디 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

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

        // 븊플리퍼 이미지 셋팅
        setImage_ViewFlipper();

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
            Rank_Check_Array.remove(Search_Text_Position);

            // 검색 가장 많이된 검색어 저장
            Rank_Search_Text.add(Search_Text);

            // 검색어 셋팅 2되면 반복 탈출
            if(Rank_Search_Text.size() > 4 || Rank_Check_Array.size() == 0){
                Toast.makeText(getApplicationContext(), String.valueOf(Search_Text_Save_Array.size()), Toast.LENGTH_SHORT).show();
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




