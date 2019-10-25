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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    // 뒤로가기 2번 종료 클래스
    BackPressCloseHandler backPressCloseHandler;

    // 로그인한 유저 아이디
    private String Login_User_Id;

    // 로그인한 유저 포지션
    private int User_position;

    // 로그 아웃 체크
    public static Activity Main_activity;

    //위치 확인 변수
    //static int position_number;

    // 리사이클러뷰 구성 선언
    private RecyclerView recyclerView, Category_recycle;
    private RecyclerView.Adapter mAdapter,cAdapter;
    private RecyclerView.LayoutManager layoutManager, LayoutManager_Category_recycle;

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
    ImageView Main_Allim_btn, Main_ad, Main_app, Main_My_Select;
    TextView Main_User_Name;

    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 유저 정보 가져오기
        User_getShared();

        // 아이템 쉐어드 불러옴
        Item_getShared();

        // 뷰 매칭
        Main_User_Name = findViewById(R.id.Main_User_Name);
        Main_app = findViewById(R.id.Main_app);
        Main_ad = findViewById(R.id.Main_ad);
        Main_My_Item = findViewById(R.id.Main_My_Item);
        Main_Icon_Search_Btn = findViewById(R.id.Main_Icon_Search_Btn);
        Icon_Cpu = findViewById(R.id.Icon_Cpu);
        Main_Icon_Sell = findViewById(R.id.Main_Icon_Sell);
        Main_Icon_Chat = findViewById(R.id.Main_Icon_Chat);
        Main_My_Menu = findViewById(R.id.Main_My_Menu);
        Main_Allim_btn = findViewById(R.id.Main_Allim_btn);
        Main_My_Select = findViewById(R.id.Main_My_Select);
        Main_User_Sell_List = findViewById(R.id.Main_User_Sell_List);

        // 리사이클러뷰 매칭
        recyclerView =  findViewById(R.id.My_TestRe);
        Category_recycle = findViewById(R.id.Category_Recycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // ??
        recyclerView.setHasFixedSize(true);
        Category_recycle.setHasFixedSize(true);

        // 레이아웃 매니저 출력방식 설정
        // use a linear layout manager
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        LayoutManager_Category_recycle = new LinearLayoutManager(Main_Activity.this, LinearLayoutManager.HORIZONTAL, false);

        Category_recycle.setLayoutManager(LayoutManager_Category_recycle);
        recyclerView.setLayoutManager(layoutManager);

        // 로그인 아이디 받기
        Login_User_Id = getIntent().getStringExtra("User_ID");

        // 로그인한 유저 포지션 확인
        User_Position_Check();

        // 게시물 기본 정보 셋팅
        Main_User_Name.setText(User_Db_ArrayList.get(User_position).getUser_name());
        Allim_Check();
        Drawable alpha = ((ImageView)findViewById(R.id.Main_My_Item)).getDrawable();
        alpha.setAlpha(50);

        // 판매 어레이 set
        setUser_Item_Sell_List();

        // 아이템 랜덤 set
        Item_Sell_Check();

        // 채팅 리스트 불러오기
        getChat_List();

        // 유저 채팅방 데이터 set
        setUser_Chat_List();

        // 채팅왔는지 체크
        //getChat_Check();

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
        Main_My_Item.setOnClickListener(new View.OnClickListener() {
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

        //카테고리 버튼 (삭제 예정)
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

        // 채팅왔는지 체크
        //getChat_Check();

        // 어뎁터 새로고침
        mAdapter.notifyDataSetChanged();

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
            Main_My_Item.setImageURI(Uri.parse(User_Sell_Item_ArrayList.get(random.nextInt(User_Sell_Item_ArrayList.size())).getitem_img()));
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
            if(item_db_array.get(i).getItem_id().equals(Login_User_Id)){
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

    // 채팅 왔는지 확인
    public void getChat_Check(){
        for (int i = 0; i < Chat_User_List.size(); i++){
            if(Chat_User_List.get(i).isChat_Check() == false){
                Drawable drawable = getResources().getDrawable(R.drawable.chat_call);
                Main_Icon_Chat.setImageDrawable(drawable);
                break;
            }
        }
    }
}




