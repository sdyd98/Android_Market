package com.example.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Main_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 뒤로가기 2번 종료 클래스
    BackPressCloseHandler backPressCloseHandler;

    // 로그인 정보
    String user_id;

    //위치 확인 변수
    //static int position_number;

    // 리사이클러뷰 구성 선언
    private RecyclerView recyclerView, Category_recycle;
    private RecyclerView.Adapter mAdapter,cAdapter;
    private RecyclerView.LayoutManager layoutManager, LayoutManager_Category_recycle;

    // 넘겨줄 데이터 세팅
    static ArrayList<Item_Profile> test1 = new ArrayList<>();
    static ArrayList<Category_Profile> Category_ArrayList = new ArrayList<>();
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
    static final int REQUEST_TEST2 = 50;

    // ?
    String img;

    // ?


    // 뷰 선언
    LinearLayout Main_Icon_Search_Btn;
    ImageView Icon_Cpu, Main_Icon_Sell,Main_Icon_Chat,Main_My_Menu, Main_My_Item;
    ImageView Main_Category_btn, Main_ad, Main_app;
    TextView Main_User_Name;
    ImageView Main_Image;
    String Item_Detail, Categori_Name;



    // 액티비티 생성
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // 뒤로가기 하기 위한 핸들러 선언
        backPressCloseHandler = new BackPressCloseHandler(this);

        // 로그인 아이디 받기
        user_id = getIntent().getStringExtra("User_ID");

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
        Main_Category_btn = findViewById(R.id.Main_Category_btn);





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

        // specify an adapter (see also next example)

        // 모든 게시글 어뎁터 생성
        mAdapter = new MyAdapter(item_db_array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 포지션 값 가져오기
                Object obj = v.getTag();
                if(obj != null) {
                    int position = (int)obj;

                    Intent intent = new Intent(Main_Activity.this, Buy_Activity.class);
                    // 게시물 고유 넘버 인텐트
                    intent.putExtra("Item_Number", ((MyAdapter)mAdapter).getData(position).getItem_number());
                    // 현재 접속 유저 아이디 인텐트
                    intent.putExtra("User_ID", user_id);

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
        recyclerView.setAdapter(mAdapter);


        // 카테고리 목록 어뎁터 생성
        cAdapter = new Category_MyAdapter(Category_ArrayList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null){
                    int position = (int)obj;
                    Intent intent = new Intent(Main_Activity.this, Category_Cpu_Activity.class);
                    intent.putExtra("Category_Name", ((Category_MyAdapter)cAdapter).getData(position).getItem_Name());
                    intent.putExtra("User_ID", user_id);
                    startActivity(intent);
                }
            }
        });
        Category_recycle.setAdapter(cAdapter);

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
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://quasarzone.co.kr/"));
                startActivity(intent);
            }
        });

        // 내상품
        Main_My_Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, My_Item_Activity.class);
                intent.putExtra("User_ID", user_id);
                startActivity(intent);
            }
        });

        // 검색
        Main_Icon_Search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        // 메뉴 (프래그먼트 사용해야함)
        Main_My_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Mymenu_Activity.class);
                startActivityForResult(intent, REQUEST_TEST2);
            }
        });

        // 채팅
        Main_Icon_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Chat_Activity.class);
                startActivity(intent);
            }
        });

        // 판매
        Main_Icon_Sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Sell_Activity.class);
                intent.putExtra("User_ID", user_id);
                startActivity(intent);
            }
        });

        //카테고리 버튼 (삭제 예정)
        Main_Category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, Category_Activity.class);
                startActivity(intent);
            }
        });
    }

    // 뒤로가기
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 이름 변경시 가져오기 (삭제 예정)
        if( requestCode == REQUEST_TEST2){
            if(My_Menu_Detail_Activity.User_Name != null){
                Main_User_Name.setText(My_Menu_Detail_Activity.User_Name);
            }
        }
         else {   // RESULT_CANCEL
      }
    }

    // 앱있는지 없는지 판별
    public boolean getPackageList() {
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

        Log.e("check_123", item_db_array_string );
        // 키값을 통해 가져온 데이터가 null 값이 아니라면
        if(item_db_array_string != null){
            try {
                // String 데이터를 JSonArray 로 파싱 하여 다시 아이템 객체 어레이에 넣음
                JSONArray jsonArray_item_db = new JSONArray(item_db_array_string);
                for(int i = 0; i < jsonArray_item_db.length(); i++){
                    String data = jsonArray_item_db.optString(i);
                    Gson gson = new Gson();
                    Item_DB item_db = gson.fromJson( data, Item_DB.class);
                    item_db_array.add(item_db);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        // main 화면이 보일때 마다 아이템 쉐어드 정보 가져옴
        item_db_array.clear();
        Item_getShared();

    }
}




