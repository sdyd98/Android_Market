package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Mymenu_Activity extends AppCompatActivity {

    // 상품 어레이 객체 생성
    ArrayList<Item_DB> item_db_array = new ArrayList<>();

    // 내가 본상품 고유번호
    static ArrayList<Integer> My_Menu_Number_Array = new ArrayList();

    // 내가 본상품
    ArrayList<Item_DB> My_Menu_Array = new ArrayList();

    // context
    public static Context CONTEXT;

    // requestcode
    static final int TEST_DATA = 20;

    // 뷰 서언
    ImageView My_Menu_Icon_Close;
    ImageView My_Menu_Icon_User;
    TextView My_Menu_Icon_Chat, My_Menu_Icon_Allim, My_Menu_Icon_Keyword_Allim, My_Menu_Icon_Heart, My_Menu_User_Name;
    LinearLayout Item_Layout, Comments_Layout, Heart_Layout, Following_Layout, Followers_Layout;

    RecyclerView My_Menu_Recycle;
    RecyclerView.Adapter My_Menu_Adapter;
    RecyclerView.LayoutManager My_Menu_LayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = this;
        setContentView(R.layout.mymenu);

        // 뷰매칭
        My_Menu_User_Name = findViewById(R.id.My_Menu_User_Name);
        My_Menu_Icon_Allim = findViewById(R.id.My_Menu_Icon_Allim);
        My_Menu_Icon_Chat = findViewById(R.id.My_Menu_Icon_Chat);
        Item_Layout = findViewById(R.id.Item_Layout);
        Comments_Layout = findViewById(R.id.Comments_Layout);
        Heart_Layout = findViewById(R.id.Heart_Layout);
        Following_Layout = findViewById(R.id.Following_Layout);
        Followers_Layout = findViewById(R.id.Follower_Layout);
        My_Menu_Icon_Close = findViewById(R.id.My_Menu_Icon_Close);
        My_Menu_Icon_User = findViewById(R.id.My_Menu_Icon_User);
        My_Menu_Icon_Keyword_Allim = findViewById(R.id.My_Menu_Icon_Keyword_Allim);
        My_Menu_Icon_Heart = findViewById(R.id.My_Menu_Icon_Heart);

        My_Menu_Recycle = findViewById(R.id.My_Menu_Recycle);
        My_Menu_Recycle.setHasFixedSize(true);
        My_Menu_LayoutManager = new LinearLayoutManager(Mymenu_Activity.this, LinearLayoutManager.HORIZONTAL, false);
        My_Menu_Recycle.setLayoutManager(My_Menu_LayoutManager);

        // 내가 본 상품          내가본 상품 판단 - > 게시물을 들어 갔을때 알려줘야함
        My_Menu_Adapter = new My_Menu_Adapter(My_Menu_Array, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = v.getTag();
                if(obj != null) {
                    int position = (int)obj;
                    ((My_Menu_Adapter)My_Menu_Adapter).getData(position);
                    Intent intent = new Intent(Mymenu_Activity.this, Buy_Activity.class);

                    // 게시물 고유 넘버 인텐트
                    intent.putExtra("Item_Number", ((My_Menu_Adapter)My_Menu_Adapter).getData(position).getItem_number());
                    // 현재 접속 유저 아이디 인텐트
                    intent.putExtra("User_ID", getIntent().getStringExtra("User_ID"));

                    startActivity(intent);
                    //intent.putStringArrayListExtra("array",((MyAdapter)mAdapter).getData(position));
                    // 하나씩 넘길수도 있고
                    // 한번에 넘길수도 있다 (Serializable)

                }
            }
        });
        My_Menu_Recycle.setAdapter(My_Menu_Adapter);

        // 이름 설정
        if(My_Menu_Detail_Activity.User_Name != null){
            My_Menu_User_Name.setText(My_Menu_Detail_Activity.User_Name);
        }

        //  이미지 설정
        if(My_Menu_Detail_Activity.My_Menu_User_Icon != null){
            My_Menu_Icon_User.setImageURI(Uri.parse(My_Menu_Detail_Activity.My_Menu_User_Icon));
        }

        // 알림 목록 클릭
        My_Menu_Icon_Allim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, allim_Activity.class);
                startActivity(intent);
            }
        });


        // 키워드 알림 클릭
        My_Menu_Icon_Keyword_Allim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Keyword_allim_Activity.class);
                startActivity(intent);
            }
        });

        // 찜목록
        My_Menu_Icon_Heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Heart_Activity.class);
                startActivity(intent);
            }
        });

        // 채팅 클릭
        My_Menu_Icon_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Chat_Activity.class);
                startActivity(intent);
            }
        });

        // 팔로워 목록
        Followers_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Followers_Activity.class);
                startActivity(intent);
            }
        });

        // 팔로잉 목록
        Following_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Following_Activity.class);
                startActivity(intent);
            }
        });


        // 후기 클릭 삭제 예정
        Comments_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Comments_Activity.class);
                startActivity(intent);
            }
        });

        // 찜 클릭
        Heart_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Heart_Activity.class);
                startActivity(intent);
            }
        });

        // 내 상품 클릭
        Item_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, My_Item_Activity.class);
                startActivity(intent);
            }
        });

        // 아이콘 선택
        My_Menu_Icon_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, My_Menu_Detail_Activity.class);
                startActivityForResult(intent, TEST_DATA);
            }
        });


        // 닫기 버튼
        My_Menu_Icon_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mymenu_Activity.this, Main_Activity.class);
                setResult( RESULT_OK, intent);
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == TEST_DATA) {
            if(My_Menu_Detail_Activity.User_Name != null){
                My_Menu_User_Name.setText(My_Menu_Detail_Activity.User_Name);
            }
            if(My_Menu_Detail_Activity.My_Menu_User_Icon != null){
                My_Menu_Icon_User.setImageURI(Uri.parse(My_Menu_Detail_Activity.My_Menu_User_Icon));
            }
        }
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

    @Override
    protected void onResume() {
        super.onResume();


        My_Menu_Array.clear();
        item_db_array.clear();

        // 내아이템 정보 전체 저장
        Item_getShared();

        // 내가본 상품 판단

        for(int i = 0; i < My_Menu_Number_Array.size(); i++){
            for(int j = 0; j < item_db_array.size(); j++){
                if(item_db_array.get(j).getItem_number() == My_Menu_Number_Array.get(i)){
                    My_Menu_Array.add(0,item_db_array.get(j));
                }
            }
        }

        My_Menu_Adapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(), My_Menu_Array.get(0).getItem_number(), Toast.LENGTH_SHORT).show();
    }
}
